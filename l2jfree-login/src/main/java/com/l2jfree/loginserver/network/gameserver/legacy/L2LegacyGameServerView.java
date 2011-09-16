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
package com.l2jfree.loginserver.network.gameserver.legacy;

import java.net.InetAddress;

import com.l2jfree.loginserver.network.gameserver.L2GameServerView;
import com.l2jfree.network.legacy.ServerStatus;

/**
 * @author savormix
 */
public final class L2LegacyGameServerView extends L2GameServerView
{
	private final L2LegacyGameServer _gameServer;
	
	/**
	 * Creates a legacy game server's view.
	 * 
	 * @param gameServer legacy game server
	 */
	public L2LegacyGameServerView(L2LegacyGameServer gameServer)
	{
		_gameServer = gameServer;
	}
	
	@Override
	public void update()
	{
		L2LegacyGameServer gs = getGameServer();
		setId(gs.getId());
		try
		{
			// FIXME subnet-based hosts
			// REGRESSION -->
			byte[] ip = InetAddress.getByName(gs.getHost()).getAddress();
			System.arraycopy(ip, 0, getIpv4(), 0, getIpv4().length);
			// <-- REGRESSION
		}
		catch (Exception e)
		{
			_log.error("Could not obtain IPv4 from " + gs.getHost(), e);
			System.arraycopy(getDefaultIp(), 0, getIpv4(), 0, getIpv4().length);
		}
		setPort(gs.getPort());
		setAge(gs.getAge());
		setPvp(gs.isPvp());
		setOnlinePlayers(gs.getOnlineAccounts().size());
		setMaxPlayers(gs.getMaxPlayers());
		setOnline(gs.getStatus() != ServerStatus.DOWN);
		setTypes(gs.getTypes());
		setBrackets(gs.isBrackets());
	}
	
	private L2LegacyGameServer getGameServer()
	{
		return _gameServer;
	}
}
