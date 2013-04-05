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

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

import com.l2jfree.gameserver.config.ReportedConfig;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.L2LegacyGameServerPacket;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.L2LegacyLoginServerPacket;
import com.l2jfree.network.mmocore.DataSizeHolder;
import com.l2jfree.network.mmocore.MMOConnection;
import com.l2jfree.security.NewCipher;
import com.l2jfree.util.HexUtil;

/**
 * @author hex1r0
 */
public final class L2LegacyLoginServer extends
		MMOConnection<L2LegacyLoginServer, L2LegacyLoginServerPacket, L2LegacyGameServerPacket>
{
	private static final String CIPHER_HEX_STRING = "5F 3B 76 2E 5D 30 35 2D 33 31 21 7C 2B 2D 25 78 54 21 5E 5B 24 00";
	
	private NewCipher _cipher = new NewCipher(HexUtil.hexStringToBytes(CIPHER_HEX_STRING));
	
	private L2LegacyLoginServerState _state;
	private byte[] _newBlowFishKey = null;
	private byte _newCipherRequested = 0;
	
	protected L2LegacyLoginServer(L2LegacyLoginServerController mmoController, SocketChannel socketChannel)
			throws ClosedChannelException
	{
		super(mmoController, socketChannel);
		
		_state = L2LegacyLoginServerState.CONNECTED;
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
		final int dataSize = size.getSize();
		
		size.decreaseSize(4); // checksum
		size.setPadding(1, 8);
		
		getCipher().decipher(buf, dataSize);
		
		if (!NewCipher.verifyChecksum(buf, dataSize))
		{
			_log.warn("Could not decipher received data: checksum mismatch. " + this);
			closeNow();
			return false;
		}
		
		return true;
	}
	
	@Override
	protected boolean encipher(ByteBuffer buf, int size)
	{
		if (_newBlowFishKey != null)
		{
			// workaround to skip BlowFishKey packet encryption with new key
			if (_newCipherRequested == 0)
			{
				_newCipherRequested = 1;
			}
			else if (_newCipherRequested != 2)
			{
				initCipher(_newBlowFishKey);
				_newCipherRequested = 2;
			}
		}
		
		final int offset = buf.position();
		
		size += 4; // checksum
		size += 8 - (size & 7); // padding
		
		NewCipher.appendChecksum(buf, size);
		
		getCipher().encipher(buf, size);
		
		buf.position(offset + size);
		
		return true;
	}
	
	public void setNewBlowfishKey(byte[] newBlowFishKey)
	{
		_newBlowFishKey = newBlowFishKey;
	}
	
	@Override
	protected L2LegacyGameServerPacket getDefaultClosePacket()
	{
		return null;
	}
	
	@Override
	protected String getUID()
	{
		return Integer.toString(ReportedConfig.ID);
	}
	
	@Override
	protected boolean isAuthed()
	{
		return getState() != L2LegacyLoginServerState.CONNECTED;
	}
	
	/**
	 * Initializes the cipher with the Blowfish key.
	 * 
	 * @param blowfishKey Blowfish Key
	 */
	public void initCipher(byte[] blowfishKey)
	{
		_cipher = new NewCipher(blowfishKey);
	}
	
	/**
	 * Returns the cipher.
	 * 
	 * @return cipher
	 */
	public NewCipher getCipher()
	{
		return _cipher;
	}
	
	/**
	 * Returns current connection's state.
	 * 
	 * @return connection's state
	 */
	@SuppressWarnings("unchecked")
	@Override
	public L2LegacyLoginServerState getState()
	{
		return _state;
	}
	
	/**
	 * Changes the connection's state.
	 * 
	 * @param state connection's state
	 */
	public void setState(L2LegacyLoginServerState state)
	{
		_state = state;
	}
}
