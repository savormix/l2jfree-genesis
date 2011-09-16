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
package com.l2jfree.gameserver.gameobjects.player;

import com.l2jfree.gameserver.gameobjects.CharacterStat;
import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.gameobjects.components.interfaces.IPlayerStat;

/**
 * @author hex1r0
 * @author NB4L1
 */
public class PlayerStat extends CharacterStat implements IPlayerStat
{
	public PlayerStat(L2Player activeChar)
	{
		super(activeChar);
	}
	
	@Override
	public final L2Player getActiveChar()
	{
		return (L2Player)super.getActiveChar();
	}
	
	@Override
	public int getMaxSP()
	{
		return 0;
	}
	
	@Override
	public int getCurrentSP()
	{
		return 0;
	}
	
	@Override
	public int getMaxCP()
	{
		return 0;
	}
	
	@Override
	public int getCurrentCP()
	{
		return getMaxCP(); // FIXME
	}
	
	@Override
	public int getCarriedWeight()
	{
		return 0;
	}
	
	@Override
	public int getMaxCarriedWeight()
	{
		// TODO formulas
		return 100;
	}
	
	@Override
	public int getKarmaPoints()
	{
		return 0;
	}
	
	@Override
	public int getVitalityPoints()
	{
		return 0;
	}
	
	@Override
	public int getFamePoints()
	{
		return 0;
	}
	
	@Override
	public int getPkCount()
	{
		return 0;
	}
	
	@Override
	public int getPvPCount()
	{
		return 0;
	}
	
	/*
	@Override
	public double getRunSpeed()
	{
		// TODO formulas
		return getActiveChar().getTemplate().getPlayerBaseTemplate(getActiveChar().getAppearance().getGender())
				.getRunSpeed();
	}
	
	@Override
	public double getWalkSpeed()
	{
		// TODO formulas
		return getActiveChar().getTemplate().getPlayerBaseTemplate(getActiveChar().getAppearance().getGender())
				.getWalkSpeed();
	}
	
	@Override
	public int getPAtk(Object object)
	{
		// TODO formulas
		return getActiveChar().getTemplate().getPlayerBaseTemplate(getActiveChar().getAppearance().getGender())
				.getPAtk();
	}
	
	@Override
	public int getPAtkSpd()
	{
		// TODO formulas
		return getActiveChar().getTemplate().getPlayerBaseTemplate(getActiveChar().getAppearance().getGender())
				.getPAtkSpd();
	}
	
	@Override
	public int getMAtk(Object object, Object object2)
	{
		// TODO formulas
		return getActiveChar().getTemplate().getPlayerBaseTemplate(getActiveChar().getAppearance().getGender())
				.getMAtk();
	}
	
	@Override
	public int getMAtkSpd()
	{
		// TODO formulas
		return getActiveChar().getTemplate().getPlayerBaseTemplate(getActiveChar().getAppearance().getGender())
				.getMAtkSpd();
	}
	
	@Override
	public int getPDef(Object object)
	{
		// TODO formulas
		return getActiveChar().getTemplate().getPlayerBaseTemplate(getActiveChar().getAppearance().getGender())
				.getPDef();
	}
	
	@Override
	public int getMDef(Object object, Object object2)
	{
		// TODO formulas
		return getActiveChar().getTemplate().getPlayerBaseTemplate(getActiveChar().getAppearance().getGender())
				.getMDef();
	}
	
	@Override
	public int getAccuracy()
	{
		// TODO formulas
		return getActiveChar().getTemplate().getPlayerBaseTemplate(getActiveChar().getAppearance().getGender())
				.getAccuracy();
	}
	
	@Override
	public int getEvasion(Object object)
	{
		// TODO formulas
		return getActiveChar().getTemplate().getPlayerBaseTemplate(getActiveChar().getAppearance().getGender())
				.getEvasion();
	}
	
	@Override
	public int getCriticalHit(Object object)
	{
		// TODO formulas
		return getActiveChar().getTemplate().getPlayerBaseTemplate(getActiveChar().getAppearance().getGender())
				.getCriticalHit();
	}
	
	@Override
	public long getExp()
	{
		return 0;
	}
	
	@Override
	public int getSTR()
	{
		// TODO formulas
		return getActiveChar().getTemplate().getPlayerBaseTemplate(getActiveChar().getAppearance().getGender())
				.getSTR();
	}
	
	@Override
	public int getDEX()
	{
		// TODO formulas
		return getActiveChar().getTemplate().getPlayerBaseTemplate(getActiveChar().getAppearance().getGender())
				.getDEX();
	}
	
	@Override
	public int getCON()
	{
		// TODO formulas
		return getActiveChar().getTemplate().getPlayerBaseTemplate(getActiveChar().getAppearance().getGender())
				.getCON();
	}
	
	@Override
	public int getINT()
	{
		// TODO formulas
		return getActiveChar().getTemplate().getPlayerBaseTemplate(getActiveChar().getAppearance().getGender())
				.getINT();
	}
	
	@Override
	public int getWIT()
	{
		// TODO formulas
		return getActiveChar().getTemplate().getPlayerBaseTemplate(getActiveChar().getAppearance().getGender())
				.getWIT();
	}
	
	@Override
	public int getMEN()
	{
		// TODO formulas
		return getActiveChar().getTemplate().getPlayerBaseTemplate(getActiveChar().getAppearance().getGender())
				.getMEN();
	}
	
	@Override
	public int getMaxHP()
	{
		// TODO formulas
		return (int)getActiveChar().getTemplate().getHP(getLevel());
	}
	
	@Override
	public int getCurrentHP()
	{
		return getMaxHP(); // FIXME
	}
	
	@Override
	public int getMaxMP()
	{
		// TODO formulas
		return (int)getActiveChar().getTemplate().getMP(getLevel());
	}
	
	@Override
	public int getCurrentMP()
	{
		return getMaxMP(); // FIXME
	}*/
}
