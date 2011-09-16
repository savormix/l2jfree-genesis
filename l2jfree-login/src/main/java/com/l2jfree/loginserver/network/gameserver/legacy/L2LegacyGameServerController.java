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

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;

import javolution.util.FastMap;

import com.l2jfree.loginserver.LoginInfo;
import com.l2jfree.loginserver.network.gameserver.legacy.packets.L2LegacyGameServerPacket;
import com.l2jfree.loginserver.network.gameserver.legacy.packets.L2LegacyLoginServerPacket;
import com.l2jfree.loginserver.network.gameserver.legacy.packets.sendable.InitLS;
import com.l2jfree.network.mmocore.MMOConfig;
import com.l2jfree.network.mmocore.MMOController;

/**
 * @author savormix
 */
public final class L2LegacyGameServerController extends
		MMOController<L2LegacyGameServer, L2LegacyGameServerPacket, L2LegacyLoginServerPacket>
{
	private static final class SingletonHolder
	{
		static
		{
			final MMOConfig cfg = new MMOConfig("Legacy Game Servers");
			cfg.setSelectorSleepTime(25);
			cfg.setThreadCount(1);
			
			try
			{
				INSTANCE = new L2LegacyGameServerController(cfg);
			}
			catch (IOException e)
			{
				throw new Error(e);
			}
		}
		
		public static final L2LegacyGameServerController INSTANCE;
	}
	
	/**
	 * Returns a singleton object.
	 * 
	 * @return an instance of this class
	 */
	public static L2LegacyGameServerController getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private final FastMap<Integer, L2LegacyGameServer> _gameServers;
	
	private L2LegacyGameServerController(MMOConfig config) throws IOException
	{
		super(config, L2LegacyGameServerPacketHandler.getInstance());
		
		L2LegacyGameServerSecurity.getInstance();
		
		_gameServers = FastMap.newInstance();
		_gameServers.setShared(true);
	}
	
	@Override
	protected L2LegacyGameServer createClient(SocketChannel socketChannel) throws ClosedChannelException
	{
		L2LegacyGameServer lgs = new L2LegacyGameServer(this, socketChannel);
		lgs.sendPacket(new InitLS((RSAPublicKey)lgs.getPublicKey()));
		return lgs;
	}
	
	/**
	 * Adds an authorized game server.
	 * 
	 * @param id game server ID
	 * @param client game server
	 */
	public void addGameServer(int id, L2LegacyGameServer client)
	{
		getGameServers().put(id, client);
	}
	
	/**
	 * Removes a possibly authorized game server.
	 * 
	 * @param client game server
	 */
	public void remGameServer(L2LegacyGameServer client)
	{
		Integer id = client.getId();
		if (id != null)
			getGameServers().remove(id);
	}
	
	/**
	 * Returns an authorized game server with the assigned ID.
	 * 
	 * @param id assigned ID
	 * @return an authorized game server or <TT>null</TT>
	 */
	public L2LegacyGameServer getById(int id)
	{
		return getGameServers().get(id);
	}
	
	/**
	 * Returns authorized game servers.
	 * 
	 * @return authorized game servers
	 */
	public Collection<L2LegacyGameServer> getAuthorized()
	{
		return getGameServers().values();
	}
	
	/**
	 * Returns authorized game servers.
	 * 
	 * @return authorized game servers
	 */
	private FastMap<Integer, L2LegacyGameServer> getGameServers()
	{
		return _gameServers;
	}
	
	@Override
	protected String getVersionInfo()
	{
		return super.getVersionInfo() + " - " + LoginInfo.getVersionInfo();
	}
}
