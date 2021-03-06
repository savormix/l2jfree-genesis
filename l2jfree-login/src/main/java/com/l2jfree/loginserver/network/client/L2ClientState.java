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
package com.l2jfree.loginserver.network.client;

/**
 * @author savormix
 */
public enum L2ClientState
{
	/** Connection between the client and login server has been established. */
	CONNECTED,
	/** Client has passed a GameGuard check. */
	GAMEGUARD_PASSED,
	/** Client has logged in with a valid account. */
	LOGGED_IN,
	/** Client has received the game server list. */
	VIEWING_LIST;
}
