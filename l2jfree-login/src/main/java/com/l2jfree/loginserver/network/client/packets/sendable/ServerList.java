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

import com.l2jfree.loginserver.account.AccountCharacterInfo;
import com.l2jfree.loginserver.account.AccountCharacterManager;
import com.l2jfree.loginserver.account.CharactersOnServer;
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
			final byte[] ip = gsv.getIpv4(client.getInetAddress());
			for (int i = 0; i < 4; i++)
				buf.writeC(ip[i]);
			buf.writeD(gsv.getPort());
			buf.writeC(gsv.getAge());
			buf.writeC(gsv.isPvp());
			buf.writeH(gsv.getOnlinePlayers());
			buf.writeH(gsv.getMaxPlayers());
			buf.writeC(gsv.isOnline());
			buf.writeD(gsv.getTypes());
			buf.writeC(gsv.isBrackets());
		}
		
		// FIXME in which chronicle were these introduced?
		final AccountCharacterInfo aci =
				AccountCharacterManager.getInstance().getCharacterInfo(client.getAccount().getAccount());
		
		final int bytesize;
		{
			int pendingRemoval = 0; // total characters pending removal
			for (final L2GameServerView gsv : _gameServers)
			{
				final CharactersOnServer cos = aci.getInfo(gsv.getId());
				pendingRemoval += cos.getDelTime().length;
			}
			bytesize = 1 + count * 3 + pendingRemoval * 4;
		}
		
		buf.writeH(bytesize);
		buf.writeC(count);
		for (final L2GameServerView gsv : _gameServers)
		{
			final int id = gsv.getId();
			buf.writeC(gsv.getId());
			
			final CharactersOnServer cos = aci.getInfo(id);
			buf.writeC(cos.getCharacters()); // total player's characters (on a GS)
			final long[] delTime = cos.getDelTime();
			buf.writeC(delTime.length); // player's characters pending removal (on a GS)
			for (long element : delTime)
				buf.writeD(element / 1000); // time of removal
		}
	}
}
