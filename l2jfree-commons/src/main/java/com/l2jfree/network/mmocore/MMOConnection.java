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
package com.l2jfree.network.mmocore;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

import javolution.util.FastList;

import com.l2jfree.util.concurrent.FIFORunnableQueue;
import com.l2jfree.util.logging.L2Logger;

/**
 * @param <T> connection
 * @param <RP> receivable packet
 * @param <SP> sendable packet
 * @author KenM
 */
public abstract class MMOConnection<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>>
{
	protected static final L2Logger _log = L2Logger.getLogger(MMOConnection.class);
	
	private final MMOController<T, RP, SP> _mmoController;
	private final ReadWriteThread<T, RP, SP> _readWriteThread;
	private final Socket _socket;
	private InetAddress _inetAddress;
	private String _hostAddress;
	
	private FastList<SP> _sendQueue;
	private final SelectionKey _selectionKey;
	
	private ByteBuffer _readBuffer;
	
	private ByteBuffer _primaryWriteBuffer;
	private ByteBuffer _secondaryWriteBuffer;
	
	private long _closeTimeout = -1;
	
	protected MMOConnection(MMOController<T, RP, SP> mmoController, SocketChannel socketChannel)
			throws ClosedChannelException
	{
		_mmoController = mmoController;
		_readWriteThread = getMMOController().getRandomReadWriteThread();
		_socket = socketChannel.socket();
		_inetAddress = _socket.getInetAddress();
		_hostAddress = _inetAddress.getHostAddress();
		_selectionKey = socketChannel.register(getReadWriteThread().getSelector(), SelectionKey.OP_READ);
		_selectionKey.attach(this);
	}
	
	/**
	 * Sends a packet to the client, by adding it to the queue, and enabling write interest.
	 * 
	 * @param sp the packet to be sent
	 */
	public synchronized void sendPacket(SP sp)
	{
		if (isClosed())
			return;
		
		try
		{
			getSelectionKey().interestOps(getSelectionKey().interestOps() | SelectionKey.OP_WRITE);
			getSendQueue2().addLast(sp);
		}
		catch (CancelledKeyException e)
		{
			// ignore
		}
	}
	
	final MMOController<T, RP, SP> getMMOController()
	{
		return _mmoController;
	}
	
	private ReadWriteThread<T, RP, SP> getReadWriteThread()
	{
		return _readWriteThread;
	}
	
	final SelectionKey getSelectionKey()
	{
		return _selectionKey;
	}
	
	final void disableReadInterest()
	{
		try
		{
			getSelectionKey().interestOps(getSelectionKey().interestOps() & ~SelectionKey.OP_READ);
		}
		catch (CancelledKeyException e)
		{
			// ignore
		}
	}
	
	final void disableWriteInterest()
	{
		try
		{
			getSelectionKey().interestOps(getSelectionKey().interestOps() & ~SelectionKey.OP_WRITE);
		}
		catch (CancelledKeyException e)
		{
			// ignore
		}
	}
	
	final Socket getSocket()
	{
		return _socket;
	}
	
	public final InetAddress getInetAddress()
	{
		return _inetAddress;
	}
	
	public final String getHostAddress()
	{
		return _hostAddress;
	}
	
	public final void setHostAddress(String hostAddress)
	{
		try
		{
			_inetAddress = InetAddress.getByName(hostAddress);
			_hostAddress = _inetAddress.getHostAddress();
		}
		catch (UnknownHostException e)
		{
			MMOController._log.warn("", e);
		}
	}
	
	final WritableByteChannel getWritableChannel()
	{
		return _socket.getChannel();
	}
	
	final ReadableByteChannel getReadableByteChannel()
	{
		return _socket.getChannel();
	}
	
	final FastList<SP> getSendQueue2()
	{
		if (_sendQueue == null)
			_sendQueue = new FastList<SP>();
		
		return _sendQueue;
	}
	
	final void createWriteBuffer(ByteBuffer buf)
	{
		if (_primaryWriteBuffer == null)
		{
			// APPENDING FOR NULL
			
			_primaryWriteBuffer = getReadWriteThread().getPooledBuffer();
			_primaryWriteBuffer.put(buf);
		}
		else
		{
			// PREPENDING ON EXISTING
			
			ByteBuffer temp = getReadWriteThread().getPooledBuffer();
			temp.put(buf);
			
			int remaining = temp.remaining();
			_primaryWriteBuffer.flip();
			int limit = _primaryWriteBuffer.limit();
			
			if (remaining >= _primaryWriteBuffer.remaining())
			{
				temp.put(_primaryWriteBuffer);
				getReadWriteThread().recycleBuffer(_primaryWriteBuffer);
				_primaryWriteBuffer = temp;
			}
			else
			{
				_primaryWriteBuffer.limit(remaining);
				temp.put(_primaryWriteBuffer);
				_primaryWriteBuffer.limit(limit);
				_primaryWriteBuffer.compact();
				_secondaryWriteBuffer = _primaryWriteBuffer;
				_primaryWriteBuffer = temp;
			}
		}
	}
	
