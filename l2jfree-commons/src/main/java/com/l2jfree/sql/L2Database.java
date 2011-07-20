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
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.persistence.config.PersistenceUnitProperties;

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
	private static final Map<String, String> _tableSchemas = new HashMap<String, String>();
	
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
		 * Obtains <TT>CURRENT_SCHEMA</TT> on an arbitrary DBMS.
		 * @author savormix
		 */
		Connection con = null;
		
		try
		{
			con = L2Database.getConnection(dataSourceName);
			
			PreparedStatement ps = con.prepareStatement("DROP TABLE _tmp_active_schema");
			ps.executeUpdate();
			ps.close();
		}
		catch (SQLException e)
		{
			// OK
		}
		finally
		{
			L2Database.close(con);
		}
		
		try
		{
			con = L2Database.getConnection(dataSourceName);
			
			PreparedStatement ps = null;
			
			ps = con.prepareStatement("CREATE TABLE _tmp_active_schema (x INT)");
			ps.executeUpdate();
			ps.close();
			
			ps = con.prepareStatement("SELECT table_schema FROM information_schema.tables WHERE table_type LIKE ? AND table_name LIKE ?");
			ps.setString(1, "BASE_TABLE");
			ps.setString(2, "_tmp_active_schema");
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next())
				_tableSchemas.put(dataSourceName, rs.getString("table_schema"));
			else
				throw new IllegalStateException("Table stolen."); // should never happen
			
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			throw e;
		}
		finally
		{
			L2Database.close(con);
		}
		
		try
		{
			con = L2Database.getConnection(dataSourceName);
			
			PreparedStatement ps = con.prepareStatement("DROP TABLE _tmp_active_schema");
			ps.executeUpdate();
			ps.close();
		}
		catch (SQLException e)
		{
			// whatever...
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
					.prepareStatement("SELECT table_catalog FROM information_schema.tables WHERE table_type LIKE ? AND table_name LIKE ? AND table_schema LIKE ?");
			ps.setString(1, "BASE_TABLE");
			ps.setString(2, tableName);
			ps.setString(3, _tableSchemas.get(dataSourceName));
			
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
}
