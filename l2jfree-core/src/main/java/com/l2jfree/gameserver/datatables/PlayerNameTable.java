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
package com.l2jfree.gameserver.datatables;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import javolution.util.FastMap;

import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.gameserver.sql.PlayerDB;
import com.l2jfree.gameserver.world.L2World;
import com.l2jfree.sql.L2Database;
import com.l2jfree.sql.L2Database.L2Query;
import com.l2jfree.util.L2Collections;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author NB4L1
 */
public final class PlayerNameTable
{
	private static final L2Logger _log = L2Logger.getLogger(PlayerNameTable.class);
	
	private static final class SingletonHolder
	{
		public static final PlayerNameTable INSTANCE = new PlayerNameTable();
	}
	
	public static PlayerNameTable getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private final Map<Integer, PlayerInfo> _mapByObjectId = new FastMap<Integer, PlayerInfo>().setShared(true);
	private final Map<String, PlayerInfo> _mapByName = new FastMap<String, PlayerInfo>().setShared(true);
	
	public static class PlayerInfoWrapper
	{
		private final int _objectId;
		private final String _accountName;
		private final String _name;
		
		public PlayerInfoWrapper(int objectId, String accountName, String name)
		{
			_objectId = objectId;
			_accountName = accountName;
			_name = name;
		}
	}
	
	private PlayerNameTable()
	{
		try
		{
			L2Database.executeQuery(new L2Query<Void>() {
				@Override
				public Void execute(EntityManager em)
				{
					final CriteriaBuilder builder = em.getCriteriaBuilder();
					final CriteriaQuery<PlayerInfoWrapper> criteria = builder.createQuery(PlayerInfoWrapper.class);
					final Root<PlayerDB> root = criteria.from(PlayerDB.class);
					
					criteria.select(builder.construct(PlayerInfoWrapper.class, //
							root.get("objectId"), root.get("accountName"), root.get("name")));
					
					for (PlayerInfoWrapper p : em.createQuery(criteria).getResultList())
					{
						final int accessLevel = 0; // p.accessLevel; // TODO
						
						update(p._objectId, p._accountName, p._name, accessLevel);
					}
					
					return null;
				}
			});
		}
		catch (RuntimeException e)
		{
			_log.warn("", e);
		}
		
		_log.info("PlayerNameTable: Loaded " + _mapByObjectId.size() + " player infos.");
	}
	
	public String getNameByObjectId(Integer objectId)
	{
		final PlayerInfo playerInfo = _mapByObjectId.get(objectId);
		
		return playerInfo == null ? null : playerInfo._name;
	}
	
	public String getNameByName(String name)
	{
		final PlayerInfo playerInfo = _mapByName.get(name.toLowerCase());
		
		return playerInfo == null ? null : playerInfo._name;
	}
	
	public Integer getObjectIdByName(String name)
	{
		final PlayerInfo playerInfo = _mapByName.get(name.toLowerCase());
		
		return playerInfo == null ? null : playerInfo._objectId;
	}
	
	public int getAccessLevelByObjectId(Integer objectId)
	{
		final PlayerInfo playerInfo = _mapByObjectId.get(objectId);
		
		return playerInfo == null ? 0 : playerInfo._accessLevel;
	}
	
	public int getAccessLevelByName(String name)
	{
		final PlayerInfo playerInfo = _mapByName.get(name.toLowerCase());
		
		return playerInfo == null ? 0 : playerInfo._accessLevel;
	}
	
	public void update(L2Player player)
	{
		update(player.getObjectId(), player.getAccountName(), player.getName(), player.getAccessLevel());
	}
	
	public void update(int objectId, String accountName, String name, int accessLevel)
	{
		PlayerInfo playerInfo = _mapByObjectId.get(objectId);
		if (playerInfo == null)
			playerInfo = new PlayerInfo(objectId);
		
		playerInfo.updateNames(accountName, name, accessLevel);
	}
	
	public IPlayerInfo getIPlayerInfoByObjectId(Integer objectId)
	{
		final L2Player player = L2World.findPlayer(objectId);
		
		if (player != null)
			return player;
		
		return _mapByObjectId.get(objectId);
	}
	
	public IPlayerInfo getIPlayerInfoByName(String name)
	{
		final L2Player player = L2World.findPlayer(name);
		
		if (player != null)
			return player;
		
		return _mapByName.get(name.toLowerCase());
	}
	
	public interface IPlayerInfo
	{
		public int getObjectId();
		
		public String getAccountName();
		
		public String getName();
		
		public int getAccessLevel();
		
		public boolean isGM();
		
		public boolean sendPacket(L2ServerPacket gsp);
	}
	
	private class PlayerInfo implements IPlayerInfo
	{
		private final int _objectId;
		
		private String _accountName;
		private String _name;
		private int _accessLevel;
		
		@Override
		public int getObjectId()
		{
			return _objectId;
		}
		
		@Override
		public String getAccountName()
		{
			return _accountName;
		}
		
		@Override
		public String getName()
		{
			return _name;
		}
		
		@Override
		public int getAccessLevel()
		{
			return _accessLevel;
		}
		
		@Override
		public boolean isGM()
		{
			return false; // FIXME
		}
		
		@Override
		public boolean sendPacket(L2ServerPacket gsp)
		{
			// do nothing
			return false;
		}
		
		private PlayerInfo(int objectId)
		{
			_objectId = objectId;
			
			final PlayerInfo playerInfo = _mapByObjectId.put(_objectId, this);
			if (playerInfo != null)
				_log.warn("PlayerNameTable: Duplicated objectId: [" + this + "] - [" + playerInfo + "]");
		}
		
		private void updateNames(String accountName, String name, int accessLevel)
		{
			_accountName = accountName;
			
			if (_name != null)
				_mapByName.remove(_name.toLowerCase());
			
			_name = name.intern();
			
			final PlayerInfo playerInfo = _mapByName.put(_name.toLowerCase(), this);
			if (playerInfo != null)
				_log.warn("PlayerNameTable: Duplicated hashName: [" + this + "] - [" + playerInfo + "]");
			
			_accessLevel = accessLevel;
		}
		
		@Override
		public String toString()
		{
			return "objectId: " + _objectId + ", accountName: " + _accountName + ", name: " + _name;
		}
	}
	
	public boolean isPlayerNameTaken(String name)
	{
		return getObjectIdByName(name) != null;
	}
	
	public int getPlayerCountForAccount(String account)
	{
		int count = 0;
		
		for (PlayerInfo playerInfo : _mapByObjectId.values())
			if (playerInfo._accountName.equalsIgnoreCase(account))
				count++;
		
		return count;
	}
	
	public Iterable<Integer> getObjectIdsForAccount(final String account)
	{
		return L2Collections.convertingIterable(_mapByObjectId.values(),
				new L2Collections.Converter<PlayerInfo, Integer>() {
					@Override
					public Integer convert(PlayerInfo playerInfo)
					{
						if (playerInfo._accountName.equalsIgnoreCase(account))
							return playerInfo._objectId;
						return null;
					}
				});
	}
}
