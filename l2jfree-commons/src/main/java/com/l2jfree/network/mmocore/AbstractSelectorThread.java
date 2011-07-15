package com.l2jfree.network.mmocore;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

/**
 * @author NB4L1
 */
abstract class AbstractSelectorThread<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>>
		extends WorkerThread<T, RP, SP>
{
	private final Selector _selector;
	
	private final long SLEEP_TIME;
	
	protected AbstractSelectorThread(MMOController<T, RP, SP> mmoController, MMOConfig config) throws IOException
	{
		super(mmoController);
		
		SLEEP_TIME = config.getSelectorSleepTime();
		
		_selector = Selector.open();
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
				return;
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
