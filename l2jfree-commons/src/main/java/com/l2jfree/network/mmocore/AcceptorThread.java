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
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.apache.commons.io.IOUtils;

import com.l2jfree.Util;

/**
 * {@link MMOController} associated {@link WorkerThread} responsible for accepting new connections.
 * 
 * @author NB4L1
 * @param <T>
 * @param <RP>
 * @param <SP>
 */
final class AcceptorThread<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>>
		extends AbstractSelectorThread<T, RP, SP>
{
	private final int _bufferSize;
	
	public AcceptorThread(MMOController<T, RP, SP> mmoController, MMOConfig config) throws IOException
	{
		super(mmoController, config);
		
		_bufferSize = config.getBufferSize();
	}
	
	public void openServerSocket(InetAddress address, int port) throws IOException
	{
		ServerSocketChannel selectable = ServerSocketChannel.open();
		selectable.configureBlocking(false);
		
		ServerSocket ss = selectable.socket();
		ss.setReuseAddress(true);
		ss.setReceiveBufferSize(getBufferSize());
		if (address == null)
		{
			ss.bind(new InetSocketAddress(port));
		}
		else
		{
			ss.bind(new InetSocketAddress(address, port));
		}
		selectable.register(getSelector(), SelectionKey.OP_ACCEPT);
	}
	
	@Override
	protected void handle(SelectionKey key)
	{
		acceptConnection(key);
	}
	
	private void acceptConnection(SelectionKey key)
	{
		SocketChannel sc;
		try
		{
			while ((sc = ((ServerSocketChannel)key.channel()).accept()) != null)
			{
				if (getMMOController().acceptConnectionFrom(sc))
				{
					sc.configureBlocking(false);
					
					Util.printSection(getMMOController().getClass().getSimpleName() + " "
							+ sc.socket().getRemoteSocketAddress().toString());
					final T con = getMMOController().createClient(sc);
					con.enableReadInterest();
				}
				else
				{
					IOUtils.closeQuietly(sc.socket());
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private int getBufferSize()
	{
		return _bufferSize;
	}
}
