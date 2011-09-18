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

/**
 * @author NB4L1
 */
public class CharacterPosition extends ObjectPosition
{
	public CharacterPosition(L2Character activeChar)
	{
		super(activeChar);
	}
	
	@Override
	public L2Character getActiveChar()
	{
		return (L2Character)super.getActiveChar();
	}
	
	@Override
	public void worldRegionActivated()
	{
		super.worldRegionActivated();
		
		getActiveChar().getAI().worldRegionActivated();
	}
	
	@Override
	public void worldRegionDeactivated()
	{
		super.worldRegionDeactivated();
		
		getActiveChar().getAI().worldRegionDeactivated();
	}
}
