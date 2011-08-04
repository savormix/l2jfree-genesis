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

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.l2jfree.gameserver.config.ThreadPoolConfig;
import com.l2jfree.util.concurrent.AbstractThreadPoolInitializer;

/**
 * @author savormix
 */
public final class L2CoreThreadPools extends AbstractThreadPoolInitializer
{
	@Override
	public void initThreadPool() throws Exception
	{
		int scheduledThreadPoolCount = ThreadPoolConfig.SCHEDULED_THREAD_POOL_COUNT;
		if (scheduledThreadPoolCount == -1)
			scheduledThreadPoolCount = Runtime.getRuntime().availableProcessors();
		
		for (int i = 0; i < scheduledThreadPoolCount; i++)
		{
			addScheduledPool(new ScheduledThreadPoolExecutor( //
					// int corePoolSize
					ThreadPoolConfig.THREADS_PER_SCHEDULED_THREAD_POOL));
			
		}
		
		int instantThreadPoolCount = ThreadPoolConfig.INSTANT_THREAD_POOL_COUNT;
		if (instantThreadPoolCount == -1)
			instantThreadPoolCount = Runtime.getRuntime().availableProcessors();
		
		for (int i = 0; i < instantThreadPoolCount; i++)
		{
			addInstantPool(new ThreadPoolExecutor( //
					// int corePoolSize
					ThreadPoolConfig.THREADS_PER_INSTANT_THREAD_POOL,
					// int maximumPoolSize
					ThreadPoolConfig.THREADS_PER_INSTANT_THREAD_POOL,
					// long keepAliveTime
					0,
					// TimeUnit unit
					TimeUnit.SECONDS,
					// BlockingQueue<Runnable> workQueue
					new ArrayBlockingQueue<Runnable>(100000)));
			
		}
		
		addLongRunningPool(new ThreadPoolExecutor( //
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
}
