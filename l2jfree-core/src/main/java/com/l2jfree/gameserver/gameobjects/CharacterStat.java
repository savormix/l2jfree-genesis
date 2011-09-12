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

import com.l2jfree.gameserver.gameobjects.components.interfaces.ICharacterStat;

/**
 * @author hex1r0
 * @author NB4L1
 */
public abstract class CharacterStat implements ICharacterStat
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
	
	@Override
	public double getMovementSpeedMultiplier()
	{
		return 0;
	}
	
	@Override
	public double getAttackSpeedMultiplier()
	{
		return 0;
	}
	
	@Override
	public double getRunSpeed()
	{
		return 0;
	}
	
	@Override
	public double getWalkSpeed()
	{
		return 0;
	}
	
	@Override
	public int getPAtk(Object object)
	{
		return 0;
	}
	
	@Override
	public int getPDef(Object object)
	{
		return 0;
	}
	
	@Override
	public int getPAtkSpd()
	{
		return 0;
	}
	
	@Override
	public int getMAtk(Object object, Object object2)
	{
		return 0;
	}
	
	@Override
	public int getMAtkSpd()
	{
		return 0;
	}
	
	@Override
	public int getAccuracy()
	{
		return 0;
	}
	
	@Override
	public int getEvasionRate(Object object)
	{
		return 0;
	}
	
	@Override
	public int getCriticalHit(Object object)
	{
		return 0;
	}
	
	@Override
	public int getMDef(Object object, Object object2)
	{
		return 0;
	}
	
	@Override
	public int getLevel()
	{
		return 0;
	}
	
	@Override
	public long getExp()
	{
		return 0;
	}
	
	@Override
	public int getSTR()
	{
		return 0;
	}
	
	@Override
	public int getDEX()
	{
		return 0;
	}
	
	@Override
	public int getCON()
	{
		return 0;
	}
	
	@Override
	public int getINT()
	{
		return 0;
	}
	
	@Override
	public int getWIT()
	{
		return 0;
	}
	
	@Override
	public int getMEN()
	{
		return 0;
	}
	
	@Override
	public int getMaxHP()
	{
		return 0;
	}
	
	@Override
	public int getCurrentHP()
	{
		return 0;
	}
	
	@Override
	public int getMaxMP()
	{
		return 0;
	}
	
	@Override
	public int getCurrentMP()
	{
		return 0;
	}
	
	@Override
	public byte getAttackElement()
	{
		return 0;
	}
	
	@Override
	public int getAttackElementPower(Element element)
	{
		return 0;
	}
	
	@Override
	public int getDefenseElementPower(Element element)
	{
		return 0;
	}
}
