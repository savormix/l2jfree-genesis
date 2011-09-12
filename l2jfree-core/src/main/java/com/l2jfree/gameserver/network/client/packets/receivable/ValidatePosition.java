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

import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.gameobjects.ObjectPosition;
import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.sendable.ValidateLocation.UpdateLocation;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author hex1r0
 * @author savormix (generated)
 */
public abstract class ValidatePosition extends L2ClientPacket
{
	/**
	 * A nicer name for {@link ValidatePosition}.
	 * 
	 * @author savormix (generated)
	 * @see ValidatePosition
	 */
	public static final class ReportLocation extends ValidatePosition
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see ValidatePosition#ValidatePosition()
		 */
		public ReportLocation()
		{
		}
	}
	
	/** Packet's identifier */
	public static final int OPCODE = 0x59;
	
	private int _x;
	private int _y;
	private int _z;
	private int _heading;
	
	//private int _vehicle;
	
	/** Constructs this packet. */
	public ValidatePosition()
	{
	}
	
	@Override
	protected int getMinimumLength()
	{
		return READ_D + READ_D + READ_D + READ_D + READ_D;
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		_x = buf.readD(); // Current client X
		_y = buf.readD(); // Current client Y
		_z = buf.readD(); // Current client Z
		_heading = buf.readD(); // Heading
		/*_vehicle = */buf.readD(); // Vehicle OID
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO: implement
		L2Player activeChar = getClient().getActiveChar();
		ObjectPosition position = activeChar.getPosition();
		{
			position.setXYZ(_x, _y, _z);
			position.setHeading(_heading);
		}
		// TODO: perhaps make an iterative task instead of replying every time
		// even though we have flood protection
		sendPacket(UpdateLocation.PACKET);
	}
}
