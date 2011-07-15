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
import java.net.UnknownHostException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

import javolution.util.FastList;

import com.l2jfree.network.mmocore.FloodManager.ErrorMode;
import com.l2jfree.network.mmocore.FloodManager.Result;

/**
 * @param <T> connection
 * @param <RP> receivable packet
 * @param <SP> sendable packet
 * @author KenM<BR>
 *         Parts of design based on networkcore from WoodenGil
 */
public abstract class MMOController<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>>
{
	protected static final MMOLogger _log = new MMOLogger(MMOController.class, 1000);
	
	private final MMOConfig _config;
	private final String _name;
	
	private AcceptorThread<T, RP, SP> _acceptorThread;
	private final FastList<ConnectorThread<T, RP, SP>> _connectorThreads = new FastList<ConnectorThread<T, RP, SP>>();
	private final ReadWriteThread<T, RP, SP>[] _readWriteThreads;
	
	@SuppressWarnings("unchecked")
	protected MMOController(MMOConfig config, IPacketHandler<T, RP, SP> packetHandler) throws IOException
	{
		_config = config;
		_name = config.getName();
		
		_readWriteThreads = new ReadWriteThread[config.getThreadCount()];
		
		for (int i = 0; i < _readWriteThreads.length; i++)
		{
			final ReadWriteThread<T, RP, SP> readWriteThread = new ReadWriteThread<T, RP, SP>(this, config,
					packetHandler);
			
			readWriteThread.setName(readWriteThread.getName() + "-" + (i + 1));
			
			_readWriteThreads[i] = readWriteThread;
		}
	}
	
	/**
	 * Opens a server socket, to accept incoming connections.
	 * 
	 * @param address the address to listen on
	 * @param port the port to listen on
	 * @throws IOException if any problem occurs while opening the acceptor
	 * @throws UnknownHostException if an invalid address was given
	 */
	public final void openServerSocket(String address, int port) throws IOException, UnknownHostException
	{
		openServerSocket(InetAddress.getByName(address), port);
	}
	
	/**
	 * Opens a server socket, to accept incoming connections.
	 * 
	 * @param address the address to listen on (should be null in order to listen on all available addresses)
	 * @param port the port to listen on
	 * @throws IOException if any problem occurs while opening the acceptor
	 */
	public final void openServerSocket(InetAddress address, int port) throws IOException
	{
		if (_acceptorThread == null)
			_acceptorThread = new AcceptorThread<T, RP, SP>(this, _config);
		
		_acceptorThread.openServerSocket(address, port);
	}
	
	/**
	 * Connects to the given address and port as a client.
	 * 
	 * @param address the address to connect to
	 * @param port the port to connect to
	 * @throws UnknownHostException if an invalid address was given
	 */
	public final void connect(String address, int port) throws UnknownHostException
	{
		connect(InetAddress.getByName(address), port);
	}
	
	/**
	 * Connects to the given address and port as a client.
	 * 
	 * @param address the address to connect to
	 * @param port the port to connect to
	 */
	public final void connect(InetAddress address, int port)
	{
		_connectorThreads.add(new ConnectorThread<T, RP, SP>(this, address, port));
	}
	
	public String getName()
	{
		return _name;
	}
	
	private int _readWriteThreadIndex;
	
	final ReadWriteThread<T, RP, SP> getRandomReadWriteThread()
	{
		return _readWriteThreads[_readWriteThreadIndex++ % _readWriteThreads.length];
	}
	
	/**
	 * Starts the mmocore threads.
	 */
	public final void start()
	{
		if (_acceptorThread != null)
			_acceptorThread.start();
		
		for (ConnectorThread<T, RP, SP> connectorThread : _connectorThreads)
			connectorThread.start();
		
		for (ReadWriteThread<T, RP, SP> readWriteThread : _readWriteThreads)
			readWriteThread.start();
	}
	
	/**
	 * Initiates the shutdown of the mmocore threads, and waits until they are finished.
	 * 
	 * @throws InterruptedException
	 */
	public final void shutdown() throws InterruptedException
	{
		if (_acceptorThread != null)
			_acceptorThread.shutdown();
		
		for (ConnectorThread<T, RP, SP> connectorThread : _connectorThreads)
			connectorThread.shutdown();
		
		for (ReadWriteThread<T, RP, SP> readWriteThread : _readWriteThreads)
			readWriteThread.shutdown();
	}
	
	// ==============================================
	
	/**
	 * Creates a client with any required custom additions.
	 * 
	 * @param socketChannel the socket channel for read-write calls
	 * @return a client backed by the given socket channel
	 * @throws ClosedChannelException
	 */
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
	
	/**
	 * @return a String with informations about this controller for debugging purposes
	 */
	protected String getVersionInfo()
	{
		return "";
	}
	
	/**
	 * An easy way to apply any special limitations on incoming connections. At default it contains a flood protection.<br>
	 * Overriding implementations should call the super method before anything else gets checked.<br>
	 * <br>
	 * NOTE: Uses a special way of logging to avoid console flood.
	 * 
	 * @param sc the inbound connection from a possible client
	 * @return true if the connection is valid, and should be allowed, no otherwise
	 */
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
	
	/**
	 * To report any occuring error.<br>
	 * <br>
	 * NOTE: Uses a special way of logging to avoid console flood.
	 * 
	 * @param mode describing the nature of the error
	 * @param client the associated client
	 * @param packet the associated packet, if there is any
	 * @param throwable the associated throwable, if there is any
	 */
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
	
	/**
	 * An easy way to apply any special limitations on incoming packets. At default it contains a flood protection.<br>
	 * Overriding implementations should call the super method before anything else gets checked.<br>
	 * <br>
	 * NOTE: Uses a special way of logging to avoid console flood.
	 * 
	 * @param client the associated client
	 * @param opcode the opcode of the potential packet (for debugging purposes)
	 * @return true if the client can be allowed to receive a packet, no otherwise
	 */
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
