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

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of {@link AbstractListBunch} backed by an {@link ArrayList}.
 * 
 * @author NB4L1
 */
public final class ArrayBunch<E> extends AbstractListBunch<E>
{
	@Override
	protected List<E> initList()
	{
		return L2Collections.newArrayList();
	}
	
	@Override
	protected void recycleList(List<E> list)
	{
		L2Collections.recycle((ArrayList<E>)list);
	}
}
