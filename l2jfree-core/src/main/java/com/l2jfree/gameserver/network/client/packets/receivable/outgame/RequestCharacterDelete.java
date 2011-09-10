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
package com.l2jfree.gameserver.network.client.packets.receivable.outgame;

import java.nio.BufferUnderflowException;

import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.sendable.outgame.AvailableCharacters;
import com.l2jfree.gameserver.network.client.packets.sendable.outgame.CharacterDeleteSuccess;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * This class represents a packet sent by the client when a character is being marked for deletion
 * ("Yes" is clicked in the deletion confirmation dialog)
 * 
 * @author hex1r0
 */
public class RequestCharacterDelete extends L2ClientPacket
{
	public static final int OPCODE = 0x0d;
	
	private int _charSlot;
	
	@Override
	protected int getMinimumLength()
	{
		return READ_D;
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		_charSlot = buf.readD();
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO mark character to delete
		sendPacket(CharacterDeleteSuccess.STATIC_PACKET);
		
		//sendPacket(new CharacterDeleteFail(REASON_DELETION_FAILED));
		//sendPacket(new CharacterDeleteFail(REASON_YOU_MAY_NOT_DELETE_CLAN_MEMBER));
		//sendPacket(new CharacterDeleteFail(REASON_CLAN_LEADERS_MAY_NOT_BE_DELETED));
		
		sendPacket(new AvailableCharacters(getClient()));
		sendActionFailed();
	}
	
}
