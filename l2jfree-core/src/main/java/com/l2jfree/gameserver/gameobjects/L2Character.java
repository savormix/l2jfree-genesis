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
package com.l2jfree.gameserver.gameobjects;

import com.l2jfree.gameserver.gameobjects.components.InventoryComponent;
import com.l2jfree.gameserver.gameobjects.components.StatComponent;
import com.l2jfree.gameserver.gameobjects.components.ViewComponent;
import com.l2jfree.gameserver.templates.L2Template;

/**
 * @author NB4L1
 */
@StatComponent(CharacterStat.class)
@ViewComponent(CharacterView.class)
@InventoryComponent(EmptyInventory.class)
public abstract class L2Character extends L2Object implements IL2Character
{
	private final CharacterStat _stat;
	private final CharacterView _view;
	private final IInventory _inventory;
	
	public L2Character(int objectId, L2Template template)
	{
		super(objectId, template);
		
		_stat = StatComponent.FACTORY.getComponent(this);
		_view = ViewComponent.FACTORY.getComponent(this);
		_inventory = InventoryComponent.FACTORY.getComponent(this);
	}
	
	public CharacterStat getStat()
	{
		return _stat;
	}
	
	public CharacterView getView()
	{
		return _view;
	}
	
	public IInventory getInventory()
	{
		return _inventory;
	}
}
