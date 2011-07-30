package com.l2jfree.sql;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.l2jfree.util.Rnd;
import com.l2jfree.util.logging.L2Logger;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public abstract class L2DataSource implements DataSource
{
	protected static final L2Logger _log = L2Logger.getLog(L2DataSource.class);
	
	public static L2DataSource valueOf(String name, ComboPooledDataSource dataSource)
	{
		final String jdbcUrl = dataSource.getJdbcUrl().toLowerCase();
		
		if (jdbcUrl.contains("mysql"))
			return new L2DataSourceMySQL(name, dataSource);
		if (jdbcUrl.contains("postgresql"))
			return new L2DataSourcePostgreSQL(name, dataSource);
		if (jdbcUrl.contains("sqlite"))
			return new L2DataSourceSQLite(name, dataSource);
		
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
	
	protected String getInformationSchemaTables()
	{
		return "information_schema.tables";
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
			final StringBuilder sb = new StringBuilder("_zzz");
			for (int i = 0; i < 5; i++)
				sb.append((char)Rnd.get('a', 'z'));
			final String table = sb.toString();
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
	
	public abstract void optimize() throws SQLException;
	
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
		return _dataSource.isWrapperFor(iface);
	}
	
	@Override
	public final <T> T unwrap(Class<T> iface) throws SQLException
	{
		return _dataSource.unwrap(iface);
	}
	
	public final void close()
	{
		_dataSource.close();
	}
}
