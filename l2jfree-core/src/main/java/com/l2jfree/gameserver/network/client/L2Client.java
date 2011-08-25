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

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.network.mmocore.DataSizeHolder;
import com.l2jfree.network.mmocore.MMOConnection;
import com.l2jfree.security.CoreCipher;

/**
 * @author savormix
 */
public final class L2Client extends MMOConnection<L2Client, L2ClientPacket, L2ServerPacket>
{
	private final CoreCipher _cipher;
	private boolean _firstTime;
	
	private L2ClientState _state;
	
	protected L2Client(SocketChannel socketChannel, byte[] cipherKey) throws ClosedChannelException
	{
		super(L2ClientController.getInstance(), socketChannel);
		// TODO Auto-generated constructor stub
		_cipher = new CoreCipher(cipherKey);
		_firstTime = true;
		
		_state = L2ClientState.CONNECTED;
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
	protected boolean decipher(ByteBuffer buf, DataSizeHolder size)
	{
		if (isFirstTime())
			return true;
		
		getCipher().decipher(buf, size.getSize());
		return true;
	}
	
	@Override
	protected boolean encipher(ByteBuffer buf, int size)
	{
		if (isFirstTime())
			setFirstTime(false);
		else
			getCipher().encipher(buf, size);
		
		buf.position(buf.position() + size);
		return true;
	}
	
	@Override
	protected L2ServerPacket getDefaultClosePacket()
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
	
	@Override
	protected boolean isAuthed()
	{
		return getState() != L2ClientState.CONNECTED;
	}
	
	/**
	 * Returns the complete cipher's key.
	 * 
	 * @return cipher's key
	 */
	public ByteBuffer getCipherKey()
	{
		return getCipher().getKey();
	}
	
	private CoreCipher getCipher()
	{
		return _cipher;
	}
	
	private boolean isFirstTime()
	{
		return _firstTime;
	}
	
	private void setFirstTime(boolean firstTime)
	{
		_firstTime = firstTime;
	}
	
	/**
	 * Returns current connection's state.
	 * 
	 * @return connection's state
	 */
	@SuppressWarnings("unchecked")
	@Override
	public L2ClientState getState()
	{
		return _state;
	}
	
	/**
	 * Changes the connection's state.
	 * 
	 * @param state connection's state
	 */
	public void setState(L2ClientState state)
	{
		_state = state;
	}
}
