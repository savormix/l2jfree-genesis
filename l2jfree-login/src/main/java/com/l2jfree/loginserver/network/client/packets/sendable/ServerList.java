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

import com.l2jfree.loginserver.network.client.L2Client;
import com.l2jfree.loginserver.network.client.packets.L2ServerPacket;
import com.l2jfree.loginserver.network.gameserver.L2GameServerCache;
import com.l2jfree.loginserver.network.gameserver.L2GameServerView;
import com.l2jfree.loginserver.network.gameserver.legacy.L2LegacyGameServer;
import com.l2jfree.loginserver.network.gameserver.legacy.L2LegacyGameServerController;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix
 */
public final class ServerList extends L2ServerPacket
{
	private final SortedSet<L2GameServerView> _gameServers;
	
	/**
	 * Constructs a packet to inform about known game servers.
	 */
	public ServerList()
	{
		_gameServers = new TreeSet<L2GameServerView>(L2GameServerCache.getInstance().getRegisteredGameServers());
		
		for (L2LegacyGameServer element : L2LegacyGameServerController.getInstance().getAuthorized())
		{
			L2GameServerView lgsv = element.getView();
			lgsv.update();
			_gameServers.remove(lgsv); // cached empty view
			_gameServers.add(lgsv); // up-to-date view
		}
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x04;
	}
	
	@Override
	protected void writeImpl(L2Client client, MMOBuffer buf)
	{
		final int count = _gameServers.size();
		buf.writeC(count);
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
		
		final int totalChars = 0; // total player's characters (on a GS)
		final int pendingRemoval = 0; // player's characters pending removal (on a GS)
		final int bytesize = 1 + count * 3 + pendingRemoval * 4;
		
		buf.writeH(bytesize);
		buf.writeC(count);
		for (L2GameServerView gsv : _gameServers)
		{
			buf.writeC(gsv.getId());
			buf.writeC(totalChars);
			buf.writeC(pendingRemoval);
			for (int i = 0; i < pendingRemoval; i++)
				buf.writeD(0); // time of removal
		}
	}
}
