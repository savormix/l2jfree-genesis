package com.l2jfree.status.commands;

import com.l2jfree.Shutdown;
import com.l2jfree.TerminationStatus;
import com.l2jfree.status.StatusCommand;

/**
 * @author NB4L1
 */
public final class Halt extends StatusCommand
{
	public Halt()
	{
		super("halts the server", "halt");
	}
	
	@Override
	protected void useCommand(String command, String params)
	{
		print("Halting...");
		Shutdown.halt(TerminationStatus.MANUAL_RESTART, getHostAddress());
	}
}
