/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jfree.sql;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import com.l2jfree.L2Config;
import com.l2jfree.lang.L2TextBuilder;
import com.l2jfree.util.Rnd;
import com.l2jfree.util.logging.L2Logger;

public abstract class L2DataSource implements DataSource
{
	public enum JDBCProvider
	{
		MySQL("mysql", "com.mysql.jdbc.Driver") {
			@Override
			public L2DataSource createDataSource(String name, ComboPooledDataSource dataSource)
			{
				return new L2DataSourceMySQL(name, dataSource);
			}
		},
		PostgreSQL("postgresql", "org.postgresql.Driver") {
			@Override
			public L2DataSource createDataSource(String name, ComboPooledDataSource dataSource)
			{
				return new L2DataSourcePostgreSQL(name, dataSource);
			}
		},
		SQLite("sqlite", "org.sqlite.JDBC") {
			@Override
			public L2DataSource createDataSource(String name, ComboPooledDataSource dataSource)
			{
				return new L2DataSourceSQLite(name, dataSource);
			}
		},
		;
		
		private final String _protocol;
		private final String _driverClass;
		
		private JDBCProvider(String protocol, String driverClass)
		{
			_protocol = protocol;
			_driverClass = driverClass;
		}
		
		public final String getProtocol()
		{
			return _protocol;
		}
		
		public final String getDriverClass()
		{
			return _driverClass;
		}
		
		public abstract L2DataSource createDataSource(String name, ComboPooledDataSource dataSource);
	}
	
	protected static final L2Logger _log = L2Logger.getLog(L2DataSource.class);
	
	public static L2DataSource valueOf(String name, ComboPooledDataSource dataSource)
	{
		final String jdbcUrl = dataSource.getJdbcUrl().toLowerCase();
		
		for (JDBCProvider provider : JDBCProvider.values())
		{
			if (!jdbcUrl.contains(provider.getProtocol()))
				continue;
			
			try
			{
				dataSource.setDriverClass(provider.getDriverClass());
			}
			catch (PropertyVetoException e)
			{
				throw new RuntimeException(e);
			}
			
			return provider.createDataSource(name, dataSource);
		}
		
		throw new IllegalArgumentException("Not supported JDBC provider!");
	}
	
	private final String _name;
	private final ComboPooledDataSource _dataSource;
	
	private String _database;
	private String _schema;
	
	protected L2DataSource(String name, ComboPooledDataSource dataSource)
	{
		_name = name;
		_dataSource = dataSource;
	}
	
	protected final ComboPooledDataSource getComboPooledDataSource()
	{
		return _dataSource;
	}
	
	protected String getInformationSchemaTables()
	{
		return "information_schema.tables";
	}
	
	protected String getProviderName()
	{
		return getClass().getSimpleName().replace(L2DataSource.class.getSimpleName(), "");
	}
	
	public void initSQLContext() throws SQLException
	{
		/**
		 * Obtains <TT>CURRENT_DATABASE</TT> and <TT>CURRENT_SCHEMA</TT> on an arbitrary DBMS.
		 * 
		 * @author savormix
		 */
		Connection con = null;
		
		// remove leftover tables
		try
		{
			con = getConnection();
			
			final List<String> tables = new ArrayList<String>();
			
			{
				final PreparedStatement ps = con.prepareStatement("SELECT table_name FROM "
						+ getInformationSchemaTables() + " WHERE table_type LIKE ? AND table_name LIKE ?");
				ps.setString(1, "BASE_TABLE");
				ps.setString(2, "_zzz%");
				
				final ResultSet rs = ps.executeQuery();
				
				while (rs.next())
					tables.add(rs.getString("table_name"));
				
				rs.close();
				ps.close();
			}
			
			int removed = 0;
			for (String table : tables)
			{
				final PreparedStatement ps = con.prepareStatement("DROP TABLE " + table);
				ps.executeUpdate();
				ps.close();
				
				removed++;
			}
			
			if (removed > 0)
				_log.info("Removed " + removed + " temporary tables.");
		}
		finally
		{
			L2Database.close(con);
		}
		
		try
		{
			con = getConnection();
			
			// generate a random table name
			final String table = "_zzz" + Rnd.getString(5, Rnd.LOWER_CASE_LETTERS);
			// DO NOT LOOK UP information_schema NOW
			
			// attempt to create in current schema instead
			{
				final PreparedStatement ps = con.prepareStatement("CREATE TABLE " + table + " (x INT)");
				ps.executeUpdate();
				ps.close();
			}
			
			// table did not exist in current schema
			// we can look it up in information_schema now
			{
				final PreparedStatement ps = con.prepareStatement("SELECT table_catalog, table_schema FROM "
						+ getInformationSchemaTables() + " WHERE table_type LIKE ? AND table_name LIKE ?");
				ps.setString(1, "BASE_TABLE");
				ps.setString(2, table);
				
				final ResultSet rs = ps.executeQuery();
				
				if (rs.next())
				{
					_database = rs.getString("table_catalog");
					_schema = rs.getString("table_schema");
				}
				else
					throw new SQLException("Table stolen."); // should never happen
					
				if (rs.next())
					throw new SQLException("Non-deterministic results."); // should never happen
					
				rs.close();
				ps.close();
			}
			
			// remove table
			{
				final PreparedStatement ps = con.prepareStatement("DROP TABLE " + table);
				ps.executeUpdate();
				ps.close();
			}
		}
		finally
		{
			L2Database.close(con);
		}
	}
	
