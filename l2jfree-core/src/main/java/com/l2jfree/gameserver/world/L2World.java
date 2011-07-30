package com.l2jfree.gameserver.world;

import com.l2jfree.gameserver.gameobjects.L2Object;
import com.l2jfree.util.concurrent.L2EntityMap;
import com.l2jfree.util.concurrent.L2ReadWriteEntityMap;
import com.l2jfree.util.logging.L2Logger;

public final class L2World
{
	private static final L2Logger _log = L2Logger.getLog(L2World.class);
	
	public static final int MAP_MIN_X = -327680; // ((10 - 19) - 1) * 32768
	public static final int MAP_MAX_X = 229376; // ((26 - 19)) * 32768
	public static final int MAP_MIN_Y = -262144; // ((10 - 17) - 1) * 32768
	public static final int MAP_MAX_Y = 294912; // ((26 - 17)) * 32768
	//public static final int MAP_MIN_Z = -32768;
	//public static final int MAP_MAX_Z = 32767;
	
	public static final int SHIFT_BY = 12;
	//public static final int SHIFT_BY_FOR_Z = 9;
	
	public static final int OFFSET_X = Math.abs(MAP_MIN_X >> SHIFT_BY);
	public static final int OFFSET_Y = Math.abs(MAP_MIN_Y >> SHIFT_BY);
	//public static final int OFFSET_Z = Math.abs(MAP_MIN_Z >> SHIFT_BY_FOR_Z);
	
	public static final int REGIONS_X = (MAP_MAX_X >> SHIFT_BY) + OFFSET_X;
	public static final int REGIONS_Y = (MAP_MAX_Y >> SHIFT_BY) + OFFSET_Y;
	//public static final int REGIONS_Z = (MAP_MAX_Z >> SHIFT_BY_FOR_Z) + OFFSET_Z;
	
	private static final L2WorldRegion[][] _worldRegions;
	
	/**
	 * Gracia border Flying objects not allowed to the east of it.
	 */
	public static final int GRACIA_MAX_X = -166168;
	public static final int GRACIA_MAX_Z = 6105;
	public static final int GRACIA_MIN_Z = -895;
	
	static
	{
		_worldRegions = new L2WorldRegion[REGIONS_X + 1][REGIONS_Y + 1];
		
		for (int tileX = 0; tileX <= REGIONS_X; tileX++)
			for (int tileY = 0; tileY <= REGIONS_Y; tileY++)
				_worldRegions[tileX][tileY] = new L2WorldRegion(tileX, tileY);
		
		for (int tileX = 0; tileX <= REGIONS_X; tileX++)
			for (int tileY = 0; tileY <= REGIONS_Y; tileY++)
				_worldRegions[tileX][tileY].initSurroundingRegions();
		
		_log.info("L2World: L2WorldRegions (" + (REGIONS_X + 1) + " x " + (REGIONS_Y + 1) + ") initialized.");
	}
	
	/**
	 * @param x coordinate
	 * @param y coordinate
	 * @return a region at the given coordinates
	 * @throws ArrayIndexOutOfBoundsException if there is no valid region at the given coordinates
	 */
	public static L2WorldRegion getRegion(int x, int y)
	{
		return _worldRegions[(x >> SHIFT_BY) + OFFSET_X][(y >> SHIFT_BY) + OFFSET_Y];
	}
	
	/**
	 * @param tileX
	 * @param tileY
	 * @return a region if the parameters define a valid region, null otherwise
	 */
	public static L2WorldRegion getValidRegion(int tileX, int tileY)
	{
		if (0 <= tileX && tileX <= REGIONS_X && 0 <= tileY && tileY <= REGIONS_Y)
			return _worldRegions[tileX][tileY];
		
		return null;
	}
	
	/**
	 * Contains all the objects in the world.
	 */
	private static final L2EntityMap<L2Object> _objects = new L2ReadWriteEntityMap<L2Object>(50000);
	
	public static void addObject(L2Object obj)
	{
		_objects.add(obj);
	}
	
	public static void removeObject(L2Object obj)
	{
		_objects.remove(obj);
	}
	
	public static L2Object findObject(Integer objectId)
	{
		return _objects.get(objectId);
	}
	
	public static <T> T findObject(Class<T> clazz, Integer objectId)
	{
		return clazz.cast(findObject(objectId));
	}
	
	/**
	 * @return a thread-safe copy of the objects in the world
	 */
	public static L2Object[] getObjects()
	{
		return _objects.toArray(L2Object.class);
	}
}
