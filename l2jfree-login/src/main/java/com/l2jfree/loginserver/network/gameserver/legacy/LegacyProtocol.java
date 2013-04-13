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
package com.l2jfree.loginserver.network.gameserver.legacy;

import com.l2jfree.network.IProtocolVersion;
import com.l2jfree.network.ProtocolVersionFactory;
import com.l2jfree.util.EnumValues;

/**
 * An enumeration specifying supported legacy/compatible gameserver communication protocols.
 * 
 * @author savormix
 */
public enum LegacyProtocol implements IProtocolVersion, ProtocolVersionFactory<LegacyProtocol>
{
	OLD_STABLE(258),
	MIXED_ONLY_LEGACY_PART_SUPPORTED(259),
	UNK1(260),
	UNK2(261),
	BLEEDING_EDGE(262);
	
	private final int _version;
	
	private LegacyProtocol(int version)
	{
		_version = version;
	}
	
	@Override
	public LegacyProtocol getByVersion(int version)
	{
		return VALUES.get(version - LegacyProtocol.values()[0].getVersion());
	}
	
	@Override
	public int getVersion()
	{
		return _version;
	}
	
	@Override
	public boolean isOlderThan(IProtocolVersion version)
	{
		return getVersion() < getVersionOrThrow(version);
	}
	
	@Override
	public boolean isOlderThanOrEqualTo(IProtocolVersion version)
	{
		return getVersion() <= getVersionOrThrow(version);
	}
	
	@Override
	public boolean isNewerThan(IProtocolVersion version)
	{
		return getVersion() > getVersionOrThrow(version);
	}
	
	@Override
	public boolean isNewerThanOrEqualTo(IProtocolVersion version)
	{
		return getVersion() >= getVersionOrThrow(version);
	}
	
	@Override
	public long getReleaseDate()
	{
		// whatever...
		return getVersion();
	}
	
	private static int getVersionOrThrow(IProtocolVersion protocol)
	{
		if (protocol instanceof LegacyProtocol)
			return protocol.getVersion();
		
		throw new UnsupportedOperationException(
				"Login server is a fixed service provider. Cross comparison is prohibited.");
	}
	
	private static final EnumValues<LegacyProtocol> VALUES = new EnumValues<LegacyProtocol>(LegacyProtocol.class);
}
