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

import com.l2jfree.gameserver.gameobjects.components.KnownListComponent;
import com.l2jfree.gameserver.gameobjects.components.empty.EmptyObjectKnownList;
import com.l2jfree.gameserver.templates.L2ItemTemplate;

/**
 * @author NB4L1
 */
@KnownListComponent(EmptyObjectKnownList.class)
public abstract class L2Item extends L2Object
{
	public L2Item(int objectId, L2ItemTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public L2ItemTemplate getTemplate()
	{
		return (L2ItemTemplate)super.getTemplate();
	}
}
