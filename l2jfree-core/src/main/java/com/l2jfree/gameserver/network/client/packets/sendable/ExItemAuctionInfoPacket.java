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
public abstract class ExItemAuctionInfoPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link ExItemAuctionInfoPacket}.
	 * 
	 * @author savormix (generated)
	 * @see ExItemAuctionInfoPacket
	 */
	public static final class ItemAuctionInfo extends ExItemAuctionInfoPacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see ExItemAuctionInfoPacket#ExItemAuctionInfoPacket()
		 */
		public ItemAuctionInfo()
		{
		}
	}
	
	private static final int[] EXT_OPCODES = { 0x68, 0x00, };
	
	/** Constructs this packet. */
	public ExItemAuctionInfoPacket()
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
		buf.writeC(0); // Open window
		buf.writeD(0); // Auction ID
		buf.writeQ(0L); // Highest bid
		buf.writeD(0); // Time left
		buf.writeD(0); // Item
		buf.writeD(0); // Item (dupe)
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
		buf.writeQ(0L); // Next starter bid, not in every packet
		buf.writeD(0); // Start time
		buf.writeD(0); // Item
		buf.writeD(0); // Item (dupe)
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
	}
}
