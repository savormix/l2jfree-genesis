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

import com.l2jfree.gameserver.gameobjects.components.interfaces.IObjectMovement;
import com.l2jfree.gameserver.util.MovementController;

/**
 * @author NB4L1
 */
public abstract class ObjectMovement implements IObjectMovement
{
	private final L2Object _activeChar;
	
	public ObjectMovement(L2Object activeChar)
	{
		_activeChar = activeChar;
	}
	
	public L2Object getActiveChar()
	{
		return _activeChar;
	}
	
	public void startMovement()
	{
		// TODO
		
		MovementController.getInstance().startMovement(_activeChar);
	}
	
	public void stopMovement()
	{
		// TODO
		
		MovementController.getInstance().stopMovement(_activeChar);
	}
	
	@Override
	public boolean isArrived()
	{
		// TODO
		// check if arrived to the exact required coordinates,
		// or if in range, if the movement's goal is only to get inside a radius from the target
		// DO NOT calculate new destination/route/path -> only check if arrived already at the designed designation or not
		
		return false;
	}
	
	@Override
	public void revalidateMovement()
	{
		// TODO
		// recalculate movement if following
		
		getActiveChar().getKnownList().updateKnownList(false);
	}
	
	@Override
	public void movementFinished()
	{
		// TODO
		
		getActiveChar().getKnownList().updateKnownList(true);
	}
}
