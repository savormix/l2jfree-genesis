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
public abstract class MoveToLocationInAirShip extends L2ClientPacket
{
	/**
	 * A nicer name for {@link MoveToLocationInAirShip}.
	 * 
	 * @author savormix (generated)
	 * @see MoveToLocationInAirShip
	 */
	public static final class RequestMovementInAircraft extends MoveToLocationInAirShip
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see MoveToLocationInAirShip#MoveToLocationInAirShip()
		 */
		public RequestMovementInAircraft()
		{
		}
	}
	
	/** Packet's identifier */
	public static final int OPCODE = 0xd0;
	/** Additional packet's identifiers */
	public static final int[] EXT_OPCODES = { 0x20, 0x00, };
	/** Second packet's identifier */
	public static final int OPCODE_2 = 0x20;
	
	/** Constructs this packet. */
	public MoveToLocationInAirShip()
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
		buf.readD(); // Aircraft OID
		buf.readD(); // Destination X
		buf.readD(); // Destination Y
		buf.readD(); // Destination Z
		buf.readD(); // Current X
		buf.readD(); // Current Y
		buf.readD(); // Current Z
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO: implement
	}
}
