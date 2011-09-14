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
import com.l2jfree.gameserver.util.PersistentId;
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
	
	private final Map<PersistentId, PlayerInfo> _mapByPersistentId = new FastMap<PersistentId, PlayerInfo>()
			.setShared(true);
	private final Map<String, PlayerInfo> _mapByName = new FastMap<String, PlayerInfo>().setShared(true);
	
	public static class PlayerInfoWrapper
	{
		private final PersistentId _persistentId;
		private final String _accountName;
		private final String _name;
		
		public PlayerInfoWrapper(Integer persistentId, String accountName, String name)
		{
			_persistentId = new PersistentId(persistentId);
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
							root.get("persistentId"), root.get("accountName"), root.get("name")));
					
					for (PlayerInfoWrapper p : em.createQuery(criteria).getResultList())
					{
						final int accessLevel = 0; // p.accessLevel; // TODO
						
						update(p._persistentId, p._accountName, p._name, accessLevel);
					}
					
					return null;
				}
			});
		}
		catch (RuntimeException e)
		{
			_log.warn("", e);
		}
		
		_log.info("PlayerNameTable: Loaded " + _mapByPersistentId.size() + " player infos.");
	}
	
	public String getNameByPersistentId(PersistentId persistentId)
	{
		final PlayerInfo playerInfo = _mapByPersistentId.get(persistentId);
		
		return playerInfo == null ? null : playerInfo._name;
	}
	
	public String getNameByName(String name)
	{
		final PlayerInfo playerInfo = _mapByName.get(name.toLowerCase());
		
		return playerInfo == null ? null : playerInfo._name;
	}
	
	public PersistentId getPersistentIdByName(String name)
	{
		final PlayerInfo playerInfo = _mapByName.get(name.toLowerCase());
		
		return playerInfo == null ? null : playerInfo._persistentId;
	}
	
	public int getAccessLevelByPersistentId(PersistentId persistentId)
	{
		final PlayerInfo playerInfo = _mapByPersistentId.get(persistentId);
		
		return playerInfo == null ? 0 : playerInfo._accessLevel;
	}
	
	public int getAccessLevelByName(String name)
	{
		final PlayerInfo playerInfo = _mapByName.get(name.toLowerCase());
		
		return playerInfo == null ? 0 : playerInfo._accessLevel;
	}
	
	public void update(L2Player player)
	{
		update(player.getPersistentId(), player.getAccountName(), player.getName(), player.getAccessLevel());
	}
	
	public void update(PersistentId persistentId, String accountName, String name, int accessLevel)
	{
		PlayerInfo playerInfo = _mapByPersistentId.get(persistentId);
		if (playerInfo == null)
			playerInfo = new PlayerInfo(persistentId);
		
		playerInfo.updateNames(accountName, name, accessLevel);
	}
	
	public IPlayerInfo getIPlayerInfoByPersistentId(PersistentId persistentId)
	{
		final L2Player player = L2World.findPlayerByPersistentId(persistentId);
		
		if (player != null)
			return player;
		
		return _mapByPersistentId.get(persistentId);
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
		public PersistentId getPersistentId();
		
		public String getAccountName();
		
		public String getName();
		
		public int getAccessLevel();
		
		public boolean isGM();
		
		public boolean sendPacket(L2ServerPacket gsp);
	}
	
	private class PlayerInfo implements IPlayerInfo
	{
		private final PersistentId _persistentId;
		
		private String _accountName;
		private String _name;
		private int _accessLevel;
		
		@Override
		public PersistentId getPersistentId()
		{
			return _persistentId;
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
		
		private PlayerInfo(PersistentId persistentId)
		{
			_persistentId = persistentId;
			
			final PlayerInfo playerInfo = _mapByPersistentId.put(_persistentId, this);
			if (playerInfo != null)
				_log.warn("PlayerNameTable: Duplicated persistentId: [" + this + "] - [" + playerInfo + "]");
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
			return "persistentId: " + _persistentId + ", accountName: " + _accountName + ", name: " + _name;
		}
	}
	
	public boolean isPlayerNameTaken(String name)
	{
		return getPersistentIdByName(name) != null;
	}
	
	public int getPlayerCountForAccount(String account)
	{
		int count = 0;
		
		for (PlayerInfo playerInfo : _mapByPersistentId.values())
			if (playerInfo._accountName.equalsIgnoreCase(account))
				count++;
		
		return count;
	}
	
	public Iterable<PersistentId> getPersistentIdsForAccount(final String account)
	{
		return L2Collections.convertingIterable(_mapByPersistentId.values(),
				new L2Collections.Converter<PlayerInfo, PersistentId>() {
					@Override
					public PersistentId convert(PlayerInfo playerInfo)
					{
						if (playerInfo._accountName.equalsIgnoreCase(account))
							return playerInfo._persistentId;
						return null;
					}
				});
	}
}
