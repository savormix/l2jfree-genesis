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
package com.l2jfree.gameserver.network.client.packets.sendable;

import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * Sent if a player idles for too long in the character selection screen.
 * 
 * @author savormix
 */
public abstract class ServerCloseSocketPacket extends StaticPacket
{
	/**
	 * A nicer name for {@link ServerCloseSocketPacket}.
	 * 
	 * @author savormix (generated)
	 * @see ServerCloseSocketPacket
	 */
	public static final class ConnectionTerminated extends ServerCloseSocketPacket
	{
		/** This packet. */
		public static final ConnectionTerminated PACKET = new ConnectionTerminated();
		
		/**
		 * Constructs this packet.
		 * 
		 * @see ServerCloseSocketPacket#ServerCloseSocketPacket()
		 */
		private ConnectionTerminated()
		{
		}
	}
	
	/** Constructs this packet. */
	public ServerCloseSocketPacket()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0xb0;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		buf.writeD(0); // 0 by default
	}
}
