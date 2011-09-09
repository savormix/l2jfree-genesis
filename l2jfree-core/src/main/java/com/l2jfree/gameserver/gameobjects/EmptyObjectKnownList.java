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

import java.util.Collection;

import com.l2jfree.util.L2Collections;

/**
 * @author NB4L1
 */
public class EmptyObjectKnownList implements IObjectKnownList
{
	public EmptyObjectKnownList(@SuppressWarnings("unused") L2Object activeChar)
	{
		// do nothing
	}
	
	@Override
	public Collection<L2Object> getKnownObjects()
	{
		return L2Collections.emptyCollection();
	}
	
	@Override
	public Collection<L2Player> getKnownPlayers()
	{
		return L2Collections.emptyCollection();
	}
	
	@Override
	public Collection<L2Object> getKnowingObjects()
	{
		return L2Collections.emptyCollection();
	}
	
	@Override
	public boolean removeObject(L2Object activeChar)
	{
		// do nothing
		return false;
	}
	
	@Override
	public void addKnowingObject(L2Object activeChar)
	{
		// do nothing
	}
	
	@Override
	public void removeKnowingObject(L2Object activeChar)
	{
		// do nothing
	}
	
	@Override
	public void update(L2Object activeChar)
	{
		// do nothing
	}
	
	@Override
	public void update(L2Object[][] surroundingObjects)
	{
		// do nothing
	}
}
