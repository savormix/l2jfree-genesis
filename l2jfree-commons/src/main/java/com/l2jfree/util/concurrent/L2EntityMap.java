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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javolution.util.FastCollection;
import javolution.util.FastMap;
import javolution.util.FastCollection.Record;

import com.l2jfree.lang.L2Entity;
import com.l2jfree.util.L2Collections;

/**
 * @author NB4L1
 */
public abstract class L2EntityMap<T extends L2Entity<Integer>>
{
	private final int _initialSize;
	
	private boolean _initialized = false;
	private Map<Integer, T> _map = L2Collections.emptyMap();
	
	private void init()
	{
		if (!_initialized)
		{
			synchronized (this)
			{
				if (!_initialized)
				{
					if (_initialSize < 0)
						_map = new FastMap<Integer, T>().setShared(this instanceof L2SharedEntityMap<?>);
					else
						_map = new FastMap<Integer, T>(_initialSize).setShared(this instanceof L2SharedEntityMap<?>);
					
					_initialized = true;
				}
			}
		}
	}
	
	protected L2EntityMap()
	{
		this(-1);
	}
	
	protected L2EntityMap(int initialSize)
	{
		_initialSize = initialSize;
	}
	
	public int size()
	{
		return _map.size();
	}
	
	public boolean isEmpty()
	{
		return _map.isEmpty();
	}
	
	public boolean contains(T obj)
	{
		return _map.containsKey(obj.getPrimaryKey());
	}
	
	public T get(Integer id)
	{
		return _map.get(id);
	}
	
	public void add(T obj)
	{
		init();
		
		_map.put(obj.getPrimaryKey(), obj);
	}
	
	public void remove(T obj)
	{
		_map.remove(obj.getPrimaryKey());
	}
	
	public void clear()
	{
		_map.clear();
	}
	
	@SuppressWarnings("unchecked")
	public T[] toArray(T[] array)
	{
		if (array.length != _map.size())
			array = (T[])Array.newInstance(array.getClass().getComponentType(), _map.size());
		
		if (_map.isEmpty() && array.length == 0)
			return array;
		else
			return _map.values().toArray(array);
	}
	
	@SuppressWarnings("unchecked")
	public T[] toArray(Class<T> clazz)
	{
		T[] array = (T[])Array.newInstance(clazz, _map.size());
		
		if (_map.isEmpty() && array.length == 0)
			return array;
		else
			return _map.values().toArray(array);
	}
	
	protected Iterator<T> iterator()
	{
		return _map.values().iterator();
	}
	
	protected void executeForEach(ForEachExecutable<T> executable)
	{
		if (_map.isEmpty())
			return;
		
		Collection<T> values = _map.values();
		
		if (values instanceof FastCollection<?>)
		{
			FastCollection<T> values2 = (FastCollection<T>)values;
			
			for (Record r = values2.head(), end = values2.tail(); (r = r.getNext()) != end;)
				executable.execute(values2.valueOf(r));
		}
		else
			throw new RuntimeException("Shouldn't happen!");
	}
	
	@Override
	public final String toString()
	{
		return super.toString() + "-" + _map.toString();
	}
}
