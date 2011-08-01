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

import com.l2jfree.lang.L2Entity;
import com.l2jfree.lang.L2TextBuilder;

public abstract class L2Object implements L2Entity<Integer>
{
	public static final L2Object[] EMPTY_ARRAY = new L2Object[0];
	
	//private final Integer _objectId;
	private final int _objectId;
	
	protected L2Object(int objectId)
	{
		//_objectId = L2Integer.valueOf(objectId);
		_objectId = objectId;
	}
	
	public final Integer getObjectId()
	{
		return _objectId;
	}
	
	@Override
	public final Integer getPrimaryKey()
	{
		return getObjectId();
	}
	
	public abstract String getName();
	
	public abstract void setName(String name);
	
	@Override
	public String toString()
	{
		final L2TextBuilder tb = L2TextBuilder.newInstance();
		tb.append("(");
		tb.append(getClass().getSimpleName());
		tb.append(") ");
		tb.append(getObjectId());
		tb.append(" - ");
		tb.append(getName());
		
		return tb.moveToString();
	}
}
