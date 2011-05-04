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

import java.util.Collection;
import java.util.Iterator;

/**
 * @author NB4L1
 */
public abstract class LazyCollection<E, C extends Collection<E>> implements Collection<E>
{
	protected boolean _initialized = false;
	protected C _collection = emptyCollection();
	
	protected LazyCollection()
	{
	}
	
	protected final void init()
	{
		if (!_initialized)
		{
			synchronized (this)
			{
				if (!_initialized)
				{
					_collection = initCollection();
					_initialized = true;
				}
			}
		}
	}
	
	protected abstract C emptyCollection();
	
	protected abstract C initCollection();
	
	@Override
	public final boolean add(E e)
	{
		init();
		
		return _collection.add(e);
	}
	
	@Override
	public final boolean addAll(Collection<? extends E> c)
	{
		init();
		
		return _collection.addAll(c);
	}
	
	@Override
	public final void clear()
	{
		_collection.clear();
	}
	
	@Override
	public final boolean contains(Object o)
	{
		return _collection.contains(o);
	}
	
	@Override
	public final boolean containsAll(Collection<?> c)
	{
		return _collection.containsAll(c);
	}
	
	@Override
	public final boolean isEmpty()
	{
		return _collection.isEmpty();
	}
	
	@Override
	public final Iterator<E> iterator()
	{
		return _collection.iterator();
	}
	
	@Override
	public final boolean remove(Object o)
	{
		return _collection.remove(o);
	}
	
	@Override
	public final boolean removeAll(Collection<?> c)
	{
		return _collection.removeAll(c);
	}
	
	@Override
	public final boolean retainAll(Collection<?> c)
	{
		return _collection.retainAll(c);
	}
	
	@Override
	public final int size()
	{
		return _collection.size();
	}
	
	@Override
	public final Object[] toArray()
	{
		return _collection.toArray();
	}
	
	@Override
	public final <T> T[] toArray(T[] a)
	{
		return _collection.toArray(a);
	}
	
	@Override
	public final String toString()
	{
		return super.toString() + "-" + _collection.toString();
	}
}
