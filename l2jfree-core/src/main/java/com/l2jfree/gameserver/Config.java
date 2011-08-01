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
package com.l2jfree.gameserver;

import com.l2jfree.L2Config;
import com.l2jfree.Shutdown;
import com.l2jfree.TerminationStatus;
import com.l2jfree.config.L2Properties;
import com.l2jfree.sql.L2Database;
import com.l2jfree.sql.L2DatabaseInstaller;
import com.l2jfree.util.concurrent.L2ThreadPool;

/**
 * @author savormix
 *
 */
public class Config extends L2Config
{
	static
	{
		L2Config.registerConfig(new DatabaseConfig());
		L2Config.registerConfig(new NetworkConfig());
		
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
			L2ThreadPool.initThreadPools(new L2CoreThreadPools());
		}
		catch (Exception e)
		{
			_log.fatal("Could not initialize thread pools!", e);
			Shutdown.exit(TerminationStatus.RUNTIME_INITIALIZATION_FAILURE);
		}
		
		try
		{
			L2Database.setDataSource("default", new L2CoreDataSource());
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
	
	protected Config()
	{
		super();
	}
	
	public static final class DatabaseConfig extends ConfigPropertiesLoader
	{
		/** Maximum amount of database connections in pool */
		public static int DB_MAX_CONNECTIONS;
		/** Database JDBC URL */
		public static String DB_URL;
		/** Database login */
		public static String DB_USER;
		/** Database password */
		public static String DB_PASSWORD;
		/** Whether to optimize database tables on startup */
		public static boolean DB_OPTIMIZE;
		
		@Override
		protected void loadImpl(L2Properties properties)
		{
			DB_MAX_CONNECTIONS = properties.getInteger("MaxConnectionsInPool", 5);
			
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
	
	public static final class NetworkConfig extends ConfigPropertiesLoader
	{
		/** Login server listens for client connections on this IP address */
		public static String NET_LISTEN_IP;
		/** Login server listens for client connections on this port */
		public static int NET_LISTEN_PORT;
		
		@Override
		protected void loadImpl(L2Properties properties)
		{
			NET_LISTEN_IP = properties.getString("ListenIP", "0.0.0.0");
			NET_LISTEN_PORT = properties.getInteger("ListenPort", 7777);
		}
		
		@Override
		protected String getName()
		{
			return "network";
		}
	}
}
