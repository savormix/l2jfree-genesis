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
package com.l2jfree.gameserver.network.client.packets.sendable.characterless;

import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.RequestCharacterNameCreatable.RequestCharacterNameCheck;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * Packet is sent in response to {@link RequestCharacterNameCheck}.
 * 
 * @author savormix (generated)
 * @since Goddess of Destruction
 * @see RequestCharacterNameCheck
 */
public abstract class ExIsCharNameCreatable extends L2ServerPacket
{
	/**
	 * A nicer name for {@link ExIsCharNameCreatable}.
	 * 
	 * @author savormix (generated)
	 * @see ExIsCharNameCreatable
	 */
	public static final class CharacterNameValidation extends ExIsCharNameCreatable
	{
		/** The specified name is available. */
		public static final CharacterNameValidation AVAILABLE = new CharacterNameValidation(-1);
		/** Your character creation has failed. */
		public static final CharacterNameValidation UNSPECIFIED = new CharacterNameValidation(0);
		/** You cannot create another character. Please delete the existing character and try again. */
		public static final CharacterNameValidation LIMIT_REACHED = new CharacterNameValidation(1);
		/** This name already exists. */
		public static final CharacterNameValidation NAME_EXISTS = new CharacterNameValidation(2);
		/** Your title cannot exceed 16 characters in length. Please try again. */
		public static final CharacterNameValidation TITLE_LENGTH = new CharacterNameValidation(3);
		/** Incorrect name. Please try again. */
		public static final CharacterNameValidation NAME_RESERVED = new CharacterNameValidation(4);
		/** Characters cannot be created from this server. */
		public static final CharacterNameValidation DISALLOWED = new CharacterNameValidation(5);
		/**
		 * Unable to create character. You are unable to create a new character on the selected
		 * server. A restriction is in place which restricts users from creating characters on
		 * different servers where no previous character exists. Please choose another server.
		 */
		public static final CharacterNameValidation LEGACY_SERVER = new CharacterNameValidation(6);
		
		/**
		 * Constructs this packet.
		 * 
		 * @param reason validation failure reason
		 * @see ExIsCharNameCreatable#ExIsCharNameCreatable(int)
		 */
		public CharacterNameValidation(int reason)
		{
			super(reason);
		}
	}
	
	private static final int[] EXT_OPCODES = { 0x0f, 0x01 };
	
	private final int _reason;
	
	/**
	 * Constructs this packet.
	 * 
	 * @param reason validation failure reason
	 */
	public ExIsCharNameCreatable(int reason)
	{
		_reason = reason;
	}
	
	@Override
	protected int getOpcode()
	{
		return 0xfe;
	}
	
	@Override
	protected int[] getAdditionalOpcodes()
	{
		return EXT_OPCODES;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeD(_reason); // Result
	}
}
