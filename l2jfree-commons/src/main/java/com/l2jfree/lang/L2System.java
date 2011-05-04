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

import java.util.concurrent.TimeUnit;

/**
 * @author NB4L1
 */
public final class L2System
{
	private L2System()
	{
	}
	
	private static final long ZERO = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1);
	
	public static long milliTime()
	{
		return System.currentTimeMillis() - ZERO;
	}
	
	/**
	 * If you are TOTALLY sure, that your server has got constant cpu clock speed,<br>
	 * then you can use the more accurate nano based time measurement.<br>
	 * <br>
	 * In case just remove the comment from below, and add around the method above.
	 */
	/*
	private static final long ZERO = System.nanoTime() - TimeUnit.DAYS.toNanos(1);
	
	public static long milliTime()
	{
		return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - ZERO);
	}
	*/

	/**
	 * Copy of HashMap.hash().
	 */
	public static int hash(int h)
	{
		h ^= (h >>> 20) ^ (h >>> 12);
		return h ^ (h >>> 7) ^ (h >>> 4);
	}
	
	public static boolean equals(Object o1, Object o2)
	{
		return o1 == null ? o2 == null : o1.equals(o2);
	}
}
