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

/**
 * @author NB4L1
 * @param <E>
 */
public abstract class AbstractObjectPool<E>
{
	protected AbstractObjectPool()
	{
	}
	
	@SuppressWarnings("static-method")
	protected int getMaximumSize()
	{
		return Integer.MAX_VALUE;
	}
	
	@SuppressWarnings("static-method")
	protected long getMaxLifeTime()
	{
		return 120000; // 2 min
	}
	
	public abstract void store(E e);
	
	/**
	 * @param e
	 */
	protected void reset(E e)
	{
		// do nothing at default
	}
	
	public abstract E get();
	
	protected abstract E create();
}
