package com.l2jfree.util;

import com.l2jfree.util.concurrent.L2ThreadPool;

/**
 * @author NB4L1
 */
public abstract class RescheduleableTask implements Runnable
{
	protected RescheduleableTask()
	{
		run();
	}
	
	@Override
	public void run()
	{
		try
		{
			runImpl();
		}
		finally
		{
			L2ThreadPool.schedule(this, getScheduleDelay());
		}
	}
	
	protected abstract void runImpl();
	
	protected abstract long getScheduleDelay();
}
