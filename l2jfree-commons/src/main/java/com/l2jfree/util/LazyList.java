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
import java.util.List;
import java.util.ListIterator;

import javolution.util.FastList;

/**
 * @author NB4L1
 * @param <E>
 */
public final class LazyList<E> extends LazyCollection<E, List<E>> implements List<E>
{
	@Override
	protected List<E> emptyCollection()
	{
		return L2Collections.emptyList();
	}
	
	@Override
	protected List<E> initCollection()
	{
		return FastList.newInstance();
	}
	
	@Override
	public void add(int index, E element)
	{
		init();
		
		_collection.add(index, element);
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		init();
		
		return _collection.addAll(index, c);
	}
	
	@Override
	public E get(int index)
	{
		return _collection.get(index);
	}
	
	@Override
	public int indexOf(Object o)
	{
		return _collection.indexOf(o);
	}
	
	@Override
	public int lastIndexOf(Object o)
	{
		return _collection.lastIndexOf(o);
	}
	
	@Override
	public ListIterator<E> listIterator()
	{
		return _collection.listIterator();
	}
	
	@Override
	public ListIterator<E> listIterator(int index)
	{
		return _collection.listIterator(index);
	}
	
	@Override
	public E remove(int index)
	{
		return _collection.remove(index);
	}
	
	@Override
	public E set(int index, E element)
	{
		return _collection.set(index, element);
	}
	
	@Override
	public List<E> subList(int fromIndex, int toIndex)
	{
		return _collection.subList(fromIndex, toIndex);
	}
}
