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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author NB4L1
 * @param <E>
 */
public class EnumValues<E extends Enum<E>> implements Iterable<E>
{
	private final E[] _values;
	
	public EnumValues(Class<E> clazz)
	{
		_values = clazz.getEnumConstants();
	}
	
	public final int length()
	{
		return _values.length;
	}
	
	public final E get(int index)
	{
		return _values[index];
	}
	
	public final E valueOf(int index)
	{
		if (index < 0 || _values.length <= index)
			return defaultValue();
		
		return _values[index];
	}
	
	protected E defaultValue()
	{
		return null;
	}
	
	@Override
	public final Iterator<E> iterator()
	{
		return new EnumValuesIterator<E>(_values);
	}
	
	private static final class EnumValuesIterator<E> implements Iterator<E>
	{
		private final E[] _values;
		
		private int _index;
		
		private EnumValuesIterator(E[] values)
		{
			_values = values;
		}
		
		@Override
		public boolean hasNext()
		{
			return _index < _values.length;
		}
		
		@Override
		public E next()
		{
			if (!hasNext())
				throw new NoSuchElementException();
			
			return _values[_index++];
		}
		
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
}
