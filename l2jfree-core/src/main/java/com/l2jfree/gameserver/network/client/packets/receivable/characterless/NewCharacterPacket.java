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
package com.l2jfree.gameserver.network.client.packets.receivable.characterless;

import java.nio.BufferUnderflowException;

import com.l2jfree.gameserver.network.client.L2ClientState;
import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.sendable.characterless.NewCharacterSuccess.CharacterTemplates;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * Sent when a player desires to create a character and clicks the 'Create' button while viewing the
 * character selection screen.
 * 
 * @author hex1r0
 * @author savormix (generated)
 * @see CharacterTemplates
 */
public abstract class NewCharacterPacket extends L2ClientPacket
{
	/**
	 * A nicer name for {@link NewCharacterPacket}.
	 * 
	 * @author savormix (generated)
	 * @see NewCharacterPacket
	 */
	public static final class RequestCharacterTemplates extends NewCharacterPacket
	{
		// only for convenience
	}
	
	/** Packet's identifier */
	public static final int OPCODE = 0x13;
	public static final L2ClientState[] STATES = new L2ClientState[] { L2ClientState.CHARACTER_MANAGEMENT };
	
	@Override
	protected int getMinimumLength()
	{
		return 0;
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		// trigger packet
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		sendPacket(CharacterTemplates.PACKET);
	}
}
