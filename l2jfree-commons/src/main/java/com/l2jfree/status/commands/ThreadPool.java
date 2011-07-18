package com.l2jfree.status.commands;

import com.l2jfree.status.StatusCommand;
import com.l2jfree.util.concurrent.L2ThreadPool;

/**
 * @author NB4L1
 */
public final class ThreadPool extends StatusCommand
{
	public ThreadPool()
	{
		super("shows threadpool statistics", "threadpool");
	}
	
	@Override
	protected void useCommand(String command, String params)
	{
		for (String line : L2ThreadPool.getStats())
			println(line);
	}
}
