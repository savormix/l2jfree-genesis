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
package com.l2jfree.network.legacy;

// Good, Normal and Full are not used for years
// Oh and they wont be used. ever.
/** This enum was designed for non-nio LS<->GS connections. */
@Deprecated
public enum ServerStatus
{
	STATUS_AUTO,
	STATUS_GOOD,
	STATUS_NORMAL,
	STATUS_FULL,
	STATUS_DOWN,
	STATUS_GM_ONLY;
	
	private static final ServerStatus[] VALUES = ServerStatus.values();
	
	public static ServerStatus valueOf(int index)
	{
		if (index < 0 || VALUES.length <= index)
			return STATUS_AUTO;
		
		return VALUES[index];
	}
}
