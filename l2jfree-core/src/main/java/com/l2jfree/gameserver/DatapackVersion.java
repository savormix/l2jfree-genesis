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

import com.l2jfree.ClientProtocolVersion;
import com.l2jfree.util.ArrayBunch;

/**
 * @author NB4L1
 */
public enum DatapackVersion
{
	FREYA(ClientProtocolVersion.FREYA, ClientProtocolVersion.HIGH_FIVE_UPDATE_3),
	HIGH_FIVE(ClientProtocolVersion.HIGH_FIVE_UPDATE_3, ClientProtocolVersion.HIGH_FIVE_UPDATE_3);
	
	private final ClientProtocolVersion[] _supportedClientProtocolVersions;
	
	private DatapackVersion(ClientProtocolVersion minimumClientProtocolVersion,
			ClientProtocolVersion maximumClientProtocolVersion)
	{
		final ArrayBunch<ClientProtocolVersion> tmp = new ArrayBunch<ClientProtocolVersion>();
		
		for (int i = minimumClientProtocolVersion.ordinal(); i <= maximumClientProtocolVersion.ordinal(); i++)
			tmp.add(ClientProtocolVersion.values()[i]);
		
		_supportedClientProtocolVersions = tmp.moveToArray(ClientProtocolVersion.class);
	}
	
	public ClientProtocolVersion[] getSupportedClientProtocolVersions()
	{
		return _supportedClientProtocolVersions;
	}
}
