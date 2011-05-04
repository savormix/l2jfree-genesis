package com.l2jfree.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javolution.util.FastMap;

public final class L2DBConnectionWrapper extends ConnectionWrapper
{
	private static final Map<StackTraceElement, Integer> CALLS = new FastMap<StackTraceElement, Integer>();
	
	private static final ThreadLocal<List<Connection>> CONNECTIONS = new ThreadLocal<List<Connection>>() {
		@Override
		protected List<Connection> initialValue()
		{
			return new ArrayList<Connection>();
		}
	};
	
	public L2DBConnectionWrapper(Connection connection)
	{
		super(connection);
		
		final List<Connection> list = CONNECTIONS.get();
		
		list.add(this);
		
		final int size = list.size();
		
		if (size > 1)
		{
			synchronized (L2DBConnectionWrapper.class)
			{
				final StackTraceElement caller = getCaller();
				
				final Integer prevValue = CALLS.get(caller);
				
				CALLS.put(caller, Math.max(size, prevValue == null ? 0 : prevValue.intValue()));
			}
		}
	}
	
	@Override
	public void close() throws SQLException
	{
		super.close();
		
		final List<Connection> list = CONNECTIONS.get();
		
		list.remove(this);
	}
	
	private static StackTraceElement getCaller()
	{
		final StackTraceElement stack[] = new Throwable().getStackTrace();
		
		for (StackTraceElement ste : stack)
		{
			if (ste.getClassName().contains("com.l2jfree.sql."))
				continue;
			
			return ste;
		}
		
		throw new InternalError();
	}
}
