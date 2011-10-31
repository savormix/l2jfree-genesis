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

import com.l2jfree.gameserver.gameobjects.CharacterStat.Element;
import com.l2jfree.gameserver.gameobjects.components.KnownListComponent;
import com.l2jfree.gameserver.gameobjects.components.empty.EmptyObjectKnownList;
import com.l2jfree.gameserver.gameobjects.components.interfaces.IElemental;
import com.l2jfree.gameserver.sql.ItemDB;
import com.l2jfree.gameserver.templates.L2ItemTemplate;
import com.l2jfree.gameserver.util.IdFactory;
import com.l2jfree.gameserver.util.IdFactory.IdRange;
import com.l2jfree.gameserver.util.PersistentId;

/**
 * @author NB4L1
 */
@SuppressWarnings("static-method")
@KnownListComponent(EmptyObjectKnownList.class)
public abstract class L2Item extends L2Object implements IElemental
{
	private PersistentId _persistentId;
	private final long _creationTime;
	private final PersistentId _ownerId;
	
	protected L2Item(L2ItemTemplate template)
	{
		super(template);
		
		// TODO
		_creationTime = System.currentTimeMillis();
		_ownerId = null;
	}
	
	protected L2Item(L2ItemTemplate template, ItemDB itemDB)
	{
		super(template);
		
		// TODO
		_creationTime = itemDB.creationTime;
		_ownerId = itemDB.getOwnerId();
	}
	
	@Override
	protected IdRange getIdRange()
	{
		return IdRange.ITEMS;
	}
	
	public PersistentId getPersistentId()
	{
		// assign persistentId only on demand (there is no need to take an ID for throw-away items - like herbs, etc)
		if (_persistentId == null)
		{
			synchronized (this)
			{
				if (_persistentId == null)
					_persistentId = IdFactory.getInstance().getNextPersistentId(IdRange.ITEMS);
			}
		}
		
		return _persistentId;
	}
	
	public long getCreationTime()
	{
		return _creationTime;
	}
	
	public PersistentId getOwnerId()
	{
		return _ownerId;
	}
	
	@Override
	public L2ItemTemplate getTemplate()
	{
		return (L2ItemTemplate)super.getTemplate();
	}
	
	@Override
	public final String getName()
	{
		return getTemplate().getName();
	}
	
	public abstract long getCount();
	
	public abstract boolean isStackable();
	
	public boolean isEquipable()
	{
		return false;
	}
	
	public boolean isEquipped()
	{
		return false;
	}
	
	public int getEnchantLevel()
	{
		return 0;
	}
	
	public boolean isNamed()
	{
		return false;
	}
	
	public int getRemainingMana()
	{
		return -1;
	}
	
	public int getRemainingTime()
	{
		return -9999;
	}
	
	@Override
	public int getAttackElementPower()
	{
		return 0;
	}
	
	@Override
	public Element getAttackElementType()
	{
		return Element.NA;
	}
	
	@Override
	public int getDefenseElementPower(Element element)
	{
		return 0;
	}
}
