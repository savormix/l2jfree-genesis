package com.l2jfree.status.commands;

import com.l2jfree.Shutdown;
import com.l2jfree.TerminationStatus;
import com.l2jfree.status.StatusCommand;

/**
 * @author NB4L1
 */
public final class ShutdownCommand extends StatusCommand
{
	public ShutdownCommand()
	{
		super("shuts down the server in [time] seconds", "shutdown");
	}
	
	@Override
	protected void useCommand(String command, String params)
	{
		try
		{
			int val = Integer.parseInt(params);
			Shutdown.start(TerminationStatus.MANUAL_SHUTDOWN, val, getHostAddress());
			println("Server Will Shutdown In " + val + " Seconds!");
			println("Type \"abort\" To Abort Shutdown!");
		}
		catch (NumberFormatException e)
		{
			Shutdown.exit(TerminationStatus.MANUAL_SHUTDOWN, getHostAddress());
		}
	}
	
	@Override
	protected String getParameterUsage()
	{
		return "[time]";
	}
}
