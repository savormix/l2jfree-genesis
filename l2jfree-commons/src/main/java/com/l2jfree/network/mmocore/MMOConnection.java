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

/**
 * @author KenM
 */
public abstract class MMOConnection<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>>
{
	private final SelectorThread<T, RP, SP> _selectorThread;
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
	
	protected MMOConnection(SelectorThread<T, RP, SP> selectorThread, SocketChannel socketChannel)
			throws ClosedChannelException
	{
		_selectorThread = selectorThread;
		_readWriteThread = getSelectorThread().getReadWriteThread();
		_socket = socketChannel.socket();
		_inetAddress = _socket.getInetAddress();
		_hostAddress = _inetAddress.getHostAddress();
		_selectionKey = socketChannel.register(getReadWriteThread().getSelector(), SelectionKey.OP_READ);
		_selectionKey.attach(this);
	}
	
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
	
	final SelectorThread<T, RP, SP> getSelectorThread()
	{
		return _selectorThread;
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
			SelectorThread._log.warn("", e);
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
	
	protected abstract void onDisconnection();
	
	protected abstract void onForcedDisconnection();
	
	protected abstract boolean decrypt(ByteBuffer buf, int size);
	
	protected abstract boolean encrypt(ByteBuffer buf, int size);
	
	protected abstract SP getDefaultClosePacket();
	
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
			_packetQueue = new FIFORunnableQueue<Runnable>() {};
		
		return _packetQueue;
	}
}
