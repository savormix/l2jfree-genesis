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
 */
public abstract class ThreadLocalObjectPool<E> extends AbstractObjectPool<E>
{
	protected ThreadLocalObjectPool()
	{
	}
	
	@Override
	public E get()
	{
		return _pools.get().get();
	}
	
	@Override
	public void store(E e)
	{
		_pools.get().store(e);
	}
	
	private final ThreadLocal<ObjectPool<E>> _pools = new ThreadLocal<ObjectPool<E>>() {
		@Override
		protected ObjectPool<E> initialValue()
		{
			return new ObjectPool<E>(false) {
				@Override
				protected int getMaximumSize()
				{
					return ThreadLocalObjectPool.this.getMaximumSize();
				}
				
				@Override
				protected long getMaxLifeTime()
				{
					return ThreadLocalObjectPool.this.getMaxLifeTime();
				}
				
				@Override
				protected void reset(E e)
				{
					ThreadLocalObjectPool.this.reset(e);
				}
				
				@Override
				protected E create()
				{
					return ThreadLocalObjectPool.this.create();
				}
			};
		}
	};
}
