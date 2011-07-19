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

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

import com.l2jfree.loginserver.network.legacy.packets.L2GameServerPacket;
import com.l2jfree.loginserver.network.legacy.packets.L2LoginServerPacket;
import com.l2jfree.network.mmocore.MMOConnection;
import com.l2jfree.network.mmocore.MMOController;

/**
 * @author savormix
 * 
 */
public final class L2GameServer extends MMOConnection<L2GameServer, L2GameServerPacket, L2LoginServerPacket>
{
	protected L2GameServer(
			MMOController<L2GameServer, L2GameServerPacket, L2LoginServerPacket> mmoController,
			SocketChannel socketChannel) throws ClosedChannelException
	{
		super(mmoController, socketChannel);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onDisconnection()
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onForcedDisconnection()
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected boolean decipher(ByteBuffer buf, int size)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected boolean encipher(ByteBuffer buf, int size)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected L2LoginServerPacket getDefaultClosePacket()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected String getUID()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
