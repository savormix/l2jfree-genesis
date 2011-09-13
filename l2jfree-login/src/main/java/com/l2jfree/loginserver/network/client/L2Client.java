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
import com.l2jfree.security.NewCipher;
import com.l2jfree.security.ScrambledKeyPair;
import com.l2jfree.util.HexUtil;
import com.l2jfree.util.Rnd;

/**
 * @author savormix
 */
public final class L2Client extends MMOConnection<L2Client, L2ClientPacket, L2ServerPacket>
{
	private final int _sessionId;
	private final int _protocol;
	private final ScrambledKeyPair _keyPair;
	private final NewCipher _cipher;
	private boolean _firstTime;
	
	private volatile L2ClientState _state;
	private SessionKey _sessionKey;
	
	private L2Account _account;
	
	/**
	 * Creates an internal object representing a login client connection.
	 * 
	 * @param mmoController connection manager
	 * @param socketChannel connection
	 * @param protocol network protocol version
	 * @throws ClosedChannelException if the given channel was closed during operations
	 */
	protected L2Client(L2ClientController mmoController, SocketChannel socketChannel, int protocol)
			throws ClosedChannelException
	{
		super(mmoController, socketChannel);
		
		_sessionId = L2ClientSecurity.getInstance().getNextSessionId();
		_protocol = protocol;
		_keyPair = L2ClientSecurity.getInstance().getKeyPair();
		_cipher = new NewCipher(L2ClientSecurity.getInstance().getBlowfishKey());
		_firstTime = true;
		_state = L2ClientState.CONNECTED;
		_sessionKey = null;
		_account = null;
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
		final boolean first = isFirstTime();
		final int offset = buf.position();
		
		size += 4; // checksum
		if (first)
			size += 4; // XOR key
		size += 8 - (size & 7); // padding
		
		if (first)
		{
			int stop = size - 8; // reserved for checksum and key
			int pos = offset;
			int key = Rnd.nextInt();
			
			while ((pos += 4) < stop)
			{
				int i = buf.getInt(pos);
				key += i;
				buf.putInt(pos, i ^ key);
			}
			buf.putInt(pos, key);
		}
		else
			NewCipher.appendChecksum(buf, size);
		
		final NewCipher cipher;
		if (first)
			cipher = new NewCipher(HexUtil.HexStringToBytes("6b 60 cb 5b 82 ce 90 b1 cc 2b 6c 55 6c 6c 6c 6c"));
		else
			cipher = getCipher();
		cipher.encipher(buf, size);
		
		buf.position(offset + size);
		return true;
	}
	
	@Override
	protected L2ServerPacket getDefaultClosePacket()
	{
		if (getState() == L2ClientState.VIEWING_LIST)
			return new PlayFailure(L2NoServiceReason.IGNORE);
		else
			return new LoginFailure(L2NoServiceReason.IGNORE);
	}
	
	@Override
	protected String getUID()
	{
		L2Account acc = getAccount();
		if (acc != null)
			return acc.getAccount();
		else
			return null;
	}
	
	@Override
	protected boolean isAuthed()
	{
		return getState() != L2ClientState.CONNECTED;
	}
	
	/**
	 * Returns the unique session ID.
	 * 
	 * @return session ID
	 */
	public int getSessionId()
	{
		return _sessionId;
	}
	
	/**
	 * Returns the network protocol version.
	 * 
	 * @return protocol version
	 */
	public int getProtocol()
	{
		return _protocol;
	}
	
	/**
	 * Returns the public key.
	 * 
	 * @return public key
	 */
	public byte[] getPublicKey()
	{
		return getKeyPair().getScrambledModulus();
	}
	
	/**
	 * Returns the private key.
	 * 
	 * @return private key
	 */
	public PrivateKey getPrivateKey()
	{
		return getKeyPair().getPair().getPrivate();
	}
	
	/**
	 * Returns the scrambled RSA key pair.
	 * 
	 * @return key pair
	 */
	private ScrambledKeyPair getKeyPair()
	{
		return _keyPair;
	}
	
	private NewCipher getCipher()
	{
		return _cipher;
	}
	
	private boolean isFirstTime()
	{
		boolean ft = _firstTime;
		_firstTime = false;
		return ft;
	}
	
	/**
	 * Returns the Blowfish key.
	 * 
	 * @return Blowfish key
	 */
	public byte[] getBlowfishKey()
	{
		return getCipher().getBlowfishKey();
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
	 * Returns the assigned session key.
	 * 
	 * @return session key
	 */
	public SessionKey getSessionKey()
	{
		return _sessionKey;
	}
	
	/**
	 * Assigns a session key.
	 * 
	 * @param sessionKey session key
	 */
	public void setSessionKey(SessionKey sessionKey)
	{
		_sessionKey = sessionKey;
	}
	
	/**
	 * Returns the associated account.
	 * 
	 * @return account
	 */
	public L2Account getAccount()
	{
		return _account;
	}
	
	/**
	 * Associates an account with this connection.
	 * 
	 * @param account account
	 */
	public void setAccount(L2Account account)
	{
		_account = account;
	}
}
