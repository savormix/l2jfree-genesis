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
import com.l2jfree.util.L2Arrays;
import com.l2jfree.util.concurrent.ExclusiveTask;
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
	
	/**
	 * @return a thread-safe copy of the objects in this region
	 */
	public L2Object[] getObjects()
	{
		return _objects.toArray(L2Object.class);
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
	 * When a player enters an empty region, it activates all the surrounding ones.
	 */
	private void startActivation()
	{
		_deactivationTask.cancel();
		
		for (L2WorldRegion region : getSurroundingRegions())
			region.setActive(true);
	}
	
	/**
	 * When the last player leaves a region, schedules a task to deactive the empty surrounding regions after a cooldown period.
	 */
	private void startDeactivation()
	{
		_deactivationTask.schedule(90000);
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
	
	private void setActive(boolean active)
	{
		// it won't allow to deactive, while there are still players nearby
		if (!active)
		{
			for (L2WorldRegion region : getSurroundingRegions())
				if (/* TODO check if a player present */true)
					active = true;
		}
		
		if (_active == active)
			return;
		
		_active = active;
		
		if (active)
		{
			for (L2Object obj : getObjects())
			{
				if (obj == null)
					continue;
				
				// TODO
			}
		}
		else
		{
			for (L2Object obj : getObjects())
			{
				if (obj == null)
					continue;
				
				// TODO
			}
		}
	}
}
