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
import com.l2jfree.config.L2Properties;
import com.l2jfree.loginserver.network.client.L2ClientConnections;
import com.l2jfree.loginserver.network.client.L2ClientSecurity;
import com.l2jfree.network.mmocore.MMOConfig;
import com.l2jfree.util.concurrent.L2ThreadPool;
import com.l2jfree.util.logging.L2Logger;

/**
 * This class contains the application entry point.
 * @author savormix
 */
public final class LoginServer extends L2Config
{
	static final L2Logger _log = L2Logger.getLogger(LoginServer.class);
	
	// TODO should be sorted into groups and redone with annotation loaders
	/** Login server listens for connections on this IP address */
	public static String LISTEN_IP;
	/** Login server listens for connections on this port */
	public static int LISTEN_PORT;
	
	/** Whether to ignore obvious signs that client has GameGuard disabled */
	public static boolean BYPASS_GAMEGUARD;
	/** Whether to show NCSoft's EULA before the game server list */
	public static boolean SHOW_EULA;
	
	/**
	 * Launches the login server.
	 * @param args ignored
	 */
	public static void main(String[] args)
	{
		registerConfig(new NetworkConfig());
		registerConfig(new ServiceConfig());
		
		try
		{
			loadConfigs();
		}
		catch (Exception e)
		{
			_log.fatal("Could not load configuration files!", e);
		}
		
		try
		{
			L2ThreadPool.initThreadPools(new L2LoginThreadPools());
		}
		catch (Exception e)
		{
			_log.fatal("Could not initialize thread pools!", e);
		}
		
		L2ClientSecurity.getInstance();
		
		MMOConfig cfg = new MMOConfig("Experimental Login");
		cfg.setSelectorSleepTime(40 * 1000 * 1000);
		cfg.setThreadCount(1);
		cfg.setAcceptTimeout(5 * 1000);
		try
		{
			final L2ClientConnections llc = new L2ClientConnections(cfg);
			llc.openServerSocket(LISTEN_IP, LISTEN_PORT);
			llc.start();
			Runtime.getRuntime().addShutdownHook(new Thread("Terminator")
			{
				/* (non-Javadoc)
				 * @see java.lang.Thread#run()
				 */
				@Override
				public void run()
				{
					try
					{
						llc.shutdown();
					}
					catch (InterruptedException e)
					{
						_log.warn(
								"Orderly shutdown sequence interrupted", e
						);
					}
				}
			});
		}
		catch (IOException e)
		{
			_log.fatal("Could not start login server!", e);
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
			LISTEN_IP = properties.getString("ListenIP", "0.0.0.0");
			LISTEN_PORT = properties.getInteger("ListenPort", 2106);
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
			BYPASS_GAMEGUARD = properties.getBool("BypassGameGuard", false);
			SHOW_EULA = properties.getBool("ShowEULA", false);
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
