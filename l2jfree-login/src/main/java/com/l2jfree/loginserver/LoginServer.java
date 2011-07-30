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
import com.l2jfree.loginserver.network.client.L2ClientConnections;
import com.l2jfree.loginserver.network.client.L2ClientSecurity;
import com.l2jfree.loginserver.network.gameserver.L2GameServerCache;
import com.l2jfree.loginserver.network.legacy.L2LegacyConnections;
import com.l2jfree.loginserver.network.legacy.L2LegacySecurity;
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
	 */
	public static void main(String[] args)
	{
		L2LoginIdentifier.getInstance().getUID();
		
		if (DB_OPTIMIZE)
			L2Database.optimize();
		
		L2GameServerCache.getInstance();
		
		if (Config.NET_ENABLE_LEGACY || Config.SVC_FORCE_LEGACY)
		{
			L2LegacySecurity.getInstance();
			
			try
			{
				L2LegacyConnections.getInstance().openServerSocket(Config.NET_LEGACY_LISTEN_IP, Config.NET_LEGACY_LISTEN_PORT);
				L2LegacyConnections.getInstance().start();
			}
			catch (Throwable e)
			{
				_log.fatal("Could not start legacy listener!", e);
				Shutdown.exit(TerminationStatus.RUNTIME_INITIALIZATION_FAILURE);
				return;
			}
		}
		
		L2ClientSecurity.getInstance();
		
		try
		{
			L2ClientConnections.getInstance().openServerSocket(Config.NET_LISTEN_IP, Config.NET_LISTEN_PORT);
			L2ClientConnections.getInstance().start();
		}
		catch (Throwable e)
		{
			_log.fatal("Could not start login server!", e);
			Shutdown.exit(TerminationStatus.RUNTIME_INITIALIZATION_FAILURE);
			return;
		}
		
		// TODO
		
		L2Config.onStartup();
		
		_log.info("Login server ready.");
		
		Shutdown.addShutdownHook(new Runnable() {
			@Override
			public void run()
			{
				try
				{
					L2ClientConnections.getInstance().shutdown();
				}
				catch (Throwable t)
				{
					_log.warn("Orderly shutdown sequence interrupted", t);
				}
			}
		});
	}
}
