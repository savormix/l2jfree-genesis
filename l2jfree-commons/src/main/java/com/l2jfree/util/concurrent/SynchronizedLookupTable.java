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

import java.util.Iterator;

import com.l2jfree.util.LookupTable;

/**
 * @author NB4L1
 */
public class SynchronizedLookupTable<E> extends LookupTable<E>
{
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
	public synchronized void clear(boolean force)
	{
		super.clear(force);
	}
	
	@Override
	public synchronized E get(final int key)
	{
		return super.get(key);
	}
	
	@Override
	public synchronized void set(final int key, final E newValue)
	{
		super.set(key, newValue);
	}
	
	@Override
	public synchronized Iterator<E> iterator()
	{
		return super.iterator(); // TODO implement
	}
}
