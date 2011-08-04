package com.l2jfree.util.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author NB4L1
 */
public abstract class AbstractThreadPoolInitializer implements ThreadPoolInitializer
{
	private final List<ScheduledThreadPoolExecutor> _scheduledPools = new ArrayList<ScheduledThreadPoolExecutor>();
	private final List<ThreadPoolExecutor> _instantPools = new ArrayList<ThreadPoolExecutor>();
	private final List<ThreadPoolExecutor> _longRunningPools = new ArrayList<ThreadPoolExecutor>();
	
	protected final void addScheduledPool(ScheduledThreadPoolExecutor pool)
	{
		_scheduledPools.add(pool);
	}
	
	protected final void addInstantPool(ThreadPoolExecutor pool)
	{
		_instantPools.add(pool);
	}
	
	protected final void addLongRunningPool(ThreadPoolExecutor pool)
	{
		_longRunningPools.add(pool);
	}
	
	@Override
	public abstract void initThreadPool() throws Exception;
	
	@Override
	public final ScheduledThreadPoolExecutor[] getScheduledPools()
	{
		return _scheduledPools.toArray(new ScheduledThreadPoolExecutor[_scheduledPools.size()]);
	}
	
	@Override
	public final ThreadPoolExecutor[] getInstantPools()
	{
		return _instantPools.toArray(new ThreadPoolExecutor[_instantPools.size()]);
	}
	
	@Override
	public final ThreadPoolExecutor[] getLongRunningPools()
	{
		return _longRunningPools.toArray(new ThreadPoolExecutor[_longRunningPools.size()]);
	}
}
