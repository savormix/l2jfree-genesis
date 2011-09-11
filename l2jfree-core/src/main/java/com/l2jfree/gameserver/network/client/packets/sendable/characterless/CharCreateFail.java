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

import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.sendable.StaticPacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * Reports why a character cannot be created.
 * 
 * @author hex1r0
 * @author savormix (generated)
 */
public abstract class CharCreateFail extends StaticPacket
{
	/**
	 * A nicer name for {@link CharCreateFail}.
	 * 
	 * @author savormix
	 * @see CharCreateFail
	 */
	public static final class CharacterCreateFailure extends CharCreateFail
	{
		/** Your character creation has failed. */
		public static final CharacterCreateFailure UNSPECIFIED = new CharacterCreateFailure(0);
		/** You cannot create another character. Please delete the existing character and try again. */
		public static final CharacterCreateFailure LIMIT_REACHED = new CharacterCreateFailure(1);
		/** This name already exists. */
		public static final CharacterCreateFailure NAME_EXISTS = new CharacterCreateFailure(2);
		/** Your title cannot exceed 16 characters in length. Please try again. */
		public static final CharacterCreateFailure TITLE_LENGTH = new CharacterCreateFailure(3);
		/** Incorrect name. Please try again. */
		public static final CharacterCreateFailure NAME_RESERVED = new CharacterCreateFailure(4);
		/** Characters cannot be created from this server. */
		public static final CharacterCreateFailure DISALLOWED = new CharacterCreateFailure(5);
		/**
		 * Unable to create character. You are unable to create a new character on the selected
		 * server. A restriction is in place which restricts users from creating characters on
		 * different servers where no previous character exists. Please choose another server.
		 */
		public static final CharacterCreateFailure LEGACY_SERVER = new CharacterCreateFailure(6);
		
		/**
		 * Constructs this packet.
		 * 
		 * @param reason creation failure reason
		 * @see CharCreateFail#CharCreateFail(int)
		 */
		private CharacterCreateFailure(int reason)
		{
			super(reason);
		}
	}
	
	private final int _reason;
	
	/**
	 * Constructs this packet.
	 * 
	 * @param reason creation failure reason
	 */
	public CharCreateFail(int reason)
	{
		_reason = reason;
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x10;
	}
	
	@Override
	protected void writeImpl(L2Client client, MMOBuffer buf) throws RuntimeException
	{
		buf.writeD(_reason); // Reason
	}
}
