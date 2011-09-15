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
import java.util.List;

import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.gameserver.network.client.packets.sendable.LeaveWorld.CloseClient;
import com.l2jfree.gameserver.network.client.packets.sendable.ServerClose;
import com.l2jfree.gameserver.network.client.packets.sendable.ServerCloseSocketPacket.ConnectionTerminated;
import com.l2jfree.gameserver.sql.PlayerDB;
import com.l2jfree.gameserver.util.PersistentId;
import com.l2jfree.lang.L2TextBuilder;
import com.l2jfree.network.mmocore.DataSizeHolder;
import com.l2jfree.network.mmocore.MMOConnection;
import com.l2jfree.security.CoreCipher;
import com.l2jfree.security.ObfuscationService;
import com.l2jfree.util.concurrent.RunnableStatsManager;

/**
 * @author savormix
 */
public final class L2Client extends MMOConnection<L2Client, L2ClientPacket, L2ServerPacket> implements IL2Client
{
	private final CoreCipher _cipher;
	private boolean _firstTime;
	
	private final ObfuscationService _deobfuscator;
	
	private volatile L2ClientState _state;
	private int _bitsInBlock;
	
	private String _accountName;
	private L2Player _activeChar;
	private PersistentId[] _playerSlotMap;
	private int _sessionId;
	
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
		
		// TODO Auto-generated constructor stub
		_cipher = new CoreCipher(L2ClientSecurity.getInstance().getKey());
		_firstTime = true;
		
		_deobfuscator = new ObfuscationService();
		
		_state = L2ClientState.CONNECTED;
		_bitsInBlock = 0;
	}
	
	@Override
	protected boolean decipher(ByteBuffer buf, DataSizeHolder size)
	{
		if (isFirstTime())
			return true;
		
		// at this point, cipher cannot be null
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
	protected String getUID()
	{
		return getAccountName();
	}
	
	@Override
	protected boolean isAuthed()
	{
		return getState() != L2ClientState.CONNECTED;
	}
	
	@Override
	public synchronized boolean sendPacket(L2ServerPacket sp)
	{
		if (sp == null)
			return false;
		
		final long begin = System.nanoTime();
		final L2Player activeChar = getActiveChar();
		
		try
		{
			if (_disconnected)
				return false;
			
			if (!sp.canBeSentTo(this, activeChar))
				return false;
			
			sp.prepareToSend(this, activeChar);
			
			if (!super.sendPacket(sp))
				return false;
			
			sp.packetSent(this, activeChar);
			return true;
		}
		finally
		{
			RunnableStatsManager.handleStats(sp.getClass(), "runImpl()", System.nanoTime() - begin);
		}
	}
	
	private volatile boolean _disconnected;
	
	public synchronized void close(boolean toLoginScreen)
	{
		close(toLoginScreen ? ServerClose.PACKET : CloseClient.PACKET);
	}
	
	@Override
	public synchronized void close(L2ServerPacket sp)
	{
		super.close(sp);
		
		_disconnected = true;
		
		new Disconnection(this).storeAndRemoveFromWorld();
	}
	
	@Override
	public synchronized void closeNow()
	{
		super.closeNow();
		
		_disconnected = true;
		
		new Disconnection(this).storeAndRemoveFromWorld();
	}
	
	@Override
	protected L2ServerPacket getDefaultClosePacket()
	{
		return ConnectionTerminated.PACKET;
	}
	
	@Override
	protected void onDisconnection()
	{
		// TODO
		//LoginServerThread.getInstance().sendLogout(getAccountName());
		L2ClientNetPing.getInstance().stopTask(this);
		
		_disconnected = true;
		
		new Disconnection(this).onDisconnection();
	}
	
	@Override
	protected void onForcedDisconnection()
	{
		if (_log.isDebugEnabled())
			_log.info("Client " + toString() + " disconnected abnormally.");
	}
	
	@Override
	public String toString()
	{
		final L2TextBuilder tb = new L2TextBuilder();
		tb.append("[State: ").append(getState());
		
		String host = getHostAddress();
		if (host != null)
			tb.append(" | IP: ").append(String.format("%-15s", host));
		
		String account = getAccountName();
		if (account != null)
			tb.append(" | Account: ").append(String.format("%-15s", account));
		
		L2Player player = getActiveChar();
		if (player != null)
			tb.append(" | Character: ").append(String.format("%-15s", player.getName()));
		
		tb.append("]");
		
		return tb.moveToString();
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
	
	public String getAccountName()
	{
		return _accountName;
	}
	
	public void setAccountName(String accountName)
	{
		_accountName = accountName;
	}
	
	public L2Player getActiveChar()
	{
		return _activeChar;
	}
	
	@Override
	public void setActiveChar(L2Player activeChar)
	{
		_activeChar = activeChar;
	}
	
	/**
	 * @param players
	 */
	public void definePlayerSlots(List<PlayerDB> players)
	{
		_playerSlotMap = new PersistentId[players.size()];
		int i = 0;
		for (PlayerDB p : players)
		{
			L2Player.disconnectIfOnline(p.getPersistentId());
			
			_playerSlotMap[i++] = p.getPersistentId();
		}
	}
	
	public L2Player loadCharacterBySlot(int slot)
	{
		if (slot >= 0 && slot < _playerSlotMap.length)
			return PlayerDB.load(_playerSlotMap[slot]);
		
		return null;
	}
	
	public int getSessionId()
	{
		return _sessionId;
	}
	
	/**
	 * @param sessionId
	 */
	public void setSessionId(int sessionId)
	{
		_sessionId = sessionId;
	}
}
