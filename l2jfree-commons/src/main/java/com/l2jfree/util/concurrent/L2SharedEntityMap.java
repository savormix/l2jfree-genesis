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

import com.l2jfree.lang.L2Entity;

/**
 * @author NB4L1
 */
public final class L2SharedEntityMap<T extends L2Entity<Integer>> extends L2EntityMap<T> implements Iterable<T>,
		ForEachExecutor<T>
{
	public L2SharedEntityMap()
	{
		super();
	}
	
	public L2SharedEntityMap(int initialSize)
	{
		super(initialSize);
	}
	
	@Override
	public Iterator<T> iterator()
	{
		return super.iterator();
	}
	
	@Override
	public void executeForEach(ForEachExecutable<T> executable)
	{
		super.executeForEach(executable);
	}
}
