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
package com.l2jfree.gameserver.gameobjects.components.empty;

import com.l2jfree.gameserver.gameobjects.L2Object;
import com.l2jfree.gameserver.gameobjects.components.interfaces.IObjectMovement;

/**
 * @author NB4L1
 */
public class EmptyObjectMovement implements IObjectMovement
{
	public EmptyObjectMovement(@SuppressWarnings("unused") L2Object activeChar)
	{
		// do nothing
	}
	
	@Override
	public int getDestinationX()
	{
		return 0;
	}
	
	@Override
	public int getDestinationY()
	{
		return 0;
	}
	
	@Override
	public int getDestinationZ()
	{
		return 0;
	}
	
	@Override
	public void moveToPawn(L2Object destination, int offset)
	{
		// do nothing
	}
	
	@Override
	public void moveToLocation(int x, int y, int z)
	{
		// do nothing
	}
	
	@Override
	public void updatePosition()
	{
		// do nothing
	}
	
	@Override
	public boolean isArrived()
	{
		return false;
	}
	
	@Override
	public void revalidateMovement()
	{
		// do nothing
	}
	
	@Override
	public void movementFinished()
	{
		// do nothing
	}
	
}
