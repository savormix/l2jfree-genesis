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

import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.l2jfree.Startup;
import com.l2jfree.Startup.StartupHook;
import com.l2jfree.util.Rnd;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author NB4L1
 */
abstract class AbstractPeriodicTaskManager implements Runnable, StartupHook
{
	static final L2Logger _log = L2Logger.getLogger(AbstractPeriodicTaskManager.class);
	
	private final ReentrantReadWriteLock _lock = new ReentrantReadWriteLock();
	private final ReentrantReadWriteLock.ReadLock _readLock = _lock.readLock();
	private final ReentrantReadWriteLock.WriteLock _writeLock = _lock.writeLock();
	
	private final int _period;
	
	AbstractPeriodicTaskManager(int period)
	{
		_period = period;
		
		Startup.addStartupHook(this);
		
		_log.info(getClass().getSimpleName() + ": Initialized.");
	}
	
	public final void readLock()
	{
		_readLock.lock();
	}
	
	public final void readUnlock()
	{
		_readLock.unlock();
	}
	
	public final void writeLock()
	{
		_writeLock.lock();
	}
	
	public final void writeUnlock()
	{
		_writeLock.unlock();
	}
	
	@Override
	public final void onStartup()
	{
		L2ThreadPool.scheduleAtFixedRate(this, 1000 + Rnd.get(_period), Rnd.get(_period - 5, _period + 5));
	}
	
	@Override
	public abstract void run();
}
