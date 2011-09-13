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

import com.l2jfree.gameserver.gameobjects.CharacterStat.Element;
import com.l2jfree.gameserver.gameobjects.components.KnownListComponent;
import com.l2jfree.gameserver.gameobjects.components.empty.EmptyObjectKnownList;
import com.l2jfree.gameserver.gameobjects.components.interfaces.IElemental;
import com.l2jfree.gameserver.templates.L2ItemTemplate;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author NB4L1
 */
@KnownListComponent(EmptyObjectKnownList.class)
public abstract class L2Item extends L2Object implements IElemental
{
	protected L2Item(int objectId, L2ItemTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public L2ItemTemplate getTemplate()
	{
		return (L2ItemTemplate)super.getTemplate();
	}
	
	@Override
	public final String getName()
	{
		return getTemplate().getName();
	}
	
	public abstract long getCount();
	
	public abstract boolean isStackable();
	
	@SuppressWarnings("static-method")
	public boolean isEquipable()
	{
		return false;
	}
	
	@Override
	public int getAttackElementPower()
	{
		return 0;
	}
	
	@Override
	public Element getAttackElementType()
	{
		return Element.NA;
	}
	
	@Override
	public int getDefenseElementPower(Element element)
	{
		return 0;
	}
	
	@Override
	public final void writeElements(MMOBuffer buf)
	{
		buf.writeH(getAttackElementType().getValue()); // Attack element
		buf.writeH(getAttackElementPower()); // Attack element power
		buf.writeH(getDefenseElementPower(Element.FIRE)); // Fire defense
		buf.writeH(getDefenseElementPower(Element.WATER)); // Water defense
		buf.writeH(getDefenseElementPower(Element.WIND)); // Wind defense
		buf.writeH(getDefenseElementPower(Element.EARTH)); // Earth defense
		buf.writeH(getDefenseElementPower(Element.HOLY)); // Holy defense
		buf.writeH(getDefenseElementPower(Element.DARK)); // Dark defense
	}
}
