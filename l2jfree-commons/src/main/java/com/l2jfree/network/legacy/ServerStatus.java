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

import com.l2jfree.util.EnumValues;

// Good, Normal and Full are not used for years
// Oh and they wont be used. ever.
/** This enum was designed for non-nio LS<->GS connections. */
public enum ServerStatus
{
	/** Online */
	AUTO("Auto"),
	/** Online */
	GOOD("Good"),
	/** Online */
	NORMAL("Normal"),
	/** Online */
	FULL("Full"),
	/** Offline */
	DOWN("Down"),
	/** Online for GMs */
	GM_ONLY("GM only");
	
	private final String _status;
	
	private ServerStatus(String status)
	{
		_status = status;
	}
	
	public String getStatus()
	{
		return _status;
	}
	
	public static final EnumValues<ServerStatus> VALUES = new EnumValues<ServerStatus>(ServerStatus.class) {
		@Override
		protected ServerStatus defaultValue()
		{
			// TODO
			// _log.warn("Unknown legacy status ID: " + id);
			return AUTO;
		}
	};
}
