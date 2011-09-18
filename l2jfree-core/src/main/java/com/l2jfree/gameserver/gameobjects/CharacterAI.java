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

import com.l2jfree.gameserver.gameobjects.ai.AIDesire;
import com.l2jfree.gameserver.gameobjects.components.interfaces.ICharacterAI;
import com.l2jfree.util.Introspection;

/**
 * @author hex1r0
 */
@SuppressWarnings("unused")
public abstract class CharacterAI implements ICharacterAI
{
	private final L2Character _activeChar;
	
	protected CharacterAI(L2Character activeChar)
	{
		_activeChar = activeChar;
	}
	
	public L2Character getActiveChar()
	{
		return _activeChar;
	}
	
	@Override
	public void worldRegionActivated()
	{
		// TODO
	}
	
	@Override
	public void worldRegionDeactivated()
	{
		// TODO
	}
	
	@Override
	public void addDesire(AIDesire desire)
	{
		// TODO
		// adds a new desire
		_log.info(getActiveChar() + ".addDesire(): " + Introspection.toString(desire));
		
		think();
	}
	
	@Override
	public void setDesire(AIDesire desire)
	{
		// TODO
		// replaces all previous desire with the given one
		_log.info(getActiveChar() + ".setDesire(): " + Introspection.toString(desire));
		
		think();
	}
	
	@Override
	public void clearDesires()
	{
		// TODO
		// remove all previous desires
		_log.info(getActiveChar() + ".clearDesires(): ");
	}
	
	@SuppressWarnings("null")
	public void think()
	{
		// TODO implement
		// check if it "can" think right now
		// if yes, then find most important desire, 
		AIDesire mostDesired = null;
		
		// and behave according to it... schedule asynchronously if necessary
		if (mostDesired != null)
			mostDesired.execute(this);
		else
		{
			// search for anything to do, check for surrounding objects, etc
			// if there is nothing, then simply stop any pending execution, and return
		}
	}
	
	// intention handlers
	public void onIntentionMove(int x, int y, int z)
	{
		// do nothing at default
	}
	
	public void onIntentionOnAction(L2Object target, boolean cantMove, boolean forceAttack)
	{
		// do nothing at default
	}
	
	// event handlers
	public void onEventArrived()
	{
		// do nothing at default
	}
}
