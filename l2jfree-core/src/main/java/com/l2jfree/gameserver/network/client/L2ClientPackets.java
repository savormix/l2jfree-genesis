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
package com.l2jfree.gameserver.network.client;

import static com.l2jfree.gameserver.network.client.L2CoreClientState.CONNECTED;

import java.nio.ByteBuffer;

import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.gameserver.network.client.packets.receivable.ProtocolVersion;
import com.l2jfree.network.mmocore.PacketHandler;

/**
 * @author savormix
 */
public final class L2ClientPackets extends PacketHandler<L2CoreClient, L2ClientPacket, L2ServerPacket>
{
	private L2ClientPackets()
	{
		// singleton
	}
	
	@Override
	public L2ClientPacket handlePacket(ByteBuffer buf, L2CoreClient client, int opcode)
	{
		switch (opcode)
		{
			// TODO Auto-generated method stub
			case ProtocolVersion.OPCODE:
				if (client.stateEquals(CONNECTED))
					return new ProtocolVersion();
				return invalidState(client, ProtocolVersion.class, opcode);
				
			default:
				return unknown(buf, client, opcode);
		}
	}
	
	/**
	 * Returns a singleton object.
	 * 
	 * @return an instance of this class
	 */
	public static L2ClientPackets getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder
	{
		public static final L2ClientPackets INSTANCE = new L2ClientPackets();
	}
}
