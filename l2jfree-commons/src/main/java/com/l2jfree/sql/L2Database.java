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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.persistence.config.PersistenceUnitProperties;

import com.l2jfree.util.Rnd;
import com.l2jfree.util.logging.L2Logger;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Database access manager.
 * 
 * @author NB4L1
 */
public final class L2Database
{
	private static final L2Logger _log = L2Logger.getLogger(L2Database.class);
	
	private static ComboPooledDataSource _defaultDataSource;
	private static EntityManagerFactory _defaultEntityManagerFactory;
	private static final Map<String, ComboPooledDataSource> _dataSources = new HashMap<String, ComboPooledDataSource>();
	private static final Map<String, SQLContext> _tableContexts = new HashMap<String, SQLContext>();
	
	public static void setDataSource(String dataSourceName, DataSourceInitializer initializer) throws Exception
	{
		if (_dataSources.containsKey(dataSourceName))
			throw new Exception("A data source with the given name has been already set!");
		
		// initialization of the ComboPooledDataSource
		final ComboPooledDataSource dataSource = initializer.initDataSource();
		
		// test the connection
		dataSource.getConnection().close();
		
		_dataSources.put(dataSourceName, dataSource);
		
		if (dataSourceName.equals("default"))
		{
			_defaultDataSource = dataSource;
			
			// initialization of the EclipseLink JPA
			final Map<Object, Object> props = new HashMap<Object, Object>();
			
			props.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, _defaultDataSource);
			
			_defaultEntityManagerFactory = Persistence.createEntityManagerFactory("default", props);
			
			// test the entity manager
			_defaultEntityManagerFactory.createEntityManager().close();
		}
		
		/**
		 * Obtains <TT>CURRENT_DATABASE</TT> and <TT>CURRENT_SCHEMA</TT>
		 * on an arbitrary DBMS.
		 * @author savormix
		 */
		Connection con = null;
		
		// remove leftover tables
		try
		{
			con = L2Database.getConnection(dataSourceName);
			PreparedStatement ps = con.prepareStatement("SELECT table_name FROM information_schema.tables WHERE table_type LIKE ? AND table_name LIKE ?");
			ps.setString(1, "BASE_TABLE");
			ps.setString(2, "zzz%");
			ResultSet rs = ps.executeQuery();
			
			List<String> left = new ArrayList<String>();
			while (rs.next())
				left.add(rs.getString("table_name"));
			
			rs.close();
			ps.close();
			
			int size = left.size();
			for (String table : left)
			{
				try
				{
					ps = con.prepareStatement("DROP TABLE " + table);
					ps.executeUpdate();
				}
				catch (SQLException e)
				{
					// whatever...
					size--;
				}
				finally
				{
					L2Database.closeQuietly(ps);
				}
			}
			
			if (size > 0)
				_log.info("Removed " + size + " temporary tables.");
		}
		catch (SQLException e)
		{
			// whatever...
		}
		finally
		{
			L2Database.close(con);
		}
		
