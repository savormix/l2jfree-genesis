package com.l2jfree.gameserver.templates;

/**
 * @author NB4L1
 */
public/* abstract */class L2Template
{
	private final int _id;
	
	public/*protected */L2Template(int id)
	{
		_id = id;
	}
	
	public final int getId()
	{
		return _id;
	}
}
