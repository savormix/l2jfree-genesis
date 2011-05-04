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
package com.l2jfree.network;

// Compatible, legacy values
// FIXME new login server/protocol
public enum ServerStatusAttributes
{
	NONE,
	SERVER_LIST_STATUS,
	SERVER_LIST_CLOCK,
	SERVER_LIST_BRACKETS,
	SERVER_LIST_MAX_PLAYERS,
	TEST_SERVER,
	SERVER_LIST_PVP,
	SERVER_LIST_UNK,
	SERVER_LIST_HIDE_NAME,
	SERVER_AGE_LIMITATION;
	
	private static final ServerStatusAttributes[] VALUES = ServerStatusAttributes.values();
	
	public static ServerStatusAttributes valueOf(int index)
	{
		if (index < 0 || VALUES.length <= index)
			return NONE;
		
		return VALUES[index];
	}
}
