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
package com.l2jfree.gameserver.templates.player;

/**
 * @author NB4L1
 */
public enum ClassLevel
{
	First(1),
	Second(20),
	Third(40),
	Fourth(76);
	
	private final int _baseLevel;
	
	private ClassLevel(int baseLevel)
	{
		_baseLevel = baseLevel;
	}
	
	public int getBaseLevel()
	{
		return _baseLevel;
	}
}
