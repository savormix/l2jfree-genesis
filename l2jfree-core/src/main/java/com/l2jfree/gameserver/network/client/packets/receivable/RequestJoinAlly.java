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
package com.l2jfree.gameserver.network.client.packets.receivable;

import java.nio.BufferUnderflowException;

import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix (generated)
 */
public abstract class RequestJoinAlly extends L2ClientPacket
{
	/**
	 * A nicer name for {@link RequestJoinAlly}.
	 * 
	 * @author savormix (generated)
	 * @see RequestJoinAlly
	 */
	public static final class RequestInviteToAlliance extends RequestJoinAlly
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see RequestJoinAlly#RequestJoinAlly()
		 */
		public RequestInviteToAlliance()
		{
		}
	}
	
	/** Packet's identifier */
	public static final int OPCODE = 0x8c;
	
	/** Constructs this packet. */
	public RequestJoinAlly()
	{
	}
	
	@Override
	protected int getMinimumLength()
	{
		return READ_D;
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.readD(); // Target OID
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO: implement
	}
}
