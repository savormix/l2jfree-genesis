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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.l2jfree.loginserver.network.legacy.packets.L2GameServerPacket;
import com.l2jfree.loginserver.network.legacy.packets.L2LoginServerPacket;
import com.l2jfree.loginserver.network.legacy.packets.sendable.LoginServerFail;
import com.l2jfree.network.mmocore.DataSizeHolder;
import com.l2jfree.network.mmocore.MMOConnection;
import com.l2jfree.network.mmocore.MMOController;

/**
 * @author savormix
 * 
 */
public final class L2GameServer extends MMOConnection<L2GameServer, L2GameServerPacket, L2LoginServerPacket>
{
	private final KeyPair _keyPair;
	private final L2LegacyCipher _cipher;
	
	private L2LegacyState _state;
	private Integer _id;
	
	protected L2GameServer(
			MMOController<L2GameServer, L2GameServerPacket, L2LoginServerPacket> mmoController,
			SocketChannel socketChannel, KeyPair keyPair) throws ClosedChannelException
	{
		super(mmoController, socketChannel);
		_keyPair = keyPair;
		_cipher = new L2LegacyCipher();
		
		_state = L2LegacyState.CONNECTED;
		_id = null;
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
		boolean success = false;
		try
		{
			success = getCipher().decipher(buf.array(), buf.position(), size);
		}
		catch (IOException e)
		{
			_log.error("Failed to decipher received data!", e);
			closeNow();
			return false;
		}
		
		if (!success)
		{
			_log.warn("Could not decipher received data: checksum mismatch. " + this);
			closeNow();
		}
		
		buf.limit(buf.limit() - 4);
		
		return success;
	}
	
	@Override
	protected boolean encipher(ByteBuffer buf, int size)
	{
		final int offset = buf.position();
		try
		{
			size = getCipher().encipher(buf.array(), offset, size);
		}
		catch (IOException e)
		{
			_log.error("Failed to encipher sent data!", e);
			return false;
		}
		
		buf.position(offset + size);
		return true;
	}
	
	@Override
	protected L2LoginServerPacket getDefaultClosePacket()
	{
		return new LoginServerFail(L2NoServiceReason.WRONG_HEXID);
	}
	
	@Override
	protected String getUID()
	{
		Integer id = getId();
		if (id == null)
			return null;
		else
			return String.valueOf(id);
	}
	
	/**
	 * Returns the public key.
	 * @return public key
	 */
	public PublicKey getPublicKey()
	{
		return getKeyPair().getPublic();
	}
	
	/**
	 * Returns the private key.
	 * @return private key
	 */
	public PrivateKey getPrivateKey()
	{
		return getKeyPair().getPrivate();
	}
	
	/**
	 * Returns the cipher.
	 * @return cipher
	 */
	public L2LegacyCipher getCipher()
	{
		return _cipher;
	}
	
	/**
	 * Returns current connection's state.
	 * @return connection's state
	 */
	public L2LegacyState getState()
	{
		return _state;
	}
	
	/**
	 * Changes the connection's state.
	 * @param state connection's state
	 */
	public void setState(L2LegacyState state)
	{
		_state = state;
	}
	
	/**
	 * Returns the assigned ID.
	 * @return game server ID
	 */
	public Integer getId()
	{
		return _id;
	}
	
	/**
	 * Assigns a game server ID.
	 * @param id game server ID
	 */
	public void setId(int id)
	{
		_id = id;
	}
	
	/**
	 * Returns the scrambled RSA key pair.
	 * @return key pair
	 */
	private KeyPair getKeyPair()
	{
		return _keyPair;
	}
}
