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
package com.l2jfree.util.concurrent;

import com.l2jfree.util.logging.L2Logger;

/**
 * @author NB4L1
 */
public final class ExecuteWrapper
{
	private static final L2Logger _log = L2Logger.getLogger(ExecuteWrapper.class);
	
	public static Runnable wrap(Runnable r)
	{
		return new TaskWrapper(r);
	}
	
	public static Runnable wrapLongRunning(Runnable r)
	{
		return new LongRunningTaskWrapper(r);
	}
	
	public static final class TaskWrapper implements Runnable
	{
		private final Runnable _runnable;
		
		public TaskWrapper(Runnable runnable)
		{
			_runnable = runnable;
		}
		
		@Override
		public void run()
		{
			ExecuteWrapper.execute(_runnable);
		}
	}
	
	public static final class LongRunningTaskWrapper implements Runnable
	{
		private final Runnable _runnable;
		
		public LongRunningTaskWrapper(Runnable runnable)
		{
			_runnable = runnable;
		}
		
		@Override
		public void run()
		{
			ExecuteWrapper.executeLongRunning(_runnable);
		}
	}
	
	public static void execute(Runnable runnable)
	{
		long begin = System.nanoTime();
		
		try
		{
			runnable.run();
		}
		catch (RuntimeException e)
		{
			_log.warn("Exception in a Runnable execution:", e);
		}
		finally
		{
			RunnableStatsManager.handleStats(runnable.getClass(), System.nanoTime() - begin);
		}
	}
	
	public static void executeLongRunning(Runnable runnable)
	{
		long begin = System.nanoTime();
		
		try
		{
			runnable.run();
		}
		catch (RuntimeException e)
		{
			_log.warn("Exception in a Runnable execution:", e);
		}
		finally
		{
			RunnableStatsManager.handleStats(runnable.getClass(), System.nanoTime() - begin,
					RunnableStatsManager.MAXIMUM_RUNTIME_IN_MILLISEC_WITHOUT_WARNING_FOR_LONG_RUNNING_TASKS);
		}
	}
	
}
