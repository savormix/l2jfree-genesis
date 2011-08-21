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
package com.l2jfree.gameserver.network.client;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.network.mmocore.MMOConfig;
import com.l2jfree.network.mmocore.MMOController;

/**
 * @author savormix
 */
public final class L2ClientConnections extends MMOController<L2CoreClient, L2ClientPacket, L2ServerPacket>
{
	private static final class SingletonHolder
	{
		static
		{
			final MMOConfig cfg = new MMOConfig("Experimental Core");
			cfg.setSelectorSleepTime(7);
			
			try
			{
				INSTANCE = new L2ClientConnections(cfg);
			}
			catch (IOException e)
			{
				throw new Error(e);
			}
		}
		
		public static final L2ClientConnections INSTANCE;
	}
	
	/**
	 * Returns a singleton object.
	 * 
	 * @return an instance of this class
	 */
	public static L2ClientConnections getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	protected L2ClientConnections(MMOConfig config) throws IOException
	{
		super(config, L2ClientPackets.getInstance());
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected L2CoreClient createClient(SocketChannel socketChannel) throws ClosedChannelException
	{
		L2ClientSecurity lcs = L2ClientSecurity.getInstance();
		L2CoreClient lcc = new L2CoreClient(socketChannel, lcs.getKey());
		// TODO Auto-generated method stub
		return lcc;
	}
}
