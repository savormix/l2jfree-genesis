package com.l2jfree.loginserver.status;

import com.l2jfree.status.StatusCommand;

/**
 * @author NB4L1
 */
public abstract class LoginStatusCommand extends StatusCommand
{
	protected LoginStatusCommand(String description, String... commands)
	{
		super(description, commands);
	}
	
	@Override
	protected LoginStatusThread getStatusThread()
	{
		return (LoginStatusThread)Thread.currentThread();
	}
}
