package com.l2jfree.config.postloadhooks;

import com.l2jfree.Shutdown;
import com.l2jfree.TerminationStatus;

/**
 * @author NB4L1
 */
public final class SuperUserValidator extends TypedPostLoadHook<String>
{
	@Override
	protected void valueLoadedImpl(String user)
	{
		if (user.equalsIgnoreCase("root") || user.equalsIgnoreCase("postgres"))
		{
			System.err.println("L2jFree servers should not use superuser accounts... exiting now!");
			
			Shutdown.exit(TerminationStatus.ENVIRONMENT_SUPERUSER);
		}
	}
	
	@Override
	protected Class<String> getRequiredType()
	{
		return String.class;
	}
}
