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
package com.l2jfree.gameserver.gameobjects.interfaces;

import com.l2jfree.gameserver.gameobjects.L2Object;
import com.l2jfree.gameserver.gameobjects.components.interfaces.ICharacterAI;
import com.l2jfree.gameserver.gameobjects.components.interfaces.ICharacterStat;
import com.l2jfree.gameserver.gameobjects.components.interfaces.ICharacterView;
import com.l2jfree.gameserver.gameobjects.components.interfaces.IInventory;

/**
 * @author NB4L1
 */
public interface IL2Character extends IL2Object
{
	public ICharacterAI getAI();
	
	public ICharacterStat getStat();
	
	@Override
	public ICharacterView getView();
	
	public IInventory getInventory();
	
	public void setName(String name);
	
	public L2Object getTarget();
	
	public void setTarget(L2Object target);
}
