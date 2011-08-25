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
package com.l2jfree.lang;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author NB4L1
 */
// TODO check if this really gives the advantage it's supposed to
public final class L2Integer
{
	private static final ReentrantLock _lock = new ReentrantLock();
	private static final Map<Integer, Integer> _map = new HashMap<Integer, Integer>();
	
	private static final int MIN = -1000;
	private static final int MAX = 100000;
	private static final int TOTAL = MAX - MIN + 1;
	
	private static final Integer[] ARRAY = new Integer[TOTAL];
	
	static
	{
		for (int i = 0; i < TOTAL; i++)
			ARRAY[i] = MIN + i;
	}
	
	public static Integer valueOf(String s)
	{
		return valueOf(Integer.parseInt(s));
	}
	
	public static Integer valueOf(int intValue)
	{
		if (MIN <= intValue && intValue <= MAX)
			return ARRAY[intValue - MIN];
		
		final Integer integerValue = intValue;
		
		_lock.lock();
		try
		{
			final Integer cachedInteger = _map.get(integerValue);
			
			if (cachedInteger != null)
				return cachedInteger;
			
			_map.put(integerValue, integerValue);
		}
		finally
		{
			_lock.unlock();
		}
		
		return integerValue;
	}
}
