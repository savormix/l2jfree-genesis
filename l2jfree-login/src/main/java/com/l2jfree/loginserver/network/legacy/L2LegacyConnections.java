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
package com.l2jfree.loginserver.network.legacy;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.security.interfaces.RSAPublicKey;

import com.l2jfree.loginserver.network.legacy.packets.L2GameServerPacket;
import com.l2jfree.loginserver.network.legacy.packets.L2LoginServerPacket;
import com.l2jfree.loginserver.network.legacy.packets.sendable.InitLS;
import com.l2jfree.network.mmocore.MMOConfig;
import com.l2jfree.network.mmocore.MMOController;

/**
 * @author savormix
 *
 */
public final class L2LegacyConnections extends MMOController<L2GameServer, L2GameServerPacket, L2LoginServerPacket>
{
	private static final class SingletonHolder
	{
		static
		{
			final MMOConfig cfg = new MMOConfig("Legacy Game Servers");
			cfg.setSelectorSleepTime(25 * 1000 * 1000);
			cfg.setThreadCount(1);
			cfg.setAcceptTimeout(5 * 1000);
			
			try
			{
				INSTANCE = new L2LegacyConnections(cfg);
			}
			catch (IOException e)
			{
				throw new Error(e);
			}
		}
		
		private static final L2LegacyConnections INSTANCE;
	}
	
	/**
	 * Returns a singleton object.
	 * @return an instance of this class
	 */
	public static L2LegacyConnections getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	protected L2LegacyConnections(MMOConfig config)
			throws IOException
	{
		super(config, L2LegacyPackets.getInstance());
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected L2GameServer createClient(SocketChannel socketChannel)
			throws ClosedChannelException
	{
		L2LegacySecurity lls = L2LegacySecurity.getInstance();
		L2GameServer lgs = new L2GameServer(this, socketChannel, lls.getKeyPair());
		lgs.sendPacket(new InitLS((RSAPublicKey) lgs.getPublicKey()));
		return lgs;
	}
}
