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
import com.l2jfree.gameserver.network.client.packets.sendable.ValidateLocation.UpdateLocation;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author hex1r0
 */
public abstract class ValidatePosition extends L2ClientPacket
{
	/**
	 * A nicer name for {@link ValidatePosition}.
	 * 
	 * @author hex1r0
	 * @see ValidatePosition
	 */
	public static final class ReportLocation extends ValidatePosition
	{
		public static final int OPCODE = 0x59;
		
		/**
		 * Constructs this packet.
		 * 
		 * @see ValidatePosition#ValidatePosition()
		 */
		public ReportLocation()
		{
		}
	}
	
	private int _x;
	private int _y;
	private int _z;
	private int _heading;
	@SuppressWarnings("unused")
	private int _vehicleObjectID;
	
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
		_x = buf.readD();
		_y = buf.readD();
		_z = buf.readD();
		_heading = buf.readD();
		_vehicleObjectID = buf.readD();
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO make real validation
		getClient().getActiveChar().getPosition().setXYZ(_x, _y, _z);
		getClient().getActiveChar().getPosition().setHeading(_heading);
		
		sendPacket(UpdateLocation.PACKET);
	}
}
