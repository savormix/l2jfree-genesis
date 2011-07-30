package com.l2jfree.gameserver;

import com.l2jfree.L2Config;
import com.l2jfree.Shutdown;
import com.l2jfree.TerminationStatus;
import com.l2jfree.config.L2Properties;
import com.l2jfree.sql.L2Database;
import com.l2jfree.sql.L2DatabaseInstaller;
import com.l2jfree.util.concurrent.L2ThreadPool;

/**
 * @author NB4L1
 */
//TODO should be sorted into groups and redone with annotation loaders
public class Config extends L2Config
{
	static
	{
		L2Config.registerConfig(new DatabaseConfig());
		
		try
		{
			L2Config.loadConfigs();
		}
		catch (Exception e)
		{
			_log.fatal("Could not load configuration files!", e);
			Shutdown.exit(TerminationStatus.RUNTIME_INVALID_CONFIGURATION);
		}
		
		try
		{
			L2ThreadPool.initThreadPools(new L2GameThreadPools());
		}
		catch (Exception e)
		{
			_log.fatal("Could not initialize thread pools!", e);
			Shutdown.exit(TerminationStatus.RUNTIME_INITIALIZATION_FAILURE);
		}
		
		try
		{
			L2Database.setDataSource("default", new L2GameDataSource());
		}
		catch (Exception e)
		{
			_log.fatal("Could not initialize DB connections!", e);
			Shutdown.exit(TerminationStatus.RUNTIME_INITIALIZATION_FAILURE);
		}
		
		try
		{
			L2DatabaseInstaller.check();
		}
		catch (Exception e)
		{
			_log.fatal("Could not initialize DB tables!", e);
			Shutdown.exit(TerminationStatus.RUNTIME_INITIALIZATION_FAILURE);
		}
	}
	
	/** Maximum amount of database connections in pool */
	public static int DB_MAX_CONNECTIONS;
	/** Database driver class */
	public static String DB_DRIVER;
	/** Database JDBC URL */
	public static String DB_URL;
	/** Database login */
	public static String DB_USER;
	/** Database password */
	public static String DB_PASSWORD;
	/** Whether to optimize database tables on startup */
	public static boolean DB_OPTIMIZE;
	
	private static final class DatabaseConfig extends ConfigPropertiesLoader
	{
		@Override
		protected void loadImpl(L2Properties properties)
		{
			DB_MAX_CONNECTIONS = properties.getInteger("MaxConnectionsInPool", 50);
			DB_DRIVER = properties.getString("DriverClass");
			
			DB_URL = properties.getString("JdbcUrl");
			if (!DB_URL.startsWith("jdbc:"))
				DB_URL = "jdbc:" + DB_URL;
			
			DB_USER = properties.getString("Login");
			if (DB_USER.equalsIgnoreCase("root") || DB_USER.equalsIgnoreCase("postgres"))
			{
				_log.info("L2jFree servers should not use DBMS superuser accounts ... exited.");
				Shutdown.exit(TerminationStatus.ENVIRONMENT_SUPERUSER);
			}
			DB_PASSWORD = properties.getString("Password", "");
			
			DB_OPTIMIZE = properties.getBool("OptimizeTables", true);
		}
		
		@Override
		protected String getName()
		{
			return "database";
		}
	}
}
