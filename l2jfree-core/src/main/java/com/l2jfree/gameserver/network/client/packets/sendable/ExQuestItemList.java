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

import java.util.Collection;

import com.l2jfree.gameserver.gameobjects.CharacterStat.Element;
import com.l2jfree.gameserver.gameobjects.L2Item;
import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.gameserver.templates.L2ItemTemplate;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix (generated)
 */
public abstract class ExQuestItemList extends L2ServerPacket
{
	/**
	 * A nicer name for {@link ExQuestItemList}.
	 * 
	 * @author savormix (generated)
	 * @see ExQuestItemList
	 */
	public static final class QuestInventory extends ExQuestItemList
	{
		/**
		 * Constructs this packet.
		 * 
		 * @param items items in quest inventory
		 * @see ExQuestItemList#ExQuestItemList(Collection)
		 */
		public QuestInventory(Collection<L2Item> items)
		{
			super(items);
		}
	}
	
	private static final int[] EXT_OPCODES = { 0xc6, 0x00 };
	
	private final Collection<L2Item> _items;
	
	/**
	 * Constructs this packet.
	 * 
	 * @param items items in quest inventory
	 */
	public ExQuestItemList(Collection<L2Item> items)
	{
		_items = items;
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
		buf.writeH(_items.size()); // Item count
		for (L2Item item : _items)
		{
			final L2ItemTemplate template = item.getTemplate();
			buf.writeD(item.getObjectId()); // Item OID
			buf.writeD(template.getId()); // Item
			buf.writeD(-1); // Slot number (always -1 (auto))
			buf.writeQ(item.getCount()); // Quantity
			buf.writeH(3); // Main item type (quest item)
			buf.writeH(0); // Special item type
			buf.writeH(false); // Equipped
			buf.writeD(0); // Used paperdoll slot(s)
			buf.writeH(0); // Enchant level
			buf.writeH(0); // Name exists
			buf.writeD(0); // Augmentation
			buf.writeD(-1); // Mana left
			buf.writeD(-9999); // Time remaining
			Element.writeElements(item, buf); // Attack and defense element info
			// 'enchant effects'
			buf.writeH(0); // 0
			buf.writeH(0); // 0
			buf.writeH(0); // 0
		}
		int sizeB = 0; // Special item count, branching condition
		buf.writeH(sizeB);
		// branch with AboveZero
		if (sizeB > 0)
		{
			buf.writeC(0); // Restriction
		}
		for (int i = 0; i < sizeB; i++)
		{
			buf.writeD(0); // Special item
		}
	}
}
