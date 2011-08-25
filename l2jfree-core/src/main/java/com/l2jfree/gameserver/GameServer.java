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
import com.l2jfree.gameserver.config.DatabaseConfig;
import com.l2jfree.gameserver.config.NetworkConfig;
import com.l2jfree.gameserver.config.SystemConfig;
import com.l2jfree.gameserver.network.client.L2ClientController;
import com.l2jfree.gameserver.network.client.L2ClientSecurity;
import com.l2jfree.lang.L2System;
import com.l2jfree.sql.L2Database;

/**
 * This class contains the application entry point.
 * 
 * @author NB4L1
 * @author savormix
 */
public final class GameServer extends Config
{
	/**
	 * Launches the game server.
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args)
	{
		if (DatabaseConfig.OPTIMIZE)
			L2Database.optimize();
		
		if (DatabaseConfig.BACKUP_ON_STARTUP)
			L2Database.backup();
		
		{
			L2ClientSecurity.getInstance();
			
			try
			{
				L2ClientController.getInstance().openServerSocket(NetworkConfig.LISTEN_IP, NetworkConfig.LISTEN_PORT);
				L2ClientController.getInstance().start();
			}
			catch (Throwable e)
			{
				_log.fatal("Could not start login server!", e);
				Shutdown.exit(TerminationStatus.RUNTIME_INITIALIZATION_FAILURE);
				return;
			}
		}
		
		// TODO
		
		Shutdown.addShutdownHook(new Runnable() {
			@Override
			public void run()
			{
				try
				{
					if (SystemConfig.DUMP_HEAP_BEFORE_SHUTDOWN)
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
					if (DatabaseConfig.BACKUP_ON_SHUTDOWN)
						L2Database.backup();
				}
				catch (Throwable t)
				{
					_log.warn("Orderly shutdown sequence interrupted", t);
				}
			}
		});
		
		L2Config.applicationLoaded("l2jfree-core", CoreInfo.getFullVersionInfo());
		
		if (SystemConfig.DUMP_HEAP_AFTER_STARTUP)
			L2System.dumpHeap(true);
	}
}