		try
		{
			con = L2Database.getConnection(dataSourceName);
			String table = null;
			while (true)
			{
				// generate a random table name
				StringBuilder sb = new StringBuilder("zzz");
				for (int i = 0; i < 5; i++)
					sb.append((char) Rnd.get('a', 'z'));
				table = sb.toString();
				// DO NOT LOOK UP information_schema NOW
				
				// attempt to create in current schema instead
				PreparedStatement ps = null;
				try
				{
					ps = con.prepareStatement("CREATE TABLE " + table + " (x INT)");
					ps.executeUpdate();
				}
				catch (SQLException e)
				{
					// already exists in current schema
					continue;
					// P.S. if DB doesn't have enough space for this table
					// then administrator will have more important problems
					// than this infinite cycle
				}
				finally
				{
					L2Database.closeQuietly(ps);
				}
				
				// table did not exist in current schema
				// we can look it up in information_schema now
				
				ps = con.prepareStatement("SELECT table_catalog, table_schema FROM information_schema.tables WHERE table_type LIKE ? AND table_name LIKE ?");
				ps.setString(1, "BASE_TABLE");
				ps.setString(2, table);
				ResultSet rs = ps.executeQuery();
				if (!rs.next())
					throw new IllegalStateException("Table stolen."); // should never happen
				String db = rs.getString("table_catalog");
				String schema = rs.getString("table_schema");
				
				boolean ndr = rs.next(); // non-deterministic results
				
				rs.close();
				ps.close();
				
				// remove table
				try
				{
					ps = con.prepareStatement("DROP TABLE " + table);
					ps.executeUpdate();
				}
				catch (SQLException e)
				{
					// whatever...
				}
				finally
				{
					L2Database.closeQuietly(ps);
				}
				
				if (!ndr) // database and schema obtained
				{
					SQLContext sqlc = new SQLContext(db, schema);
					_tableContexts.put(dataSourceName, sqlc);
					break;
				}
			}
		}
		catch (SQLException e)
		{
			throw e;
		}
		finally
		{
			L2Database.close(con);
		}
	}
	
	public static void shutdown()
	{
		try
		{
			_defaultEntityManagerFactory.close();
		}
		catch (Throwable t)
		{
			_log.fatal("", t);
		}
		
		try
		{
			for (ComboPooledDataSource dataSource : _dataSources.values())
			{
				try
				{
					dataSource.close();
				}
				catch (Throwable t)
				{
					_log.fatal("", t);
				}
			}
		}
		catch (Throwable t)
		{
			_log.fatal("", t);
		}
	}
	
	public static Connection getConnection() throws SQLException
	{
		return getConnection("default", _defaultDataSource);
	}
	
	public static Connection getConnection(String dataSourceName) throws SQLException
	{
		return getConnection(dataSourceName, _dataSources.get(dataSourceName));
	}
	
	private static Connection getConnection(String dataSourceName, ComboPooledDataSource dataSource)
			throws SQLException
	{
		try
		{
			if (dataSource == null)
				throw new SQLException("Unknown data source!");
			
			return dataSource.getConnection();
			// TODO for debug purposes
			//return new L2DBConnectionWrapper(dataSource.getConnection());
		}
		catch (SQLException e)
		{
			_log.fatal("L2Database: Failed to retrieve database connection from data source: " + dataSourceName, e);
			throw e;
		}
	}
	
	public static EntityManager getEntityManager()
	{
		return _defaultEntityManagerFactory.createEntityManager();
	}
	
	public static void close(Connection con)
	{
		if (con == null)
			return;
		
		try
		{
			con.close();
		}
		catch (SQLException e)
		{
			_log.warn("L2Database: Failed to close database connection!", e);
		}
	}
	
	public static void close(EntityManager em)
	{
		if (em == null)
			return;
		
		try
		{
			em.close();
		}
		catch (Exception e)
		{
			_log.warn("L2Database: Failed to close entity manager!", e);
		}
	}
	
	public static boolean tableExists(String tableName)
	{
		return tableExists("default", tableName);
	}
	
	public static boolean tableExists(String dataSourceName, String tableName)
	{
		boolean tableExists = false;
		
		Connection con = null;
		try
		{
			con = L2Database.getConnection(dataSourceName);
			
			PreparedStatement ps = con
					.prepareStatement("SELECT table_name FROM information_schema.tables WHERE table_type LIKE ? AND table_name LIKE ? AND table_schema LIKE ? AND table_catalog LIKE ?");
			ps.setString(1, "BASE_TABLE");
			ps.setString(2, tableName);
			SQLContext sqlc = _tableContexts.get(dataSourceName);
			ps.setString(3, sqlc.getSchema());
			ps.setString(4, sqlc.getDatabase());
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next())
				tableExists = true;
			
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			L2Database.close(con);
		}
		
		return tableExists;
	}
	
	private static void closeQuietly(Statement s)
	{
		try
		{
			s.close();
		}
		catch (Exception e)
		{
			// ignore
		}
	}
	
	private static class SQLContext
	{
		private final String _database;
		private final String _schema;
		
		private SQLContext(String database, String schema)
		{
			_database = database;
			_schema = schema;
		}
		
		public String getDatabase()
		{
			return _database;
		}
		
		public String getSchema()
		{
			return _schema;
		}
	}
}
