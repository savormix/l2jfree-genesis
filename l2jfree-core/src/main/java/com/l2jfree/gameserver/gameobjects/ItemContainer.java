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
package com.l2jfree.gameserver.gameobjects;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

import javolution.util.FastList;
import javolution.util.FastMap;

import com.l2jfree.gameserver.gameobjects.components.interfaces.IItemContainer;
import com.l2jfree.gameserver.gameobjects.item.L2SingularItem;
import com.l2jfree.gameserver.gameobjects.item.L2StackableItem;
import com.l2jfree.util.L2Collections;

/**
 * @author NB4L1
 */
public abstract class ItemContainer implements IItemContainer
{
	private final Map<Integer, L2Item> _itemsByObjectId = new FastMap<Integer, L2Item>();
	private final Map<Integer, L2StackableItem> _stackableItemsByItemId = new FastMap<Integer, L2StackableItem>();
	private final Map<Integer, List<L2SingularItem>> _singularItemsByItemId =
			new FastMap<Integer, List<L2SingularItem>>();
	
	private final ReentrantLock _lock = new ReentrantLock();
	
	protected ItemContainer()
	{
	}
	
	public final L2Item getItemByObjectId(int objectId)
	{
		_lock.lock();
		try
		{
			return _itemsByObjectId.get(objectId);
		}
		finally
		{
			_lock.unlock();
		}
	}
	
	public final L2Item getItemByItemId(int itemId)
	{
		_lock.lock();
		try
		{
			final L2StackableItem stackableItem = _stackableItemsByItemId.get(itemId);
			
			if (stackableItem != null)
				return stackableItem;
			
			final List<L2SingularItem> singularItemsByItemId = _singularItemsByItemId.get(itemId);
			
			if (singularItemsByItemId == null || singularItemsByItemId.isEmpty())
				return null;
			
			return singularItemsByItemId.get(0);
		}
		finally
		{
			_lock.unlock();
		}
	}
	
	public final L2StackableItem getStackableItemByItemId(int itemId)
	{
		_lock.lock();
		try
		{
			return _stackableItemsByItemId.get(itemId);
		}
		finally
		{
			_lock.unlock();
		}
	}
	
	public final L2SingularItem getSingularItemByItemId(int itemId)
	{
		_lock.lock();
		try
		{
			final List<L2SingularItem> singularItemsByItemId = _singularItemsByItemId.get(itemId);
			
			if (singularItemsByItemId == null || singularItemsByItemId.isEmpty())
				return null;
			
			return singularItemsByItemId.get(0);
		}
		finally
		{
			_lock.unlock();
		}
	}
	
	public List<L2Item> getItemsByItemId(int itemId)
	{
		_lock.lock();
		try
		{
			final L2StackableItem stackableItem = _stackableItemsByItemId.get(itemId);
			
			if (stackableItem != null)
				return Arrays.asList((L2Item)stackableItem);
			
			final List<L2SingularItem> singularItemsByItemId = _singularItemsByItemId.get(itemId);
			
			if (singularItemsByItemId == null || singularItemsByItemId.isEmpty())
				return L2Collections.emptyList();
			
			if (singularItemsByItemId.size() <= 8)
				return new CopyOnWriteArrayList<L2Item>(singularItemsByItemId);
			else
				return new FastList<L2Item>(singularItemsByItemId);
		}
		finally
		{
			_lock.unlock();
		}
	}
	
	public final List<L2SingularItem> getSingularItemsByItemId(int itemId)
	{
		_lock.lock();
		try
		{
			final List<L2SingularItem> singularItemsByItemId = _singularItemsByItemId.get(itemId);
			
			if (singularItemsByItemId == null || singularItemsByItemId.isEmpty())
				return L2Collections.emptyList();
			
			if (singularItemsByItemId.size() <= 8)
				return new CopyOnWriteArrayList<L2SingularItem>(singularItemsByItemId);
			else
				return new FastList<L2SingularItem>(singularItemsByItemId);
		}
		finally
		{
			_lock.unlock();
		}
	}
}
