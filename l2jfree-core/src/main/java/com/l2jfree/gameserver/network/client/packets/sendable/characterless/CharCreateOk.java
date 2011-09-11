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
 * Sent to report that a character has been successfully created.
 * 
 * @author savormix
 */
public abstract class CharCreateOk extends StaticPacket
{
	/**
	 * A nicer name for {@link CharCreateOk}.
	 * 
	 * @author savormix
	 * @see CharCreateOk
	 */
	public static final class CharacterCreateSuccess extends CharCreateOk
	{
		public static final CharacterCreateSuccess PACKET = new CharacterCreateSuccess();
		
		/**
		 * Constructs this packet.
		 * 
		 * @see CharCreateOk#CharCreateOk()
		 */
		private CharacterCreateSuccess()
		{
		}
	}
	
	/** Constructs this packet. */
	public CharCreateOk()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x0f;
	}
	
	@Override
	protected void writeImpl(L2Client client, MMOBuffer buf) throws RuntimeException
	{
		buf.writeD(1); // 1
	}
}
