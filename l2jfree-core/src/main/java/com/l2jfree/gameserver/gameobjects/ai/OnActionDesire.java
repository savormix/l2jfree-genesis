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

import com.l2jfree.gameserver.gameobjects.CharacterAI;
import com.l2jfree.gameserver.gameobjects.L2Object;

/**
 * @author NB4L1
 */
public class OnActionDesire extends AIDesire
{
	private final L2Object _target;
	private final boolean _cantMove;
	private final boolean _forceAttack;
	
	public OnActionDesire(L2Object target, boolean cantMove, boolean forceAttack)
	{
		_target = target;
		_cantMove = cantMove;
		_forceAttack = forceAttack;
	}
	
	@Override
	public void execute(CharacterAI ai)
	{
		ai.onIntentionOnAction(_target, _cantMove, _forceAttack);
	}
}
