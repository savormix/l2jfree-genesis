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
package com.l2jfree.loginserver;

import com.l2jfree.L2Config;
import com.l2jfree.Shutdown;
import com.l2jfree.TerminationStatus;
import com.l2jfree.config.L2Properties;
import com.l2jfree.config.annotation.ConfigClass;
import com.l2jfree.sql.L2Database;
import com.l2jfree.sql.L2DatabaseInstaller;
import com.l2jfree.util.concurrent.L2ThreadPool;

/**
 * @author NB4L1
 * @author savormix
 */
// TODO should be sorted into groups and redone with annotation loaders
public class Config extends L2Config
{
	static
	{
		L2Config.registerConfig(new DatabaseConfig());
		L2Config.registerConfig(new NetworkConfig());
		L2Config.registerConfig(new ServiceConfig());
		
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
			L2ThreadPool.initThreadPools(new L2LoginThreadPools());
		}
		catch (Exception e)
		{
			_log.fatal("Could not initialize thread pools!", e);
			Shutdown.exit(TerminationStatus.RUNTIME_INITIALIZATION_FAILURE);
		}
		
		try
		{
			L2Database.setDataSource("default", new L2LoginDataSource());
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
	
	@ConfigClass(folderName = "config", fileName = "database")
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
			
			DB_OPTIMIZE = properties.getBoolean("OptimizeTables", true);
		}
	}
	
	@ConfigClass(folderName = "config", fileName = "network")
	public static final class NetworkConfig extends ConfigPropertiesLoader
	{
		/** Login server listens for client connections on this IP address */
		public static String NET_LISTEN_IP;
		/** Login server listens for client connections on this port */
		public static int NET_LISTEN_PORT;
		
		/** Whether to listen for legacy game servers */
		public static boolean NET_ENABLE_LEGACY;
		/** Login server listens for legacy game server connections on this IP address */
		public static String NET_LEGACY_LISTEN_IP;
		/** Login server listens for legacy game server connections on this port */
		public static int NET_LEGACY_LISTEN_PORT;
		
		@Override
		protected void loadImpl(L2Properties properties)
		{
			NET_LISTEN_IP = properties.getString("ListenIP", "0.0.0.0");
			NET_LISTEN_PORT = properties.getInteger("ListenPort", 2106);
			
			NET_ENABLE_LEGACY = properties.getBoolean("EnableLegacyListener", true);
			NET_LEGACY_LISTEN_IP = properties.getString("LegacyListenIP", "127.0.0.1");
			NET_LEGACY_LISTEN_PORT = properties.getInteger("LegacyListenPort", 9014);
		}
	}
	
	@ConfigClass(folderName = "config", fileName = "service")
	public static final class ServiceConfig extends ConfigPropertiesLoader
	{
		/** Whether to behave as a traditional login server */
		public static boolean SVC_FORCE_LEGACY;
		
		/** Whether to check for obvious signs that client has GameGuard disabled */
		public static boolean SVC_CHECK_GAMEGUARD;
		/** Whether to show NCSoft's EULA before the game server list */
		public static boolean SVC_SHOW_EULA;
		
		/** Whether to authorize only registered game servers */
		public static boolean SVC_STRICT_AUTHORIZATION;
		/** Whether to register game servers if they request an unused ID */
		public static boolean SVC_SAVE_REQUESTS;
		
		@Override
		protected void loadImpl(L2Properties properties)
		{
			SVC_FORCE_LEGACY = properties.getBoolean("ForceLegacyMode", false);
			
			SVC_CHECK_GAMEGUARD = properties.getBoolean("CheckGameGuard", true);
			SVC_SHOW_EULA = properties.getBoolean("ShowEULA", false);
			
			SVC_STRICT_AUTHORIZATION = properties.getBoolean("StrictAuthorization", true);
			SVC_SAVE_REQUESTS = properties.getBoolean("PersistentRequests", false);
		}
	}
}
