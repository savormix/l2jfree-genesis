/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
