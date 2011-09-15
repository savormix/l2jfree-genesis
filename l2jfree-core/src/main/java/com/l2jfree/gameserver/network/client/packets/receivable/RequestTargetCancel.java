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
public abstract class RequestTargetCancel extends L2ClientPacket
{
	/**
	 * A nicer name for {@link RequestTargetCancel}.
	 * 
	 * @author savormix (generated)
	 * @see RequestTargetCancel
	 */
	public static final class RequestCancelTarget extends RequestTargetCancel
	{
		// only for convenience
	}
	
	/** Packet's identifier */
	public static final int OPCODE = 0x48;
	
	@Override
	protected int getMinimumLength()
	{
		return READ_H;
	}
	
	private boolean _mouse;
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		_mouse = buf.readH() != 0; // Controller
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO: implement
		if (_mouse)
		{
			// cancel target
		}
		else
		{
			// if skill cast is in progress
			{
				// cancel skill casting
			}
			// else
			{
				// cancel target
			}
		}
	}
}
