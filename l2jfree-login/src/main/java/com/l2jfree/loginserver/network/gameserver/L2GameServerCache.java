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
package com.l2jfree.loginserver.network.gameserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javolution.util.FastMap;

import com.l2jfree.loginserver.network.legacy.L2GameServer;
import com.l2jfree.loginserver.network.legacy.L2LegacyConnections;
import com.l2jfree.loginserver.network.legacy.L2NoServiceReason;
import com.l2jfree.loginserver.network.legacy.packets.sendable.LoginServerFail;
import com.l2jfree.sql.L2Database;
import com.l2jfree.util.concurrent.L2ThreadPool;
import com.l2jfree.util.logging.L2Logger;

/**
 * This class is designed to allow login server to function eternally.
 * @author savormix
 */
public class L2GameServerCache
{
	private static final L2Logger _log = L2Logger.getLogger(L2GameServerCache.class);
	
	private final FastMap<Integer, L2GameServerView> _gameServers;
	private final Object _authorizationLock;
	private boolean _loaded;
	
	private L2GameServerCache()
	{
		_gameServers = FastMap.newInstance();
		_gameServers.setShared(true);
		_authorizationLock = new Object();
		_loaded = false;
		
		load();
	}
	
	private synchronized L2GameServerCache load()
	{
		if (isLoaded())
			return this;
		
		Connection con = null;
		try
		{
			con = L2Database.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT id FROM gameserver");
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				Integer id = rs.getInt("id");
				getGameServers().put(id, new L2OfflineGameServerView(id));
			}
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			_log.error("Could not retrieve registered game server data!", e);
		}
		finally
		{
			L2Database.close(con);
		}
		
		L2ThreadPool.scheduleAtFixedRate(new Updater(), 45000, 15000);
		
		_loaded = true;
		return this;
	}
	
	/**
	 * Returns possibly out-of-date empty views of all registered game servers.
	 * @return empty registered game server views
	 */
	public Collection<L2GameServerView> getRegisteredGameServers()
	{
		return getGameServers().values();
	}
	
	private FastMap<Integer, L2GameServerView> getGameServers()
	{
		return _gameServers;
	}
	
	/**
	 * Returns the object onto which game server list related threads
	 * must lock before attempting to authorize a game server.
	 * @return the game server authorization lock
	 */
	public Object getAuthorizationLock()
	{
		return _authorizationLock;
	}
	
	private boolean isLoaded()
	{
		return _loaded;
	}
	
	private class Updater implements Runnable
	{
		@Override
		public void run()
		{
			Set<GameServer> valid;
			
			Connection con = null;
			try
			{
				con = L2Database.getConnection();
				PreparedStatement ps = con.prepareStatement("SELECT id, authData FROM gameserver");
				ResultSet rs = ps.executeQuery();
				
				valid = new TreeSet<GameServer>();
				while (rs.next())
				{
					GameServer gs = new GameServer(rs.getInt("id"), rs.getString("authData"));
					valid.add(gs); // a registered server
					// updates are quite rare, so stalling is OK
					synchronized (getAuthorizationLock())
					{
						if (getGameServers().containsKey(gs.getId())) // cached
						{
							L2GameServer lgs = L2LegacyConnections.getInstance().getById(gs.getId());
							if (lgs != null && !gs.getAuth().equals(lgs.getAuth()))
							{
								// invalid authorization, impostor online
								lgs.close(new LoginServerFail(L2NoServiceReason.NOT_AUTHED));
								_log.info("Connection with game server on ID " + gs.getId() +
										" has been terminated: no longer authorized!");
							}
						}
						else // not cached
						{
							getGameServers().put(gs.getId(),
									new L2OfflineGameServerView(gs.getId()));
							_log.info("Added a game server on ID " + gs.getId() +
									" to cache. Now it will be shown in the server list even if offline.");
						}
					}
				}
				
				rs.close();
				ps.close();
			}
			catch (SQLException e)
			{
				_log.error("Could not retrieve registered game server data!", e);
				return;
			}
			finally
			{
				L2Database.close(con);
			}
			
			// Let's see if cache is up-to-date
			Object[] cached = getGameServers().keySet().toArray();
			for (Object id : cached)
			{
				if (!valid.remove(new GameServer(id))) // was registered before this update
				{
					// updates are quite rare, so stalling is OK
					synchronized (getAuthorizationLock())
					{
						getGameServers().remove(id);
						_log.info("Removed game server on ID " + id +
								" from cache. It will no longer be shown in the server list.");
						L2GameServer lgs = L2LegacyConnections.getInstance().getById((Integer) id);
						if (lgs != null && lgs.getAuth() != null) // was authorized based on old data
						{
							lgs.close(new LoginServerFail(L2NoServiceReason.NOT_AUTHED));
							_log.info("Connection with game server on ID " + id +
									" has been terminated: no longer authorized!");
						}
						// otherwise login server assigned this ID temporarily
					}
				}
			}
		}
	}
	
	private static class GameServer implements Comparable<GameServer>
	{
		private final Integer _id;
		private final String _auth;
		
		private GameServer(Integer id, String auth)
		{
			_id = id;
			_auth = auth;
		}
		
		private GameServer(Object integer)
		{
			this((Integer) integer, null);
		}
		
		public Integer getId()
		{
			return _id;
		}
		
		public String getAuth()
		{
			return _auth;
		}
		
		@Override
		public int compareTo(GameServer gs)
		{
			final int id;
			if (gs == null)
				id = 0;
			else
				id = gs.getId();
			return getId() - id;
		}
	}
	
	/**
	 * Returns a singleton object.
	 * @return an instance of this class
	 */
	public static L2GameServerCache getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder
	{
		public static final L2GameServerCache INSTANCE = new L2GameServerCache();
	}
}
