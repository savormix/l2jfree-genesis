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
package com.l2jfree.gameserver.world;

import java.util.ArrayList;

import com.l2jfree.gameserver.gameobjects.IL2Playable;
import com.l2jfree.gameserver.gameobjects.L2Object;
import com.l2jfree.util.L2Arrays;
import com.l2jfree.util.concurrent.ExclusiveTask;
import com.l2jfree.util.concurrent.FIFOSimpleExecutableQueue;
import com.l2jfree.util.concurrent.L2EntityMap;
import com.l2jfree.util.concurrent.L2ReadWriteEntityMap;

public final class L2WorldRegion
{
	private final int _tileX;
	private final int _tileY;
	
	public L2WorldRegion(int tileX, int tileY)
	{
		_tileX = tileX;
		_tileY = tileY;
	}
	
	/**
	 * Contains the surrounding regions - including this one.<br>
	 * The regions are ordered: self, regions with direct contact, diagonal regions.<br>
	 * Useful for gathering objects nearby.
	 */
	private L2WorldRegion[] _surroundingRegions = new L2WorldRegion[0];
	
	public void initSurroundingRegions()
	{
		final ArrayList<L2WorldRegion> tmp = new ArrayList<L2WorldRegion>();
		
		// first self
		tmp.add(L2World.getRegion(_tileX + 0, _tileY + 0));
		
		// then regions with direct contact
		tmp.add(L2World.getRegion(_tileX - 1, _tileY + 0));
		tmp.add(L2World.getRegion(_tileX + 1, _tileY + 0));
		tmp.add(L2World.getRegion(_tileX + 0, _tileY - 1));
		tmp.add(L2World.getRegion(_tileX + 0, _tileY + 1));
		
		// finally diagonal regions
		tmp.add(L2World.getRegion(_tileX - 1, _tileY - 1));
		tmp.add(L2World.getRegion(_tileX + 1, _tileY + 1));
		tmp.add(L2World.getRegion(_tileX + 1, _tileY - 1));
		tmp.add(L2World.getRegion(_tileX - 1, _tileY + 1));
		
		_surroundingRegions = L2Arrays.compact(tmp.toArray(new L2WorldRegion[tmp.size()]));
	}
	
	/**
	 * @return the surrounding regions - including this one. Useful for gathering objects nearby.
	 */
	public L2WorldRegion[] getSurroundingRegions()
	{
		return _surroundingRegions;
	}
	
	/**
	 * Contains all the objects in this region.
	 */
	private final L2EntityMap<L2Object> _objects = new L2ReadWriteEntityMap<L2Object>();
	private final L2EntityMap<IL2Playable> _playables = new L2ReadWriteEntityMap<IL2Playable>();
	
	/**
	 * @return a thread-safe copy of the objects in this region
	 */
	public L2Object[] getObjects()
	{
		return _objects.toArray(L2Object.class);
	}
	
	public IL2Playable[] getPlayables()
	{
		return _playables.toArray(IL2Playable.class);
	}
	
	public void addObject(L2Object object)
	{
		if (object == null)
			return;
		
		_objects.add(object);
		
		if (object instanceof IL2Playable)
		{
			_playables.add((IL2Playable)object);
			
			if (_playables.size() == 1)
			{
				_deactivationTask.cancel();
				
				for (L2WorldRegion region : getSurroundingRegions())
					region.setActive(true);
			}
		}
	}
	
	public void removeObject(L2Object object)
	{
		if (object == null)
			return;
		
		_objects.remove(object);
		
		if (object instanceof IL2Playable)
		{
			_playables.remove((IL2Playable)object);
			
			if (_playables.isEmpty())
			{
				_deactivationTask.schedule(90000);
			}
		}
	}
	
	/**
	 * The task responsible for deactivating surrounding regions.
	 */
	private final DeactivationTask _deactivationTask = new DeactivationTask();
	
	private final class DeactivationTask extends ExclusiveTask
	{
		@Override
		protected void onElapsed()
		{
			for (L2WorldRegion region : getSurroundingRegions())
				region.setActive(false);
		}
	}
	
	/**
	 * Indicates whether the region is active, or ot is in a dormant state.
	 */
	private volatile boolean _active = false;
	
	/**
	 * @return true, if the region active, false, if it is in a dormant state
	 */
	public boolean isActive()
	{
		return _active;
	}
	
	private synchronized void setActive(boolean active)
	{
		// it won't allow to deactive, while there are still players nearby
		if (!active)
		{
			for (L2WorldRegion region : getSurroundingRegions())
				if (!region._playables.isEmpty())
					active = true;
		}
		
		if (_active == active)
			return;
		
		_active = active;
		
		if (active)
		{
			for (L2Object obj : getObjects())
				if (obj != null)
					obj.getPosition().worldRegionActivated();
		}
		else
		{
			for (L2Object obj : getObjects())
				if (obj != null)
					obj.getPosition().worldRegionDeactivated();
		}
	}
	
	public L2Object[][] getAllSurroundingObjects2DArray()
	{
		final L2Object[][] result = new L2Object[_surroundingRegions.length][];
		
		for (int i = 0; i < _surroundingRegions.length; i++)
			result[i] = _surroundingRegions[i].getObjects();
		
		return result;
	}
	
	private final FIFOSimpleExecutableQueue<L2Object> _knownListUpdater = new FIFOSimpleExecutableQueue<L2Object>() {
		@Override
		protected void removeAndExecuteAll()
		{
			final L2Object[][] surroundingObjects = getAllSurroundingObjects2DArray();
			
			for (L2Object obj; (obj = removeFirst()) != null;)
				obj.getKnownList().update(surroundingObjects);
		}
	};
	
	public void updateKnownList(L2Object obj)
	{
		_knownListUpdater.execute(obj);
	}
}
