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
package com.l2jfree.gameserver;

import com.l2jfree.network.ClientProtocolVersion;
import com.l2jfree.util.ArrayBunch;

/**
 * @author NB4L1
 */
public enum DatapackVersion
{
	FREYA(ClientProtocolVersion.FREYA, ClientProtocolVersion.HIGH_FIVE_UPDATE_3, true),
	HIGH_FIVE(ClientProtocolVersion.HIGH_FIVE_UPDATE_3, false),
	GODDESS_OF_DESTRUCTION(ClientProtocolVersion.GODDESS_OF_DESTRUCTION, false);
	
	private final ClientProtocolVersion[] _supportedClientProtocolVersions;
	private final boolean _enabled;
	
	private DatapackVersion(ClientProtocolVersion clientProtocolVersion, boolean enabled)
	{
		this(clientProtocolVersion, clientProtocolVersion, enabled);
	}
	
	private DatapackVersion(ClientProtocolVersion minimumClientProtocolVersion,
			ClientProtocolVersion maximumClientProtocolVersion, boolean enabled)
	{
		final ArrayBunch<ClientProtocolVersion> tmp = new ArrayBunch<ClientProtocolVersion>();
		
		for (int i = minimumClientProtocolVersion.ordinal(); i <= maximumClientProtocolVersion.ordinal(); i++)
			tmp.add(ClientProtocolVersion.values()[i]);
		
		_supportedClientProtocolVersions = tmp.moveToArray(ClientProtocolVersion.class);
		_enabled = enabled;
	}
	
	public ClientProtocolVersion[] getSupportedClientProtocolVersions()
	{
		return _supportedClientProtocolVersions;
	}
	
	public boolean isEnabled()
	{
		return _enabled;
	}
}
