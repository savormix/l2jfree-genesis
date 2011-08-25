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

/**
 * @author savormix
 */
public enum L2LegacyType
{
	/** A game server */
	NORMAL(0),
	/** Week time limited */
	RELAX(1),
	/** Only shown by test clients */
	TEST(2),
	/** Name not shown */
	NAMELESS(3),
	/** No new characters allowed */
	LEGACY(4),
	/** Event server */
	EVENT(5),
	/** Free server */
	FREE(6);
	
	private final int _mask;
	
	private L2LegacyType(int type)
	{
		_mask = 1 << type;
	}
	
	/**
	 * Returns type bit mask.
	 * 
	 * @return type mask
	 */
	public int getMask()
	{
		return _mask;
	}
	
	/**
	 * Determines whether the given game server is of a specific type.
	 * 
	 * @param lgs game server
	 * @param type type
	 * @return if the server is of given type
	 */
	public static boolean isOfType(L2LegacyGameServer lgs, L2LegacyType type)
	{
		return isOfType(lgs.getTypes(), type);
	}
	
	private static boolean isOfType(int mask, L2LegacyType type)
	{
		return (mask & type.getMask()) != 0;
	}
}
