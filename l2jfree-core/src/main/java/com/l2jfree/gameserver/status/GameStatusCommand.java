package com.l2jfree.gameserver.status;

import com.l2jfree.status.StatusCommand;

/**
 * @author NB4L1
 */
public abstract class GameStatusCommand extends StatusCommand
{
	protected GameStatusCommand(String description, String... commands)
	{
		super(description, commands);
	}
	
	@Override
	protected GameStatusThread getStatusThread()
	{
		return (GameStatusThread)Thread.currentThread();
	}
}
