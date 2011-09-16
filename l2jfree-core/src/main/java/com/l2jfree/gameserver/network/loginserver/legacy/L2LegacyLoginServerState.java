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
package com.l2jfree.gameserver.network.loginserver.legacy;

/**
 * @author hex1r0
 */
public enum L2LegacyLoginServerState
{
	/**
	 * Connection between the game server and login server has been established.
	 */
	CONNECTED,
	/**
	 * Servers know how to encipher data.
	 */
	KEYS_EXCHANGED,
	/**
	 * An ID was successfully assigned to the game server.
	 */
	AUTHED;
}
