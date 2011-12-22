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

import com.l2jfree.ClientProtocolVersion;
import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix (generated)
 */
public abstract class ExBuySellListPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link ExBuySellListPacket}.
	 * 
	 * @author savormix (generated)
	 * @see ExBuySellListPacket
	 */
	public static final class BuyList extends ExBuySellListPacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see ExBuySellListPacket#ExBuySellListPacket()
		 */
		public BuyList()
		{
		}
	}
	
	private static final int[] EXT_OPCODES = { 0xb7, 0x00 };
	
	/** Constructs this packet. */
	public ExBuySellListPacket()
	{
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
		final boolean god = client.getVersion().isNewerThanOrEqualTo(ClientProtocolVersion.GODDESS_OF_DESTRUCTION);
		buf.writeD(0); // Sell/Refund, branching condition
		int sizeA;
		if (god)
		{
			// branch with Zero
			{
				buf.writeQ(0L); // Viewer's adena
				buf.writeD(0); // List ID
				buf.writeD(0); // Viewer's item count
				sizeA = 0; // Buyable count
			}
			// branch with AboveZero
			{
				buf.writeD(0); // Viewer's item count
				sizeA = 0; // Sellable count
			}
		}
		else
		{
			buf.writeQ(0L); // Viewer's adena
			buf.writeD(0); // List ID
			sizeA = 0; // Buyable count
		}
		buf.writeH(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeD(0); // Item
			buf.writeD(0); // Item (dupe)
			buf.writeD(0); // Slot number
			buf.writeQ(0L); // In stock
			buf.writeH(0); // Main item type
			buf.writeH(0); // Special item type
			buf.writeH(0); // Equipped
			buf.writeD(0); // Used paperdoll slot(s)
			buf.writeH(0); // Enchant level
			buf.writeH(0); // Name exists
			buf.writeD(0); // Augmentation
			buf.writeD(0); // Mana left
			buf.writeD(0); // Time remaining
			if (god)
				buf.writeH(0); // ??? 1
			buf.writeH(0); // Attack element
			buf.writeH(0); // Attack element power
			buf.writeH(0); // Fire defense
			buf.writeH(0); // Water defense
			buf.writeH(0); // Wind defense
			buf.writeH(0); // Earth defense
			buf.writeH(0); // Holy defense
			buf.writeH(0); // Dark defense
			buf.writeH(0); // 0
			buf.writeH(0); // 0
			buf.writeH(0); // 0
			if (god)
				buf.writeD(0); // ??? 0
			buf.writeQ(0L); // Price
		}
		// branch with AboveZero
		{
			final int sizeB = 0; // Refundable count
			buf.writeH(sizeB);
			for (int i = 0; i < sizeB; i++)
			{
				buf.writeD(0); // Item OID
				buf.writeD(0); // Item
				buf.writeD(0); // Slot number
				buf.writeQ(0L); // Quantity
				buf.writeH(0); // Main item type
				buf.writeH(0); // Special item type
				buf.writeH(0); // Equipped
				buf.writeD(0); // Used paperdoll slot(s)
				buf.writeH(0); // Enchant level
				buf.writeH(0); // Name exists
				buf.writeD(0); // Augmentation
				buf.writeD(0); // Mana left
				buf.writeD(0); // Time remaining
				if (god)
					buf.writeH(0); // ??? 1
				buf.writeH(0); // Attack element
				buf.writeH(0); // Attack element power
				buf.writeH(0); // Fire defense
				buf.writeH(0); // Water defense
				buf.writeH(0); // Wind defense
				buf.writeH(0); // Earth defense
				buf.writeH(0); // Holy defense
				buf.writeH(0); // Dark defense
				buf.writeH(0); // 0
				buf.writeH(0); // 0
				buf.writeH(0); // 0
				if (god)
					buf.writeD(0); // ??? 0
				buf.writeD(0); // Slot number in list
				buf.writeQ(0L); // Price
			}
			buf.writeC(0); // Close dialog
		}
	}
}
