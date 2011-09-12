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
package com.l2jfree.gameserver.network.client.packets.sendable;

import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix (generated)
 */
public abstract class HennaUnequipItemInfo extends L2ServerPacket
{
	/**
	 * A nicer name for {@link HennaUnequipItemInfo}.
	 * 
	 * @author savormix (generated)
	 * @see HennaUnequipItemInfo
	 */
	public static final class HennaRemovalInfo extends HennaUnequipItemInfo
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see HennaUnequipItemInfo#HennaUnequipItemInfo()
		 */
		public HennaRemovalInfo()
		{
		}
	}
	
	/** Constructs this packet. */
	public HennaUnequipItemInfo()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0xe7;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeD(0); // Henna
		buf.writeD(0); // Recovered item
		buf.writeQ(0L); // Recovered quantity
		buf.writeQ(0L); // Price of removal
		buf.writeD(0); // 1
		buf.writeQ(0L); // Viewer's adena
		buf.writeD(0); // Current INT
		buf.writeC(0); // INT without henna
		buf.writeD(0); // Current STR
		buf.writeC(0); // STR without henna
		buf.writeD(0); // Current CON
		buf.writeC(0); // CON without henna
		buf.writeD(0); // Current MEN
		buf.writeC(0); // MEN without henna
		buf.writeD(0); // Current DEX
		buf.writeC(0); // DEX without henna
		buf.writeD(0); // Current WIT
		buf.writeC(0); // WIT without henna
	}
}
