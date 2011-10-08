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
package com.l2jfree.gameserver.util;

import java.util.ArrayList;

import com.l2jfree.gameserver.gameobjects.L2Object;
import com.l2jfree.util.L2Collections;
import com.l2jfree.util.L2FastSet;
import com.l2jfree.util.concurrent.AbstractPeriodicTaskManager;
import com.l2jfree.util.concurrent.FIFOSimpleExecutableQueue;
import com.l2jfree.util.concurrent.RunnableStatsManager;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author NB4L1
 */
public final class MovementController extends AbstractPeriodicTaskManager
{
	private static final L2Logger _log = L2Logger.getLogger(MovementController.class);
	
	private static final class SingletonHolder
	{
		public static final MovementController INSTANCE = new MovementController();
	}
	
	public static MovementController getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private final L2FastSet<L2Object> _movingObjects = new L2FastSet<L2Object>().setShared(true);
	
	private final MovementRevalidator _movementRevalidator = new MovementRevalidator();
	private final MovementFinisher _movementFinisher = new MovementFinisher();
	
	private MovementController()
	{
		super(100);
	}
	
	public void startMovement(L2Object obj)
	{
		_movingObjects.add(obj);
	}
	
	public void stopMovement(L2Object obj)
	{
		_movingObjects.remove(obj);
		_movementRevalidator.remove(obj);
		_movementFinisher.remove(obj);
	}
	
	@Override
	public void run()
	{
		final ArrayList<L2Object> arrivedObjects = L2Collections.newArrayList();
		
		for (L2Object obj : _movingObjects)
			obj.getMovement().updatePosition();
		
		for (L2Object obj : _movingObjects)
			if (obj.getMovement().isArrived())
				arrivedObjects.add(obj);
		
		// remove all arrived objects from moving
		_movingObjects.removeAll(arrivedObjects);
		
		// execute movementFinished related - possibly bigger tasks - asynchronously for all arrived object
		_movementFinisher.executeAll(arrivedObjects);
		
		// execute revalidateMovement related - possibly bigger tasks - asynchronously for every moving object
		_movementRevalidator.executeAll(_movingObjects);
		
		L2Collections.recycle(arrivedObjects);
	}
	
	private final class MovementRevalidator extends FIFOSimpleExecutableQueue<L2Object>
	{
		@Override
		protected void removeAndExecuteAll()
		{
			for (L2Object obj; (obj = removeFirst()) != null;)
			{
				final long begin = System.nanoTime();
				
				try
				{
					obj.getMovement().revalidateMovement();
				}
				catch (RuntimeException e)
				{
					_log.warn("Exception in movement revalidation:", e);
				}
				finally
				{
					RunnableStatsManager.handleStats(obj.getClass(), "revalidateMovement()", System.nanoTime() - begin);
				}
			}
		}
	}
	
	private final class MovementFinisher extends FIFOSimpleExecutableQueue<L2Object>
	{
		@Override
		protected void removeAndExecuteAll()
		{
			for (L2Object obj; (obj = removeFirst()) != null;)
			{
				final long begin = System.nanoTime();
				
				try
				{
					obj.getMovement().movementFinished();
				}
				catch (RuntimeException e)
				{
					_log.warn("Exception in finishing movement:", e);
				}
				finally
				{
					RunnableStatsManager.handleStats(obj.getClass(), "movementFinished()", System.nanoTime() - begin);
				}
			}
		}
	}
	
}