	@SuppressWarnings("unused")
	public void optimize() throws SQLException
	{
		_log.warn("TableOptimizer: Provider (" + getProviderName() + ") not yet supported.");
	}
	
	protected final boolean writeBackup(String databaseName, InputStream in) throws IOException
	{
		FileUtils.forceMkdir(new File("backup/database"));
		
		final Date time = new Date();
		
		final L2TextBuilder tb = L2TextBuilder.newInstance();
		tb.append("backup/database/DatabaseBackup_");
		tb.append(new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()));
		tb.append("_uptime-").append(L2Config.getShortUptime());
		tb.append(".zip");
		
		final File backupFile = new File(tb.moveToString());
		
		int written = 0;
		ZipOutputStream out = null;
		try
		{
			out = new ZipOutputStream(new FileOutputStream(backupFile));
			out.setMethod(ZipOutputStream.DEFLATED);
			out.setLevel(Deflater.BEST_COMPRESSION);
			out.setComment("L2jFree Schema Backup Utility\r\n\r\nBackup date: "
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS z").format(new Date()));
			out.putNextEntry(new ZipEntry(databaseName + ".sql"));
			
			byte[] buf = new byte[4096];
			for (int read; (read = in.read(buf)) != -1;)
			{
				out.write(buf, 0, read);
				
				written += read;
			}
		}
		finally
		{
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
		
		if (written == 0)
		{
			backupFile.delete();
			return false;
		}
		
		_log.info("DatabaseBackupManager: Schema `" + databaseName + "` backed up successfully in "
				+ (System.currentTimeMillis() - time.getTime()) / 1000 + " s.");
		return true;
	}
	
	public void backup()
	{
		_log.warn("DatabaseBackupManager: Provider (" + getProviderName() + ") not yet supported.");
	}
	
	public final boolean tableExists(String tableName)
	{
		boolean tableExists = false;
		
		Connection con = null;
		try
		{
			con = getConnection();
			
			final PreparedStatement ps = con
					.prepareStatement("SELECT table_name FROM "
							+ getInformationSchemaTables()
							+ " WHERE table_type LIKE ? AND table_name LIKE ? AND table_schema LIKE ? AND table_catalog LIKE ?");
			ps.setString(1, "BASE_TABLE");
			ps.setString(2, tableName);
			ps.setString(3, _schema);
			ps.setString(4, _database);
			
			final ResultSet rs = ps.executeQuery();
			
			if (rs.next())
				tableExists = true;
			
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			_log.warn("", e);
			return false;
		}
		finally
		{
			L2Database.close(con);
		}
		
		return tableExists;
	}
	
	@Override
	public final Connection getConnection() throws SQLException
	{
		try
		{
			return _dataSource.getConnection();
			// TODO for debug purposes
			//return new L2DBConnectionWrapper(_dataSource.getConnection());
		}
		catch (SQLException e)
		{
			_log.fatal("L2DataSource: Failed to retrieve database connection from data source: " + _name, e);
			throw e;
		}
	}
	
	@Override
	public final Connection getConnection(String username, String password) throws SQLException
	{
		try
		{
			return _dataSource.getConnection(username, password);
			// TODO for debug purposes
			//return new L2DBConnectionWrapper(_dataSource.getConnection(username, password));
		}
		catch (SQLException e)
		{
			_log.fatal("L2DataSource: Failed to retrieve database connection from data source: " + _name, e);
			throw e;
		}
	}
	
	@Override
	public final PrintWriter getLogWriter() throws SQLException
	{
		return _dataSource.getLogWriter();
	}
	
	@Override
	public final int getLoginTimeout() throws SQLException
	{
		return _dataSource.getLoginTimeout();
	}
	
	@Override
	public final void setLogWriter(PrintWriter out) throws SQLException
	{
		_dataSource.setLogWriter(out);
	}
	
	@Override
	public final void setLoginTimeout(int seconds) throws SQLException
	{
		_dataSource.setLoginTimeout(seconds);
	}
	
	@Override
	public final boolean isWrapperFor(Class<?> iface) throws SQLException
	{
		return ((DataSource)_dataSource).isWrapperFor(iface); // don't ask -.-
	}
	
	@Override
	public final <T> T unwrap(Class<T> iface) throws SQLException
	{
		return ((DataSource)_dataSource).unwrap(iface); // don't ask -.-
	}
	
	public final void close()
	{
		_dataSource.close();
	}
}
