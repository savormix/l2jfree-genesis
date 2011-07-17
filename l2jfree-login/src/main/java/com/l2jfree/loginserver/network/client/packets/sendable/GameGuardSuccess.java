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
package com.l2jfree.loginserver.network.client.packets.sendable;

import com.l2jfree.loginserver.network.client.L2LoginClient;
import com.l2jfree.loginserver.network.client.packets.L2ServerPacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix
 */
public final class GameGuardSuccess extends L2ServerPacket
{
	private final int _sessionId;
	
	/**
	 * Constructs a packet to inform about successful GameGuard authorization.
	 * @param sessionId session ID
	 */
	public GameGuardSuccess(int sessionId)
	{
		_sessionId = sessionId;
	}
	
	/* (non-Javadoc)
	 * @see com.l2jfree.loginserver.network.client.packets.L2ServerPacket#getOpcode()
	 */
	@Override
	protected int getOpcode()
	{
		return 0x0b;
	}
	
	/* (non-Javadoc)
	 * @see com.l2jfree.loginserver.network.client.packets.L2ServerPacket#writeImpl(com.l2jfree.loginserver.network.client.L2LoginClient, com.l2jfree.network.mmocore.MMOBuffer)
	 */
	@Override
	protected void writeImpl(L2LoginClient client, MMOBuffer buf)
	{
		buf.writeD(_sessionId);
		buf.write0(16);
	}
}
