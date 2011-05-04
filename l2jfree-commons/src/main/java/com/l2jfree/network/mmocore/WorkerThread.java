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
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

/**
 * @author NB4L1
 */
abstract class WorkerThread<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>>
		extends Thread
{
	private final SelectorThread<T, RP, SP> _selectorThread;
	private final Selector _selector;
	
	private volatile boolean _shutdown;
	
	private final long SLEEP_TIME;
	
	protected WorkerThread(String name, SelectorThread<T, RP, SP> selectorThread, SelectorConfig sc) throws IOException
	{
		super(name);
		
		SLEEP_TIME = sc.getSelectorSleepTime();
		
		_selectorThread = selectorThread;
		_selector = Selector.open();
	}
	
	protected final SelectorThread<T, RP, SP> getSelectorThread()
	{
		return _selectorThread;
	}
	
	protected final Selector getSelector()
	{
		return _selector;
	}
	
	@Override
	public final void run()
	{
		// main loop
		for (;;)
		{
			// check for shutdown
			if (isShuttingDown())
			{
				close();
				break;
			}
			
			try
			{
				if (getSelector().selectNow() > 0)
				{
					Set<SelectionKey> keys = getSelector().selectedKeys();
					
					for (SelectionKey key : keys)
					{
						handle(key);
					}
					
					keys.clear();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (RuntimeException e)
			{
				e.printStackTrace();
			}
			
			cleanup();
			
			try
			{
				Thread.sleep(SLEEP_TIME);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			cleanup();
		}
	}
	
	protected abstract void handle(SelectionKey key) throws IOException;
	
	protected void cleanup()
	{
	}
	
	public final void shutdown() throws InterruptedException
	{
		_shutdown = true;
		
		join();
	}
	
	final boolean isShuttingDown()
	{
		return _shutdown;
	}
	
	private void close()
	{
		for (SelectionKey key : getSelector().keys())
		{
			try
			{
				key.channel().close();
			}
			catch (IOException e)
			{
				// ignore
			}
		}
		
		try
		{
			getSelector().close();
		}
		catch (IOException e)
		{
			// Ignore
		}
	}
}
