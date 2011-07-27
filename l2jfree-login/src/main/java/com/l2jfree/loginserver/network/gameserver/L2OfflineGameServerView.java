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

import com.l2jfree.loginserver.network.legacy.status.L2LegacyAgeLimit;
import com.l2jfree.loginserver.network.legacy.status.L2LegacyType;

/**
 * @author savormix
 *
 */
public class L2OfflineGameServerView extends L2GameServerView
{
	/**
	 * Creates a view for an offline server.
	 * @param id game server ID
	 */
	public L2OfflineGameServerView(int id)
	{
		setId(id);
		System.arraycopy(getDefaultIp(), 0, getIpv4(), 0, getIpv4().length);
		setPort(0);
		setAge(L2LegacyAgeLimit.ANY.getMin());
		setPvp(false);
		setOnlinePlayers(0);
		setMaxPlayers(0);
		setOnline(false);
		setTypes(L2LegacyType.NORMAL.getMask());
		setBrackets(false);
	}
	
	@Override
	public void update()
	{
		// do nothing
	}
}
