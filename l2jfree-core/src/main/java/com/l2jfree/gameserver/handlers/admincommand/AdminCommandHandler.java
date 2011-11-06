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
package com.l2jfree.gameserver.handlers.admincommand;

import java.util.StringTokenizer;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.l2jfree.Startup;
import com.l2jfree.Startup.StartupHook;
import com.l2jfree.Util;
import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.util.HandlerRegistry;
import com.l2jfree.util.concurrent.L2ThreadPool;
import com.l2jfree.util.concurrent.RunnableStatsManager;
import com.l2jfree.util.logging.L2Logger;
import com.l2jfree.util.logging.ListeningLog;
import com.l2jfree.util.logging.ListeningLog.LogListener;

/**
 * @author NB4L1
 */
// FIXME privileges, access rights
// TODO type and object based parameters (detection, validation, etc)
public final class AdminCommandHandler
{
	private static final L2Logger _log = L2Logger.getLogger(AdminCommandHandler.class);
	
	private static final HandlerRegistry<String, IAdminCommandHandler> _handlers =
			new HandlerRegistry<String, IAdminCommandHandler>();
	
	static
	{
		Startup.addStartupHook(new StartupHook() {
			@Override
			public void onStartup()
			{
				_log.info("AdminCommandHandler: Loaded " + _handlers.size() + " handlers.");
			}
		});
	}
	
	public static void register(IAdminCommandHandler handler)
	{
		_handlers.register(handler.getAdminCommand(), handler);
	}
	
	public static void useAdminCommand(final L2Player activeChar, final String message)
	{
		final StringTokenizer st = new StringTokenizer(message, " ");
		
		final String command = st.nextToken();
		final String[] params = new String[st.countTokens()];
		for (int i = 0; st.hasMoreTokens(); i++)
			params[i] = st.nextToken();
		
		final IAdminCommandHandler handler = _handlers.get(command);
		
		if (handler == null)
		{
			activeChar.sendMessage("No handler registered.");
			_log.warn("No handler registered for bypass '" + message + "'");
			return;
		}
		
		final Future<?> task = L2ThreadPool.submitLongRunning(new Runnable() {
			@Override
			public void run()
			{
				_activeGm.set(activeChar);
				
				final long begin = System.currentTimeMillis();
				try
				{
					handler.useAdminCommand(command, params, activeChar);
				}
				catch (RuntimeException e)
				{
					activeChar.sendMessage("Exception during execution of  '" + message + "': " + e.toString());
					
					throw e;
				}
				finally
				{
					_activeGm.set(null);
					
					final long runtime = System.currentTimeMillis() - begin;
					
					if (runtime < RunnableStatsManager.MAXIMUM_RUNTIME_IN_MILLISEC_WITHOUT_WARNING_FOR_TASKS)
						return;
					
					activeChar.sendMessage("The execution of '" + message + "' took " + Util.formatNumber(runtime)
							+ " msec.");
				}
			}
		});
		
		try
		{
			task.get(1000, TimeUnit.MILLISECONDS);
			return;
		}
		catch (Exception e)
		{
			activeChar.sendMessage("The execution of '" + message
					+ "' takes more time than 1000 msec, so execution done asynchronusly.");
		}
	}
	
	private static final ThreadLocal<L2Player> _activeGm = new ThreadLocal<L2Player>();
	
	static
	{
		ListeningLog.addListener(new LogListener() {
			@Override
			public void write(String s)
			{
				final L2Player gm = _activeGm.get();
				
				if (gm != null)
					gm.sendMessage(s);
			}
		});
	}
}
