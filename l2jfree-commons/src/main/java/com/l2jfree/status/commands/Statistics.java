package com.l2jfree.status.commands;

import com.l2jfree.L2Config;
import com.l2jfree.lang.L2System;
import com.l2jfree.status.StatusCommand;

/**
 * @author NB4L1
 */
public final class Statistics extends StatusCommand
{
	public Statistics()
	{
		super("displays basic server statistics", "status", "stats");
	}
	
	@Override
	protected void useCommand(String command, String params)
	{
		println("Server statistics: ");
		println("  --->  Server uptime: " + L2Config.getUptime());
		println("  ---> Active threads: " + Thread.activeCount());
		println("  --->    Memory used: " + L2System.usedMemory());
	}
}
