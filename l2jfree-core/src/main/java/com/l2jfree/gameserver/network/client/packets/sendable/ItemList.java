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

import com.l2jfree.ClientProtocolVersion;
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
public abstract class ItemList extends L2ServerPacket
{
	/**
	 * A nicer name for {@link ItemList}.
	 * 
	 * @author savormix (generated)
	 * @see ItemList
	 */
	public static final class MyInventory extends ItemList
	{
		/**
		 * Constructs this packet.
		 * 
		 * @param openWindow whether to open the inventory window
		 * @param items items in inventory
		 * @see ItemList#ItemList(boolean, Collection)
		 */
		public MyInventory(boolean openWindow, Collection<L2Item> items)
		{
			super(openWindow, items);
		}
	}
	
	private final boolean _openWindow;
	private final Collection<L2Item> _items;
	
	/**
	 * Constructs this packet.
	 * 
	 * @param openWindow whether to open the inventory window
	 * @param items items in inventory
	 */
	public ItemList(boolean openWindow, Collection<L2Item> items)
	{
		_openWindow = openWindow;
		_items = items;
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x11;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		final boolean god = client.getVersion().isNewerThanOrEqualTo(ClientProtocolVersion.GODDESS_OF_DESTRUCTION);
		buf.writeH(_openWindow); // Open window
		buf.writeH(_items.size()); // Item count
		int slot = 0;
		for (L2Item item : _items)
		{
			final L2ItemTemplate template = item.getTemplate();
			buf.writeD(item.getObjectId()); // Item OID
			buf.writeD(template.getId()); // Item
			buf.writeD(slot++); // TODO: Slot number (-1 for equipped items, 0..(inv size - 1 - equip count) for other items)
			buf.writeQ(item.getCount()); // Quantity
			buf.writeH(template.getType()); // Main item type
			buf.writeH(0); // Special item type
			buf.writeH(item.isEquipped()); // Equipped
			buf.writeD(template.getEquipSlot().getMask()); // Used paperdoll slot(s)
			buf.writeH(item.getEnchantLevel()); // Enchant level
			buf.writeH(item.isNamed()); // Name exists
			buf.writeD(0); // Augmentation
			buf.writeD(item.getRemainingMana()); // Mana left
			buf.writeD(item.getRemainingTime()); // Time remaining
			if (god)
				buf.writeH(1); // ??? 1
			Element.writeElements(item, buf); // Attack and defense element info
			// 'enchant effects'
			buf.writeH(0); // 0
			buf.writeH(0); // 0
			buf.writeH(0); // 0
			if (god)
				buf.writeD(0); // ??? 0
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
