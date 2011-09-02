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

/** This enum was designed for non-nio LS<->GS connections. */
@Deprecated
public enum LoginServerFailReason
{
	REASON_NONE("None"), // 0x00
	REASON_IP_BANNED("Reason: ip banned"), // 0x01
	REASON_IP_RESERVED("Reason: ip reserved"), // 0x02
	REASON_WRONG_HEXID("Reason: wrong hexid"), // 0x03
	REASON_ID_RESERVED("Reason: id reserved"), // 0x04
	REASON_NO_FREE_ID("Reason: no free ID"), // 0x05
	REASON_NOT_AUTHED("Not authed"), // 0x06
	REASON_ALREADY_LOGGED_IN("Reason: already logged in"); // 0x07
	
	private final String _reason;
	
	private LoginServerFailReason(String reason)
	{
		_reason = reason;
	}
	
	public String getReasonString()
	{
		return _reason;
	}
	
	public static final EnumValues<LoginServerFailReason> VALUES = new EnumValues<LoginServerFailReason>(
			LoginServerFailReason.class) {
		@Override
		protected LoginServerFailReason defaultValue()
		{
			return REASON_NONE;
		}
	};
	
	public static LoginServerFailReason valueOf(int index)
	{
		return VALUES.valueOf(index);
	}
}
