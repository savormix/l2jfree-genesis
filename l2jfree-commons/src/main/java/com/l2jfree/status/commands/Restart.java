package com.l2jfree.status.commands;

import com.l2jfree.Shutdown;
import com.l2jfree.TerminationStatus;
import com.l2jfree.status.StatusCommand;

/**
 * @author NB4L1
 */
public final class Restart extends StatusCommand
{
	public Restart()
	{
		super("restarts the server in [time] seconds", "restart");
	}
	
	@Override
	protected void useCommand(String command, String params)
	{
		try
		{
			int val = Integer.parseInt(params);
			Shutdown.start(TerminationStatus.MANUAL_RESTART, val, getHostAddress());
			println("Server Will Restart In " + val + " Seconds!");
			println("Type \"abort\" To Abort Shutdown!");
		}
		catch (NumberFormatException e)
		{
			Shutdown.exit(TerminationStatus.MANUAL_RESTART, getHostAddress());
		}
	}
	
	@Override
	protected String getParameterUsage()
	{
		return "[time]";
	}
}
