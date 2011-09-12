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
public abstract class MoveBackwardToLocation extends L2ClientPacket
{
	/**
	 * A nicer name for {@link MoveBackwardToLocation}.
	 * 
	 * @author savormix (generated)
	 * @see MoveBackwardToLocation
	 */
	public static final class RequestMovement extends MoveBackwardToLocation
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see MoveBackwardToLocation#MoveBackwardToLocation()
		 */
		public RequestMovement()
		{
		}
	}
	
	/** Packet's identifier */
	public static final int OPCODE = 0x0f;
	
	/** Constructs this packet. */
	public MoveBackwardToLocation()
	{
	}
	
	@Override
	protected int getMinimumLength()
	{
		return READ_D + READ_D + READ_D + READ_D + READ_D + READ_D + READ_D;
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.readD(); // Destination X
		buf.readD(); // Destination Y
		buf.readD(); // Destination Z
		buf.readD(); // Current client X
		buf.readD(); // Current client Y
		buf.readD(); // Current client Z
		buf.readD(); // Controller
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO: implement
	}
}
