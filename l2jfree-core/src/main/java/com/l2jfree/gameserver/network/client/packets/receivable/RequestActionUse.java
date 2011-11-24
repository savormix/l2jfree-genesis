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
public class RequestActionUse extends L2ClientPacket
{
	/** Packet's identifier */
	public static final int OPCODE = 0x56;
	
	@Override
	protected int getMinimumLength()
	{
		return READ_D + READ_D + READ_C;
	}
	
	/* Fields for storing read data */
	private int _actionId;
	private boolean _ctrlPressed;
	private boolean _shiftPressed;
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		_actionId = buf.readD(); // Action
		_ctrlPressed = buf.readD() == 1; // Ctrl (force attack)
		_shiftPressed = buf.readC() == 1; // Shift (do not move)
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO: implement
		System.out.println("RequestActionUse.runImpl() - actionId: " + _actionId + ", ctrlPressed: " + _ctrlPressed
				+ ", shiftPressed: " + _shiftPressed);
	}
}
