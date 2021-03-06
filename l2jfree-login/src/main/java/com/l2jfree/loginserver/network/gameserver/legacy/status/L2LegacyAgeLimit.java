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
package com.l2jfree.loginserver.network.gameserver.legacy.status;

import com.l2jfree.loginserver.network.gameserver.legacy.L2LegacyGameServer;
import com.l2jfree.util.EnumValues;

/**
 * @author savormix
 */
public enum L2LegacyAgeLimit
{
	/** No age restriction */
	ANY(0),
	/** 15 or older (Korea-only, non-PvP) */
	_15(15);
	/* 18 or older (Korea-only, PvP): no longer applicable */
	//_18(18)*/;
	
	private final int _min;
	
	private L2LegacyAgeLimit(int min)
	{
		_min = min;
	}
	
	/**
	 * Returns age required to play.
	 * 
	 * @return minimal player age
	 */
	public int getMin()
	{
		return _min;
	}
	
	/**
	 * Returns whether the game server's age limit is displayed in the server list.
	 * 
	 * @param lgs game server
	 * @return whether the limit is visible or not
	 */
	public static boolean isDisplayed(L2LegacyGameServer lgs)
	{
		for (L2LegacyAgeLimit llal : VALUES)
			if (lgs.getAge() == llal.getMin())
				return true;
		return false;
	}
	
	public static final EnumValues<L2LegacyAgeLimit> VALUES = new EnumValues<L2LegacyAgeLimit>(L2LegacyAgeLimit.class);
}
