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
import java.sql.SQLException;
import java.util.ArrayDeque;

import com.l2jfree.util.logging.L2Logger;

/**
 * @author DiezelMax, NB4L1
 */
public abstract class SQLQueryQueue implements Runnable
{
	private static final L2Logger _log = L2Logger.getLogger(SQLQueryQueue.class);
	
	private final ArrayDeque<SQLQuery> _queue = new ArrayDeque<SQLQuery>();
	
	public SQLQueryQueue()
	{
	}
	
	protected abstract Connection getConnection() throws SQLException;
	
	public final void execute(SQLQuery query)
	{
		add(query);
		
		run();
	}
	
	public final void add(SQLQuery query)
	{
		if (query == null)
			return;
		
		synchronized (_queue)
		{
			_queue.addLast(query);
		}
	}
	
	private SQLQuery getNextQuery()
	{
		synchronized (_queue)
		{
			return _queue.pollFirst();
		}
	}
	
	@Override
	public final synchronized void run()
	{
		if (_queue.isEmpty())
			return;
		
		Connection con = null;
		try
		{
			con = getConnection();
			
			for (SQLQuery query; (query = getNextQuery()) != null;)
			{
				try
				{
					query.execute(con);
				}
				catch (Exception e)
				{
					_log.fatal("", e);
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
