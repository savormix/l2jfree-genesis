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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.ArrayUtils;

import com.l2jfree.L2Config;
import com.l2jfree.util.ArrayBunch;
import com.l2jfree.util.concurrent.RunnableStatsManager.SortBy;
import com.l2jfree.util.logging.L2Logger;

// TODO
public final class L2ThreadPool
{
	private static final L2Logger _log = L2Logger.getLogger(L2ThreadPool.class);
	
	public static final long MAXIMUM_RUNTIME_IN_MILLISEC_WITHOUT_WARNING = 5000;
	
	private static final long MAX_DELAY = TimeUnit.NANOSECONDS.toMillis(Long.MAX_VALUE - System.nanoTime()) / 2;
	
	private static ScheduledThreadPoolExecutor[] _scheduledPools = new ScheduledThreadPoolExecutor[0];
	private static ThreadPoolExecutor[] _instantPools = new ThreadPoolExecutor[0];
	private static ThreadPoolExecutor[] _longRunningPools = new ThreadPoolExecutor[0];
	
	private static int getPoolSize(ThreadPoolExecutor[] threadPools)
	{
		int result = 0;
		
		for (ThreadPoolExecutor threadPool : threadPools)
			result += threadPool.getPoolSize();
		
		return result;
	}
	
	public static void initThreadPools(ThreadPoolInitializer initializer) throws Exception
	{
		if (_scheduledPools != null || _instantPools != null || _longRunningPools != null)
			throw new Exception("The thread pool has been already set!");
		
		initializer.initThreadPool();
		
		_scheduledPools = initializer.getScheduledPools();
		_instantPools = initializer.getInstantPools();
		_longRunningPools = initializer.getLongRunningPools();
		
		if (ArrayUtils.isEmpty(_scheduledPools))
		{
			_log.info("No scheduled thread pool has been manually initialized, so initializing default one.");
			
			_scheduledPools = new ScheduledThreadPoolExecutor[] { new ScheduledThreadPoolExecutor( //
					// int corePoolSize
					4) };
		}
		
		if (ArrayUtils.isEmpty(_instantPools))
		{
			_log.info("No instant thread pool has been manually initialized, so initializing default one.");
			
			_instantPools = new ThreadPoolExecutor[] { new ThreadPoolExecutor( //
					// int corePoolSize
					0,
					// int maximumPoolSize
					Integer.MAX_VALUE,
					// long keepAliveTime
					60L,
					// TimeUnit unit
					TimeUnit.SECONDS,
					// BlockingQueue<Runnable> workQueue
					new SynchronousQueue<Runnable>()) };
		}
		
		if (ArrayUtils.isEmpty(_longRunningPools))
		{
			_log.info("No long running thread pool has been manually initialized, so initializing default one.");
			
			_longRunningPools = new ThreadPoolExecutor[] { new ThreadPoolExecutor( //
					// int corePoolSize
					0,
					// int maximumPoolSize
					Integer.MAX_VALUE,
					// long keepAliveTime
					60L,
					// TimeUnit unit
					TimeUnit.SECONDS,
					// BlockingQueue<Runnable> workQueue
					new SynchronousQueue<Runnable>()) };
		}
		
		for (ThreadPoolExecutor threadPool : getThreadPools())
		{
			threadPool.setRejectedExecutionHandler(new L2RejectedExecutionHandler());
			threadPool.prestartAllCoreThreads();
		}
		
		scheduleAtFixedRate(new Runnable() {
			@Override
			public void run()
			{
				purge();
			}
		}, 60000, 60000);
		
		_log.info("L2ThreadPool: Initialized with ");
		_log.info("\t... " + getPoolSize(_scheduledPools) + " scheduler,");
		_log.info("\t... " + getPoolSize(_instantPools) + " instant,");
		_log.info("\t... " + getPoolSize(_longRunningPools) + " long running thread(s).");
	}
	
	private static long validate(long delay)
	{
		return Math.max(0, Math.min(MAX_DELAY, delay));
	}
	
	private static final class ThreadPoolExecuteWrapper extends ExecuteWrapper
	{
		private ThreadPoolExecuteWrapper(Runnable runnable)
		{
			super(runnable);
		}
		
