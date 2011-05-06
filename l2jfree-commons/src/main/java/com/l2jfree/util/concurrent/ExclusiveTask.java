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
package com.l2jfree.util.concurrent;

import java.util.concurrent.Future;

/**
 * @author NB4L1
 */
public abstract class ExclusiveTask implements Runnable
{
	private final boolean _returnIfAlreadyRunning;
	
	private Future<?> _future;
	private boolean _isRunning;
	private Thread _currentThread;
	
	protected ExclusiveTask(boolean returnIfAlreadyRunning)
	{
		_returnIfAlreadyRunning = returnIfAlreadyRunning;
	}
	
	protected ExclusiveTask()
	{
		this(false);
	}
	
	public synchronized final boolean isScheduled()
	{
		return _future != null && !_future.isDone();
	}
	
	public synchronized final void cancel()
	{
		if (_future != null)
		{
			_future.cancel(false);
			_future = null;
		}
	}
	
	public synchronized final void schedule(long delay)
	{
		cancel();
		
		_future = L2ThreadPool.schedule(this, delay);
	}
	
	public synchronized final void scheduleAtFixedRate(long delay, long period)
	{
		cancel();
		
		_future = L2ThreadPool.scheduleAtFixedRate(this, delay, period);
	}
	
	@Override
	public void run()
	{
		if (tryLock())
		{
			try
			{
				onElapsed();
			}
			finally
			{
				unlock();
			}
		}
	}
	
	protected abstract void onElapsed();
	
	private synchronized boolean tryLock()
	{
		if (_returnIfAlreadyRunning)
		{
			if (_isRunning)
				return false;
			
			_isRunning = true;
			return true;
		}
		
		_currentThread = Thread.currentThread();
		notifyAll();
		
		while (_isRunning)
		{
			try
			{
				wait();
			}
			catch (InterruptedException e)
			{
			}
			
			if (_currentThread != Thread.currentThread())
				return false;
		}
		
		_isRunning = true;
		return true;
	}
	
	private synchronized void unlock()
	{
		if (_returnIfAlreadyRunning)
		{
			_isRunning = false;
			return;
		}
		
		_isRunning = false;
		notifyAll();
	}
}
