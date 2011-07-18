package com.l2jfree.status.commands;

import com.l2jfree.status.StatusCommand;
import com.l2jfree.util.concurrent.L2ThreadPool;

/**
 * @author NB4L1
 */
public final class Purge extends StatusCommand
{
	public Purge()
	{
		super("purges the threadpool", "purge");
	}
	
	@Override
	protected void useCommand(String command, String params)
	{
		L2ThreadPool.purge();
		
		println("Threadpool purged.");
	}
}