		@Override
		protected long getMaximumRuntimeInMillisecWithoutWarning()
		{
			return MAXIMUM_RUNTIME_IN_MILLISEC_WITHOUT_WARNING;
		}
	}
	
	private static int _threadPoolRandomizer;
	
	private static <T> T getRandomPool(T[] threadPools)
	{
		return threadPools[_threadPoolRandomizer++ % threadPools.length];
	}
	
	// ===========================================================================================
	
	public static ScheduledFuture<?> schedule(Runnable r, long delay)
	{
		r = new ThreadPoolExecuteWrapper(r);
		delay = validate(delay);
		
		final ScheduledThreadPoolExecutor stpe = getRandomPool(_scheduledPools);
		final ScheduledFuture<?> sf = stpe.schedule(r, delay, TimeUnit.MILLISECONDS);
		
		return new ScheduledFutureWrapper(sf);
	}
	
	// ===========================================================================================
	
	public static ScheduledFuture<?> scheduleAtFixedRate(Runnable r, long delay, long period)
	{
		r = new ThreadPoolExecuteWrapper(r);
		delay = validate(delay);
		period = validate(period);
		
		final ScheduledThreadPoolExecutor stpe = getRandomPool(_scheduledPools);
		final ScheduledFuture<?> sf = stpe.scheduleAtFixedRate(r, delay, period, TimeUnit.MILLISECONDS);
		
		return new ScheduledFutureWrapper(sf);
	}
	
	// ===========================================================================================
	
	public static void execute(Runnable r)
	{
		r = new ThreadPoolExecuteWrapper(r);
		
		final ThreadPoolExecutor tpe = getRandomPool(_instantPools);
		tpe.execute(r);
	}
	
	public static void executeLongRunning(Runnable r)
	{
		r = new ExecuteWrapper(r);
		
		final ThreadPoolExecutor tpe = getRandomPool(_longRunningPools);
		tpe.execute(r);
	}
	
	// ===========================================================================================
	
	public static Future<?> submit(Runnable r)
	{
		r = new ThreadPoolExecuteWrapper(r);
		
		final ThreadPoolExecutor tpe = getRandomPool(_instantPools);
		final Future<?> f = tpe.submit(r);
		
		return new FutureWrapper(f);
	}
	
	public static Future<?> submitLongRunning(Runnable r)
	{
		r = new ExecuteWrapper(r);
		
		final ThreadPoolExecutor tpe = getRandomPool(_longRunningPools);
		final Future<?> f = tpe.submit(r);
		
		return new FutureWrapper(f);
	}
	
	// ===========================================================================================
	
	public static List<String> getStats()
	{
		List<String> list = new ArrayList<String>();
		
		list.add("");
		
		for (int i = 0; i < _scheduledPools.length; i++)
		{
			list.add("Scheduled pool #" + i + ":");
			list.add("=================================================");
			list.add("\tgetActiveCount: ...... " + _scheduledPools[i].getActiveCount());
			list.add("\tgetCorePoolSize: ..... " + _scheduledPools[i].getCorePoolSize());
			list.add("\tgetPoolSize: ......... " + _scheduledPools[i].getPoolSize());
			list.add("\tgetLargestPoolSize: .. " + _scheduledPools[i].getLargestPoolSize());
			list.add("\tgetMaximumPoolSize: .. " + _scheduledPools[i].getMaximumPoolSize());
			list.add("\tgetCompletedTaskCount: " + _scheduledPools[i].getCompletedTaskCount());
			list.add("\tgetQueuedTaskCount: .. " + _scheduledPools[i].getQueue().size());
			list.add("\tgetTaskCount: ........ " + _scheduledPools[i].getTaskCount());
			list.add("");
		}
		
		for (int i = 0; i < _instantPools.length; i++)
		{
			list.add("Instant pool #" + i + ":");
			list.add("=================================================");
			list.add("\tgetActiveCount: ...... " + _instantPools[i].getActiveCount());
			list.add("\tgetCorePoolSize: ..... " + _instantPools[i].getCorePoolSize());
			list.add("\tgetPoolSize: ......... " + _instantPools[i].getPoolSize());
			list.add("\tgetLargestPoolSize: .. " + _instantPools[i].getLargestPoolSize());
			list.add("\tgetMaximumPoolSize: .. " + _instantPools[i].getMaximumPoolSize());
			list.add("\tgetCompletedTaskCount: " + _instantPools[i].getCompletedTaskCount());
			list.add("\tgetQueuedTaskCount: .. " + _instantPools[i].getQueue().size());
			list.add("\tgetTaskCount: ........ " + _instantPools[i].getTaskCount());
			list.add("");
		}
		
		for (int i = 0; i < _instantPools.length; i++)
		{
			list.add("Long running pool #" + i + ":");
			list.add("=================================================");
			list.add("\tgetActiveCount: ...... " + _longRunningPools[i].getActiveCount());
			list.add("\tgetCorePoolSize: ..... " + _longRunningPools[i].getCorePoolSize());
			list.add("\tgetPoolSize: ......... " + _longRunningPools[i].getPoolSize());
			list.add("\tgetLargestPoolSize: .. " + _longRunningPools[i].getLargestPoolSize());
			list.add("\tgetMaximumPoolSize: .. " + _longRunningPools[i].getMaximumPoolSize());
			list.add("\tgetCompletedTaskCount: " + _longRunningPools[i].getCompletedTaskCount());
			list.add("\tgetQueuedTaskCount: .. " + _longRunningPools[i].getQueue().size());
			list.add("\tgetTaskCount: ........ " + _longRunningPools[i].getTaskCount());
			list.add("");
		}
		
		return list;
	}
	
