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
	
	@Override
	public int getDestinationX()
	{
		// TODO
		return _destX;
	}
	
	@Override
	public int getDestinationY()
	{
		// TODO
		return _destY;
	}
	
	@Override
	public int getDestinationZ()
	{
		// TODO
		return _destZ;
	}
	
	protected long _lastMovedTimestamp;
	
	protected int _destX;
	protected int _destY;
	protected int _destZ;
	
	@Override
	public void startMovement()
	{
		_lastMovedTimestamp = System.currentTimeMillis();
		
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
		long timeSpent = System.currentTimeMillis() - _lastMovedTimestamp;
		
		L2Character cha = (L2Character)getActiveChar();
		ObjectPosition pos = cha.getPosition();
		
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		
		double distanceMoved = cha.getStat()./*getMoveSpeed()*/getRunSpeed() * timeSpent / 1000.0;
		double distFraction = distanceMoved / Math.sqrt(_destX * _destX + _destY * _destY + _destZ * _destZ);
		
		if (distFraction > 1)
		{
			getActiveChar().getPosition().setXYZ(_destX, _destY, _destZ);
			return true;
		}
		else
		{
			x *= distFraction;
			y *= distFraction;
			z *= distFraction;
			
			getActiveChar().getPosition().setXYZ(x, y, z);
		}
		
		_lastMovedTimestamp = System.currentTimeMillis();
		
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
