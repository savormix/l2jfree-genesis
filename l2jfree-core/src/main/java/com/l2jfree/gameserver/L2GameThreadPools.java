package com.l2jfree.gameserver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.l2jfree.util.concurrent.ThreadPoolInitializer;

/**
 * @author NB4L1
 */
public final class L2GameThreadPools implements ThreadPoolInitializer
{
	private final List<ScheduledThreadPoolExecutor> _scheduledPools = new ArrayList<ScheduledThreadPoolExecutor>();
	private final List<ThreadPoolExecutor> _instantPools = new ArrayList<ThreadPoolExecutor>();
	private final List<ThreadPoolExecutor> _longRunningPools = new ArrayList<ThreadPoolExecutor>();
	
	@Override
	public void initThreadPool() throws Exception
	{
		// TODO Auto-generated method stub
		for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++)
		{
			_scheduledPools.add(new ScheduledThreadPoolExecutor( //
					// int corePoolSize
					4));
			
		}
		
		for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++)
		{
			_instantPools.add(new ThreadPoolExecutor( //
					// int corePoolSize
					2,
					// int maximumPoolSize
					2,
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
