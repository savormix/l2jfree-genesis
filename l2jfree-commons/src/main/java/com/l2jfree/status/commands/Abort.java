package com.l2jfree.status.commands;

import com.l2jfree.Shutdown;
import com.l2jfree.status.StatusCommand;

/**
 * @author NB4L1
 */
public final class Abort extends StatusCommand
{
	public Abort()
	{
		super("aborts shutdown/restart", "abort");
	}
	
	@Override
	protected void useCommand(String command, String params)
	{
		Shutdown.abort(getHostAddress());
		println("OK! - Shutdown/Restart Aborted.");
	}
}
