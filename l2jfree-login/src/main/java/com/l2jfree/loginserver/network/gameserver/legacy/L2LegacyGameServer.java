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
package com.l2jfree.loginserver.network.gameserver.legacy;

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javolution.util.FastSet;

import com.l2jfree.loginserver.network.gameserver.legacy.packets.L2LegacyGameServerPacket;
import com.l2jfree.loginserver.network.gameserver.legacy.packets.L2LegacyLoginServerPacket;
import com.l2jfree.loginserver.network.gameserver.legacy.packets.sendable.LoginServerFail;
import com.l2jfree.loginserver.network.gameserver.legacy.status.L2LegacyStatus;
import com.l2jfree.network.mmocore.DataSizeHolder;
import com.l2jfree.network.mmocore.MMOConnection;
import com.l2jfree.security.NewCipher;
import com.l2jfree.util.HexUtil;

/**
 * @author savormix
 */
public final class L2LegacyGameServer extends
		MMOConnection<L2LegacyGameServer, L2LegacyGameServerPacket, L2LegacyLoginServerPacket>
{
	private final KeyPair _keyPair;
	
	private NewCipher _cipher;
	
	private L2LegacyGameServerView _view;
	private L2LegacyGameServerState _state;
	private Integer _id;
	private String _auth;
	private boolean _allowedToBan;
	
	// GameServerAuth
	private String _host;
	private int _port;
	// Can be modified via ServerStatus
	private int _maxPlayers;
	
	// ServerStatus
	private L2LegacyStatus _status;
	private int _types;
	private boolean _brackets;
	private int _age;
	
	// PlayersInGame, PlayerAuthRequest, PlayerLogout
	private final FastSet<String> _onlineAccounts;
	
	// unmanaged
	private final boolean _pvp;
	
	/**
	 * Creates an internal object representing a game server connection.
	 * 
	 * @param mmoController connection manager
	 * @param socketChannel connection
	 * @throws ClosedChannelException if the given channel was closed during operations
	 */
	protected L2LegacyGameServer(L2LegacyGameServerController mmoController, SocketChannel socketChannel)
			throws ClosedChannelException
	{
		super(mmoController, socketChannel);
		
		_keyPair = L2LegacyGameServerSecurity.getInstance().getKeyPair();
		_cipher = new NewCipher(
				HexUtil.HexStringToBytes("5F 3B 76 2E 5D 30 35 2D 33 31 21 7C 2B 2D 25 78 54 21 5E 5B 24 00"));
		
		_state = L2LegacyGameServerState.CONNECTED;
		_id = null;
		_allowedToBan = false;
		
		_host = "0.0.0.0";
		
		_status = L2LegacyStatus.DOWN;
		_onlineAccounts = FastSet.newInstance();
		_pvp = true;
	}
	
	@Override
	protected void onDisconnection()
	{
		L2LegacyGameServerController.getInstance().remGameServer(this);
	}
	
	@Override
	protected void onForcedDisconnection()
	{
		L2LegacyGameServerController.getInstance().remGameServer(this);
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
		final int offset = buf.position();
		
		size += 4; // checksum
		size += 8 - (size & 7); // padding
		
		NewCipher.appendChecksum(buf, size);
		
		getCipher().encipher(buf, size);
		
		buf.position(offset + size);
		return true;
	}
	
	@Override
	protected L2LegacyLoginServerPacket getDefaultClosePacket()
	{
		return new LoginServerFail(L2NoServiceReason.NO_FREE_ID);
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
	
	@Override
	protected boolean isAuthed()
	{
		return getState() != L2LegacyGameServerState.CONNECTED;
	}
	
	/**
	 * Returns the public key.
	 * 
	 * @return public key
	 */
	public PublicKey getPublicKey()
	{
		return getKeyPair().getPublic();
	}
	
	/**
	 * Returns the private key.
	 * 
	 * @return private key
	 */
	public PrivateKey getPrivateKey()
	{
		return getKeyPair().getPrivate();
	}
	
	/**
	 * Returns the scrambled RSA key pair.
	 * 
	 * @return key pair
	 */
	private KeyPair getKeyPair()
	{
		return _keyPair;
	}
	
	/**
	 * Initializes the cipher with the Blowfish key received from the game server.
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
	 * Returns a view over this game server.
	 * 
	 * @return view
	 */
	public L2LegacyGameServerView getView()
	{
		if (_view == null)
			_view = new L2LegacyGameServerView(this);
		return _view;
	}
	
	/**
	 * Returns current connection's state.
	 * 
	 * @return connection's state
	 */
	@SuppressWarnings("unchecked")
	@Override
	public L2LegacyGameServerState getState()
	{
		return _state;
	}
	
	/**
	 * Changes the connection's state.
	 * 
	 * @param state connection's state
	 */
	public void setState(L2LegacyGameServerState state)
	{
		_state = state;
	}
	
	/**
	 * Returns the assigned ID.
	 * 
	 * @return game server ID
	 */
	public Integer getId()
	{
		return _id;
	}
	
	/**
	 * Assigns a game server ID.
	 * 
	 * @param id game server ID
	 */
	public void setId(int id)
	{
		_id = id;
	}
	
	/**
	 * Returns the game server's authorization string. <BR>
	 * <BR>
	 * If login server [temporarily] assigned a free ID to this game server, then <TT>null</TT> is
	 * returned instead.
	 * 
	 * @return authorization data
	 */
	public String getAuth()
	{
		return _auth;
	}
	
	/**
	 * Specifies the authorization string used by this game server.
	 * 
	 * @param auth authorization data
	 */
	public void setAuth(String auth)
	{
		_auth = auth;
	}
	
	/**
	 * Returns whether account ban requests from this game server should be served.
	 * 
	 * @return can this game server ban accounts
	 */
	public boolean isAllowedToBan()
	{
		return _allowedToBan;
	}
	
	/**
	 * Specifies whether account ban requests from this game server should be served.
	 * 
	 * @param allowedToBan can this game server ban accounts
	 */
	public void setAllowedToBan(boolean allowedToBan)
	{
		_allowedToBan = allowedToBan;
	}
	
	/**
	 * Returns the advertised listening IP.
	 * 
	 * @return the listening IP
	 */
	public String getHost()
	{
		return _host;
	}
	
	/**
	 * Sets the listening IP.
	 * 
	 * @param host game server's listening IP
	 */
	public void setHost(String host)
	{
		if (host != null)
			_host = host;
	}
	
	/**
	 * Returns the advertised listening port.
	 * 
	 * @return game server's listening port
	 */
	public int getPort()
	{
		return _port;
	}
	
	/**
	 * Sets the listening port.
	 * 
	 * @param port game server's listening port
	 */
	public void setPort(int port)
	{
		_port = port;
	}
	
	/**
	 * Returns how many players are allowed to be connected at any given time.
	 * 
	 * @return maximum players online
	 */
	public int getMaxPlayers()
	{
		return _maxPlayers;
	}
	
	/**
	 * Sets the maximum number of players online.
	 * 
	 * @param maxPlayers maximum players online
	 */
	public void setMaxPlayers(int maxPlayers)
	{
		_maxPlayers = maxPlayers;
	}
	
	/**
	 * Returns game server status to be displayed in the server list.
	 * 
	 * @return server status
	 */
	public L2LegacyStatus getStatus()
	{
		return _status;
	}
	
	/**
	 * Change game server status.
	 * 
	 * @param status server status
	 */
	public void setStatus(L2LegacyStatus status)
	{
		if (status != null)
			_status = status;
	}
	
	/**
	 * Returns game server types to be displayed in the server list.
	 * 
	 * @return server types
	 */
	public int getTypes()
	{
		return _types;
	}
	
	/**
	 * Specify game server types to be displayed in the server list.
	 * 
	 * @param types server types
	 */
	public void setTypes(int types)
	{
		_types = types;
	}
	
	/**
	 * Returns whether square brackets should be displayed before the game server's name in the
	 * server list.
	 * 
	 * @return whether to show square brackets
	 */
	public boolean isBrackets()
	{
		return _brackets;
	}
	
	/**
	 * Specify whether square brackets should be displayed before the game server's name in the
	 * server list.
	 * 
	 * @param brackets whether to show square brackets
	 */
	public void setBrackets(boolean brackets)
	{
		_brackets = brackets;
	}
	
	/**
	 * Returns age required to play on this game server.
	 * 
	 * @return minimal player age
	 */
	public int getAge()
	{
		return _age;
	}
	
	/**
	 * Changes age required to play on this game server.
	 * 
	 * @param age minimal player age
	 */
	public void setAge(int age)
	{
		_age = age;
	}
	
	/**
	 * Returns names of accounts logged into this game server.
	 * 
	 * @return online account names
	 */
	public FastSet<String> getOnlineAccounts()
	{
		return _onlineAccounts;
	}
	
	/**
	 * All legacy game servers allow PvP.
	 * 
	 * @return <TT>true</TT>
	 */
	public boolean isPvp()
	{
		return _pvp;
	}
}
