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

import com.l2jfree.gameserver.gameobjects.L2Object;
import com.l2jfree.gameserver.gameobjects.ObjectPosition;
import com.l2jfree.gameserver.gameobjects.interfaces.IL2Playable;
import com.l2jfree.gameserver.util.ObjectId;
import com.l2jfree.util.ArrayBunch;
import com.l2jfree.util.L2Arrays;
import com.l2jfree.util.concurrent.ExclusiveTask;
import com.l2jfree.util.concurrent.FIFOSimpleExecutableQueue;
import com.l2jfree.util.concurrent.L2EntityMap;
import com.l2jfree.util.concurrent.L2ReadWriteEntityMap;
import com.l2jfree.util.concurrent.RunnableStatsManager;
import com.l2jfree.util.logging.L2Logger;

public final class L2WorldRegion
{
	private static final L2Logger _log = L2Logger.getLogger(L2WorldRegion.class);
	
	private final int _tileX;
	private final int _tileY;
	
	protected L2WorldRegion(int tileX, int tileY)
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
	
	protected void initSurroundingRegions()
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
	private L2WorldRegion[] getSurroundingRegions()
	{
		return _surroundingRegions;
	}
	
	/**
	 * Contains all the objects in this region, when they are actually visible.<br>
	 * It also means objects are removed during teleport.
	 */
	private final L2EntityMap<ObjectId, L2Object> _objects = new L2ReadWriteEntityMap<ObjectId, L2Object>();
	private final L2EntityMap<ObjectId, IL2Playable> _playables = new L2ReadWriteEntityMap<ObjectId, IL2Playable>();
	
	/**
	 * @return a thread-safe copy of the objects in this region
	 */
	private L2Object[] getVisibleObjects()
	{
		return _objects.toArray(L2Object.class);
	}
	
	private IL2Playable[] getVisiblePlayables()
	{
		return _playables.toArray(IL2Playable.class);
	}
	
	public synchronized void addVisibleObject(L2Object object)
	{
		if (object instanceof IL2Playable)
		{
			if (_playables.isEmpty())
			{
				_deactivationTask.cancel();
				
				for (L2WorldRegion region : getSurroundingRegions())
					region.setActive(true);
			}
			
			_playables.add((IL2Playable)object);
		}
		
		_objects.add(object);
	}
	
	public synchronized void removeVisibleObject(L2Object object)
	{
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
		
		final L2Object[] visibleObject = getVisibleObjects();
		
		if (active)
		{
			// fill up knownlists first
			final L2Object[][] surroundingObjects = getAllSurroundingVisibleObjects2DArray();
			
			for (L2Object obj : visibleObject)
				if (obj != null)
					obj.getKnownList().updateSurroundingObjects(surroundingObjects);
			
			// call object specific activation method
			for (L2Object obj : visibleObject)
				if (obj != null)
					obj.getPosition().worldRegionActivated();
		}
		else
		{
			// call object specific deactivation method
			for (L2Object obj : visibleObject)
				if (obj != null)
					obj.getPosition().worldRegionDeactivated();
			
			// and finally clear knownlists
			for (L2Object obj : visibleObject)
				if (obj != null)
					obj.getKnownList().removeAllKnownObjects();
		}
	}
	
	private L2Object[][] getAllSurroundingVisibleObjects2DArray()
	{
		final L2Object[][] result = new L2Object[_surroundingRegions.length][];
		
		for (int i = 0; i < _surroundingRegions.length; i++)
			result[i] = _surroundingRegions[i].getVisibleObjects();
		
		return result;
	}
	
