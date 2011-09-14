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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.BitSet;

import org.apache.commons.lang3.StringUtils;

import com.l2jfree.gameserver.sql.PlayerDB;
import com.l2jfree.sql.L2Database;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author NB4L1
 */
public final class IdFactory
{
	private static final L2Logger _log = L2Logger.getLogger(IdFactory.class);
	
	private static final class SingletonHolder
	{
		static
		{
			try
			{
				INSTANCE = new IdFactory();
			}
			catch (Exception e)
			{
				throw new Error(e);
			}
		}
		
		public static final IdFactory INSTANCE;
	}
	
	public static IdFactory getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static final String[] REMOVE_LEFTOVER_QUERIES = { };
	private static final String[] REMOVE_EXPIRED_QUERIES = { };
	
	// even if they could overlap theoretically without issue, let's ensure they don't :)
	public static enum IdRange
	{
		// TODO
		CLANS(20 * 1000000, 40 * 1000000),
		PLAYERS(50 * 1000000, 100 * 1000000, "players.persistentId"),
		ITEMS(200 * 1000000, 400 * 1000000),
		MISC(500 * 1000000, 1000 * 1000000);
		
		private final int _minimumAllowedId;
		private final int _maximumAllowedId;
		private final String[] _tablesAndColumns;
		
		private IdRange(int minimumAllowedId, int maximumAllowedId, String... tablesAndColumns)
		{
			_minimumAllowedId = minimumAllowedId;
			_maximumAllowedId = maximumAllowedId;
			_tablesAndColumns = tablesAndColumns;
		}
		
		public String[] getTablesAndColumns()
		{
			return _tablesAndColumns;
		}
		
		public int toBitIndex(int id)
		{
			return id - _minimumAllowedId;
		}
		
		public int toId(int bitIndex)
		{
			return _minimumAllowedId + bitIndex;
		}
		
		public boolean isInRange(int id, String action)
		{
			if (_minimumAllowedId <= id && id <= _maximumAllowedId)
				return true;
			
			_log.warn(getClass().getSimpleName() + ": " + action + " of ID: " + id
					+ " failed, because it's out of range [" + _minimumAllowedId + ".." + _maximumAllowedId + "}");
			return false;
		}
	}
	
	private final RangedPersistentIdFactory[] _rangedPersistentIdFactories;
	private final RangedObjectIdFactory[] _rangedObjectIdFactories;
	
	private IdFactory() throws SQLException
	{
		PlayerDB.setOfflineAll();
		
		removeLeftover();
		removeExpired();
		
		_rangedPersistentIdFactories = new RangedPersistentIdFactory[IdRange.values().length];
		for (IdRange idRange : IdRange.values())
			_rangedPersistentIdFactories[idRange.ordinal()] = new RangedPersistentIdFactory(idRange);
		
		_rangedObjectIdFactories = new RangedObjectIdFactory[IdRange.values().length];
		for (IdRange idRange : IdRange.values())
			_rangedObjectIdFactories[idRange.ordinal()] = new RangedObjectIdFactory(idRange);
		
		_log.info("IdFactory: Initialized.");
	}
	
	private static void removeLeftover()
	{
		int removed = 0;
		
		Connection con = null;
		try
		{
			con = L2Database.getConnection();
			
			final Statement st = con.createStatement();
			
			for (String query : REMOVE_LEFTOVER_QUERIES)
			{
				removed += st.executeUpdate(query);
			}
			
			st.close();
		}
		catch (SQLException e)
		{
			_log.warn("", e);
		}
		finally
		{
			L2Database.close(con);
		}
		
		_log.info("IdFactory: Removed " + removed + " leftover entries from database.");
	}
	
	private static void removeExpired()
	{
		int removed = 0;
		
		Connection con = null;
		try
		{
			con = L2Database.getConnection();
			
			for (String query : REMOVE_EXPIRED_QUERIES)
			{
				final PreparedStatement ps = con.prepareStatement(query);
				ps.setLong(1, System.currentTimeMillis());
				
				removed += ps.executeUpdate();
				
				ps.close();
			}
		}
		catch (SQLException e)
		{
			_log.warn("", e);
		}
		finally
		{
			L2Database.close(con);
		}
		
		_log.info("IdFactory: Removed " + removed + " expired entries from database.");
	}
	
