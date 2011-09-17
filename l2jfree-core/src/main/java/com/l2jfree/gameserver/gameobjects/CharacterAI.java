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

import com.l2jfree.gameserver.gameobjects.components.interfaces.ICharacterAI;

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
	
	public static abstract class Desire
	{
		protected abstract void execute(CharacterAI ai);
	}
	
	public static class IdleDesire extends Desire
	{
		@Override
		protected void execute(CharacterAI ai)
		{
			ai.onIntentionIdle();
		}
	}
	
	public static class ActiveDesire extends Desire
	{
		@Override
		protected void execute(CharacterAI ai)
		{
			ai.onIntentionActive();
		}
	}
	
	public static class MoveDesire extends Desire
	{
		private final int _x;
		private final int _y;
		private final int _z;
		
		public MoveDesire(int x, int y, int z)
		{
			_x = x;
			_y = y;
			_z = z;
		}
		
		@Override
		protected void execute(CharacterAI ai)
		{
			ai.onIntentionMove(_x, _y, _z);
		}
	}
	
	public void addDesire(Desire desire)
	{
		// TODO
		// adds a new desire
		
		think();
	}
	
	public void setDesire(Desire desire)
	{
		// TODO
		// replaces all previous desire with the given one
		
		think();
	}
	
	@SuppressWarnings("null")
	public void think()
	{
		// TODO implement
		// check if it "can" think right now
		// if yes, then find most important desire, 
		Desire mostDesired = null;
		
		// and behave according to it... schedule asynchronously if necessary
		if (mostDesired != null)
			mostDesired.execute(this);
		else
			onIntentionIdle();
	}
	
	// intention handlers
	protected void onIntentionIdle()
	{
		// do nothing at default
	}
	
	protected void onIntentionActive()
	{
		// do nothing at default
	}
	
	protected void onIntentionMove(int x, int y, int z)
	{
		// do nothing at default
	}
	
	// event handlers
	public void onEventArrived()
	{
		// do nothing at default
	}
}
