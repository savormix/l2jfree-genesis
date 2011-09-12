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
package com.l2jfree.gameserver.gameobjects.item;

import com.l2jfree.gameserver.gameobjects.L2Item;
import com.l2jfree.gameserver.templates.L2ItemTemplate;

/**
 * To ensure proper initialization of items (backdoor for non-public constructors).
 * 
 * @author NB4L1
 */
public final class ItemFactory
{
	public static L2Item createItem(int objectId, L2ItemTemplate template)
	{
		// TODO
		final boolean stackable = false;
		final boolean equipable = false;
		
		if (stackable)
			return new L2StackableItem(objectId, template);
		
		if (equipable)
			return new L2EquipableItem(objectId, template);
		
		return new L2SingleItem(objectId, template);
	}
}
