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
package com.l2jfree.gameserver.sql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.l2jfree.gameserver.gameobjects.L2Item;
import com.l2jfree.gameserver.gameobjects.item.ItemFactory;
import com.l2jfree.gameserver.util.PersistentId;
import com.l2jfree.sql.L2DBEntity;
import com.l2jfree.sql.L2Database;

/**
 * @author NB4L1
 */
@Entity
@Table(name = "items")
public class ItemDB extends L2DBEntity
{
	@Id
	@Column(name = "persistentId", nullable = false, updatable = false)
	private Integer persistentId;
	
	@Column(name = "creationTime", nullable = false, updatable = false)
	public long creationTime;
	
	@Column(name = "count", nullable = false)
	public long count;
	
	public PersistentId getPersistentId()
	{
		return new PersistentId(persistentId);
	}
	
	public void setPersistentId(PersistentId persistentId)
	{
		this.persistentId = persistentId.intValue();
	}
	
	@Override
	public Object getPrimaryKey()
	{
		return persistentId;
	}
	
	@Override
	protected Class<?> getClassForEqualsCheck()
	{
		return ItemDB.class;
	}
	
	public static L2Item load(PersistentId persistentId)
	{
		try
		{
			final ItemDB itemDB = ItemDB.find(persistentId);
			
			if (itemDB == null)
				return null;
			
			return ItemFactory.restoreItem(itemDB);
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static void store(L2Item item)
	{
		try
		{
			final ItemDB itemDB = new ItemDB();
			itemDB.setPersistentId(item.getPersistentId());
			itemDB.creationTime = item.getCreationTime();
			itemDB.count = item.getCount();
			
			L2Database.merge(itemDB);
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
		}
	}
	
	public static ItemDB find(PersistentId persistentId)
	{
		return L2Database.find(ItemDB.class, persistentId.intValue());
	}
}
