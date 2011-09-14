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

import com.l2jfree.lang.L2Entity;

/**
 * @author NB4L1
 * @param <K>
 * @param <V>
 */
public final class L2SynchronizedEntityMap<K, V extends L2Entity<K>> extends L2EntityMap<K, V>
{
	public L2SynchronizedEntityMap()
	{
		super();
	}
	
	public L2SynchronizedEntityMap(int initialSize)
	{
		super(initialSize);
	}
	
	@Override
	public synchronized int size()
	{
		return super.size();
	}
	
	@Override
	public synchronized boolean isEmpty()
	{
		return super.isEmpty();
	}
	
	@Override
	public synchronized boolean contains(V obj)
	{
		return super.contains(obj);
	}
	
	@Override
	public synchronized V get(K id)
	{
		return super.get(id);
	}
	
	@Override
	public synchronized void add(V obj)
	{
		super.add(obj);
	}
	
	@Override
	public synchronized void remove(V obj)
	{
		super.remove(obj);
	}
	
	@Override
	public synchronized void clear()
	{
		super.clear();
	}
	
	@Override
	public synchronized V[] toArray(V[] array)
	{
		return super.toArray(array);
	}
	
	@Override
	public synchronized V[] toArray(Class<V> clazz)
	{
		return super.toArray(clazz);
	}
}
