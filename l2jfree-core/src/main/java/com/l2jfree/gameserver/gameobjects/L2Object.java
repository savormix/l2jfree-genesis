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
import com.l2jfree.gameserver.gameobjects.components.PositionComponent;
import com.l2jfree.gameserver.gameobjects.components.interfaces.IObjectKnownList;
import com.l2jfree.gameserver.gameobjects.interfaces.IL2Object;
import com.l2jfree.gameserver.templates.L2Template;
import com.l2jfree.gameserver.util.IdFactory;
import com.l2jfree.gameserver.util.IdFactory.IdRange;
import com.l2jfree.gameserver.util.ObjectId;
import com.l2jfree.gameserver.world.L2World;
import com.l2jfree.lang.L2TextBuilder;
import com.l2jfree.util.logging.L2Logger;

@PositionComponent(ObjectPosition.class)
@KnownListComponent(ObjectKnownList.class)
public abstract class L2Object implements IL2Object
{
	protected static final L2Logger _log = L2Logger.getLogger(L2Object.class);
	
	public static final L2Object[] EMPTY_ARRAY = new L2Object[0];
	
	private final ObjectId _objectId;
	private final L2Template _template;
	
	private final ObjectPosition _position;
	private final IObjectKnownList _knownList;
	
	protected L2Object(L2Template template)
	{
		_objectId = IdFactory.getInstance().getNextObjectId(getIdRange());
		_template = template;
		
		_position = PositionComponent.FACTORY.getComponent(this);
		_knownList = KnownListComponent.FACTORY.getComponent(this);
	}
	
	@SuppressWarnings("static-method")
	protected IdRange getIdRange()
	{
		return IdRange.MISC;
	}
	
	@Override
	protected void finalize()
	{
		IdFactory.getInstance().releaseObjectId(getIdRange(), getObjectId());
	}
	
	@Override
	public final ObjectId getObjectId()
	{
		return _objectId;
	}
	
	@Override
	public L2Template getTemplate()
	{
		return _template;
	}
	
	@Override
	public ObjectPosition getPosition()
	{
		return _position;
	}
	
	@Override
	public IObjectKnownList getKnownList()
	{
		return _knownList;
	}
	
	@Override
	public final ObjectId getPrimaryKey()
	{
		return getObjectId();
	}
	
	@Override
	public abstract String getName();
	
	@Override
	public String toString()
	{
		final L2TextBuilder tb = new L2TextBuilder();
		tb.append("(");
		tb.append(getClass().getSimpleName());
		tb.append(") objectId: ");
		tb.append(getObjectId());
		tb.append(" - name: ");
		tb.append(getName());
		
		return tb.moveToString();
	}
	
	public static final byte OBJECT_STATE_INITIALIZED = 0;
	public static final byte OBJECT_STATE_ALIVE = 1;
	public static final byte OBJECT_STATE_DELETED = 2;
	
	private byte _objectState = OBJECT_STATE_INITIALIZED;
	
	public final byte getObjectState()
	{
		return _objectState;
	}
	
	private synchronized boolean setState(byte expected, byte value)
	{
		if (_objectState != expected)
		{
			_log.warn("Object state validation failed! Expected: " + expected + ", found: " + _objectState);
			return false;
		}
		
		_objectState = value;
		return true;
	}
	
	public boolean addToWorld()
	{
		if (!setState(OBJECT_STATE_INITIALIZED, OBJECT_STATE_ALIVE))
			return false;
		
		L2World.addObject(this);
		return true;
	}
	
	public boolean removeFromWorld()
	{
		if (!setState(OBJECT_STATE_ALIVE, OBJECT_STATE_DELETED))
			return false;
		
		L2World.removeObject(this);
		return true;
	}
}
