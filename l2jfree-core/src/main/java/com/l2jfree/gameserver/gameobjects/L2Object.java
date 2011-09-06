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
	static
	{
		ComponentFactory.POSITION.register(L2Object.class, ObjectPosition.class);
	}
	
	public static final L2Object[] EMPTY_ARRAY = new L2Object[0];
	
	private final int _objectId;
	private final ObjectPosition _position;
	
	protected L2Object(int objectId)
	{
		_objectId = objectId;
		_position = initPosition();
	}
	
	public final int getObjectId()
	{
		return _objectId;
	}
	
	protected final ObjectPosition initPosition()
	{
		//return new ObjectPosition(this);
		return ComponentFactory.POSITION.getComponent(this);
	}
	
	public final ObjectPosition getPosition()
	{
		return _position;
	}
	
	@Override
	public final Integer getPrimaryKey()
	{
		return getObjectId();
	}
	
	public abstract String getName();
	
	public abstract void setName(String name);
	
	@Override
	public final String toString()
	{
		final L2TextBuilder tb = new L2TextBuilder();
		tb.append("(");
		tb.append(getClass().getSimpleName());
		tb.append(") ");
		tb.append(getObjectId());
		tb.append(" - ");
		tb.append(getName());
		
		return tb.moveToString();
	}
	
	/**
	 * @return
	 * @see ComponentFactory
	 */
	public abstract int getTemplateId();
}
