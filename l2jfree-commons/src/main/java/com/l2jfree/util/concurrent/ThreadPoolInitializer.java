package com.l2jfree.util.concurrent;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

public interface ThreadPoolInitializer
{
	public void initThreadPool() throws Exception;
	
	public ScheduledThreadPoolExecutor[] getScheduledPools();
	
	public ThreadPoolExecutor[] getInstantPools();
	
	public ThreadPoolExecutor[] getLongRunningPools();
}
