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

import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.sendable.characterless.CharacterDeleteFail.CharacterDeleteFailure;
import com.l2jfree.gameserver.network.client.packets.sendable.characterless.CharacterDeleteSuccess;
import com.l2jfree.gameserver.network.client.packets.sendable.characterless.CharacterSelectionInfo.AvailableCharacters;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author hex1r0
 * @author savormix (generated)
 * @see CharacterDeleteFailure
 * @see CharacterDeleteSuccess
 * @see AvailableCharacters
 */
public abstract class CharacterDeletePacket extends L2ClientPacket
{
	/**
	 * A nicer name for {@link CharacterDeletePacket}.
	 * 
	 * @author savormix (generated)
	 * @see CharacterDeletePacket
	 */
	public static final class RequestDeleteCharacter extends CharacterDeletePacket
	{
		// only for convenience
	}
	
	/** Packet's identifier */
	public static final int OPCODE = 0x0d;
	
	@Override
	protected int getMinimumLength()
	{
		return READ_D;
	}
	
	/* Fields for storing read data */
	@SuppressWarnings("unused")
	private int _charSlot;
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		_charSlot = buf.readD(); // Slot
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO: implement
		sendPacket(CharacterDeleteSuccess.PACKET);
		AvailableCharacters.sendToClient(getClient());
	}
}
