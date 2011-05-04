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

import java.util.concurrent.TimeUnit;

import com.l2jfree.lang.L2TextBuilder;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author NB4L1
 */
public class ExecuteWrapper implements Runnable
{
	private static final L2Logger _log = L2Logger.getLogger(ExecuteWrapper.class);
	
	private final Runnable _runnable;
	
	public ExecuteWrapper(Runnable runnable)
	{
		_runnable = runnable;
	}
	
	@Override
	public final void run()
	{
		ExecuteWrapper.execute(_runnable, getMaximumRuntimeInMillisecWithoutWarning());
	}
	
	protected long getMaximumRuntimeInMillisecWithoutWarning()
	{
		return Long.MAX_VALUE;
	}
	
	public static void execute(Runnable runnable)
	{
		execute(runnable, Long.MAX_VALUE);
	}
	
	public static void execute(Runnable runnable, long maximumRuntimeInMillisecWithoutWarning)
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
			long runtimeInNanosec = System.nanoTime() - begin;
			Class<? extends Runnable> clazz = runnable.getClass();
			
			RunnableStatsManager.handleStats(clazz, runtimeInNanosec);
			
			long runtimeInMillisec = TimeUnit.NANOSECONDS.toMillis(runtimeInNanosec);
			
			if (runtimeInMillisec > maximumRuntimeInMillisecWithoutWarning)
			{
				L2TextBuilder tb = L2TextBuilder.newInstance();
				
				tb.append(clazz);
				tb.append(" - execution time: ");
				tb.append(runtimeInMillisec);
				tb.append("msec");
				
				_log.warn(tb.moveToString());
			}
		}
	}
}
