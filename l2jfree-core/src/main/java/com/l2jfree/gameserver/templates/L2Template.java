package com.l2jfree.gameserver.templates;

/**
 * @author NB4L1
 */
public abstract class L2Template
{
	protected L2Template()
	{
	}
	
	public abstract int getId();
	
	public String getName()
	{
		// TODO
		return "dummy" + getClass().getSimpleName();
	}
}
