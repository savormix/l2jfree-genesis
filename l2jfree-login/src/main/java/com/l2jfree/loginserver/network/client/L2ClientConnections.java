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
package com.l2jfree.loginserver.network.client;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

import com.l2jfree.loginserver.network.client.packets.L2ClientPacket;
import com.l2jfree.loginserver.network.client.packets.L2ServerPacket;
import com.l2jfree.loginserver.network.client.packets.sendable.Init;
import com.l2jfree.network.mmocore.MMOConfig;
import com.l2jfree.network.mmocore.MMOController;

/**
 * @author savormix
 */
public final class L2ClientConnections extends MMOController<L2LoginClient, L2ClientPacket, L2ServerPacket>
{
	private static final int PROTOCOL_VERSION = 0xc621;
	
	/**
	 * Placeholder javadoc
	 * @param config param
	 * @throws IOException exception
	 */
	public L2ClientConnections(MMOConfig config) throws IOException
	{
		super(config, L2ClientPackets.getInstance());
		// TODO Auto-generated constructor stub
	}
	
	/* (non-Javadoc)
	 * @see com.l2jfree.network.mmocore.MMOController#createClient(java.nio.channels.SocketChannel)
	 */
	@Override
	protected L2LoginClient createClient(SocketChannel socketChannel)
			throws ClosedChannelException
	{
		L2ClientSecurity lcs = L2ClientSecurity.getInstance();
		L2LoginClient llc = new L2LoginClient(this, socketChannel, lcs.getNextSessionId(),
				PROTOCOL_VERSION, lcs.getKeyPair(), lcs.getBlowfishKey());
		llc.sendPacket(new Init(llc));
		return llc;
	}
}
