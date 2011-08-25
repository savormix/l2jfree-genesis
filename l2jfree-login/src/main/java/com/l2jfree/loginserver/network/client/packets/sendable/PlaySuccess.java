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

import com.l2jfree.loginserver.network.client.L2Client;
import com.l2jfree.loginserver.network.client.L2ClientController;
import com.l2jfree.loginserver.network.client.L2ClientSecurity;
import com.l2jfree.loginserver.network.client.packets.L2ServerPacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix
 */
public final class PlaySuccess extends L2ServerPacket
{
	private final long _sessionKey;
	private final int _server;
	
	/**
	 * Constructs a packet to inform the client that it can now log into the game server.
	 * 
	 * @param client a connection wrapper
	 * @param server game server ID
	 */
	public PlaySuccess(L2Client client, int server)
	{
		_sessionKey = L2ClientSecurity.getInstance().assignSessionKey(client);
		L2ClientController.getInstance().authorize(client);
		_server = server;
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x07;
	}
	
	@Override
	protected void writeImpl(L2Client client, MMOBuffer buf)
	{
		buf.writeQ(_sessionKey);
		buf.writeC(_server);
	}
}
