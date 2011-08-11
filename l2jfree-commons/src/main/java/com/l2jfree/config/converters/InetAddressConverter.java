package com.l2jfree.config.converters;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Converts IP addresses.
 * 
 * @author savormix
 */
public final class InetAddressConverter extends TypedConverter<InetAddress>
{
	@Override
	protected InetAddress convertFromString(String value)
	{
		try
		{
			return InetAddress.getByName(value);
		}
		catch (UnknownHostException e)
		{
			throw new IllegalArgumentException(e);
		}
	}
	
	@Override
	protected String convertToString(InetAddress obj)
	{
		return obj.getHostAddress();
	}
	
	@Override
	protected Class<InetAddress> getRequiredType()
	{
		return InetAddress.class;
	}
}
