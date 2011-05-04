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
package com.l2jfree.util;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.l2jfree.lang.L2Thread;
import com.l2jfree.util.concurrent.EmptyLock;

/**
 * @author NB4L1
 */
public abstract class ObjectPool<E> extends AbstractObjectPool<E>
{
	private static final WeakHashMap<ObjectPool<?>, Object> POOLS = new WeakHashMap<ObjectPool<?>, Object>();
	
	static
	{
		final L2Thread t = new L2Thread(ObjectPool.class.getName()) {
			@Override
			protected void runTurn()
			{
				try
				{
					for (ObjectPool<?> pool : POOLS.keySet())
						if (pool != null)
							pool.purge();
				}
				catch (ConcurrentModificationException e)
				{
					// skip it
				}
			}
			
			@Override
			protected void sleepTurn() throws InterruptedException
			{
				Thread.sleep(60000);
			}
		};
		t.setDaemon(true);
		t.start();
	}
	
	private final Lock _lock;
	
	private Object[] _elements = new Object[0];
	private long[] _access = new long[0];
	private int _size = 0;
	
	protected ObjectPool()
	{
		this(true);
	}
	
	protected ObjectPool(boolean concurrent)
	{
		_lock = concurrent ? new ReentrantLock() : new EmptyLock();
		
		POOLS.put(this, POOLS);
	}
	
	public int getCurrentSize()
	{
		_lock.lock();
		try
		{
			return _size;
		}
		finally
		{
			_lock.unlock();
		}
	}
	
	public void clear()
	{
		_lock.lock();
		try
		{
			_elements = new Object[0];
			_access = new long[0];
			_size = 0;
		}
		finally
		{
			_lock.unlock();
		}
	}
	
	@Override
	public void store(E e)
	{
		if (e == null || getCurrentSize() >= getMaximumSize())
			return;
		
		reset(e);
		
		_lock.lock();
		try
		{
			if (_size == _elements.length)
			{
				_elements = Arrays.copyOf(_elements, _elements.length + 10);
				_access = Arrays.copyOf(_access, _access.length + 10);
			}
			
			_elements[_size] = e;
			_access[_size] = System.currentTimeMillis();
			
			_size++;
		}
		finally
		{
			_lock.unlock();
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public E get()
	{
		Object obj = null;
		
		_lock.lock();
		try
		{
			if (_size > 0)
			{
				_size--;
				
				obj = _elements[_size];
				
				_elements[_size] = null;
				_access[_size] = 0;
			}
		}
		finally
		{
			_lock.unlock();
		}
		
		return obj == null ? create() : (E)obj;
	}
	
	public void purge()
	{
		_lock.lock();
		try
		{
			int newIndex = 0;
			for (int oldIndex = 0; oldIndex < _elements.length; oldIndex++)
			{
				final Object obj = _elements[oldIndex];
				final long time = _access[oldIndex];
				
				_elements[oldIndex] = null;
				_access[oldIndex] = 0;
				
				if (obj == null || time + getMaxLifeTime() < System.currentTimeMillis())
					continue;
				
				_elements[newIndex] = obj;
				_access[newIndex] = time;
				
				newIndex++;
			}
			
			_elements = Arrays.copyOf(_elements, newIndex);
			_access = Arrays.copyOf(_access, newIndex);
			_size = newIndex;
		}
		finally
		{
			_lock.unlock();
		}
	}
}
