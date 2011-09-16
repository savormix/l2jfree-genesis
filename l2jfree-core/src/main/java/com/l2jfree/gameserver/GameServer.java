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

import java.net.UnknownHostException;

import com.l2jfree.L2Config;
import com.l2jfree.Shutdown;
import com.l2jfree.TerminationStatus;
import com.l2jfree.Util;
import com.l2jfree.gameserver.config.DatabaseConfig;
import com.l2jfree.gameserver.config.NetworkConfig;
import com.l2jfree.gameserver.config.SystemConfig;
import com.l2jfree.gameserver.datatables.PlayerNameTable;
import com.l2jfree.gameserver.datatables.PlayerTemplateTable;
import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.gameobjects.components.ComponentFactory;
import com.l2jfree.gameserver.network.client.Disconnection;
import com.l2jfree.gameserver.network.client.L2ClientConnections;
import com.l2jfree.gameserver.network.client.L2ClientSecurity;
import com.l2jfree.gameserver.network.loginserver.legacy.L2LegacyLoginServerController;
import com.l2jfree.gameserver.sql.PersistentProperties;
import com.l2jfree.gameserver.util.IdFactory;
import com.l2jfree.gameserver.world.L2World;
import com.l2jfree.lang.L2System;
import com.l2jfree.sql.L2Database;
import com.l2jfree.sql.SQLQueryQueue;

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
		
		if (DatabaseConfig.BACKUP_ON_STARTUP && !L2Config.LAUNCHED_FROM_IDE)
			L2Database.backup();
		
		Util.printSection("World");
		
		IdFactory.getInstance();
		SQLQueryQueue.getInstance();
		
		L2Config.load(L2World.class);
		L2Config.load(ComponentFactory.class);
		L2Config.load(PersistentProperties.class);
		
		PlayerNameTable.getInstance();
		
		//GameStatusServer.initInstance();
		
		Util.printSection("Templates");
		
		PlayerTemplateTable.getInstance();
		
		Util.printSection("Network");
		
		{
			L2ClientSecurity.getInstance();
			
			try
			{
				L2ClientConnections.getInstance().openServerSocket(NetworkConfig.LISTEN_IP, NetworkConfig.LISTEN_PORT);
				L2ClientConnections.getInstance().start();
			}
			catch (Throwable e)
			{
				_log.fatal("Could not start Game Server!", e);
				Shutdown.exit(TerminationStatus.RUNTIME_INITIALIZATION_FAILURE);
				return;
			}
			
			try
			{
				L2LegacyLoginServerController.getInstance().connect(NetworkConfig.LOGIN_HOST, NetworkConfig.LOGIN_PORT,
						true);
				L2LegacyLoginServerController.getInstance().start();
			}
			catch (UnknownHostException e)
			{
				_log.warn("Conuld not connect to login server: ", e);
			}
		}
		
		// TODO
		
		Shutdown.addShutdownHook(new Runnable() {
			@Override
			public void run()
			{
				for (L2Player player : L2World.getPlayers())
				{
					try
					{
						new Disconnection(player).defaultSequence(true);
					}
					catch (Throwable t)
					{
						_log.warn("Orderly shutdown sequence interrupted", t);
					}
				}
				
				PersistentProperties.store();
				
				SQLQueryQueue.getInstance().executeNow();
				
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
					L2ClientConnections.getInstance().shutdown();
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
		
		L2Config.applicationLoaded("l2jfree-core", CoreInfo.getFullVersionInfo(), SystemConfig.DUMP_HEAP_AFTER_STARTUP);
	}
}
