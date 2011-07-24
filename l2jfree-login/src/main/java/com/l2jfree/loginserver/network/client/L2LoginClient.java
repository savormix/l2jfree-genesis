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
package com.l2jfree.loginserver.network.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.security.PrivateKey;

import com.l2jfree.loginserver.network.client.L2ClientSecurity.SessionKey;
import com.l2jfree.loginserver.network.client.packets.L2ClientPacket;
import com.l2jfree.loginserver.network.client.packets.L2ServerPacket;
import com.l2jfree.loginserver.network.client.packets.sendable.LoginFailure;
import com.l2jfree.loginserver.network.client.packets.sendable.PlayFailure;
import com.l2jfree.network.mmocore.DataSizeHolder;
import com.l2jfree.network.mmocore.MMOConnection;
import com.l2jfree.network.mmocore.MMOController;
import com.l2jfree.security.ScrambledKeyPair;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author savormix
 *
 */
public final class L2LoginClient extends MMOConnection<L2LoginClient, L2ClientPacket, L2ServerPacket>
{
	private static final L2Logger _log = L2Logger.getLogger(L2LoginClient.class);
	
	private final int _sessionId;
	private final int _protocol;
	private final ScrambledKeyPair _keyPair;
	private final L2ClientCipher _cipher;
	
	private L2LoginClientState _state;
	private SessionKey _sessionKey;
	
	private L2Account _account;
	
	protected L2LoginClient(
			MMOController<L2LoginClient, L2ClientPacket, L2ServerPacket> mmoController,
			SocketChannel socketChannel, int sessionId, int protocol,
			ScrambledKeyPair keyPair, byte[] blowfishKey) throws ClosedChannelException
	{
		super(mmoController, socketChannel);
		_sessionId = sessionId;
		_protocol = protocol;
		_keyPair = keyPair;
		_cipher = new L2ClientCipher(blowfishKey);
		_state = L2LoginClientState.CONNECTED;
		_sessionKey = null;
		_account = null;
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
		
		return success;
	}
	
	/* (non-Javadoc)
	 * @see com.l2jfree.network.mmocore.MMOConnection#encrypt(java.nio.ByteBuffer, int)
	 */
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
	
	/* (non-Javadoc)
	 * @see com.l2jfree.network.mmocore.MMOConnection#getDefaultClosePacket()
	 */
	@Override
	protected L2ServerPacket getDefaultClosePacket()
	{
		if (getState() == L2LoginClientState.VIEWING_LIST)
			return new PlayFailure(L2NoServiceReason.IGNORE);
		else
			return new LoginFailure(L2NoServiceReason.IGNORE);
	}
	
	/* (non-Javadoc)
	 * @see com.l2jfree.network.mmocore.MMOConnection#getUID()
	 */
	@Override
	protected String getUID()
	{
		L2Account acc = getAccount();
		if (acc != null)
			return acc.getAccount();
		else
			return null;
	}
	
	/**
	 * Returns the unique session ID.
	 * @return session ID
	 */
	public int getSessionId()
	{
		return _sessionId;
	}
	
	/**
	 * Returns the network protocol version.
	 * @return protocol version
	 */
	public int getProtocol()
	{
		return _protocol;
	}
	
	/**
	 * Returns the public key.
	 * @return public key
	 */
	public byte[] getPublicKey()
	{
		return getKeyPair().getScrambledModulus();
	}
	
	/**
	 * Returns the private key.
	 * @return private key
	 */
	public PrivateKey getPrivateKey()
	{
		return getKeyPair().getPair().getPrivate();
	}
	
	/**
	 * Returns the scrambled RSA key pair.
	 * @return key pair
	 */
	private ScrambledKeyPair getKeyPair()
	{
		return _keyPair;
	}
	
	private L2ClientCipher getCipher()
	{
		return _cipher;
	}
	
	/**
	 * Returns the Blowfish key.
	 * @return Blowfish key
	 */
	public byte[] getBlowfishKey()
	{
		return getCipher().getBlowfishCipher().getBlowfishKey();
	}
	
	/**
	 * Returns current connection's state.
	 * @return connection's state
	 */
	public L2LoginClientState getState()
	{
		return _state;
	}
	
	/**
	 * Changes the connection's state.
	 * @param state connection's state
	 */
	public void setState(L2LoginClientState state)
	{
		_state = state;
	}
	
	/**
	 * Returns the assigned session key.
	 * @return session key
	 */
	public SessionKey getSessionKey()
	{
		return _sessionKey;
	}
	
	/**
	 * Assigns a session key.
	 * @param sessionKey session key
	 */
	public void setSessionKey(SessionKey sessionKey)
	{
		_sessionKey = sessionKey;
	}
	
	/**
	 * Returns the associated account.
	 * @return account
	 */
	public L2Account getAccount()
	{
		return _account;
	}
	
	/**
	 * Associates an account with this connection.
	 * @param account account
	 */
	public void setAccount(L2Account account)
	{
		_account = account;
	}
}
