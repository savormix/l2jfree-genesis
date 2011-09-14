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

import java.util.Collection;

import javolution.util.FastMap;

import com.l2jfree.gameserver.gameobjects.L2Character;
import com.l2jfree.gameserver.gameobjects.L2Object;
import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.util.PersistentId;
import com.l2jfree.util.concurrent.L2EntityMap;
import com.l2jfree.util.concurrent.L2ReadWriteEntityMap;
import com.l2jfree.util.logging.L2Logger;

public final class L2World
{
	private static final L2Logger _log = L2Logger.getLogger(L2World.class);
	
	public static final int MAP_MIN_X = -327680; // ((10 - 19) - 1) * 32768
	public static final int MAP_MAX_X = 229376; // ((26 - 19)) * 32768
	public static final int MAP_MIN_Y = -262144; // ((10 - 17) - 1) * 32768
	public static final int MAP_MAX_Y = 294912; // ((26 - 17)) * 32768
	public static final int MAP_MIN_Z = -32768;
	public static final int MAP_MAX_Z = 32767;
	
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
	private static final L2EntityMap<Integer, L2Object> _objects = new L2ReadWriteEntityMap<Integer, L2Object>(50000);
	private static final FastMap<String, L2Player> _players = new FastMap<String, L2Player>(1000).setShared(true);
	private static final Collection<L2Player> _unmodifiablePlayers = _players.unmodifiable().values();
	private static final FastMap<PersistentId, L2Player> _playersByPersistentId = new FastMap<PersistentId, L2Player>(
			1000).setShared(true);
	
	// TODO check replace
	public static void addObject(L2Object obj)
	{
		_objects.add(obj);
		
		if (obj instanceof L2Player)
		{
			final L2Player player = (L2Player)obj;
			
			_players.put(player.getName().toLowerCase(), player);
			_playersByPersistentId.put(player.getPersistentId(), player);
		}
	}
	
	public static void removeObject(L2Object obj)
	{
		_objects.remove(obj);
		
		if (obj instanceof L2Player)
		{
			final L2Player player = (L2Player)obj;
			
			_players.remove(player.getName().toLowerCase());
			_playersByPersistentId.remove(player.getPersistentId());
		}
	}
	
	public static void updateOnlinePlayer(L2Player player, String oldName, String newName)
	{
		// do not add if it wasn't already added
		if (oldName == null || _players.remove(oldName.toLowerCase()) == null)
			return;
		
		_players.put(newName.toLowerCase(), player);
	}
	
	public static L2Object findObject(Integer objectId)
	{
		return _objects.get(objectId);
	}
	
	public static <T extends L2Object> T findObject(Class<T> clazz, Integer objectId)
	{
		final L2Object obj = findObject(objectId);
		
		return clazz.isInstance(obj) ? clazz.cast(obj) : null;
	}
	
	public static L2Character findCharacter(int objectId)
	{
		return findObject(L2Character.class, objectId);
	}
	
	public static L2Player findPlayer(int objectId)
	{
		return findObject(L2Player.class, objectId);
	}
	
	public static L2Player findPlayer(String name)
	{
		return _players.get(name.toLowerCase());
	}
	
	public static L2Player findPlayerByPersistentId(PersistentId persistentId)
	{
		return _playersByPersistentId.get(persistentId);
	}
	
	/**
	 * @return a thread-safe copy of the objects in the world
	 */
	public static L2Object[] getObjects()
	{
		return _objects.toArray(L2Object.class);
	}
	
	public static Collection<L2Player> getPlayers()
	{
		return _unmodifiablePlayers;
	}
}
