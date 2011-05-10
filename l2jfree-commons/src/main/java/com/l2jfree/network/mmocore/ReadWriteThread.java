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

import javolution.util.FastList;

import com.l2jfree.network.mmocore.FloodManager.ErrorMode;

/**
 * @author NB4L1
 */
final class ReadWriteThread<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>>
		extends WorkerThread<T, RP, SP>
{
	// Implementations
	private final IPacketHandler<T, RP, SP> _packetHandler;
	
	// Pending Close
	private final FastList<T> _pendingClose = new FastList<T>();
	
	// Configs
	private final int BUFFER_SIZE;
	private final int HELPER_BUFFER_COUNT;
	private final int MAX_SEND_PER_PASS;
	private final int MAX_READ_PER_PASS;
	private final int MAX_SEND_BYTE_PER_PASS;
	private final int MAX_READ_BYTE_PER_PASS;
	private final int HEADER_SIZE = 2;
	private final ByteOrder BYTE_ORDER;
	
	// MAIN BUFFERS
	private final ByteBuffer DIRECT_WRITE_BUFFER;
	private final ByteBuffer WRITE_BUFFER;
	private final ByteBuffer READ_BUFFER;
	
	// ByteBuffers General Purpose Pool
	private final FastList<ByteBuffer> _bufferPool = new FastList<ByteBuffer>();
	
	// wrapper for read and write operations
	private final MMOBuffer MMO_BUFFER = new MMOBuffer();
	
	public ReadWriteThread(MMOController<T, RP, SP> mmoController, MMOConfig config, IPacketHandler<T, RP, SP> packetHandler)
			throws IOException
	{
		super(mmoController, config);
		
		BUFFER_SIZE = config.getBufferSize();
		HELPER_BUFFER_COUNT = config.getHelperBufferCount();
		MAX_SEND_PER_PASS = config.getMaxSendPerPass();
		MAX_READ_PER_PASS = config.getMaxReadPerPass();
		MAX_SEND_BYTE_PER_PASS = config.getMaxSendBytePerPass();
		MAX_READ_BYTE_PER_PASS = config.getMaxReadBytePerPass();
		BYTE_ORDER = config.getByteOrder();
		
		DIRECT_WRITE_BUFFER = ByteBuffer.allocateDirect(BUFFER_SIZE).order(BYTE_ORDER);
		WRITE_BUFFER = ByteBuffer.allocate(BUFFER_SIZE).order(BYTE_ORDER);
		READ_BUFFER = ByteBuffer.allocate(BUFFER_SIZE).order(BYTE_ORDER);
		
		initBufferPool();
		_packetHandler = packetHandler;
	}
	
	private void initBufferPool()
	{
		for (int i = 0; i < HELPER_BUFFER_COUNT; i++)
		{
			getFreeBuffers().addLast(ByteBuffer.allocate(BUFFER_SIZE).order(BYTE_ORDER));
		}
	}
	
	final ByteBuffer getPooledBuffer()
	{
		if (getFreeBuffers().isEmpty())
			return ByteBuffer.allocate(BUFFER_SIZE).order(BYTE_ORDER);
		else
			return getFreeBuffers().removeFirst();
	}
	
	final void recycleBuffer(ByteBuffer buf)
	{
		if (getFreeBuffers().size() < HELPER_BUFFER_COUNT)
		{
			buf.clear();
			getFreeBuffers().addLast(buf);
		}
	}
	
	private FastList<ByteBuffer> getFreeBuffers()
	{
		return _bufferPool;
	}
	
	private FastList<T> getPendingClose()
	{
		return _pendingClose;
	}
	
	public IPacketHandler<T, RP, SP> getPacketHandler()
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
			buf = READ_BUFFER;
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
						
						if (readPackets >= MAX_READ_PER_PASS || readBytes >= MAX_READ_BYTE_PER_PASS)
							break;
						
						if (!tryReadPacket2(con, buf))
							break;
						
						readPackets++;
						readBytes += (buf.position() - startPos);
					}
					break;
				}
			}
			
			// stop reading, if we have reached a config limit
			if (readPackets >= MAX_READ_PER_PASS || readBytes >= MAX_READ_BYTE_PER_PASS)
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
			if (buf == READ_BUFFER)
			{
				con.setReadBuffer(getPooledBuffer().put(READ_BUFFER));
			}
			else
			{
				buf.compact();
			}
		}
		else
		{
			if (buf == READ_BUFFER)
			{
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
			final int size = (buf.getShort() & 0xFFFF) - HEADER_SIZE;
			
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
				
				return true;
			}
			else
			{
				// we dont have enough bytes for the packet so we need to read and revert the header
				buf.position(buf.position() - HEADER_SIZE);
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
		int pos = buf.position();
		
		if (client.decrypt(buf, dataSize) && buf.hasRemaining())
		{
			// apply limit
			int limit = buf.limit();
			buf.limit(pos + dataSize);
			
			final int opcode = buf.get() & 0xFF;
			
			if (getMMOController().canReceivePacketFrom(client, opcode))
			{
				RP cp = getPacketHandler().handlePacket(buf, client, opcode);
				
				if (cp != null)
				{
					MMO_BUFFER.setByteBuffer(buf);
					cp.setClient(client);
					
					try
					{
						if (MMO_BUFFER.getAvailableBytes() < cp.getMinimumLength())
						{
							getMMOController().report(ErrorMode.BUFFER_UNDER_FLOW, client, cp, null);
						}
						else if (cp.read(MMO_BUFFER))
						{
							client.getPacketQueue().execute(cp);
							
							if (buf.hasRemaining())
							{
								// disabled until packet structures updated properly
								//report(ErrorMode.BUFFER_OVER_FLOW, client, cp, null);
								
								MMOController._log.info("Invalid packet format (buf: " + buf + ", dataSize: " + dataSize
										+ ", pos: " + pos + ", limit: " + limit + ", opcode: " + opcode
										+ ") used for reading - " + client + " - " + cp.getType() + " - "
										+ getMMOController().getVersionInfo());
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
					
					MMO_BUFFER.setByteBuffer(null);
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
			wroteBytes += DIRECT_WRITE_BUFFER.position();
			DIRECT_WRITE_BUFFER.flip();
			
			int size = DIRECT_WRITE_BUFFER.remaining();
			
			int result = -1;
			
			try
			{
				result = con.getWritableChannel().write(DIRECT_WRITE_BUFFER);
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
						else if (wrotePackets >= MAX_SEND_PER_PASS || wroteBytes >= MAX_SEND_BYTE_PER_PASS)
							return;
					}
				}
				else
				// incomplete write
				{
					con.createWriteBuffer(DIRECT_WRITE_BUFFER);
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
		DIRECT_WRITE_BUFFER.clear();
		
		// if theres pending content add it
		if (con.hasPendingWriteBuffer())
		{
			con.movePendingWriteBufferTo(DIRECT_WRITE_BUFFER);
			// ADDED PENDING TO DIRECT
			
			//wrotePackets += x; // not stored yet, so...
			wroteBytes += DIRECT_WRITE_BUFFER.position();
		}
		
		// don't write additional, if there are still pending content
		if (!con.hasPendingWriteBuffer())
		{
			for (; DIRECT_WRITE_BUFFER.remaining() >= 2;)
			{
				final int startPos = DIRECT_WRITE_BUFFER.position();
				
				if (wrotePackets >= MAX_SEND_PER_PASS || wroteBytes >= MAX_SEND_BYTE_PER_PASS)
					break;
				
				final SP sp;
				
				synchronized (con)
				{
					final FastList<SP> sendQueue = con.getSendQueue2();
					
					if (sendQueue.isEmpty())
						break;
					
					sp = sendQueue.removeFirst();
				}
				
				// put into WriteBuffer
				putPacketIntoWriteBuffer(con, sp);
				WRITE_BUFFER.flip();
				
				if (DIRECT_WRITE_BUFFER.remaining() >= WRITE_BUFFER.limit())
				{
					// put last written packet to the direct buffer
					DIRECT_WRITE_BUFFER.put(WRITE_BUFFER);
					
					wrotePackets++;
					wroteBytes += (DIRECT_WRITE_BUFFER.position() - startPos);
				}
				else
				{
					// there isn't enough space in the direct buffer
					con.createWriteBuffer(WRITE_BUFFER);
					break;
				}
			}
		}
		
		return wrotePackets;
	}
	
	private void putPacketIntoWriteBuffer(T client, SP sp)
	{
		WRITE_BUFFER.clear();
		
		// set the write buffer
		MMO_BUFFER.setByteBuffer(WRITE_BUFFER);
		
		// reserve space for the size
		WRITE_BUFFER.position(HEADER_SIZE);
		
		// write content to buffer
		try
		{
			sp.write(client, MMO_BUFFER);
		}
		catch (RuntimeException e)
		{
			MMOController._log.fatal("Failed writing: " + client + " - " + sp.getType() + " - "
					+ getMMOController().getVersionInfo(), e);
		}
		
		// calculate size and encrypt content
		int dataSize = WRITE_BUFFER.position() - HEADER_SIZE;
		WRITE_BUFFER.position(HEADER_SIZE);
		client.encrypt(WRITE_BUFFER, dataSize);
		
		// recalculate size after encryption
		dataSize = WRITE_BUFFER.position() - HEADER_SIZE;
		
		// prepend header
		WRITE_BUFFER.position(0);
		WRITE_BUFFER.putShort((short)(HEADER_SIZE + dataSize));
		
		WRITE_BUFFER.position(HEADER_SIZE + dataSize);
		
		// set the write buffer
		MMO_BUFFER.setByteBuffer(null);
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
				con.getSocket().close();
			}
			catch (IOException e)
			{
				// ignore, we are closing anyway
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
}
