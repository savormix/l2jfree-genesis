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
import java.net.InetAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

import com.l2jfree.network.mmocore.FloodManager.ErrorMode;
import com.l2jfree.network.mmocore.FloodManager.Result;

/**
 * @author KenM<BR>
 *         Parts of design based on networkcore from WoodenGil
 */
public abstract class SelectorThread<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>>
{
	protected static final MMOLogger _log = new MMOLogger(SelectorThread.class, 1000);
	
	private final String _name;
	
	private final AcceptorThread<T, RP, SP> _acceptorThread;
	private final ReadWriteThread<T, RP, SP>[] _readWriteThreads;
	
	@SuppressWarnings("unchecked")
	protected SelectorThread(SelectorConfig sc, IPacketHandler<T, RP, SP> packetHandler) throws IOException
	{
		_name = sc.getName();
		
		_acceptorThread = new AcceptorThread<T, RP, SP>(this, sc);
		_readWriteThreads = new ReadWriteThread[sc.getSelectorThreadCount()];
		
		for (int i = 0; i < _readWriteThreads.length; i++)
		{
			final ReadWriteThread<T, RP, SP> readWriteThread = new ReadWriteThread<T, RP, SP>(this, sc, packetHandler);
			
			readWriteThread.setName(readWriteThread.getName() + "-" + (i + 1));
			
			_readWriteThreads[i] = readWriteThread;
		}
	}
	
	public final void openServerSocket(String address, int port) throws IOException
	{
		openServerSocket(InetAddress.getByName(address), port);
	}
	
	public final void openServerSocket(InetAddress address, int port) throws IOException
	{
		getAcceptorThread().openServerSocket(address, port);
	}
	
	public String getName()
	{
		return _name;
	}
	
	private AcceptorThread<T, RP, SP> getAcceptorThread()
	{
		return _acceptorThread;
	}
	
	private ReadWriteThread<T, RP, SP>[] getReadWriteThreads()
	{
		return _readWriteThreads;
	}
	
	private int _readWriteThreadIndex;
	
	final ReadWriteThread<T, RP, SP> getReadWriteThread()
	{
		return getReadWriteThreads()[_readWriteThreadIndex++ % getReadWriteThreads().length];
	}
	
	public final void start()
	{
		getAcceptorThread().start();
		
		for (ReadWriteThread<T, RP, SP> readWriteThread : getReadWriteThreads())
			readWriteThread.start();
	}
	
	public final void shutdown() throws InterruptedException
	{
		getAcceptorThread().shutdown();
		
		for (ReadWriteThread<T, RP, SP> readWriteThread : getReadWriteThreads())
			readWriteThread.shutdown();
	}
	
	// ==============================================
	
	protected abstract T createClient(SocketChannel socketChannel) throws ClosedChannelException;
	
	// ==============================================
	
	private final FloodManager _accepts;
	private final FloodManager _packets;
	private final FloodManager _errors;
	
	{
		// TODO fine tune
		_accepts = new FloodManager(1000, // 1000 msec per tick
				new FloodManager.FloodFilter(10, 20, 10), // short period
				new FloodManager.FloodFilter(30, 60, 60)); // long period
		
		_packets = new FloodManager(1000, // 1000 msec per tick
				new FloodManager.FloodFilter(250, 300, 2));
		
		_errors = new FloodManager(200, // 200 msec per tick
				new FloodManager.FloodFilter(10, 10, 1));
	}
	
	protected String getVersionInfo()
	{
		return "";
	}
	
	protected boolean acceptConnectionFrom(SocketChannel sc)
	{
		final String host = sc.socket().getInetAddress().getHostAddress();
		
		final Result isFlooding = _accepts.isFlooding(host, true);
		
		switch (isFlooding)
		{
			case REJECTED:
			{
				// TODO punish, warn, log, etc
				_log.warn("Rejected connection from " + host);
				return false;
			}
			case WARNED:
			{
				// TODO punish, warn, log, etc
				_log.warn("Connection over warn limit from " + host);
				return true;
			}
			default:
				return true;
		}
	}
	
	public void report(ErrorMode mode, T client, RP packet, Throwable throwable)
	{
		final Result isFlooding = _errors.isFlooding(client.getValidUID(), true);
		
		final StringBuilder sb = new StringBuilder();
		if (isFlooding != Result.ACCEPTED)
		{
			sb.append("Flooding with ");
		}
		sb.append(mode);
		sb.append(": ");
		sb.append(client);
		if (packet != null)
		{
			sb.append(" - ");
			sb.append(packet.getType());
		}
		final String versionInfo = getVersionInfo();
		if (versionInfo != null && !versionInfo.isEmpty())
		{
			sb.append(" - ");
			sb.append(versionInfo);
		}
		
		if (throwable != null)
			_log.info(sb, throwable);
		else
			_log.info(sb);
		
		//if (isFlooding != Result.ACCEPTED)
		//{
		//	// TODO punish, warn, log, etc
		//}
	}
	
	protected boolean canReceivePacketFrom(T client, int opcode)
	{
		final String key = client.getValidUID();
		
		switch (Result.max(_packets.isFlooding(key, true), _errors.isFlooding(key, false)))
		{
			case REJECTED:
			{
				// TODO punish, warn, log, etc
				_log.warn("Rejected packet (0x" + Integer.toHexString(opcode) + ") from " + client);
				return false;
			}
			case WARNED:
			{
				// TODO punish, warn, log, etc
				_log.warn("Packet over warn limit (0x" + Integer.toHexString(opcode) + ") from " + client);
				return true;
			}
			default:
				return true;
		}
	}
}
