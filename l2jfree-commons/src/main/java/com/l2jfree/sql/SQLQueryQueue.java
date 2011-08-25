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
package com.l2jfree.sql;

import java.sql.Connection;

import com.l2jfree.util.concurrent.FIFOSimpleExecutableQueue;
import com.l2jfree.util.concurrent.RunnableStatsManager;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author DiezelMax, NB4L1
 */
public final class SQLQueryQueue extends FIFOSimpleExecutableQueue<SQLQuery>
{
	private static final L2Logger _log = L2Logger.getLogger(SQLQueryQueue.class);
	
	private static final class SingletonHolder
	{
		public static final SQLQueryQueue INSTANCE = new SQLQueryQueue();
	}
	
	public static SQLQueryQueue getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private SQLQueryQueue()
	{
		// singleton
	}
	
	@Override
	protected void removeAndExecuteAll()
	{
		Connection con = null;
		try
		{
			con = L2Database.getConnection();
			
			for (SQLQuery query; (query = removeFirst()) != null;)
			{
				final long begin = System.nanoTime();
				
				try
				{
					query.execute(con);
				}
				catch (RuntimeException e)
				{
					_log.warn("Exception in a SQLQuery execution:", e);
				}
				finally
				{
					RunnableStatsManager
							.handleStats(query.getClass(), "execute(Connection)", System.nanoTime() - begin);
				}
			}
		}
		catch (Exception e)
		{
			_log.fatal("", e);
		}
		finally
		{
			L2Database.close(con);
		}
	}
}
