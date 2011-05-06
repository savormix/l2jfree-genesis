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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.l2jfree.config.annotation.ConfigClass;
import com.l2jfree.config.annotation.ConfigField;
import com.l2jfree.config.model.ConfigClassInfo;

public final class DefaultThreadPoolInitializer implements ThreadPoolInitializer
{
	@ConfigClass(folderName = "config", fileName = "threadpool")
	private static class DefaultThreadPoolConfig
	{
		@ConfigField(name = "ScheduledThreadPoolCount", value = "2", eternal = true)
		public static int SCHEDULED_THREAD_POOL_COUNT;
		
		@ConfigField(name = "ThreadsPerScheduledThreadPool", value = "3", eternal = true)
		public static int THREADS_PER_SCHEDULED_THREAD_POOL;
		
		@ConfigField(name = "InstantThreadPoolCount", value = "2", eternal = true)
		public static int INSTANT_THREAD_POOL_COUNT;
		
		@ConfigField(name = "ThreadsPerInstantThreadPool", value = "2", eternal = true)
		public static int THREADS_PER_INSTANT_THREAD_POOL;
	}
	
	private final List<ScheduledThreadPoolExecutor> _scheduledPools = new ArrayList<ScheduledThreadPoolExecutor>();
	private final List<ThreadPoolExecutor> _instantPools = new ArrayList<ThreadPoolExecutor>();
	private final List<ThreadPoolExecutor> _longRunningPools = new ArrayList<ThreadPoolExecutor>();
	
	@Override
	public void initThreadPool() throws Exception
	{
		// loads the config
		ConfigClassInfo.valueOf(DefaultThreadPoolConfig.class).load();
		
		for (int i = 0; i < DefaultThreadPoolConfig.SCHEDULED_THREAD_POOL_COUNT; i++)
		{
			_scheduledPools.add(new ScheduledThreadPoolExecutor( //
					// int corePoolSize
					DefaultThreadPoolConfig.THREADS_PER_SCHEDULED_THREAD_POOL));
			
		}
		
		for (int i = 0; i < DefaultThreadPoolConfig.INSTANT_THREAD_POOL_COUNT; i++)
		{
			_instantPools.add(new ThreadPoolExecutor( //
					// int corePoolSize
					DefaultThreadPoolConfig.THREADS_PER_INSTANT_THREAD_POOL,
					// int maximumPoolSize
					DefaultThreadPoolConfig.THREADS_PER_INSTANT_THREAD_POOL,
					// long keepAliveTime
					0,
					// TimeUnit unit
					TimeUnit.SECONDS,
					// BlockingQueue<Runnable> workQueue
					new ArrayBlockingQueue<Runnable>(100000)));
			
		}
		
		_longRunningPools.add(new ThreadPoolExecutor( //
				// int corePoolSize
				0,
				// int maximumPoolSize
				Integer.MAX_VALUE,
				// long keepAliveTime
				60L,
				// TimeUnit unit
				TimeUnit.SECONDS,
				// BlockingQueue<Runnable> workQueue
				new SynchronousQueue<Runnable>()));
	}
	
	@Override
	public ScheduledThreadPoolExecutor[] getScheduledPools()
	{
		return _scheduledPools.toArray(new ScheduledThreadPoolExecutor[_scheduledPools.size()]);
	}
	
	@Override
	public ThreadPoolExecutor[] getInstantPools()
	{
		return _instantPools.toArray(new ThreadPoolExecutor[_instantPools.size()]);
	}
	
	@Override
	public ThreadPoolExecutor[] getLongRunningPools()
	{
		return _longRunningPools.toArray(new ThreadPoolExecutor[_longRunningPools.size()]);
	}
}
