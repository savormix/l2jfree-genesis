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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.persistence.config.PersistenceUnitProperties;

import com.l2jfree.util.logging.L2Logger;

/**
 * Database access manager.
 * 
 * @author NB4L1
 */
public final class L2Database
{
	private static final L2Logger _log = L2Logger.getLogger(L2Database.class);
	
	public static final String DEFAULT_DATA_SOURCE_NAME = "default";
	
	private static L2DataSource _defaultDataSource;
	private static EntityManagerFactory _defaultEntityManagerFactory;
	private static final Map<String, L2DataSource> _dataSources = new HashMap<String, L2DataSource>();
	
	public static void setDataSource(String dataSourceName, DataSourceInitializer initializer) throws Exception
	{
		if (_dataSources.containsKey(dataSourceName))
			throw new Exception("A data source with the given name has been already set!");
		
		// initialization of the ComboPooledDataSource
		final L2DataSource dataSource = L2DataSource.valueOf(dataSourceName, initializer.initDataSource());
		
		// test the connection
		dataSource.getConnection().close();
		
		// initialize the sql context
		dataSource.initSQLContext();
		
		_dataSources.put(dataSourceName, dataSource);
		
		if (dataSourceName.equals(DEFAULT_DATA_SOURCE_NAME))
			_defaultDataSource = dataSource;
		
		if (!initializer.createEntityManagerFactory())
			return;
		
		if (dataSourceName.equals(DEFAULT_DATA_SOURCE_NAME))
		{
			// initialization of the EclipseLink JPA
			final Map<Object, Object> props = new HashMap<Object, Object>();
			
			props.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, _defaultDataSource);
			
			_defaultEntityManagerFactory = Persistence.createEntityManagerFactory(DEFAULT_DATA_SOURCE_NAME, props);
			
			// test the entity manager
			_defaultEntityManagerFactory.createEntityManager().close();
		}
		else
		{
			// until we have validation for persistence.xml (to check if there is any config for the given datasource)
			// LOW add persistence.xml validation
			throw new Exception("Entity manager initialization required for a non-default datasource!");
		}
	}
	
	public static void optimize()
	{
		optimize(DEFAULT_DATA_SOURCE_NAME, _defaultDataSource);
	}
	
	public static void optimize(String dataSourceName)
	{
		optimize(dataSourceName, _dataSources.get(dataSourceName));
	}
	
	private static void optimize(String dataSourceName, L2DataSource dataSource)
	{
		try
		{
			if (dataSource == null)
				throw new SQLException("Unknown data source!");
			
			dataSource.optimize();
		}
		catch (Exception e)
		{
			_log.fatal("L2Database: Failed to optimise the database tables of data source: " + dataSourceName, e);
		}
	}
	
	public static void backup()
	{
		backup(DEFAULT_DATA_SOURCE_NAME, _defaultDataSource);
	}
	
	public static void backup(String dataSourceName)
	{
		backup(dataSourceName, _dataSources.get(dataSourceName));
	}
	
	private static void backup(String dataSourceName, L2DataSource dataSource)
	{
		try
		{
			if (dataSource == null)
				throw new SQLException("Unknown data source!");
			
			dataSource.backup();
		}
		catch (Exception e)
		{
			_log.fatal("L2Database: Failed to backup the database of data source: " + dataSourceName, e);
		}
	}
	
	public static void shutdown()
	{
		try
		{
			if (_defaultEntityManagerFactory != null)
				_defaultEntityManagerFactory.close();
		}
		catch (Throwable t)
		{
			_log.fatal("", t);
		}
		
		try
		{
			for (L2DataSource dataSource : _dataSources.values())
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
		return getConnection(DEFAULT_DATA_SOURCE_NAME, _defaultDataSource);
	}
	
	public static Connection getConnection(String dataSourceName) throws SQLException
	{
		return getConnection(dataSourceName, _dataSources.get(dataSourceName));
	}
	
	private static Connection getConnection(String dataSourceName, L2DataSource dataSource) throws SQLException
	{
		try
		{
			if (dataSource == null)
				throw new SQLException("Unknown data source!");
			
			return dataSource.getConnection();
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
		return tableExists(DEFAULT_DATA_SOURCE_NAME, tableName);
	}
	
	public static boolean tableExists(String dataSourceName, String tableName)
	{
		final L2DataSource dataSource = _dataSources.get(dataSourceName);
		
		if (dataSource == null)
		{
			_log.warn("", new SQLException("Unknown data source!"));
			return false;
		}
		
		return dataSource.tableExists(tableName);
	}
	
	public static void closeQuietly(Statement s)
	{
		if (s == null)
			return;
		
		try
		{
			s.close();
		}
		catch (SQLException e)
		{
			// ignore
		}
	}
}
