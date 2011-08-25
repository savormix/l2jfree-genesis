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

import java.util.ArrayDeque;
import java.util.Collection;

import com.l2jfree.util.L2FastSet;

/**
 * @author NB4L1
 * @param <T>
 */
public abstract class AbstractFIFOPeriodicTaskManager<T> extends AbstractPeriodicTaskManager
{
	private final L2FastSet<T> _queue = new L2FastSet<T>();
	
	private final ArrayDeque<T> _activeTasks = new ArrayDeque<T>();
	
	protected AbstractFIFOPeriodicTaskManager(int period)
	{
		super(period);
	}
	
	public final void add(T t)
	{
		writeLock();
		try
		{
			_queue.add(t);
		}
		finally
		{
			writeUnlock();
		}
	}
	
	public final void addAll(Collection<T> c)
	{
		writeLock();
		try
		{
			_queue.addAll(c);
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
			_activeTasks.addAll(_queue);
			
			_queue.clear();
		}
		finally
		{
			writeUnlock();
		}
		
		for (T task; (task = _activeTasks.pollFirst()) != null;)
		{
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
