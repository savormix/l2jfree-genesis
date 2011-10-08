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

import com.l2jfree.gameserver.gameobjects.components.interfaces.ICharacterView;
import com.l2jfree.gameserver.gameobjects.components.interfaces.IObjectMovement;

/**
 * @author hex1r0
 * @author NB4L1
 */
public abstract class CharacterView extends ObjectView implements ICharacterView
{
	protected CharacterView(L2Character activeChar)
	{
		super(activeChar);
	}
	
	@Override
	public L2Character getActiveChar()
	{
		return (L2Character)super.getActiveChar();
	}
	
	// ============================================================
	private int _destinationX;
	private int _destinationY;
	private int _destinationZ;
	
	@Override
	public final void refreshDestination()
	{
		final IObjectMovement movement = getActiveChar().getMovement();
		
		_destinationX = movement.getDestinationX();
		_destinationY = movement.getDestinationY();
		_destinationZ = movement.getDestinationZ();
	}
	
	@Override
	public final int getDestinationX()
	{
		return _destinationX;
	}
	
	@Override
	public final int getDestinationY()
	{
		return _destinationY;
	}
	
	@Override
	public final int getDestinationZ()
	{
		return _destinationZ;
	}
}
