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
	public int getMaxCP()
	{
		return 0;
	}
	
	@Override
	public int getCurrentCP()
	{
		return 0;
	}
	
	@Override
	public int getCarriedWeight()
	{
		return 0;
	}
	
	@Override
	public int getMaxCarriedWeight()
	{
		return 0;
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
}