	private static abstract class RangedIdFactory
	{
		protected final IdRange _idRange;
		
		protected final BitSet _occupiedBits = new BitSet();
		protected volatile int _lastReturnedBitIndex = 0;
		
		private RangedIdFactory(IdRange idRange)
		{
			_idRange = idRange;
		}
		
		public synchronized final int getNextId()
		{
			final int bitIndex = _occupiedBits.nextClearBit(_lastReturnedBitIndex);
			final int id = _idRange.toId(bitIndex);
			
			if (!_idRange.isInRange(id, "requesting"))
			{
				// FIXME there are no more free ids
			}
			
			_occupiedBits.set(bitIndex);
			_lastReturnedBitIndex = Math.max(bitIndex, _occupiedBits.length() - 1);
			return id;
		}
	}
	
	// LOW compaction capability
	private static final class RangedPersistentIdFactory extends RangedIdFactory
	{
		private RangedPersistentIdFactory(IdRange idRange) throws SQLException
		{
			super(idRange);
			
			int loaded = 0;
			
			Connection con = null;
			try
			{
				con = L2Database.getConnection();
				
				final Statement st = con.createStatement();
				
				for (String tablesAndColumns : _idRange.getTablesAndColumns())
				{
					final String[] split = StringUtils.split(tablesAndColumns, ".", 2);
					
					final String table = split[0];
					final String column = split[1];
					
					final ResultSet rs = st.executeQuery("SELECT " + column + " FROM " + table);
					
					while (rs.next())
					{
						final int id = rs.getInt(1);
						
						loadId(id);
						
						loaded++;
					}
					
					rs.close();
				}
				
				st.close();
			}
			finally
			{
				L2Database.close(con);
			}
			
			_log.info("IdFactory: Loaded " + loaded + " " + _idRange.name().replaceFirst("S$", "")
					+ " IDs from database.");
		}
		
		private synchronized void loadId(final int id)
		{
			if (!_idRange.isInRange(id, "loading"))
				return;
			
			final int bitIndex = _idRange.toBitIndex(id);
			
			if (_occupiedBits.get(bitIndex))
			{
				_log.warn("IdFactory: loading of ID: " + id + " failed, because it's already loaded");
				return;
			}
			
			_occupiedBits.set(bitIndex);
			_lastReturnedBitIndex = Math.max(bitIndex, _occupiedBits.length() - 1);
		}
	}
	
	private static final class RangedObjectIdFactory extends RangedIdFactory
	{
		private RangedObjectIdFactory(IdRange idRange)
		{
			super(idRange);
		}
		
		public synchronized void releaseId(final int id)
		{
			if (!_idRange.isInRange(id, "releasing"))
				return;
			
			final int bitIndex = _idRange.toBitIndex(id);
			
			if (!_occupiedBits.get(bitIndex))
			{
				_log.warn("IdFactory: releasing of ID: " + id + " failed, because it's already free");
				return;
			}
			
			_occupiedBits.clear(bitIndex);
			// _lastReturnedBitIndex is unmodified for a purpose -> we don't want to reuse this id yet
			// this is done only for the - almost impossible occasion - that we run out if IDs
			// so we have to restart from beginning :)
		}
	}
	
	public PersistentId getNextPersistentId(IdRange range)
	{
		return new PersistentId(_rangedPersistentIdFactories[range.ordinal()].getNextId());
	}
	
	public ObjectId getNextObjectId(IdRange range)
	{
		return new ObjectId(_rangedObjectIdFactories[range.ordinal()].getNextId());
	}
	
	public void releaseObjectId(IdRange range, ObjectId objectId)
	{
		_rangedObjectIdFactories[range.ordinal()].releaseId(objectId.intValue());
	}
}
