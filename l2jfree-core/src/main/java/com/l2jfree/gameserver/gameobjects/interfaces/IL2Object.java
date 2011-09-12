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

import com.l2jfree.gameserver.gameobjects.ObjectPosition;
import com.l2jfree.gameserver.gameobjects.components.interfaces.IObjectKnownList;
import com.l2jfree.gameserver.templates.L2Template;
import com.l2jfree.lang.L2Entity;

/**
 * @author NB4L1
 */
public interface IL2Object extends L2Entity<Integer>
{
	public int getObjectId();
	
	public L2Template getTemplate();
	
	public ObjectPosition getPosition();
	
	public IObjectKnownList getKnownList();
	
	@Override
	public Integer getPrimaryKey();
	
	public String getName();
	
	public void setName(String name);
	
	@Override
	public String toString();
}