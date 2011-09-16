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

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;

import javolution.util.FastList;

import org.apache.commons.io.IOUtils;

import com.l2jfree.network.mmocore.FloodManager.ErrorMode;
import com.l2jfree.util.HexUtil;

/**
 * {@link MMOController} associated {@link WorkerThread} responsible for read-write operations, and
 * also for closing connections.
 * 
 * @author NB4L1
 * @param <T>
 * @param <RP>
 * @param <SP>
 */
final class ReadWriteThread<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>>
		extends AbstractSelectorThread<T, RP, SP>
{
	private static final int PACKET_HEADER_SIZE = 2;
	
	// Implementations
	private final PacketHandler<T, RP, SP> _packetHandler;
	
	// Pending Close
	private final FastList<T> _pendingClose;
	
	// Configs
	private final int _bufferSize;
	private final int _helperBufferCount;
	private final int _maxOutgoingPacketsPerPass;
	private final int _maxIncomingPacketsPerPass;
	private final int _maxOutgoingBytesPerPass;
	private final int _maxIncomingBytesPerPass;
	private final ByteOrder _byteOrder;
	
	// MAIN BUFFERS
	private final ByteBuffer _directWriteBuffer;
	private final ByteBuffer _writeBuffer;
	private final ByteBuffer _readBuffer;
	
	// ByteBuffers General Purpose Pool
	private final ArrayDeque<ByteBuffer> _bufferPool;
	
	// wrapper for read and write operations
	private final MMOBuffer _mmoBuffer;
	private final DataSizeHolder _dataSizeHolder;
	
	public ReadWriteThread(MMOController<T, RP, SP> mmoController, MMOConfig config,
			PacketHandler<T, RP, SP> packetHandler) throws IOException
	{
		super(mmoController, config);
		
		_bufferSize = config.getBufferSize();
		_helperBufferCount = config.getHelperBufferCount();
		_maxOutgoingPacketsPerPass = config.getMaxOutgoingPacketsPerPass();
		_maxIncomingPacketsPerPass = config.getMaxIncomingPacketsPerPass();
		_maxOutgoingBytesPerPass = config.getMaxOutgoingBytesPerPass();
		_maxIncomingBytesPerPass = config.getMaxIncomingBytesPerPass();
		_byteOrder = config.getByteOrder();
		
		_directWriteBuffer = ByteBuffer.allocateDirect(getBufferSize()).order(getByteOrder());
		_writeBuffer = ByteBuffer.allocate(getBufferSize()).order(getByteOrder());
		_readBuffer = ByteBuffer.allocate(getBufferSize()).order(getByteOrder());
		
		_bufferPool = new ArrayDeque<ByteBuffer>(getHelperBufferCount());
		for (int i = 0; i < getHelperBufferCount(); i++)
			getFreeBuffers().addLast(ByteBuffer.allocate(getBufferSize()).order(getByteOrder()));
		_mmoBuffer = new MMOBuffer();
		_dataSizeHolder = new DataSizeHolder();
		
		_packetHandler = packetHandler;
		_pendingClose = FastList.newInstance();
	}
	
	final ByteBuffer getPooledBuffer()
	{
		final ByteBuffer buffer = getFreeBuffers().pollFirst();
		
		if (buffer == null)
			return ByteBuffer.allocate(getBufferSize()).order(getByteOrder());
		else
			return buffer;
	}
	
	final void recycleBuffer(ByteBuffer buf)
	{
		if (getFreeBuffers().size() < getHelperBufferCount())
		{
			buf.clear();
			getFreeBuffers().addLast(buf);
		}
	}
	
	private ArrayDeque<ByteBuffer> getFreeBuffers()
	{
		return _bufferPool;
	}
	
	private FastList<T> getPendingClose()
	{
		return _pendingClose;
	}
	
	public PacketHandler<T, RP, SP> getPacketHandler()
	{
		return _packetHandler;
	}
	
	final void closeConnection(T con)
	{
		synchronized (getPendingClose())
		{
			getPendingClose().addLast(con);
		}
	}
	
	@Override
	protected void handle(SelectionKey key)
	{
		switch (key.readyOps())
		{
			case SelectionKey.OP_CONNECT:
				finishConnection(key);
				break;
			case SelectionKey.OP_READ:
				readPacket(key);
				break;
			case SelectionKey.OP_WRITE:
				writePacket(key);
				break;
			case SelectionKey.OP_READ | SelectionKey.OP_WRITE:
				writePacket(key);
				// key might have been invalidated on writePacket
				if (key.isValid())
					readPacket(key);
				break;
			default:
				System.err.println("Unknown readyOps: " + key.readyOps() + " for " + key.attachment());
				break;
		}
	}
	
	@Override
	protected void cleanup()
	{
		closePendingConnections();
	}
	
	private void finishConnection(SelectionKey key)
	{
		try
		{
			((SocketChannel)key.channel()).finishConnect();
		}
		catch (IOException e)
		{
			@SuppressWarnings("unchecked")
			T con = (T)key.attachment();
			closeConnectionImpl(con, true);
			return;
		}
		
		// key might have been invalidated on finishConnect()
		if (key.isValid())
		{
			key.interestOps(key.interestOps() | SelectionKey.OP_READ);
			key.interestOps(key.interestOps() & ~SelectionKey.OP_CONNECT);
		}
	}
	
	private void readPacket(SelectionKey key)
	{
		@SuppressWarnings("unchecked")
		T con = (T)key.attachment();
		
		ByteBuffer buf = con.getReadBuffer();
		
		if (buf == null)
		{
			buf = getReadBuffer();
			buf.clear();
		}
		
		int readPackets = 0;
		int readBytes = 0;
		
		for (;;)
		{
			final int remainingFreeSpace = buf.remaining();
			int result = -2;
			
			try
			{
				result = con.getReadableByteChannel().read(buf);
			}
			catch (IOException e)
			{
				//error handling goes bellow
			}
			
			switch (result)
			{
				case -2: // IOException
				{
					closeConnectionImpl(con, true);
					return;
				}
				case -1: // EOS
				{
					closeConnectionImpl(con, false);
					return;
				}
				default:
				{
					buf.flip();
					// try to read as many packets as possible
					for (;;)
					{
						final int startPos = buf.position();
						
						// FIXME hangs connection
						/*if (con.isReadingBlocked())
							break;*/
						
						if (readPackets >= getMaxIncomingPacketsPerPass() || readBytes >= getMaxIncomingBytesPerPass())
							break;
						
						if (!tryReadPacket2(con, buf))
							break;
						
						readPackets++;
						readBytes += (buf.position() - startPos);
					}
					break;
				}
			}
			
			// stop reading, if running a blocking packet
			// FIXME hangs connection
			/*if (con.isReadingBlocked())
				break;*/
			
			// stop reading, if we have reached a config limit
			if (readPackets >= getMaxIncomingPacketsPerPass() || readBytes >= getMaxIncomingBytesPerPass())
				break;
			
			// if the buffer wasn't filled completely, we should stop trying as the input channel is empty
			if (remainingFreeSpace > result)
				break;
			
			// compact the buffer for reusing the remaining bytes
			if (buf.hasRemaining())
				buf.compact();
			else
				buf.clear();
		}
		
		// check if there are some more bytes in buffer and allocate/compact to prevent content lose.
		if (buf.hasRemaining())
		{
			if (buf == getReadBuffer())
			{
				con.setReadBuffer(getPooledBuffer().put(getReadBuffer()));
			}
			else
			{
				buf.compact();
			}
		}
		else
		{
			if (buf == getReadBuffer())
			{
				// no additional buffers used
			}
			else
			{
				con.setReadBuffer(null);
				recycleBuffer(buf);
			}
		}
	}
	
	private boolean tryReadPacket2(T con, ByteBuffer buf)
	{
		// check if header could be processed
		if (buf.remaining() >= 2)
		{
			// parse all headers and get expected packet size
			final int size = (buf.getChar() - PACKET_HEADER_SIZE);
			
			// do we got enough bytes for the packet?
			if (size <= buf.remaining())
			{
				// avoid parsing dummy packets (packets without body)
				if (size > 0)
				{
					int pos = buf.position();
					parseClientPacket(buf, size, con);
					buf.position(pos + size);
				}
				else
				{
					// let's report error to trigger protection
					getMMOController().report(ErrorMode.EMPTY_PACKET, con, null, null);
				}
				
				return true;
			}
			else
			{
				// we dont have enough bytes for the packet so we need to read and revert the header
				buf.position(buf.position() - PACKET_HEADER_SIZE);
				return false;
			}
		}
		else
		{
			// we dont have enough data for header so we need to read
			return false;
		}
	}
	
	private void parseClientPacket(ByteBuffer buf, int dataSize, T client)
	{
		final int pos = buf.position();
		final DataSizeHolder dsh = getDataSizeHolder().init(dataSize);
		
		if (client.decipher(buf, dsh) && buf.hasRemaining())
		{
			// remove useless bytes
			dsh.decreaseSize(dsh.getMinPadding());
			// calculate possibly remaining useless bytes
			final int maxPossiblePadding = dsh.getMaxPadding() - dsh.getMinPadding();
			
			// apply limit
			final int limit = buf.limit();
			buf.limit(pos + dsh.getSize());
			
			final int opcode = buf.get() & 0xFF;
			
			if (getMMOController().canReceivePacketFrom(client, opcode))
			{
				RP cp = getPacketHandler().handlePacket(buf, client, opcode);
				
				if (cp != null)
				{
					// remove useless bytes #2, using packet specs
					int maxLeftoverPadding = maxPossiblePadding;
					final int overflow = buf.remaining() - cp.getMaximumLength();
					if (maxPossiblePadding > 0 && // there may be useless bytes
							overflow > 0) // and we have too much
					{
						// avoid any damage to the packet body
						final int removable = Math.min(overflow, maxPossiblePadding);
						buf.limit(buf.limit() - removable);
						maxLeftoverPadding -= removable;
					}
					
					getMmoBuffer().setByteBuffer(buf);
					cp.setClient(client);
					
					try
					{
						if (getMmoBuffer().getAvailableBytes() < cp.getMinimumLength())
						{
							getMMOController().report(ErrorMode.BUFFER_UNDER_FLOW, client, cp, null);
						}
						else if (getMmoBuffer().getAvailableBytes() > cp.getMaximumLength())
						{
							getMMOController().report(ErrorMode.BUFFER_OVER_FLOW, client, cp, null);
						}
						else
						{
							cp.read(getMmoBuffer());
							
							client.executePacket(cp);
							
							if (buf.hasRemaining() && // some unused data, a bad sign
									buf.remaining() > maxLeftoverPadding) // and definitely not padded bytes
							{
								// FIXME disabled until packet structures updated properly
								//report(ErrorMode.BUFFER_OVER_FLOW, client, cp, null);
								
								MMOController._log.info("Invalid packet format (buf: " + buf + ", dataSize: "
										+ dataSize + ", pos: " + pos + ", limit: " + limit + ", opcode: 0x"
										+ HexUtil.fillHex(opcode, 2) + ") used for reading - " + client + " - "
										+ cp.getType() + " - " + getMMOController().getVersionInfo());
							}
						}
					}
					catch (BufferUnderflowException e)
					{
						getMMOController().report(ErrorMode.BUFFER_UNDER_FLOW, client, cp, e);
					}
					catch (RuntimeException e)
					{
						getMMOController().report(ErrorMode.FAILED_READING, client, cp, e);
					}
					
					getMmoBuffer().setByteBuffer(null);
				}
			}
			
			buf.limit(limit);
		}
	}
	
	private void writePacket(SelectionKey key)
	{
		@SuppressWarnings("unchecked")
		T con = (T)key.attachment();
		
		int wrotePackets = 0;
		int wroteBytes = 0;
		
		for (;;)
		{
			wrotePackets += prepareWriteBuffer2(con, wrotePackets, wroteBytes);
			wroteBytes += getDirectWriteBuffer().position();
			getDirectWriteBuffer().flip();
			
			int size = getDirectWriteBuffer().remaining();
			
			int result = -1;
			
			try
			{
				result = con.getWritableChannel().write(getDirectWriteBuffer());
			}
			catch (IOException e)
			{
				// error handling goes on the if bellow
			}
			
			// check if no error happened
			if (result >= 0)
			{
				// check if we wrote everything
				if (result == size)
				{
					// complete write
					synchronized (con)
					{
						if (con.getSendQueue2().isEmpty() && !con.hasPendingWriteBuffer())
						{
							con.disableWriteInterest();
							return;
						}
						else if (wrotePackets >= getMaxOutgoingPacketsPerPass()
								|| wroteBytes >= getMaxOutgoingBytesPerPass())
							return;
					}
				}
				else
				// incomplete write
				{
					con.createWriteBuffer(getDirectWriteBuffer());
					return;
				}
			}
			else
			{
				closeConnectionImpl(con, true);
				return;
			}
		}
	}
	
	private int prepareWriteBuffer2(T con, int wrotePackets, int wroteBytes)
	{
		getDirectWriteBuffer().clear();
		
		// if theres pending content add it
		if (con.hasPendingWriteBuffer())
		{
			con.movePendingWriteBufferTo(getDirectWriteBuffer());
			// ADDED PENDING TO DIRECT
			
			//wrotePackets += x; // not stored yet, so...
			wroteBytes += getDirectWriteBuffer().position();
		}
		
		// don't write additional, if there are still pending content
		if (!con.hasPendingWriteBuffer())
		{
			while (getDirectWriteBuffer().remaining() >= 2)
			{
				final int startPos = getDirectWriteBuffer().position();
				
				if (wrotePackets >= getMaxOutgoingPacketsPerPass() || wroteBytes >= getMaxOutgoingBytesPerPass())
					break;
				
				final SP sp;
				
				synchronized (con)
				{
					sp = con.getSendQueue2().pollFirst();
					
					if (sp == null)
						break;
				}
				
				// put into WriteBuffer
				putPacketIntoWriteBuffer(con, sp);
				getWriteBuffer().flip();
				
				if (getDirectWriteBuffer().remaining() >= getWriteBuffer().limit())
				{
					// put last written packet to the direct buffer
					getDirectWriteBuffer().put(getWriteBuffer());
					
					wrotePackets++;
					wroteBytes += (getDirectWriteBuffer().position() - startPos);
				}
				else
				{
					// there isn't enough space in the direct buffer
					con.createWriteBuffer(getWriteBuffer());
					break;
				}
			}
		}
		
		return wrotePackets;
	}
	
	private void putPacketIntoWriteBuffer(T client, SP sp)
	{
		getWriteBuffer().clear();
		
		// set the write buffer
		getMmoBuffer().setByteBuffer(getWriteBuffer());
		
		// reserve space for the size
		getWriteBuffer().position(PACKET_HEADER_SIZE);
		
		// write content to buffer
		try
		{
			sp.write(client, getMmoBuffer());
		}
		catch (RuntimeException e)
		{
			MMOController._log.fatal("Failed writing: " + client + " - " + sp.getType() + " - "
					+ getMMOController().getVersionInfo(), e);
		}
		
		// calculate size and encrypt content
		int dataSize = getWriteBuffer().position() - PACKET_HEADER_SIZE;
		getWriteBuffer().position(PACKET_HEADER_SIZE);
		client.encipher(getWriteBuffer(), dataSize);
		
		// recalculate size after encryption
		dataSize = getWriteBuffer().position() - PACKET_HEADER_SIZE;
		
		// prepend header
		getWriteBuffer().position(0);
		getWriteBuffer().putChar((char)(PACKET_HEADER_SIZE + dataSize));
		
		getWriteBuffer().position(PACKET_HEADER_SIZE + dataSize);
		
		// set the write buffer
		getMmoBuffer().setByteBuffer(null);
	}
	
	private void closePendingConnections()
	{
		// process pending close
		synchronized (getPendingClose())
		{
			for (FastList.Node<T> n = getPendingClose().head(), end = getPendingClose().tail(); (n = n.getNext()) != end;)
			{
				final T con = n.getValue();
				
				synchronized (con)
				{
					if (con.getSendQueue2().isEmpty() && !con.hasPendingWriteBuffer() || con.closeTimeouted())
					{
						FastList.Node<T> temp = n.getPrevious();
						getPendingClose().delete(n);
						n = temp;
						closeConnectionImpl(con, false);
					}
				}
			}
		}
	}
	
	private void closeConnectionImpl(T con, boolean forced)
	{
		try
		{
			if (forced)
				con.onForcedDisconnection();
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			// notify connection
			con.onDisconnection();
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				// close socket and the SocketChannel
				IOUtils.closeQuietly(con.getSocket());
			}
			finally
			{
				con.releaseBuffers();
				// clear attachment
				con.getSelectionKey().attach(null);
				// cancel key
				con.getSelectionKey().cancel();
			}
		}
	}
	
	private int getBufferSize()
	{
		return _bufferSize;
	}
	
	public int getHelperBufferCount()
	{
		return _helperBufferCount;
	}
	
	private int getMaxOutgoingPacketsPerPass()
	{
		return _maxOutgoingPacketsPerPass;
	}
	
	private int getMaxIncomingPacketsPerPass()
	{
		return _maxIncomingPacketsPerPass;
	}
	
	private int getMaxOutgoingBytesPerPass()
	{
		return _maxOutgoingBytesPerPass;
	}
	
	private int getMaxIncomingBytesPerPass()
	{
		return _maxIncomingBytesPerPass;
	}
	
	private ByteOrder getByteOrder()
	{
		return _byteOrder;
	}
	
	private ByteBuffer getDirectWriteBuffer()
	{
		return _directWriteBuffer;
	}
	
	private ByteBuffer getWriteBuffer()
	{
		return _writeBuffer;
	}
	
	private ByteBuffer getReadBuffer()
	{
		return _readBuffer;
	}
	
	private MMOBuffer getMmoBuffer()
	{
		return _mmoBuffer;
	}
	
	private DataSizeHolder getDataSizeHolder()
	{
		return _dataSizeHolder;
	}
}
