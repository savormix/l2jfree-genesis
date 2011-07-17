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

import com.l2jfree.loginserver.network.client.L2LoginClient;
import com.l2jfree.loginserver.network.client.packets.L2ServerPacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix
 *
 */
public final class ServerList extends L2ServerPacket
{
	/**
	 * Constructs a packet to inform about known game servers.
	 */
	public ServerList()
	{
		// TODO Auto-generated constructor stub
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
		buf.writeC(1); // how many servers
		buf.writeC(0); // last server ID
		
		buf.writeC(1); // server ID
		// advertised IP
		buf.writeC(127);
		buf.writeC(0);
		buf.writeC(0);
		buf.writeC(1);
		buf.writeD(7777); // port
		buf.writeC(21); // min age
		buf.writeC(true); // PvP
		buf.writeH(0); // players on
		buf.writeH(0); // max players
		buf.writeC(false); // online
		
		int bits = 0;
        //if (false) // ???
        //    bits |= 0x01;
        if (true) // clock
            bits |= 0x02;
        //if (false) // hide name?
        //	bits |= 0x03;
        //if (false) // test server
        //    bits |= 0x04;
        buf.writeD(bits);

        buf.writeC(true); // brackets
	}
}
