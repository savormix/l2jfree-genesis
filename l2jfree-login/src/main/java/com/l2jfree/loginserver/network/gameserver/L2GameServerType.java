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
package com.l2jfree.loginserver.network.gameserver;

import com.l2jfree.util.EnumValues;

/**
 * @author savormix
 */
public enum L2GameServerType
{
	NONE,
	UNKNOWN,
	RELAX,
	TEST,
	UNNAMED,
	CC_RESTRICTED,
	EVENT;
	
	public int enable(int mask)
	{
		return mask | (1 << (ordinal() - 1));
	}
	
	public int disable(int mask)
	{
		return mask ^ (1 << (ordinal() - 1));
	}
	
	public int set(int mask, boolean enabled)
	{
		if (enabled)
			return enable(mask);
		else
			return disable(mask);
	}
	
	public static final EnumValues<L2GameServerType> VALUES = new EnumValues<L2GameServerType>(L2GameServerType.class) {
		@Override
		protected L2GameServerType defaultValue()
		{
			return NONE;
		}
	};
	
	public static L2GameServerType valueOf(int index)
	{
		return VALUES.valueOf(index);
	}
}
