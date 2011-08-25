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

import java.util.Set;

import javolution.util.FastSet;

/**
 * @author NB4L1
 * @param <T>
 */
public abstract class AbstractIterativePeriodicTaskManager<T> extends AbstractPeriodicTaskManager
{
	private final Set<T> _startList = new FastSet<T>();
	private final Set<T> _stopList = new FastSet<T>();
	
	private final FastSet<T> _activeTasks = new FastSet<T>();
	
	protected AbstractIterativePeriodicTaskManager(int period)
	{
		super(period);
	}
	
	public boolean hasTask(T task)
	{
		readLock();
		try
		{
			if (_stopList.contains(task))
				return false;
			
			return _activeTasks.contains(task) || _startList.contains(task);
		}
		finally
		{
			readUnlock();
		}
	}
	
	public void startTask(T task)
	{
		writeLock();
		try
		{
			_startList.add(task);
			
			_stopList.remove(task);
		}
		finally
		{
			writeUnlock();
		}
	}
	
	public void stopTask(T task)
	{
		writeLock();
		try
		{
			_stopList.add(task);
			
			_startList.remove(task);
		}
		finally
		{
			writeUnlock();
		}
	}
	
	@Override
	public final void run()
	{
		writeLock();
		try
		{
			_activeTasks.addAll(_startList);
			_activeTasks.removeAll(_stopList);
			
			_startList.clear();
			_stopList.clear();
		}
		finally
		{
			writeUnlock();
		}
		
		for (FastSet.Record r = _activeTasks.head(), end = _activeTasks.tail(); (r = r.getNext()) != end;)
		{
			final T task = _activeTasks.valueOf(r);
			final long begin = System.nanoTime();
			
			try
			{
				callTask(task);
			}
			catch (RuntimeException e)
			{
				_log.warn("", e);
			}
			finally
			{
				RunnableStatsManager.handleStats(task.getClass(), getCalledMethodName(), System.nanoTime() - begin);
			}
		}
	}
	
	protected abstract void callTask(T task);
	
	protected abstract String getCalledMethodName();
}
