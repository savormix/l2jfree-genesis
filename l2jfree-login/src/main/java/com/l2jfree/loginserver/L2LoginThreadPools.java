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

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.l2jfree.loginserver.config.ThreadPoolConfig;
import com.l2jfree.util.concurrent.AbstractThreadPoolInitializer;

/**
 * @author savormix
 */
public final class L2LoginThreadPools extends AbstractThreadPoolInitializer
{
	@Override
	public void initThreadPool() throws Exception
	{
		int threadPerScheduledThreadPool = ThreadPoolConfig.THREADS_PER_SCHEDULED_THREAD_POOL;
		if (threadPerScheduledThreadPool == -1)
			threadPerScheduledThreadPool = Runtime.getRuntime().availableProcessors();
		
		addScheduledPool(new ScheduledThreadPoolExecutor( //
				// int corePoolSize
				threadPerScheduledThreadPool));
		
		addInstantPool(new ThreadPoolExecutor( //
				// int corePoolSize
				1,
				// int maximumPoolSize
				Integer.MAX_VALUE,
				// long keepAliveTime
				60L,
				// TimeUnit unit
				TimeUnit.SECONDS,
				// BlockingQueue<Runnable> workQueue
				new SynchronousQueue<Runnable>()));
		
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
