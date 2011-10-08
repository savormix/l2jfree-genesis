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

import com.l2jfree.gameserver.gameobjects.components.interfaces.IObjectView;
import com.l2jfree.gameserver.util.ObjectId;

/**
 * @author NB4L1
 */
public abstract class ObjectView implements IObjectView
{
	private final L2Object _activeChar;
	
	protected ObjectView(L2Object activeChar)
	{
		_activeChar = activeChar;
	}
	
	public L2Object getActiveChar()
	{
		return _activeChar;
	}
	
	@Override
	public final ObjectId getObjectId()
	{
		return getActiveChar().getObjectId();
	}
	
	@Override
	public void refresh()
	{
		refreshPosition();
	}
	
	// ============================================================
	private int _x;
	private int _y;
	private int _z;
	private int _heading;
	
	@Override
	public final void refreshPosition()
	{
		final ObjectPosition position = getActiveChar().getPosition();
		
		_x = position.getX();
		_y = position.getY();
		_z = position.getZ();
		_heading = position.getHeading();
	}
	
	@Override
	public final int getX()
	{
		return _x;
	}
	
	@Override
	public final int getY()
	{
		return _y;
	}
	
	@Override
	public final int getZ()
	{
		return _z;
	}
	
	@Override
	public final int getHeading()
	{
		return _heading;
	}
}
