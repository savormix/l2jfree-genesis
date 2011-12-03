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
import java.util.regex.Pattern;

import com.l2jfree.gameserver.datatables.PlayerNameTable;
import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.sendable.characterless.ExIsCharNameCreatable.CharacterNameValidation;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * Packet is sent when the name check or character creation button is clicked.
 * 
 * @author savormix (generated)
 * @since Goddess of Destruction
 * @see CharacterNameValidation
 */
public abstract class RequestCharacterNameCreatable extends L2ClientPacket
{
	/**
	 * A nicer name for {@link RequestCharacterNameCreatable}.
	 * 
	 * @author savormix (generated)
	 * @see RequestCharacterNameCreatable
	 */
	public static final class RequestCharacterNameCheck extends RequestCharacterNameCreatable
	{
		// only for convenience
	}
	
	/** Packet's identifier */
	public static final int OPCODE = 0xd0;
	/** Packet's second identifier */
	public static final int OPCODE_2 = 0xb0;
	
	@Override
	protected int getMinimumLength()
	{
		return READ_S;
	}
	
	private String _name;
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		_name = buf.readS(); // Name
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO: implement
		final Pattern CNAME_PATTERN = Pattern.compile("[A-Za-z0-9-]{2,16}");
		if (!CNAME_PATTERN.matcher(_name).matches())
		{
			if (_log.isDebugEnabled())
				_log.debug("charname: " + _name + " is invalid. validation failed.");
			sendPacket(CharacterNameValidation.NAME_RESERVED);
		}
		/*
		else if (NpcTable.getInstance().getTemplateByName(_name) != null || obsceneCheck(_name))
		{
			if (_log.isDebugEnabled())
				_log.debug("charname: " + _name + " overlaps with a NPC. creation failed.");
			sendPacket(CharacterNameValidation.NAME_RESERVED);
		}
		*/
		else if (PlayerNameTable.getInstance().isPlayerNameTaken(_name))
		{
			if (_log.isDebugEnabled())
				_log.debug("charname: " + _name + " already exists. validation failed.");
			sendPacket(CharacterNameValidation.NAME_EXISTS);
		}
		else
		{
			if (_log.isDebugEnabled())
				_log.debug("validated charname: " + _name);
			sendPacket(CharacterNameValidation.AVAILABLE);
		}
	}
}
