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
import com.l2jfree.lang.L2System;
import com.l2jfree.loginserver.config.DatabaseConfig;
import com.l2jfree.loginserver.config.NetworkConfig;
import com.l2jfree.loginserver.config.ServiceConfig;
import com.l2jfree.loginserver.config.SystemConfig;
import com.l2jfree.loginserver.network.client.L2ClientController;
import com.l2jfree.loginserver.network.gameserver.L2GameServerCache;
import com.l2jfree.loginserver.network.gameserver.legacy.L2LegacyGameServerController;
import com.l2jfree.sql.L2Database;

/**
 * This class contains the application entry point.
 * 
 * @author savormix
 */
public final class LoginServer extends Config
{
	/**
	 * Launches the login server.
	 * 
	 * @param args ignored
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		if (DatabaseConfig.OPTIMIZE)
			L2Database.optimize();
		
		if (DatabaseConfig.BACKUP_ON_STARTUP && !L2Config.LAUNCHED_FROM_IDE)
			L2Database.backup();
		
		L2LoginIdentifier.getInstance().getUID();
		
		L2GameServerCache.getInstance();
		
		//LoginStatusServer.initInstance();
		
		try
		{
			if (NetworkConfig.ENABLE_LEGACY || ServiceConfig.FORCE_LEGACY)
			{
				final L2LegacyGameServerController lgsc = L2LegacyGameServerController.getInstance();
				lgsc.openServerSocket(NetworkConfig.LEGACY_LISTEN_IP, NetworkConfig.LEGACY_LISTEN_PORT);
				lgsc.start();
			}
		}
		catch (Throwable e)
		{
			_log.fatal("Could not start Legacy Game Server listener!", e);
			Shutdown.exit(TerminationStatus.RUNTIME_INITIALIZATION_FAILURE);
			return;
		}
		
		try
		{
			final L2ClientController lcc = L2ClientController.getInstance();
			lcc.openServerSocket(NetworkConfig.LISTEN_IP, NetworkConfig.LISTEN_PORT);
			lcc.start();
		}
		catch (Throwable e)
		{
			_log.fatal("Could not start Login Server!", e);
			Shutdown.exit(TerminationStatus.RUNTIME_INITIALIZATION_FAILURE);
			return;
		}
		
		// TODO
		
		Shutdown.addShutdownHook(new Runnable() {
			@Override
			public void run()
			{
				try
				{
					if (SystemConfig.DUMP_HEAP_BEFORE_SHUTDOWN && !L2Config.LAUNCHED_FROM_IDE)
						L2System.dumpHeap(true);
				}
				catch (Throwable t)
				{
					_log.warn("Orderly shutdown sequence interrupted", t);
				}
				
				try
				{
					L2ClientController.getInstance().shutdown();
				}
				catch (Throwable t)
				{
					_log.warn("Orderly shutdown sequence interrupted", t);
				}
				
				try
				{
					if (NetworkConfig.ENABLE_LEGACY || ServiceConfig.FORCE_LEGACY)
						L2LegacyGameServerController.getInstance().shutdown();
				}
				catch (Throwable t)
				{
					_log.warn("Orderly shutdown sequence interrupted", t);
				}
				
				try
				{
					if (DatabaseConfig.BACKUP_ON_SHUTDOWN && !L2Config.LAUNCHED_FROM_IDE)
						L2Database.backup();
				}
				catch (Throwable t)
				{
					_log.warn("Orderly shutdown sequence interrupted", t);
				}
			}
		});
		
		L2Config.applicationLoaded("l2jfree-login", LoginInfo.getFullVersionInfo(),
				SystemConfig.DUMP_HEAP_AFTER_STARTUP);
		
		ServiceConfig.STRICT_AUTHORIZATION = false;
	}
}
