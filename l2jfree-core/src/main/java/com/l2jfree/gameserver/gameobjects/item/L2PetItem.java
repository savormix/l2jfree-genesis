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
 * @author savormix
 */
public final class L2PetItem extends L2SingularItem
{
	// TODO
	private boolean _named;
	private int _petLevel;
	
	protected L2PetItem(L2ItemTemplate template)
	{
		super(template);
	}
	
	protected L2PetItem(L2ItemTemplate template, ItemDB itemDB)
	{
		super(template, itemDB);
	}
	
	@Override
	public int getEnchantLevel()
	{
		return _petLevel;
	}
	
	@Override
	public boolean isNamed()
	{
		return _named;
	}
}
