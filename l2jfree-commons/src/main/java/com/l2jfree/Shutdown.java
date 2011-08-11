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
package com.l2jfree;

import java.util.HashSet;
import java.util.Set;

/**
 * This class provides the functions for shutting down and restarting the server.
 * 
 * @author NB4L1
 */
public final class Shutdown
{
	private Shutdown()
	{
		// utility class
	}
	
	private static TerminationStatus _mode = TerminationStatus.INVALID;
	private static ShutdownCounter _shutdownCounter;
	
	public static boolean isInProgress()
	{
		return _shutdownCounter != null;
	}
	
	public static synchronized void start(TerminationStatus mode, int seconds, String initiator)
	{
		if (isInProgress())
			abort(initiator);
		
		if (initiator != null)
			System.err.println(initiator + " issued a shutdown command: " + mode.getDescription() + " in " + seconds
					+ " seconds!");
		else
			System.err.println("A shutdown command was issued: " + mode.getDescription() + " in " + seconds
					+ " seconds!");
		
		_mode = mode;
		
		_shutdownCounter = new ShutdownCounter(seconds);
		_shutdownCounter.start();
	}
	
	public static synchronized void abort(String initiator)
	{
		if (!isInProgress())
			return;
		
		if (initiator != null)
			System.err.println(initiator + " issued an abort abort: " + _mode.getDescription() + " has been stopped!");
		else
			System.err.println("An abort command was issued: " + _mode.getDescription() + " has been stopped!");
		
		_mode = TerminationStatus.INVALID;
		
		_shutdownCounter = null;
	}
	
	private static final class ShutdownCounter extends Thread
	{
		private int _counter;
		
		private ShutdownCounter(int counter)
		{
			_counter = counter;
		}
		
		@Override
		public void run()
		{
			while (_counter > 0 && this == _shutdownCounter)
			{
				_counter--;
				
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			
			// shutdown aborted
			if (this != _shutdownCounter)
				return;
			
			// last point where logging is operational :(
			System.err.println("Shutdown countdown is over: " + _mode.getDescription() + " NOW!");
			
			Runtime.getRuntime().exit(_mode.getStatusCode());
		}
	}
	
	/**
	 * Issues an orderly shutdown.
	 * 
	 * @param initiator initiator's ID/name
	 */
	public static void shutdown(String initiator)
	{
		exit(TerminationStatus.MANUAL_SHUTDOWN, initiator);
	}
	
	/**
	 * Issues an orderly shutdown and requests a restart.
	 * 
	 * @param initiator initiator's ID/name
	 */
	public static void restart(String initiator)
	{
		exit(TerminationStatus.MANUAL_RESTART, initiator);
	}
	
	/**
	 * Exits the server.
	 * 
	 * @param mode indicates the cause and/or effect of the action
	 */
	public static void exit(TerminationStatus mode)
	{
		exit(mode, null);
	}
	
	/**
	 * Exits the server.
	 * 
	 * @param mode indicates the cause and/or effect of the action
	 * @param initiator initiator's ID/name
	 */
	public static void exit(TerminationStatus mode, String initiator)
	{
		try
		{
			if (initiator != null)
				System.err.println(initiator + " issued a shutdown command: " + mode.getDescription() + "!");
			else
				System.err.println("A shutdown command was issued: " + mode.getDescription() + "!");
		}
		finally
		{
			Runtime.getRuntime().exit(mode.getStatusCode());
		}
	}
	
	/**
	 * Issues a forced shutdown and requests a restart.
	 * 
	 * @param initiator initiator's ID/name
	 */
	public static void halt(String initiator)
	{
		halt(TerminationStatus.MANUAL_RESTART, initiator);
	}
	
	/**
	 * Halts the server.
	 * 
	 * @param mode indicates the cause and/or effect of the action
	 */
	public static void halt(TerminationStatus mode)
	{
		halt(mode, null);
	}
	
	/**
	 * Halts the server.
	 * 
	 * @param mode indicates the cause and/or effect of the action
	 * @param initiator initiator's ID/name
	 */
	public static void halt(TerminationStatus mode, String initiator)
	{
		try
		{
			if (initiator != null)
				System.err.println(initiator + " issued a halt command: " + mode.getDescription() + "!");
			else
				System.err.println("A halt command was issued: " + mode.getDescription() + "!");
			
			L2Config.flush();
		}
		finally
		{
			Runtime.getRuntime().halt(mode.getStatusCode());
		}
	}
	
	public static void initShutdownHook()
	{
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run()
			{
				Util.printSection("Shutdown");
				
				try
				{
					runShutdownHooks();
					
					L2Config.flush();
				}
				finally
				{
					Runtime.getRuntime().halt(_mode.getStatusCode());
				}
			}
		});
	}
	
	private static final Set<Runnable> _shutdownHooks = new HashSet<Runnable>();
	
	public static synchronized void addShutdownHook(Runnable hook)
	{
		_shutdownHooks.add(hook);
	}
	
	private static synchronized void runShutdownHooks()
	{
		for (Runnable shutdownHook : _shutdownHooks)
		{
			try
			{
				shutdownHook.run();
			}
			catch (Throwable t)
			{
				t.printStackTrace();
			}
		}
		
		L2Config.shutdownApplication();
	}
}
