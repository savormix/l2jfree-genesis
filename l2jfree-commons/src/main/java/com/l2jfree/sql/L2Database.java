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
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

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
			final Map<Object, Object> props = initializer.initEntityManagerFactoryProperties(_defaultDataSource);
			
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
	
	public static int executeUpdate(String sql)
	{
		int result = -1;
		
		Connection con = null;
		try
		{
			con = L2Database.getConnection();
			
			final Statement st = con.createStatement();
			result = st.executeUpdate(sql);
			st.close();
		}
		catch (SQLException e)
		{
			_log.warn("L2Database: Failed to execute update!", e);
		}
		finally
		{
			L2Database.close(con);
		}
		
		return result;
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
	
	// =================================================================================================================
	
	public static <T> T find(Class<T> entityClass, Object primaryKey)
	{
		final T result;
		
		final EntityManager em = L2Database.getEntityManager();
		{
			result = em.find(entityClass, primaryKey);
			
			if (result != null)
				em.detach(result);
		}
		em.close();
		
		return result;
	}
	
	public static void persist(Object entity)
	{
		final EntityManager em = L2Database.getEntityManager();
		{
			em.getTransaction().begin();
			{
				em.persist(entity);
			}
			em.getTransaction().commit();
		}
		em.close();
	}
	
	public static void merge(Object entity)
	{
		final EntityManager em = L2Database.getEntityManager();
		{
			em.getTransaction().begin();
			{
				em.merge(entity);
			}
			em.getTransaction().commit();
		}
		em.close();
	}
	
	public static <T> T mergeAndDetach(T entity)
	{
		final T result;
		
		final EntityManager em = L2Database.getEntityManager();
		{
			em.getTransaction().begin();
			{
				result = em.merge(entity);
			}
			em.getTransaction().commit();
			
			if (result != null)
				em.detach(result);
		}
		em.close();
		
		return result;
	}
	
	public static void remove(Object entity)
	{
		final EntityManager em = L2Database.getEntityManager();
		{
			em.getTransaction().begin();
			{
				em.remove(entity);
			}
			em.getTransaction().commit();
		}
		em.close();
	}
	
	// =================================================================================================================
	
	private static Query createQuery(boolean named, EntityManager em, String query)
	{
		if (named)
			return em.createNamedQuery(query);
		else
			return em.createQuery(query);
	}
	
	public interface QueryConfigurator
	{
		public void configure(Query q);
	}
	
	// =================================================================================================================
	
	@SuppressWarnings("unchecked")
	private static <T> T getSingleResult(boolean named, String query, QueryConfigurator queryConfigurator)
	{
		final T result;
		
		final EntityManager em = L2Database.getEntityManager();
		{
			final Query q = createQuery(named, em, query);
			
			if (queryConfigurator != null)
				queryConfigurator.configure(q);
			
			result = (T)q.getSingleResult();
			
			if (result != null)
				em.detach(result);
		}
		em.close();
		
		return result;
	}
	
	public static <T> T getSingleResultByNamedQuery(String namedQuery)
	{
		return getSingleResult(true, namedQuery, null);
	}
	
	public static <T> T getSingleResultByNamedQuery(String namedQuery, QueryConfigurator queryConfigurator)
	{
		return getSingleResult(true, namedQuery, queryConfigurator);
	}
	
	public static <T> T getSingleResultByQuery(String query)
	{
		return getSingleResult(false, query, null);
	}
	
	public static <T> T getSingleResultByQuery(String query, QueryConfigurator queryConfigurator)
	{
		return getSingleResult(false, query, queryConfigurator);
	}
	
	// =================================================================================================================
	
	@SuppressWarnings("unchecked")
	private static <T> List<T> getResultList(boolean named, String query, QueryConfigurator queryConfigurator)
	{
		final List<T> result;
		
		final EntityManager em = L2Database.getEntityManager();
		{
			final Query q = createQuery(named, em, query);
			
			if (queryConfigurator != null)
				queryConfigurator.configure(q);
			
			result = q.getResultList();
			
			for (T t : result)
				if (t != null)
					em.detach(t);
		}
		em.close();
		
		return result;
	}
	
	public static <T> List<T> getResultListByNamedQuery(String namedQuery)
	{
		return getResultList(true, namedQuery, null);
	}
	
	public static <T> List<T> getResultListByNamedQuery(String namedQuery, QueryConfigurator queryConfigurator)
	{
		return getResultList(true, namedQuery, queryConfigurator);
	}
	
	public static <T> List<T> getResultListByQuery(String query)
	{
		return getResultList(false, query, null);
	}
	
	public static <T> List<T> getResultListByQuery(String query, QueryConfigurator queryConfigurator)
	{
		return getResultList(false, query, queryConfigurator);
	}
	
	// =================================================================================================================
	
	private static void executeUpdate(boolean named, String query, QueryConfigurator queryConfigurator)
	{
		final EntityManager em = L2Database.getEntityManager();
		{
			em.getTransaction().begin();
			{
				final Query q = createQuery(named, em, query);
				
				if (queryConfigurator != null)
					queryConfigurator.configure(q);
				
				q.executeUpdate();
			}
			em.getTransaction().commit();
		}
		em.close();
	}
	
	public static void executeUpdateByNamedQuery(String namedQuery)
	{
		executeUpdate(true, namedQuery, null);
	}
	
	public static void executeUpdateByNamedQuery(String namedQuery, QueryConfigurator queryConfigurator)
	{
		executeUpdate(true, namedQuery, queryConfigurator);
	}
	
	public static void executeUpdateByQuery(String query)
	{
		executeUpdate(false, query, null);
	}
	
	public static void executeUpdateByQuery(String query, QueryConfigurator queryConfigurator)
	{
		executeUpdate(false, query, queryConfigurator);
	}
}
