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
import com.l2jfree.gameserver.network.client.packets.sendable.MoveToLocation.Move;
import com.l2jfree.gameserver.network.client.packets.sendable.MoveToPawn.Follow;
import com.l2jfree.gameserver.network.client.packets.sendable.StopMove.Arrived;
import com.l2jfree.gameserver.util.MovementController;
import com.l2jfree.lang.L2Math;

/**
 * @author NB4L1
 */
public class ObjectMovement implements IObjectMovement
{
	private final L2Object _activeChar;
	
	private int _destinationX;
	private int _destinationY;
	private int _destinationZ;
	
	private L2Object _destination;
	private int _destinationOffset;
	
	private long _lastMovedTimestamp;
	
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
		return _destinationX;
	}
	
	@Override
	public int getDestinationY()
	{
		return _destinationY;
	}
	
	@Override
	public int getDestinationZ()
	{
		return _destinationZ;
	}
	
	@Override
	public void moveToPawn(L2Object destination, int offset)
	{
		_destinationX = 0;
		_destinationY = 0;
		_destinationZ = 0;
		
		_destination = destination;
		_destinationOffset = offset;
		
		_lastMovedTimestamp = System.currentTimeMillis();
		
		if (_activeChar instanceof L2Character)
		{
			final L2Character cha = (L2Character)_activeChar;
			cha.broadcastPacket(new Follow(cha, _destination, _destinationOffset));
		}
		else
		{
			// TODO vehicles, etc
		}
		
		MovementController.getInstance().startMovement(_activeChar);
	}
	
	@Override
	public void moveToLocation(int x, int y, int z)
	{
		_destinationX = x;
		_destinationY = y;
		_destinationZ = z;
		
		_destination = null;
		_destinationOffset = 0;
		
		_lastMovedTimestamp = System.currentTimeMillis();
		
		if (_activeChar instanceof L2Character)
		{
			final L2Character cha = (L2Character)_activeChar;
			cha.broadcastPacket(new Move(cha));
		}
		else
		{
			// TODO vehicles, etc
		}
		
		MovementController.getInstance().startMovement(_activeChar);
	}
	
	public void stopMovement()
	{
		// TODO
		
		MovementController.getInstance().stopMovement(_activeChar);
	}
	
	@Override
	public void updatePosition()
	{
		final ObjectPosition pos = _activeChar.getPosition();
		final double currX = pos.getX();
		final double currY = pos.getY();
		final double currZ = pos.getZ();
		
		final double destX;
		final double destY;
		final double destZ;
		if (_destination != null)
		{
			final ObjectPosition destPos = _destination.getPosition();
			destX = destPos.getX();
			destY = destPos.getY();
			destZ = destPos.getZ();
		}
		else
		{
			destX = _destinationX;
			destY = _destinationY;
			destZ = _destinationZ;
		}
		
		final double diffX = destX - currX;
		final double diffY = destY - currY;
		final double diffZ = destZ - currZ;
		
		final double moveSpeed = 100; // FIXME cha.getStat()./*getMoveSpeed()*/getRunSpeed()
		final double timeSpent = System.currentTimeMillis() - _lastMovedTimestamp;
		
		final double distMoved = moveSpeed * timeSpent / 1000.0;
		final double distLeft = Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
		
		if (_destination != null)
		{
			final double offset = _destinationOffset;
			final double collisionRadius = 0; // FIXME collision radius
			
			if (distMoved + offset + collisionRadius < distLeft)
			{
				final double distFraction = distMoved / distLeft;
				
				final int x = (int)(currX + distFraction * diffX);
				final int y = (int)(currY + distFraction * diffY);
				final int z = (int)(currZ + distFraction * diffZ);
				
				pos.setXYZ(x, y, z);
			}
			else
			{
				final double distFraction = Math.min(distMoved, distLeft - collisionRadius) / distLeft;
				
				final int x = (int)(currX + distFraction * diffX);
				final int y = (int)(currY + distFraction * diffY);
				final int z = (int)(currZ + distFraction * diffZ);
				
				pos.setXYZ(x, y, z);
			}
		}
		else
		{
			if (distMoved < distLeft)
			{
				final double distFraction = distMoved / distLeft;
				
				final int x = (int)(currX + distFraction * diffX);
				final int y = (int)(currY + distFraction * diffY);
				final int z = (int)(currZ + distFraction * diffZ);
				
				pos.setXYZ(x, y, z);
			}
			else
			{
				pos.setXYZ((int)destX, (int)destY, (int)destZ);
			}
		}
		
		_lastMovedTimestamp = System.currentTimeMillis();
	}
	
	@Override
	public boolean isArrived()
	{
		if (_destination != null)
		{
			final ObjectPosition pos = _activeChar.getPosition();
			final ObjectPosition destPos = _destination.getPosition();
			
			final double distLeft = L2Math.calculateDistance(pos, destPos);
			
			final double offset = _destinationOffset;
			final double collisionRadius = 0; // FIXME collision radius
			
			return !(offset + collisionRadius < distLeft);
		}
		else
		{
			final ObjectPosition pos = _activeChar.getPosition();
			
			return pos.getX() == _destinationX && pos.getY() == _destinationY && pos.getZ() == _destinationZ;
		}
	}
	
	@Override
	public void revalidateMovement()
	{
		// TODO
		// occasionally send movement related packets in order to sync coordinates with client
		
		getActiveChar().getKnownList().updateKnownList(false);
	}
	
	@Override
	public void movementFinished()
	{
		// TODO
		
		((L2Character)getActiveChar()).broadcastPacket(new Arrived((L2Character)getActiveChar()));
		getActiveChar().getKnownList().updateKnownList(true);
	}
}
