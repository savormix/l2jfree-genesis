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
 * Reports why a player's character could not be deleted.
 * 
 * @author hex1r0
 * @author savormix (generated)
 */
public abstract class CharacterDeleteFail extends StaticPacket
{
	/**
	 * A nicer name for {@link CharacterDeleteFail}.
	 * 
	 * @author savormix
	 * @see CharacterDeleteFail
	 */
	public static final class CharacterDeleteFailure extends CharacterDeleteFail
	{
		/** You have failed to delete the character. */
		public static final CharacterDeleteFailure UNSPECIFIED = new CharacterDeleteFailure(1);
		/** You may not delete a clan member. Withdraw from the clan first and try again. */
		public static final CharacterDeleteFailure CLAN_MEMBER = new CharacterDeleteFailure(2);
		/** Clan leaders may not be deleted. Dissolve the clan first and try again. */
		public static final CharacterDeleteFailure CLAN_LEADER = new CharacterDeleteFailure(3);
		/** You cannot delete characters on this server right now. */
		public static final CharacterDeleteFailure TEMPORARY = new CharacterDeleteFailure(4);
		
		/**
		 * Constructs this packet.
		 * 
		 * @param reason deletion failure reason
		 * @see CharacterDeleteFail#CharacterDeleteFail(int)
		 */
		private CharacterDeleteFailure(int reason)
		{
			super(reason);
		}
	}
	
	private final int _reason;
	
	/**
	 * Constructs this packet.
	 * 
	 * @param reason deletion failure reason
	 */
	public CharacterDeleteFail(int reason)
	{
		_reason = reason;
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x1e;
	}
	
	@Override
	protected void writeImpl(L2Client client, MMOBuffer buf) throws RuntimeException
	{
		buf.writeD(_reason); // Reason
	}
}
