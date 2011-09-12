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

import com.l2jfree.gameserver.gameobjects.components.IComponent;

/**
 * @author hex1r0
 * @author NB4L1
 */
@SuppressWarnings({ "unused", "static-method" })
public abstract class CharacterStat implements IComponent
{
	public static enum Element
	{
		FIRE,
		WATER,
		WIND,
		EARTH,
		HOLY,
		DARK;
		
		public static final byte NONE = -1;
	}
	
	private final L2Character _activeChar;
	
	protected CharacterStat(L2Character activeChar)
	{
		_activeChar = activeChar;
	}
	
	public L2Character getActiveChar()
	{
		return _activeChar;
	}
	
	public double getMovementSpeedMultiplier()
	{
		return 0;
	}
	
	public double getAttackSpeedMultiplier()
	{
		return 0;
	}
	
	public double getRunSpeed()
	{
		return 0;
	}
	
	public double getWalkSpeed()
	{
		return 0;
	}
	
	public int getPAtk(Object object)
	{
		return 0;
	}
	
	public int getPDef(Object object)
	{
		return 0;
	}
	
	public int getPAtkSpd()
	{
		return 0;
	}
	
	public int getMAtk(Object object, Object object2)
	{
		return 0;
	}
	
	public int getMAtkSpd()
	{
		return 0;
	}
	
	public int getAccuracy()
	{
		return 0;
	}
	
	public int getEvasionRate(Object object)
	{
		return 0;
	}
	
	public int getCriticalHit(Object object)
	{
		return 0;
	}
	
	public int getMDef(Object object, Object object2)
	{
		return 0;
	}
	
	public int getLevel()
	{
		return 0;
	}
	
	public long getExp()
	{
		return 0;
	}
	
	public int getSTR()
	{
		return 0;
	}
	
	public int getDEX()
	{
		return 0;
	}
	
	public int getCON()
	{
		return 0;
	}
	
	public int getINT()
	{
		return 0;
	}
	
	public int getWIT()
	{
		return 0;
	}
	
	public int getMEN()
	{
		return 0;
	}
	
	public int getMaxHP()
	{
		return 0;
	}
	
	public int getCurrentHP()
	{
		return 0;
	}
	
	public int getMaxMP()
	{
		return 0;
	}
	
	public int getCurrentMP()
	{
		return 0;
	}
	
	public byte getAttackElement()
	{
		return 0;
	}
	
	public int getAttackElementPower(Element element)
	{
		return 0;
	}
	
	public int getDefenseElementPower(Element element)
	{
		return 0;
	}
	
}
