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

import java.util.Set;

/**
 * @author NB4L1
 * @param <E>
 */
public final class LazySet<E> extends LazyCollection<E, Set<E>> implements Set<E>
{
	private boolean _shared = false;
	
	@Override
	protected Set<E> emptyCollection()
	{
		return L2Collections.emptySet();
	}
	
	@Override
	protected Set<E> initCollection()
	{
		return new L2FastSet<E>().setShared(_shared);
	}
	
	public LazySet<E> setShared()
	{
		_shared = true;
		
		synchronized (this)
		{
			if (_initialized)
			{
				((L2FastSet<E>)_collection).setShared(true);
			}
		}
		
		return this;
	}
}
