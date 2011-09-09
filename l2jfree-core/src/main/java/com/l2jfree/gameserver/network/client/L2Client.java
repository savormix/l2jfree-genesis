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
import com.l2jfree.security.ObfuscationService;

/**
 * @author savormix
 */
public final class L2Client extends MMOConnection<L2Client, L2ClientPacket, L2ServerPacket>
{
	private final CoreCipher _cipher = new CoreCipher(L2ClientSecurity.getInstance().getKey());
	private boolean _firstTime = true;
	
	private final ObfuscationService _deobfuscator = new ObfuscationService();
	
	private L2ClientState _state = L2ClientState.CONNECTED;
	private int _bitsInBlock = 0;
	
	/**
	 * Creates an internal object representing a game client connection.
	 * 
	 * @param mmoController connection manager
	 * @param socketChannel connection
	 * @throws ClosedChannelException if the given channel was closed during operations
	 */
	protected L2Client(L2ClientConnections mmoController, SocketChannel socketChannel) throws ClosedChannelException
	{
		super(mmoController, socketChannel);
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
		
		getDeobfuscator().decodeOpcodes(buf, size.getSize());
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
	 * Returns the packet opcode deobfuscator.
	 * 
	 * @return opcode deobfuscator
	 */
	public ObfuscationService getDeobfuscator()
	{
		return _deobfuscator;
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
	
	/**
	 * Returns how many bits to demand client to process in regular intervals.
	 * 
	 * @return bits in block
	 */
	public int getBitsInBlock()
	{
		return _bitsInBlock;
	}
	
	/**
	 * Changes how many bits to demand client to process in regular intervals.
	 * 
	 * @param bitsInBlock bit count (should be <TT>bitsInBlock & 7 == 0</TT>)
	 */
	public void setBitsInBlock(int bitsInBlock)
	{
		_bitsInBlock = bitsInBlock;
	}
}
