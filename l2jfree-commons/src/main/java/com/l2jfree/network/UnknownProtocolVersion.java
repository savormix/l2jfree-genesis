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

/**
 * Represents an unknown protocol version.
 * 
 * @author savormix
 * @param <V> version type
 */
public abstract class UnknownProtocolVersion<V extends IProtocolVersion> implements IProtocolVersion
{
	private final int _version;
	private final V _latestKnown;
	
	public UnknownProtocolVersion(int version, V latestKnown)
	{
		_version = version;
		_latestKnown = latestKnown;
	}

	@Override
	public int getVersion()
	{
		return _version;
	}

	@Override
	public boolean isOlderThan(IProtocolVersion version)
	{
		return _latestKnown.isOlderThan(version);
	}

	@Override
	public boolean isOlderThanOrEqualTo(IProtocolVersion version)
	{
		return _latestKnown.isOlderThanOrEqualTo(version);
	}

	@Override
	public boolean isNewerThan(IProtocolVersion version)
	{
		return _latestKnown.isNewerThan(version);
	}

	@Override
	public boolean isNewerThanOrEqualTo(IProtocolVersion version)
	{
		return _latestKnown.isNewerThanOrEqualTo(version);
	}

	@Override
	public long getReleaseDate()
	{
		return _latestKnown.getReleaseDate();
	}
	
	protected V getLatestKnown()
	{
		return _latestKnown;
	}
	
	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		sb.append('@').append(getVersion()).append(", ");
		sb.append(" using ").append(_latestKnown);
		return sb.toString();
	}
}
