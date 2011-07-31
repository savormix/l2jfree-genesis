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
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @author NB4L1
 */
final class ConnectorThread<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>>
		extends WorkerThread<T, RP, SP>
{
	private final InetAddress _address;
	private final int _port;
	private final boolean _persistent;
	
	protected ConnectorThread(MMOController<T, RP, SP> mmoController,
			InetAddress address, int port, boolean persistent)
	{
		super(mmoController);
		
		_address = address;
		_port = port;
		_persistent = persistent;
	}
	
	@Override
	public void run()
	{
		// main loop
		for (;;)
		{
			try
			{
				// Connection
				System.out.println("Connecting to " + _address + ":" + _port);
				
				final SocketChannel selectable = SocketChannel.open();
				selectable.configureBlocking(false);
				selectable.connect(new InetSocketAddress(_address, _port));
				while (!selectable.finishConnect())
				{
					try
					{
						Thread.sleep(100);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				
				final T con = getMMOController().createClient(selectable);
				
				for (;;)
				{
					try
					{
						Thread.sleep(1000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					
					// check for shutdown
					if (isShuttingDown())
						return;
					
					// FIXME test which one is proper
					if (!selectable.isOpen() || con.isClosed() || con.getSocket().isClosed())
						break;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			if (!_persistent)
				return;
			
			System.out.println("Disconnected, trying to reconnect in 5 sec!");
			
			try
			{
				Thread.sleep(5000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
