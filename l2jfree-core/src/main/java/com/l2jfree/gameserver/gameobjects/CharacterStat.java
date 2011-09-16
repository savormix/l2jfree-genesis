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
import com.l2jfree.gameserver.gameobjects.components.interfaces.IElemental;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author hex1r0
 * @author NB4L1
 */
public abstract class CharacterStat implements ICharacterStat
{
	public static enum Element
	{
		/**
		 * Not applicable:
		 * <UL>
		 * <LI>Item is not a weapon or player has no weapon equipped</LI>
		 * <LI>[Equipped] Weapon is not elementally enhanced</LI>
		 * </UL>
		 */
		NA(-2),
		/** Doesn't seem to be used. An in-depth analysis is required */
		NONE(-1),
		FIRE(0),
		WATER(1),
		WIND(2),
		EARTH(3),
		HOLY(4),
		DARK(5);
		
		private final int _value;
		
		Element(int value)
		{
			_value = value;
		}
		
		public int getValue()
		{
			return _value;
		}
		
		public static void writeElements(IElemental elemental, MMOBuffer buf)
		{
			buf.writeH(elemental.getAttackElementType().getValue()); // Attack element
			buf.writeH(elemental.getAttackElementPower()); // Attack element power
			buf.writeH(elemental.getDefenseElementPower(Element.FIRE)); // Fire defense
			buf.writeH(elemental.getDefenseElementPower(Element.WATER)); // Water defense
			buf.writeH(elemental.getDefenseElementPower(Element.WIND)); // Wind defense
			buf.writeH(elemental.getDefenseElementPower(Element.EARTH)); // Earth defense
			buf.writeH(elemental.getDefenseElementPower(Element.HOLY)); // Holy defense
			buf.writeH(elemental.getDefenseElementPower(Element.DARK)); // Dark defense
		}
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
		return 1; // FIXME
	}
	
	@Override
	public double getAttackSpeedMultiplier()
	{
		return 1; // FIXME
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
	public int getPDef(Object object)
	{
		return 0;
	}
	
	@Override
	public int getMDef(Object object, Object object2)
	{
		return 0;
	}
	
	@Override
	public int getAccuracy()
	{
		return 0;
	}
	
	@Override
	public int getEvasion(Object object)
	{
		return 0;
	}
	
	@Override
	public int getCriticalHit(Object object)
	{
		return 0;
	}
	
	@Override
	public int getLevel()
	{
		return 1; // FIXME
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
	public Element getAttackElement()
	{
		return Element.NA;
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
