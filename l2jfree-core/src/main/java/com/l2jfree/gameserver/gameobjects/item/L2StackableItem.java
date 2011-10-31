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
import com.l2jfree.gameserver.sql.ItemDB;
import com.l2jfree.gameserver.templates.L2ItemTemplate;

/**
 * @author NB4L1
 */
public final class L2StackableItem extends L2Item
{
	// TODO
	private final long _count;
	
	protected L2StackableItem(L2ItemTemplate template)
	{
		super(template);
		
		_count = 1;
	}
	
	protected L2StackableItem(L2ItemTemplate template, ItemDB itemDB)
	{
		super(template, itemDB);
		
		_count = itemDB.count;
	}
	
	@Override
	public final long getCount()
	{
		return _count;
	}
	
	@Override
	public final boolean isStackable()
	{
		return true; // ALWAYS
	}
}
