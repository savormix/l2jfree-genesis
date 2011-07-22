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

import javolution.util.FastMap;

import com.l2jfree.sql.L2Database;
import com.l2jfree.util.concurrent.L2ThreadPool;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author savormix
 *
 */
public class L2GameServerCache
{
	private static final L2Logger _log = L2Logger.getLogger(L2GameServerCache.class);
	
	private final FastMap<Integer, L2GameServerView> _gameServers;
	private boolean _loaded;
	
	private L2GameServerCache()
	{
		_gameServers = FastMap.newInstance();
		_loaded = false;
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
		
		L2ThreadPool.scheduleAtFixedRate(new Updater(), 60000, 90000);
		
		_loaded = true;
		return this;
	}
	
	/**
	 * Returns all registered game server empty views.
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
	
	private boolean isLoaded()
	{
		return _loaded;
	}
	
	private class Updater implements Runnable
	{
		@Override
		public void run()
		{
			// No removal, because a server might be online.
			Connection con = null;
			try
			{
				con = L2Database.getConnection();
				PreparedStatement ps = con.prepareStatement("SELECT id FROM gameserver");
				ResultSet rs = ps.executeQuery();
				while (rs.next())
				{
					Integer id = rs.getInt("id");
					if (!getGameServers().containsKey(id))
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
	
	private static class SingletonHolder
	{
		private static final L2GameServerCache INSTANCE = new L2GameServerCache().load();
	}
}
