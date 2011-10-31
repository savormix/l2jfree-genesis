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

import com.l2jfree.gameserver.sql.ItemDB;
import com.l2jfree.gameserver.templates.L2ItemTemplate;

/**
 * @author NB4L1
 */
public final class L2EquipableItem extends L2SingularItem
{
	// TODO
	private boolean _equipped;
	private int _enchantLevel;
	
	protected L2EquipableItem(L2ItemTemplate template)
	{
		super(template);
	}
	
	protected L2EquipableItem(L2ItemTemplate template, ItemDB itemDB)
	{
		super(template, itemDB);
	}
	
	@Override
	public boolean isEquipable()
	{
		return true; // ALWAYS
	}
	
	@Override
	public boolean isEquipped()
	{
		return _equipped;
	}
	
	@Override
	public int getEnchantLevel()
	{
		return _enchantLevel;
	}
	
	@Override
	public int getRemainingMana()
	{
		// TODO: implement
		return super.getRemainingMana();
	}
}
