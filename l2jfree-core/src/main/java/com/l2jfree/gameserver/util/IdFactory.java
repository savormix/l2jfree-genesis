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

import com.l2jfree.sql.L2Database;
import com.l2jfree.util.concurrent.L2ThreadPool;
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
		PLAYERS(50 * 1000000, 100 * 1000000),
		ITEMS(200 * 1000000, 400 * 1000000),
		TEMPORARY(500 * 1000000, 1000 * 1000000);
		
		private final int _minimumAllowedObjectId;
		private final int _maximumAllowedObjectId;
		private final String[] _tables;
		
		private IdRange(int minimumAllowedObjectId, int maximumAllowedObjectId, String... tables)
		{
			_minimumAllowedObjectId = minimumAllowedObjectId;
			_maximumAllowedObjectId = maximumAllowedObjectId;
			_tables = tables;
		}
		
		public String[] getTables()
		{
			return _tables;
		}
		
		public int toBitIndex(int objectId)
		{
			return objectId - _minimumAllowedObjectId;
		}
		
		public int toObjectId(int bitIndex)
		{
			return _minimumAllowedObjectId + bitIndex;
		}
		
		public boolean isInRange(int objectId, String action)
		{
			if (_minimumAllowedObjectId <= objectId && objectId <= _maximumAllowedObjectId)
				return true;
			
			_log.warn("IdFactory: " + action + " of objectId: " + objectId + " failed, because it's out of range ["
					+ _minimumAllowedObjectId + ".." + _maximumAllowedObjectId + "}");
			return false;
		}
	}
	
	private final RangedIdFactory[] _rangedIdFactories = new RangedIdFactory[IdRange.values().length];
	
	private IdFactory() throws SQLException
	{
		// FIXME
		//L2Database.executeUpdate("UPDATE players SET online = 0");
		
		removeLeftover();
		removeExpired();
		
		for (IdRange idRange : IdRange.values())
			_rangedIdFactories[idRange.ordinal()] = new RangedIdFactory(idRange);
		
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
	
	private static final class RangedIdFactory
	{
		private final IdRange _idRange;
		
		private final BitSet _occupiedBits = new BitSet();
		private volatile int _lastReturnedBitIndex = 0;
		
		private RangedIdFactory(IdRange idRange) throws SQLException
		{
			_idRange = idRange;
			
			Connection con = null;
			try
			{
				con = L2Database.getConnection();
				
				final Statement st = con.createStatement();
				
				for (String table : _idRange.getTables())
				{
					final ResultSet rs = st.executeQuery("SELECT objectId FROM " + table);
					
					while (rs.next())
					{
						final int objectId = rs.getInt(1);
						
						loadId(objectId);
					}
					
					rs.close();
				}
				
				st.close();
			}
			finally
			{
				L2Database.close(con);
			}
			
			L2ThreadPool.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run()
				{
					resetLastReturnedBitIndex();
				}
			}, 60000, 60000);
		}
		
		private synchronized void loadId(final int objectId)
		{
			if (!_idRange.isInRange(objectId, "loading"))
				return;
			
			final int bitIndex = _idRange.toBitIndex(objectId);
			
			if (_occupiedBits.get(bitIndex))
			{
				_log.warn("IdFactory: loading of objectId: " + objectId + " failed, because it's already loaded");
				return;
			}
			
			_occupiedBits.set(bitIndex);
		}
		
		public synchronized void releaseId(final int objectId)
		{
			if (!_idRange.isInRange(objectId, "releasing"))
				return;
			
			final int bitIndex = _idRange.toBitIndex(objectId);
			
			if (!_occupiedBits.get(bitIndex))
			{
				_log.warn("IdFactory: releasing of objectId: " + objectId + " failed, because it's already free");
				return;
			}
			
			_occupiedBits.clear(bitIndex);
		}
		
		public synchronized int getNextId()
		{
			final int bitIndex = _occupiedBits.nextClearBit(_lastReturnedBitIndex);
			
			_occupiedBits.set(bitIndex);
			_lastReturnedBitIndex = bitIndex;
			
			final int objectId = _idRange.toObjectId(bitIndex);
			
			if (!_idRange.isInRange(objectId, "requesting"))
			{
				// FIXME
			}
			
			return objectId;
		}
		
		public synchronized void resetLastReturnedBitIndex()
		{
			// to ensure it uses released ids too :P
			_lastReturnedBitIndex = 0;
		}
	}
	
	public int getNextId(IdRange range)
	{
		return _rangedIdFactories[range.ordinal()].getNextId();
	}
	
	public void releaseId(IdRange range, int objectId)
	{
		_rangedIdFactories[range.ordinal()].releaseId(objectId);
	}
}
