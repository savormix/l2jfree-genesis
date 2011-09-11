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
public abstract class MultiSellListPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link MultiSellListPacket}.
	 * 
	 * @author savormix (generated)
	 * @see MultiSellListPacket
	 */
	public static final class ExchangeList extends MultiSellListPacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see MultiSellListPacket#MultiSellListPacket()
		 */
		public ExchangeList()
		{
		}
	}

	/** Constructs this packet. */
	public MultiSellListPacket()
	{
	}

	@Override
	protected int getOpcode()
	{
		return 0xd0;
	}

	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeD(0); // List ID
		buf.writeD(0); // Page
		buf.writeD(0); // Last page
		buf.writeD(0); // Exchanges per page
		final int sizeA = 0; // Exchanges in this page
		buf.writeD(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeD(0); // Exchange ID
			{
				buf.writeC(0); // Stackable
				buf.writeH(0); // Enchant level
				buf.writeD(0); // Augmentation
				buf.writeD(0); // Mana left
				buf.writeH(0); // Attack element
				buf.writeH(0); // Attack element power
				buf.writeH(0); // Fire defense
				buf.writeH(0); // Water defense
				buf.writeH(0); // Wind defense
				buf.writeH(0); // Earth defense
				buf.writeH(0); // Holy defense
				buf.writeH(0); // Dark defense
			}
			final int sizeB = 0; // Receivable count
			buf.writeH(sizeB);
			final int sizeC = 0; // Consumable count
			buf.writeH(sizeC);
			for (int k = 0; k < sizeB; k++)
			{
				buf.writeD(0); // Receivable
				buf.writeD(0); // Used paperdoll slot(s)
				buf.writeH(0); // Main item type
				buf.writeQ(0L); // Quantity
				buf.writeH(0); // Enchant level
				buf.writeD(0); // Augmentation
				buf.writeD(0); // Mana left
				buf.writeH(0); // Attack element
				buf.writeH(0); // Attack element power
				buf.writeH(0); // Fire defense
				buf.writeH(0); // Water defense
				buf.writeH(0); // Wind defense
				buf.writeH(0); // Earth defense
				buf.writeH(0); // Holy defense
				buf.writeH(0); // Dark defense
			}
			for (int j = 0; j < sizeC; j++)
			{
				buf.writeD(0); // Consumable
				buf.writeH(0); // Main item type
				buf.writeQ(0L); // Quantity
				buf.writeH(0); // Enchant level
				buf.writeD(0); // Augmentation
				buf.writeD(0); // Mana left
				buf.writeH(0); // Attack element
				buf.writeH(0); // Attack element power
				buf.writeH(0); // Fire defense
				buf.writeH(0); // Water defense
				buf.writeH(0); // Wind defense
				buf.writeH(0); // Earth defense
				buf.writeH(0); // Holy defense
				buf.writeH(0); // Dark defense
			}
		}
	}
}
