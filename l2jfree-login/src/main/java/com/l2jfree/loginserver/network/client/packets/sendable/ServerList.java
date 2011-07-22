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
package com.l2jfree.loginserver.network.client.packets.sendable;

import java.util.SortedSet;
import java.util.TreeSet;

import com.l2jfree.loginserver.network.client.L2LoginClient;
import com.l2jfree.loginserver.network.client.packets.L2ServerPacket;
import com.l2jfree.loginserver.network.gameserver.L2GameServerCache;
import com.l2jfree.loginserver.network.gameserver.L2GameServerView;
import com.l2jfree.loginserver.network.legacy.L2GameServer;
import com.l2jfree.loginserver.network.legacy.L2LegacyConnections;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix
 *
 */
public final class ServerList extends L2ServerPacket
{
	private final SortedSet<L2GameServerView> _gameServers;
	
	/**
	 * Constructs a packet to inform about known game servers.
	 */
	public ServerList()
	{
		_gameServers = new TreeSet<L2GameServerView>();
		for (L2GameServerView lgsv : L2GameServerCache.getInstance().getRegisteredGameServers())
			_gameServers.add(lgsv);
		
		Object[] authed = L2LegacyConnections.getInstance().getAuthorized().toArray();
		for (int i = 0; i < authed.length; i++)
		{
			L2GameServerView lgsv = ((L2GameServer) authed[i]).getView();
			lgsv.update();
			_gameServers.remove(lgsv);
			_gameServers.add(lgsv);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.l2jfree.loginserver.network.client.packets.L2ServerPacket#getOpcode()
	 */
	@Override
	protected int getOpcode()
	{
		return 0x04;
	}
	
	/* (non-Javadoc)
	 * @see com.l2jfree.loginserver.network.client.packets.L2ServerPacket#writeImpl(com.l2jfree.loginserver.network.client.L2LoginClient, com.l2jfree.network.mmocore.MMOBuffer)
	 */
	@Override
	protected void writeImpl(L2LoginClient client, MMOBuffer buf)
	{
		buf.writeC(_gameServers.size());
		buf.writeC(client.getAccount().getLastServerId());
		
		for (L2GameServerView gsv : _gameServers)
		{
			buf.writeC(gsv.getId());
			for (int i = 0; i < 4; i++)
				buf.writeC(gsv.getIpv4()[i]);
			buf.writeD(gsv.getPort());
			buf.writeC(gsv.getAge());
			buf.writeC(gsv.isPvp());
			buf.writeH(gsv.getOnlinePlayers());
			buf.writeH(gsv.getMaxPlayers());
			buf.writeC(gsv.isOnline());
			buf.writeD(gsv.getTypes());
			buf.writeC(gsv.isBrackets());
		}
	}
}
