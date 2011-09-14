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
import javolution.util.FastCollection.Record;
import javolution.util.FastMap;

import com.l2jfree.lang.L2Entity;
import com.l2jfree.util.L2Collections;

/**
 * @author NB4L1
 * @param <K>
 * @param <V>
 */
public abstract class L2EntityMap<K, V>
{
	public static abstract class L2EntityKeyRetriever<K, V>
	{
		public abstract K getKeyFromValue(V v);
		
		public static final L2EntityKeyRetriever<Object, Object> DEFAULT = new L2EntityKeyRetriever<Object, Object>() {
			@Override
			public Object getKeyFromValue(Object o)
			{
				return ((L2Entity<?>)o).getPrimaryKey();
			}
		};
		
	}
	
	private final int _initialSize;
	private final L2EntityKeyRetriever<K, V> _keyRetriever;
	
	private boolean _initialized = false;
	private Map<K, V> _map = L2Collections.emptyMap();
	
	private void init()
	{
		if (!_initialized)
		{
			synchronized (this)
			{
				if (!_initialized)
				{
					if (_initialSize < 0)
						_map = new FastMap<K, V>().setShared(this instanceof L2SharedEntityMap<?, ?>);
					else
						_map = new FastMap<K, V>(_initialSize).setShared(this instanceof L2SharedEntityMap<?, ?>);
					
					_initialized = true;
				}
			}
		}
	}
	
	protected L2EntityMap()
	{
		this(-1, null);
	}
	
	protected L2EntityMap(int initialSize)
	{
		this(initialSize, null);
	}
	
	protected L2EntityMap(L2EntityKeyRetriever<K, V> keyRetriever)
	{
		this(-1, keyRetriever);
	}
	
	@SuppressWarnings("unchecked")
	protected L2EntityMap(int initialSize, L2EntityKeyRetriever<K, V> keyRetriever)
	{
		_initialSize = initialSize;
		_keyRetriever = keyRetriever != null ? keyRetriever : (L2EntityKeyRetriever<K, V>)L2EntityKeyRetriever.DEFAULT;
	}
	
	public int size()
	{
		return _map.size();
	}
	
	public boolean isEmpty()
	{
		return _map.isEmpty();
	}
	
	public boolean contains(V obj)
	{
		return _map.containsKey(_keyRetriever.getKeyFromValue(obj));
	}
	
	public V get(K id)
	{
		return _map.get(id);
	}
	
	public void add(V obj)
	{
		init();
		
		final K primaryKey = _keyRetriever.getKeyFromValue(obj);
		final V oldValue = _map.get(primaryKey);
		
		if (oldValue == null || replace(primaryKey, oldValue, obj))
			_map.put(primaryKey, obj);
	}
	
	protected boolean replace(K primaryKey, V oldValue, V newValue)
	{
		throw new IllegalStateException("[" + oldValue + "] replaced with [" + newValue + "] - primaryKey: "
				+ primaryKey + "!");
	}
	
	public void remove(V obj)
	{
		_map.remove(_keyRetriever.getKeyFromValue(obj));
	}
	
	public void clear()
	{
		_map.clear();
	}
	
	@SuppressWarnings("unchecked")
	public V[] toArray(V[] array)
	{
		if (array.length != _map.size())
			array = (V[])Array.newInstance(array.getClass().getComponentType(), _map.size());
		
		if (_map.isEmpty() && array.length == 0)
			return array;
		else
			return _map.values().toArray(array);
	}
	
	@SuppressWarnings("unchecked")
	public V[] toArray(Class<V> clazz)
	{
		V[] array = (V[])Array.newInstance(clazz, _map.size());
		
		if (_map.isEmpty() && array.length == 0)
			return array;
		else
			return _map.values().toArray(array);
	}
	
	protected Iterator<V> iterator()
	{
		return _map.values().iterator();
	}
	
	protected void executeForEach(ForEachExecutable<V> executable)
	{
		if (_map.isEmpty())
			return;
		
		Collection<V> values = _map.values();
		
		if (values instanceof FastCollection<?>)
		{
			FastCollection<V> values2 = (FastCollection<V>)values;
			
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
