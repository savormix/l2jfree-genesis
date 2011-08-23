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

/**
 * @author NB4L1
 */
public abstract class FIFOSimpleExecutableQueue<T> extends FIFOExecutableQueue
{
	private final ArrayDeque<T> _queue = new ArrayDeque<T>();
	
	public final void add(T t)
	{
		synchronized (_queue)
		{
			_queue.addLast(t);
		}
	}
	
	public final void addAll(Collection<T> c)
	{
		synchronized (_queue)
		{
			_queue.addAll(c);
		}
	}
	
	public final void execute(T t)
	{
		add(t);
		
		execute();
	}
	
	public final void executeAll(Collection<T> c)
	{
		addAll(c);
		
		execute();
	}
	
	public final void executeNow(T t)
	{
		add(t);
		
		executeNow();
	}
	
	public final void executeAllNow(Collection<T> c)
	{
		addAll(c);
		
		executeNow();
	}
	
	public final void remove(T t)
	{
		synchronized (_queue)
		{
			_queue.remove(t);
		}
	}
	
	@Override
	protected final boolean isEmpty()
	{
		synchronized (_queue)
		{
			return _queue.isEmpty();
		}
	}
	
	protected final T removeFirst()
	{
		synchronized (_queue)
		{
			return _queue.pollFirst();
		}
	}
	
	@Override
	protected abstract void removeAndExecuteAll();
}
