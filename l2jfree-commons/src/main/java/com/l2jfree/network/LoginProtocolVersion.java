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
package com.l2jfree.network;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * An enumeration of known login server protocol versions.
 * 
 * @author savormix
 */
public enum LoginProtocolVersion implements ILoginProtocolVersion
{
	INTERLUDE(50721, true, "2007-04-13");
	
	private final int _version;
	private final boolean _enabled;
	private final long _timestamp;
	
	private LoginProtocolVersion(int version, boolean enabled, String date)
	{
		_version = version;
		_enabled = enabled;
		try
		{
			_timestamp = new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime();
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public int getVersion()
	{
		return _version;
	}
	
	public boolean isEnabled()
	{
		return _enabled;
	}
	
	@Override
	public boolean isOlderThan(IProtocolVersion version) throws UnsupportedOperationException
	{
		return getReleaseDate() < version.getReleaseDate();
	}
	
	@Override
	public boolean isOlderThanOrEqualTo(IProtocolVersion version) throws UnsupportedOperationException
	{
		return getReleaseDate() <= version.getReleaseDate();
	}
	
	@Override
	public boolean isNewerThan(IProtocolVersion version) throws UnsupportedOperationException
	{
		return getReleaseDate() > version.getReleaseDate();
	}
	
	@Override
	public boolean isNewerThanOrEqualTo(IProtocolVersion version) throws UnsupportedOperationException
	{
		return getReleaseDate() >= version.getReleaseDate();
	}
	
	@Override
	public long getReleaseDate()
	{
		return _timestamp;
	}
}
