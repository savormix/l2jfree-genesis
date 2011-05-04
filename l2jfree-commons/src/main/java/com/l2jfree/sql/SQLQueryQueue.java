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

import javolution.util.FastList;

import com.l2jfree.util.logging.L2Logger;

/**
 * @author DiezelMax, NB4L1
 */
public abstract class SQLQueryQueue implements Runnable
{
	private static final L2Logger _log = L2Logger.getLogger(SQLQueryQueue.class);
	
	private final FastList<SQLQuery> _queue = new FastList<SQLQuery>();
	
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
		synchronized (_queue)
		{
			_queue.add(query);
		}
	}
	
	private SQLQuery getNextQuery()
	{
		synchronized (_queue)
		{
			if (_queue.isEmpty())
				return null;
			
			return _queue.removeFirst();
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
			L2DB.close(con);
		}
	}
}
