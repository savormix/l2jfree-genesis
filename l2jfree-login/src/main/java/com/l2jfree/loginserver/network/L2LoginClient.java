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
package com.l2jfree.loginserver.network;

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

import com.l2jfree.loginserver.network.packets.L2ClientPacket;
import com.l2jfree.loginserver.network.packets.L2ServerPacket;
import com.l2jfree.network.mmocore.MMOConnection;
import com.l2jfree.network.mmocore.MMOController;

/**
 * @author savormix
 *
 */
public final class L2LoginClient extends MMOConnection<L2LoginClient, L2ClientPacket, L2ServerPacket>
{
	protected L2LoginClient(
			MMOController<L2LoginClient, L2ClientPacket, L2ServerPacket> mmoController,
			SocketChannel socketChannel) throws ClosedChannelException
	{
		super(mmoController, socketChannel);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.l2jfree.network.mmocore.MMOConnection#onDisconnection()
	 */
	@Override
	protected void onDisconnection()
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.l2jfree.network.mmocore.MMOConnection#onForcedDisconnection()
	 */
	@Override
	protected void onForcedDisconnection()
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.l2jfree.network.mmocore.MMOConnection#decrypt(java.nio.ByteBuffer, int)
	 */
	@Override
	protected boolean decrypt(ByteBuffer buf, int size)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.l2jfree.network.mmocore.MMOConnection#encrypt(java.nio.ByteBuffer, int)
	 */
	@Override
	protected boolean encrypt(ByteBuffer buf, int size)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.l2jfree.network.mmocore.MMOConnection#getDefaultClosePacket()
	 */
	@Override
	protected L2ServerPacket getDefaultClosePacket()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.l2jfree.network.mmocore.MMOConnection#getUID()
	 */
	@Override
	protected String getUID()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
