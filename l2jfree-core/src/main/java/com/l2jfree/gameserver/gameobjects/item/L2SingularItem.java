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

import com.l2jfree.gameserver.gameobjects.CharacterStat.Element;
import com.l2jfree.gameserver.gameobjects.L2Item;
import com.l2jfree.gameserver.templates.L2ItemTemplate;

/**
 * @author NB4L1
 */
public class L2SingularItem extends L2Item
{
	protected L2SingularItem(int objectId, L2ItemTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public final long getCount()
	{
		return 1L; // ALWAYS
	}
	
	@Override
	public final boolean isStackable()
	{
		return false; // ALWAYS
	}
	
	@Override
	public int getAttackElementPower()
	{
		// TODO: implement
		return super.getAttackElementPower();
	}
	
	@Override
	public Element getAttackElementType()
	{
		// TODO: implement
		return super.getAttackElementType();
	}
	
	@Override
	public int getDefenseElementPower(Element element)
	{
		// TODO: implement
		return super.getDefenseElementPower(element);
	}
}