	private static ThreadPoolExecutor[] getThreadPools()
	{
		final ArrayBunch<ThreadPoolExecutor> bunch = new ArrayBunch<ThreadPoolExecutor>();
		
		bunch.addAll(_scheduledPools);
		bunch.addAll(_instantPools);
		bunch.addAll(_longRunningPools);
		
		return bunch.moveToArray(ThreadPoolExecutor.class);
	}
	
	private static boolean awaitTermination(long timeoutInMillisec) throws InterruptedException
	{
		final long begin = System.currentTimeMillis();
		
		while (System.currentTimeMillis() - begin < timeoutInMillisec)
		{
			boolean done = true;
			
			for (ThreadPoolExecutor threadPool : getThreadPools())
				done &= threadPool.awaitTermination(10, TimeUnit.MILLISECONDS) && threadPool.getActiveCount() == 0;
			
			if (done)
				return true;
		}
		
		return false;
	}
	
	private static int getTaskCount(ThreadPoolExecutor[] threadPools)
	{
		int result = 0;
		
		for (ThreadPoolExecutor threadPool : threadPools)
			result += threadPool.getQueue().size() + threadPool.getActiveCount();
		
		return result;
	}
	
	public static void shutdown()
	{
		final long begin = System.currentTimeMillis();
		
		try
		{
			System.out.println("L2ThreadPool: Shutting down.");
			System.out.println("\t... executing " + getTaskCount(_scheduledPools) + " scheduled tasks.");
			System.out.println("\t... executing " + getTaskCount(_instantPools) + " instant tasks.");
			System.out.println("\t... executing " + getTaskCount(_longRunningPools) + " long running tasks.");
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		
		try
		{
			for (ThreadPoolExecutor threadPool : getThreadPools())
			{
				try
				{
					threadPool.shutdown();
				}
				catch (Throwable t)
				{
					t.printStackTrace();
				}
			}
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		
		boolean success = false;
		try
		{
			success |= awaitTermination(5000);
			
			for (ScheduledThreadPoolExecutor scheduledPool : _scheduledPools)
			{
				scheduledPool.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
				scheduledPool.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
			}
			
			success |= awaitTermination(10000);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		
		try
		{
			System.out.println("\t... success: " + success + " in " + (System.currentTimeMillis() - begin) + " msec.");
			System.out.println("\t... " + getTaskCount(_scheduledPools) + " scheduled tasks left.");
			System.out.println("\t... " + getTaskCount(_instantPools) + " instant tasks left.");
			System.out.println("\t... " + getTaskCount(_longRunningPools) + " long running tasks left.");
			
			if (TimeUnit.HOURS.toMillis(12) < (System.currentTimeMillis() - L2Config.SERVER_STARTED))
				RunnableStatsManager.dumpClassStats(SortBy.TOTAL);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
	
	public static void purge()
	{
		for (ThreadPoolExecutor threadPool : getThreadPools())
			threadPool.purge();
	}
}
