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
	
	protected ConnectorThread(MMOController<T, RP, SP> mmoController, InetAddress address, int port)
	{
		super(mmoController);
		
		_address = address;
		_port = port;
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
