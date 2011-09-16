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
package com.l2jfree.gameserver.gameobjects.ai;

import java.lang.reflect.Array;

import com.l2jfree.gameserver.gameobjects.components.interfaces.ICharacterAi;

/**
 * @author hex1r0
 */
public abstract class CharacterAi implements ICharacterAi
{
	// Will be populated with necessary values
	public static enum Intention
	{
		/** Do nothing */
		IDLE,
		
		/** Watch for everything that happens with surrounding objects */
		ACTIVE,
		
		/** Move to target position */
		MOVE_TO,
	}
	
	// Will be populated with necessary values
	public static enum Event
	{
		/** Active character arrived to target position */
		ARRIVED,
	}
	
	public final void setIntention(Intention intention)
	{
		setIntention(intention, null);
	}
	
	public final void setIntention(Intention intention, Object[] args)
	{
		switch (intention)
		{
			case IDLE:
				onIntentionIdle();
				break;
			case ACTIVE:
				onIntentionActive();
				break;
			case MOVE_TO:
				onIntentionMoveTo((Integer)Array.get(args[0], 0), (Integer)Array.get(args[0], 1),
						(Integer)Array.get(args[0], 2));
				break;
		}
	}
	
	protected abstract void onIntentionIdle();
	
	protected abstract void onIntentionActive();
	
	protected abstract void onIntentionMoveTo(int x, int y, int z);
	
	public final void notifyEvent(Event event)
	{
		notifyEvent(event, null);
	}
	
	public final void notifyEvent(Event event, Object[] args)
	{
		switch (event)
		{
			case ARRIVED:
				onEvtArrived();
				break;
		}
	}
	
	protected abstract void onEvtArrived();
}
