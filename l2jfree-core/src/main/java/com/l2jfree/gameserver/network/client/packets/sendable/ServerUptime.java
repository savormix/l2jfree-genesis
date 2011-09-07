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

import java.lang.management.ManagementFactory;

import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * Server sends this packet to all characters inside game world at 30 second intervals.<BR>
 * <BR>
 * This packet is not sent while at character selection list??
 * 
 * @author savormix
 */
//FIXME in which chronicle was this introduced?
public final class ServerUptime extends L2ServerPacket
{
	/** An instance of this packet. */
	public static final ServerUptime PACKET = new ServerUptime();
	
	private ServerUptime()
	{
		// singleton
	}
	
	@Override
	protected int getOpcode()
	{
		return 0xd9;
	}
	
	@Override
	protected void writeImpl(L2Client client, MMOBuffer buf) throws RuntimeException
	{
		buf.writeD(ManagementFactory.getRuntimeMXBean().getUptime());
	}
}