	private final FIFOSimpleExecutableQueue<L2Object> _knownListUpdater = new FIFOSimpleExecutableQueue<L2Object>() {
		@Override
		protected void removeAndExecuteAll()
		{
			final L2Object[][] surroundingObjects = getAllSurroundingVisibleObjects2DArray();
			
			for (L2Object obj; (obj = removeFirst()) != null;)
			{
				final long begin = System.nanoTime();
				
				try
				{
					obj.getKnownList().updateSurroundingObjects(surroundingObjects);
				}
				catch (RuntimeException e)
				{
					_log.warn("Exception in a KnownList update!", e);
				}
				finally
				{
					RunnableStatsManager.handleStats(obj.getClass(), "updateKnownList()", System.nanoTime() - begin);
				}
			}
		}
	};
	
	public void updateKnownList(L2Object obj, boolean force)
	{
		if (force)
			obj.getKnownList().updateSurroundingObjects(getAllSurroundingVisibleObjects2DArray());
		else
			_knownListUpdater.execute(obj);
	}
	
	public static L2Object[] getVisibleObjectsAround2D(L2Object object, long radius)
	{
		if (object == null)
			return L2Object.EMPTY_ARRAY;
		
		final ObjectPosition objectPosition = object.getPosition();
		final L2WorldRegion selfRegion = objectPosition.getWorldRegion();
		
		if (selfRegion == null)
			return L2Object.EMPTY_ARRAY;
		
		final long x = objectPosition.getX();
		final long y = objectPosition.getY();
		final long sqRadius = radius * radius;
		
		final ArrayBunch<L2Object> result = new ArrayBunch<L2Object>();
		
		for (L2WorldRegion region : selfRegion.getSurroundingRegions())
		{
			for (L2Object obj : region.getVisibleObjects())
			{
				if (obj == null)
					continue;
				
				final ObjectPosition objPosition = obj.getPosition();
				
				if (obj == object || !objPosition.isVisible())
					continue;
				
				final long dx = objPosition.getX() - x;
				final long dy = objPosition.getY() - y;
				
				if (dx * dx + dy * dy > sqRadius)
					continue;
				
				result.add(obj);
			}
		}
		
		return result.moveToArray(new L2Object[result.size()]);
	}
	
	public static L2Object[] getVisibleObjectsAround(L2Object object, long radius)
	{
		if (object == null)
			return L2Object.EMPTY_ARRAY;
		
		final ObjectPosition objectPosition = object.getPosition();
		final L2WorldRegion selfRegion = objectPosition.getWorldRegion();
		
		if (selfRegion == null)
			return L2Object.EMPTY_ARRAY;
		
		final long x = objectPosition.getX();
		final long y = objectPosition.getY();
		final long z = objectPosition.getZ();
		final long sqRadius = radius * radius;
		
		final ArrayBunch<L2Object> result = new ArrayBunch<L2Object>();
		
		for (L2WorldRegion region : selfRegion.getSurroundingRegions())
		{
			for (L2Object obj : region.getVisibleObjects())
			{
				if (obj == null)
					continue;
				
				final ObjectPosition objPosition = obj.getPosition();
				
				if (obj == object || !objPosition.isVisible())
					continue;
				
				final long dx = objPosition.getX() - x;
				final long dy = objPosition.getY() - y;
				final long dz = objPosition.getZ() - z;
				
				if (dx * dx + dy * dy + dz * dz > sqRadius)
					continue;
				
				result.add(obj);
			}
		}
		
		return result.moveToArray(new L2Object[result.size()]);
	}
	
	public static IL2Playable[] getVisiblePlayablesAround(L2Object object)
	{
		if (object == null)
			return IL2Playable.EMPTY_ARRAY;
		
		final L2WorldRegion selfRegion = object.getPosition().getWorldRegion();
		
		if (selfRegion == null)
			return IL2Playable.EMPTY_ARRAY;
		
		final ArrayBunch<IL2Playable> result = new ArrayBunch<IL2Playable>();
		
		for (L2WorldRegion region : selfRegion.getSurroundingRegions())
		{
			for (IL2Playable obj : region.getVisiblePlayables())
			{
				if (obj == null || obj == object || !obj.getPosition().isVisible())
					continue;
				
				result.add(obj);
			}
		}
		
		return result.moveToArray(new IL2Playable[result.size()]);
	}
}
