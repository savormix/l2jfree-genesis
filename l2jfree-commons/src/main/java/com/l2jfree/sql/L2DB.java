package com.l2jfree.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.persistence.config.PersistenceUnitProperties;

import com.l2jfree.L2Config;
import com.l2jfree.util.logging.L2Logger;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Abstract database access manager.
 * 
 * @author NB4L1
 */
public abstract class L2DB
{
	private static final L2Logger _log = L2Logger.getLogger(L2DB.class);
	
	private final ComboPooledDataSource _source;
	private final EntityManagerFactory _entityManagerFactory;
	
	protected L2DB()
	{
		try
		{
			// ====================================================================================
			// initialization of the ComboPooledDataSource
			//
			_source = initComboPooledDataSource();
			
			// test the connection
			_source.getConnection().close();
			
			// ====================================================================================
			// initialization of the EclipseLink JPA
			//
			_entityManagerFactory = initEntityManagerFactory();
			
			// test the entity manager
			_entityManagerFactory.createEntityManager().close();
		}
		catch (Exception e)
		{
			throw new Error("L2DB: Failed to init database connections:", e);
		}
	}
	
	// TODO replace global config variables
	protected ComboPooledDataSource initComboPooledDataSource() throws Exception
	{
		if (L2Config.DATABASE_MAX_CONNECTIONS < 10)
		{
			L2Config.DATABASE_MAX_CONNECTIONS = 10;
			_log.warn("at least " + L2Config.DATABASE_MAX_CONNECTIONS + " db connections are required.");
		}
		
		final ComboPooledDataSource source = new ComboPooledDataSource();
		source.setAutoCommitOnClose(true);
		
		source.setInitialPoolSize(10);
		source.setMinPoolSize(10);
		source.setMaxPoolSize(L2Config.DATABASE_MAX_CONNECTIONS);
		
		source.setAcquireRetryAttempts(0); // try to obtain connections indefinitely (0 = never quit)
		source.setAcquireRetryDelay(500); // 500 miliseconds wait before try to acquire connection again
		source.setCheckoutTimeout(0); // 0 = wait indefinitely for new connection
		// if pool is exhausted
		source.setAcquireIncrement(5); // if pool is exhausted, get 5 more connections at a time
		// cause there is a "long" delay on acquire connection
		// so taking more than one connection at once will make connection pooling
		// more effective.
		
		// this "connection_test_table" is automatically created if not already there
		source.setAutomaticTestTable("connection_test_table");
		source.setTestConnectionOnCheckin(false);
		
		// testing OnCheckin used with IdleConnectionTestPeriod is faster than testing on checkout
		
		source.setIdleConnectionTestPeriod(3600); // test idle connection every 60 sec
		source.setMaxIdleTime(1800); // 0 = idle connections never expire
		// *THANKS* to connection testing configured above
		// but I prefer to disconnect all connections not used
		// for more than 1 hour
		
		// enables statement caching, there is a "semi-bug" in c3p0 0.9.0 but in 0.9.0.2 and later it's fixed
		source.setMaxStatementsPerConnection(100);
		
		source.setBreakAfterAcquireFailure(false); // never fail if any way possible
		// setting this to true will make
		// c3p0 "crash" and refuse to work
		// till restart thus making acquire
		// errors "FATAL" ... we don't want that
		// it should be possible to recover
		source.setDriverClass(L2Config.DATABASE_DRIVER);
		source.setJdbcUrl(L2Config.DATABASE_URL);
		source.setUser(L2Config.DATABASE_LOGIN);
		source.setPassword(L2Config.DATABASE_PASSWORD);
		
		return source;
	}
	
	protected EntityManagerFactory initEntityManagerFactory() throws Exception
	{
		final Map<Object, Object> props = new HashMap<Object, Object>();
		
		props.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, _source);
		
		return Persistence.createEntityManagerFactory("default", props);
	}
	
	public void shutdown() throws Exception
	{
		try
		{
			_entityManagerFactory.close();
		}
		catch (Throwable t)
		{
			_log.fatal("", t);
		}
		
		_source.close();
	}
	
	public Connection getConnection() throws SQLException
	{
		try
		{
			return _source.getConnection();
			// TODO for debug purposes
			//return new L2DBConnectionWrapper(_source.getConnection());
		}
		catch (SQLException e)
		{
			_log.fatal("L2DB: Failed to retrieve database connection!", e);
			throw e;
		}
	}
	
	public EntityManager getEntityManager()
	{
		return _entityManagerFactory.createEntityManager();
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
			_log.warn("L2DB: Failed to close database connection!", e);
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
			_log.warn("L2DB: Failed to close entity manager!", e);
		}
	}
}
