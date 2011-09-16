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

import com.l2jfree.gameserver.gameobjects.components.AiComponent;
import com.l2jfree.gameserver.gameobjects.components.InventoryComponent;
import com.l2jfree.gameserver.gameobjects.components.StatComponent;
import com.l2jfree.gameserver.gameobjects.components.ViewComponent;
import com.l2jfree.gameserver.gameobjects.components.empty.EmptyInventory;
import com.l2jfree.gameserver.gameobjects.components.interfaces.ICharacterAi;
import com.l2jfree.gameserver.gameobjects.components.interfaces.ICharacterStat;
import com.l2jfree.gameserver.gameobjects.components.interfaces.ICharacterView;
import com.l2jfree.gameserver.gameobjects.components.interfaces.IInventory;
import com.l2jfree.gameserver.gameobjects.interfaces.IL2Character;
import com.l2jfree.gameserver.templates.L2Template;

/**
 * @author NB4L1
 */
@InventoryComponent(EmptyInventory.class)
public abstract class L2Character extends L2Object implements IL2Character
{
	private final ICharacterAi _ai;
	private final ICharacterStat _stat;
	private final ICharacterView _view;
	private final IInventory _inventory;
	
	protected L2Character(L2Template template)
	{
		super(template);
		
		_ai = AiComponent.Factory.INSTANCE.getComponent(this);
		_stat = StatComponent.Factory.INSTANCE.getComponent(this);
		_view = ViewComponent.Factory.INSTANCE.getComponent(this);
		_inventory = InventoryComponent.Factory.INSTANCE.getComponent(this);
	}
	
	@Override
	public ICharacterAi getAi()
	{
		return _ai;
	}
	
	@Override
	public ICharacterStat getStat()
	{
		return _stat;
	}
	
	@Override
	public ICharacterView getView()
	{
		return _view;
	}
	
	@Override
	public IInventory getInventory()
	{
		return _inventory;
	}
	
	@Override
	public void setName(String name)
	{
		throw new UnsupportedOperationException("setName() is unsupported for " + getClass().getSimpleName());
	}
}
