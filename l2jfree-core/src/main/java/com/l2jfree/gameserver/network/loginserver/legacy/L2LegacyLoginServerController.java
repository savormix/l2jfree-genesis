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

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

import com.l2jfree.gameserver.network.loginserver.legacy.packets.L2LegacyGameServerPacket;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.L2LegacyLoginServerPacket;
import com.l2jfree.network.mmocore.MMOConfig;
import com.l2jfree.network.mmocore.MMOController;

/**
 * @author hex1r0
 */
public class L2LegacyLoginServerController extends
		MMOController<L2LegacyLoginServer, L2LegacyLoginServerPacket, L2LegacyGameServerPacket>
{
	
	private static final class SingletonHolder
	{
		static
		{
			final MMOConfig cfg = new MMOConfig(L2LegacyLoginServerController.class.getSimpleName());
			cfg.setSelectorSleepTime(5);
			cfg.setThreadCount(1);
			
			try
			{
				INSTANCE = new L2LegacyLoginServerController(cfg, L2LegacyLoginServerPacketHandler.getInstance());
			}
			catch (IOException e)
			{
				throw new Error(e);
			}
		}
		
		public static final L2LegacyLoginServerController INSTANCE;
	}
	
	/**
	 * Returns a singleton object.
	 * 
	 * @return an instance of this class
	 */
	public static L2LegacyLoginServerController getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	/**
	 * @param config
	 * @param packetHandler
	 * @throws IOException
	 */
	protected L2LegacyLoginServerController(MMOConfig config, L2LegacyLoginServerPacketHandler packetHandler)
			throws IOException
	{
		super(config, packetHandler);
	}
	
	@Override
	protected L2LegacyLoginServer createClient(SocketChannel socketChannel) throws ClosedChannelException
	{
		L2LegacyLoginServer lls = new L2LegacyLoginServer(this, socketChannel);
		
		return lls;
	}
}