	final boolean hasPendingWriteBuffer()
	{
		return _primaryWriteBuffer != null;
	}
	
	final void movePendingWriteBufferTo(ByteBuffer dest)
	{
		_primaryWriteBuffer.flip();
		dest.put(_primaryWriteBuffer);
		getReadWriteThread().recycleBuffer(_primaryWriteBuffer);
		_primaryWriteBuffer = _secondaryWriteBuffer;
		_secondaryWriteBuffer = null;
	}
	
	final void setReadBuffer(ByteBuffer buf)
	{
		_readBuffer = buf;
	}
	
	final ByteBuffer getReadBuffer()
	{
		return _readBuffer;
	}
	
	final boolean isClosed()
	{
		return _closeTimeout != -1;
	}
	
	final boolean closeTimeouted()
	{
		return System.currentTimeMillis() > _closeTimeout;
	}
	
	/**
	 * Clears the packet queue, and closes the client with the default close packet.<br>
	 * <br>
	 * NOTE: It allows a short timeout (100 msec) only, to close the client as soon as possible.
	 */
	@SuppressWarnings("unchecked")
	public synchronized void closeNow()
	{
		if (isClosed())
			return;
		
		getSendQueue2().clear();
		sendPacket(getDefaultClosePacket());
		
		// close the client as soon as possible
		// for a normal connection 100 msec should be enough to write the pending packets
		_closeTimeout = System.currentTimeMillis() + 100;
		
		disableReadInterest();
		getReadWriteThread().closeConnection((T)this);
	}
	
	/**
	 * Clears the packet queue, and closes the client with the given packet.<br>
	 * <br>
	 * NOTE: It allows a longer timeout (10 sec), to let the client have the chance to get all of the packets.
	 */
	@SuppressWarnings("unchecked")
	public synchronized void close(SP sp)
	{
		if (isClosed())
			return;
		
		getSendQueue2().clear();
		sendPacket(sp);
		
		// let the client have the chance to get all of the packets
		// even for a connection with issues 10'000 msec should be enough to write the pending packets
		_closeTimeout = System.currentTimeMillis() + 10000;
		
		disableReadInterest();
		getReadWriteThread().closeConnection((T)this);
	}
	
	final void releaseBuffers()
	{
		if (_primaryWriteBuffer != null)
		{
			getReadWriteThread().recycleBuffer(_primaryWriteBuffer);
			_primaryWriteBuffer = null;
			if (_secondaryWriteBuffer != null)
			{
				getReadWriteThread().recycleBuffer(_secondaryWriteBuffer);
				_secondaryWriteBuffer = null;
			}
		}
		
		if (_readBuffer != null)
		{
			getReadWriteThread().recycleBuffer(_readBuffer);
			_readBuffer = null;
		}
	}
	
	/**
	 * Called <b><font color="red">on EVERY</font></b> disconnection.
	 */
	protected abstract void onDisconnection();
	
	/**
	 * Called <b><font color="red">ONLY on FORCED - most likely Exception caused - </font></b> disconnection.
	 */
	protected abstract void onForcedDisconnection();
	
	protected abstract boolean decipher(ByteBuffer buf, int size);
	
	protected abstract boolean encipher(ByteBuffer buf, int size);
	
	/**
	 * @return the default close packet used by {@link #closeNow()}.
	 */
	protected abstract SP getDefaultClosePacket();
	
	/**
	 * @return a String used to identify the "real" user "behind" the client for flood protection<br>
	 *         it must stay the same after a disconnection (for example it could be the account name)
	 */
	protected abstract String getUID();
	
	final String getValidUID()
	{
		final String UID = getUID();
		
		return UID == null || UID.isEmpty() ? getHostAddress() : UID;
	}
	
	private FIFORunnableQueue<Runnable> _packetQueue;
	
	public FIFORunnableQueue<Runnable> getPacketQueue()
	{
		if (_packetQueue == null)
			_packetQueue = new FIFORunnableQueue<Runnable>() {
				/* Instantiating an abstract class */
			};
		
		return _packetQueue;
	}
}
