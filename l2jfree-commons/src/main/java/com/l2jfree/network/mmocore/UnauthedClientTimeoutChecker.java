package com.l2jfree.network.mmocore;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

import javolution.util.FastMap;

import com.l2jfree.util.concurrent.L2ThreadPool;

public class UnauthedClientTimeoutChecker implements Runnable
{
	private static final class SingletonHolder
	{
		private static final UnauthedClientTimeoutChecker INSTANCE = new UnauthedClientTimeoutChecker();
	}
	
	public static UnauthedClientTimeoutChecker getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private final Map<MMOConnection<?, ?, ?>, Long> _unauthedClients = new FastMap<MMOConnection<?, ?, ?>, Long>();
	private final ReentrantLock _lock = new ReentrantLock();
	
	private UnauthedClientTimeoutChecker()
	{
		L2ThreadPool.scheduleAtFixedRate(this, 1000, 1000);
	}
	
	public void clientCreated(MMOConnection<?, ?, ?> client)
	{
		_lock.lock();
		try
		{
			_unauthedClients.put(client, System.currentTimeMillis());
		}
		finally
		{
			_lock.unlock();
		}
	}
	
	@Override
	public void run()
	{
		_lock.lock();
		try
		{
			for (Entry<MMOConnection<?, ?, ?>, Long> entry : _unauthedClients.entrySet())
			{
				final MMOConnection<?, ?, ?> client = entry.getKey();
				
				if (entry.getValue() + 10000 < System.currentTimeMillis())
				{
					if (!client.isAuthed())
						client.closeNow();
					
					_unauthedClients.remove(client);
				}
			}
		}
		finally
		{
			_lock.unlock();
		}
	}
}
