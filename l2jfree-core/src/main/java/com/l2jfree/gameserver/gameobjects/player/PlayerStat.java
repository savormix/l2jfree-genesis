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

/**
 * @author hex1r0
 * @author NB4L1
 */
@SuppressWarnings({ "static-method" })
public class PlayerStat extends CharacterStat
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
	
	public int getMaxSP()
	{
		return 0;
	}
	
	public int getMaxCP()
	{
		return 0;
	}
	
	public int getCurrentCP()
	{
		return 0;
	}
	
	public int getCarriedWeight()
	{
		return 0;
	}
	
	public int getMaxCarriedWeight()
	{
		return 0;
	}
	
	public int getKarmaPoints()
	{
		return 0;
	}
	
	public int getVitalityPoints()
	{
		return 0;
	}
	
	public int getFamePoints()
	{
		return 0;
	}
	
	public int getPkCount()
	{
		return 0;
	}
	
	public int getPvPCount()
	{
		return 0;
	}
}
