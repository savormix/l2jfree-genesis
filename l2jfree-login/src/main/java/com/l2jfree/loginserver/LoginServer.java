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

import java.io.IOException;

import com.l2jfree.L2Config;
import com.l2jfree.Shutdown;
import com.l2jfree.TerminationStatus;
import com.l2jfree.config.L2Properties;
import com.l2jfree.loginserver.database.L2LoginInstaller;
import com.l2jfree.loginserver.network.client.L2ClientConnections;
import com.l2jfree.loginserver.network.client.L2ClientSecurity;
import com.l2jfree.network.mmocore.MMOConfig;
import com.l2jfree.sql.L2Database;
import com.l2jfree.sql.TableOptimizer;
import com.l2jfree.util.concurrent.L2ThreadPool;
import com.l2jfree.util.logging.L2Logger;

/**
 * This class contains the application entry point.
 * 
 * @author savormix
 */
public final class LoginServer extends L2Config
{
	static final L2Logger _log = L2Logger.getLogger(LoginServer.class);
	
	// TODO should be sorted into groups and redone with annotation loaders
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
	/** Whether to optimize database tables */
	public static boolean DB_OPTIMIZE;
	/** Whether to install database tables */
	public static boolean DB_INSTALL;
	
	/** Login server listens for connections on this IP address */
	public static String NET_LISTEN_IP;
	/** Login server listens for connections on this port */
	public static int NET_LISTEN_PORT;
	
	/** Whether to behave as a traditional login server */
	public static boolean SVC_FORCE_LEGACY;
	/** Whether to check for obvious signs that client has GameGuard disabled */
	public static boolean SVC_CHECK_GAMEGUARD;
	/** Whether to show NCSoft's EULA before the game server list */
	public static boolean SVC_SHOW_EULA;
	
	/**
	 * Launches the login server.
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args)
	{
		registerConfig(new DatabaseConfig());
		registerConfig(new NetworkConfig());
		registerConfig(new ServiceConfig());
		
		try
		{
			loadConfigs();
		}
		catch (Exception e)
		{
			_log.fatal("Could not load configuration files!", e);
			Shutdown.exit(TerminationStatus.RUNTIME_INVALID_CONFIGURATION);
		}
		
		L2LoginIdentifier.getInstance().getUID();
		
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
			_log.fatal("Could not initialize DB!", e);
			Shutdown.exit(TerminationStatus.RUNTIME_INITIALIZATION_FAILURE);
		}
		TableOptimizer.optimize(DB_URL);
		L2LoginInstaller.getInstance().install();
		
		L2ClientSecurity.getInstance();
		
		MMOConfig cfg = new MMOConfig("Experimental Login");
		cfg.setSelectorSleepTime(40 * 1000 * 1000);
		cfg.setThreadCount(1);
		cfg.setAcceptTimeout(5 * 1000);
		try
		{
			final L2ClientConnections llc = new L2ClientConnections(cfg);
			llc.openServerSocket(NET_LISTEN_IP, NET_LISTEN_PORT);
			llc.start();
			_log.info("Login server ready.");
			
			Shutdown.addShutdownHook(new Runnable() {
				@Override
				public void run()
				{
					try
					{
						llc.shutdown();
					}
					catch (InterruptedException e)
					{
						_log.warn("Orderly shutdown sequence interrupted", e);
					}
				}
			});
		}
		catch (IOException e)
		{
			_log.fatal("Could not start login server!", e);
			Shutdown.exit(TerminationStatus.RUNTIME_INITIALIZATION_FAILURE);
		}
	}
	
	/**
	 * @author savormix
	 *
	 */
	public static class DatabaseConfig extends ConfigPropertiesLoader
	{
		/* (non-Javadoc)
		 * @see com.l2jfree.L2Config.ConfigPropertiesLoader#loadImpl(com.l2jfree.config.L2Properties)
		 */
		@Override
		protected void loadImpl(L2Properties properties)
		{
			DB_MAX_CONNECTIONS = properties.getInteger("MaxConnectionsInPool", 5);
			DB_DRIVER = properties.getString("DriverClass");
			
			DB_URL = properties.getString("JdbcUrl");
			if (!DB_URL.startsWith("jdbc:"))
				DB_URL = "jdbc:" + DB_URL;
			
			DB_USER = properties.getString("Login");
			DB_PASSWORD = properties.getString("Password", "");
			
			DB_OPTIMIZE = properties.getBool("OptimizeTables", true);
			DB_INSTALL = properties.getBool("AutoCreateTables", false);
		}
		
		/* (non-Javadoc)
		 * @see com.l2jfree.L2Config.ConfigLoader#getName()
		 */
		@Override
		protected String getName()
		{
			return "database";
		}
	}
	
	protected static class NetworkConfig extends ConfigPropertiesLoader
	{
		/* (non-Javadoc)
		 * @see com.l2jfree.L2Config.ConfigPropertiesLoader#loadImpl(com.l2jfree.config.L2Properties)
		 */
		@Override
		protected void loadImpl(L2Properties properties)
		{
			NET_LISTEN_IP = properties.getString("ListenIP", "0.0.0.0");
			NET_LISTEN_PORT = properties.getInteger("ListenPort", 2106);
		}
		
		/* (non-Javadoc)
		 * @see com.l2jfree.L2Config.ConfigLoader#getName()
		 */
		@Override
		protected String getName()
		{
			return "network";
		}
	}
	
	protected static class ServiceConfig extends ConfigPropertiesLoader
	{
		/* (non-Javadoc)
		 * @see com.l2jfree.L2Config.ConfigPropertiesLoader#loadImpl(com.l2jfree.config.L2Properties)
		 */
		@Override
		protected void loadImpl(L2Properties properties)
		{
			SVC_FORCE_LEGACY = properties.getBool("ForceLegacyMode", false);
			SVC_CHECK_GAMEGUARD = properties.getBool("CheckGameGuard", true);
			SVC_SHOW_EULA = properties.getBool("ShowEULA", false);
		}
		
		/* (non-Javadoc)
		 * @see com.l2jfree.L2Config.ConfigLoader#getName()
		 */
		@Override
		protected String getName()
		{
			return "service";
		}
	}
}
