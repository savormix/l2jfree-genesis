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
package com.l2jfree.loginserver.network;

import java.nio.ByteBuffer;

import com.l2jfree.loginserver.network.packets.L2ClientPacket;
import com.l2jfree.loginserver.network.packets.L2ServerPacket;
import com.l2jfree.network.mmocore.IPacketHandler;

/**
 * This class maps Lineage II game client packet opcodes to
 * login server's receivable packets.
 * @author savormix
 */
public final class L2LoginPackets implements IPacketHandler<L2LoginClient, L2ClientPacket, L2ServerPacket>
{
	/* (non-Javadoc)
	 * @see com.l2jfree.network.mmocore.IPacketHandler#handlePacket(java.nio.ByteBuffer, com.l2jfree.network.mmocore.MMOConnection, int)
	 */
	@Override
	public L2ClientPacket handlePacket(ByteBuffer buf, L2LoginClient client,
			int opcode)
	{
		switch (opcode)
		{
		// TODO Auto-generated method stub
		default:
			return null;
		}
	}
}
