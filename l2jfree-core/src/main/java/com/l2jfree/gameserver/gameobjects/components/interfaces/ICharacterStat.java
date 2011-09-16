/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option); any later
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
package com.l2jfree.gameserver.gameobjects.components.interfaces;

import com.l2jfree.gameserver.gameobjects.CharacterStat.Element;
import com.l2jfree.gameserver.gameobjects.components.IComponent;

/**
 * @author NB4L1
 */
public interface ICharacterStat extends IComponent
{
	public double getMovementSpeedMultiplier();
	
	public double getAttackSpeedMultiplier();
	
	public double getRunSpeed();
	
	public double getWalkSpeed();
	
	public int getPAtk(Object object);
	
	public int getPDef(Object object);
	
	public int getPAtkSpd();
	
	public int getMAtk(Object object, Object object2);
	
	public int getMAtkSpd();
	
	public int getAccuracy();
	
	public int getEvasion(Object object);
	
	public int getCriticalHit(Object object);
	
	public int getMDef(Object object, Object object2);
	
	public int getLevel();
	
	public long getExp();
	
	public int getSTR();
	
	public int getDEX();
	
	public int getCON();
	
	public int getINT();
	
	public int getWIT();
	
	public int getMEN();
	
	public int getMaxHP();
	
	public int getCurrentHP();
	
	public int getMaxMP();
	
	public int getCurrentMP();
	
	public Element getAttackElement();
	
	public int getAttackElementPower(Element element);
	
	public int getDefenseElementPower(Element element);
}
