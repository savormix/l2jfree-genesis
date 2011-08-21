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
package com.l2jfree.network.mmocore;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastMap.Entry;

import com.l2jfree.util.concurrent.L2ThreadPool;

/**
 * A flood-protected logger, which collects log entries, and flushes them periodically by grouping
 * similar entries, in order to save up resources.
 * 
 * @author NB4L1
 */
public final class MMOLogger
{
	private static final class MMOLogEntry
	{
		private final Level _level;
		private final String _className;
		private final String _methodName;
		private final String _message;
		private final Throwable _throwable;
		
		private MMOLogEntry(Level level, String className, String methodName, String message, Throwable throwable)
		{
			_level = level;
			_className = className;
			_methodName = methodName;
			_message = message;
			_throwable = throwable;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (!(obj instanceof MMOLogEntry))
				return false;
			
			final MMOLogEntry entry = (MMOLogEntry)obj;
			
			return equals(_level, entry._level) // Level
					&& equals(_className, entry._className) // String
					&& equals(_methodName, entry._methodName) // String
					&& equals(_message, entry._message) // String
					&& equals(_throwable, entry._throwable); // Throwable
		}
		
		private static boolean equals(Throwable t1, Throwable t2)
		{
			if (t1 == t2)
				return true;
			
			if (t1 == null || t2 == null)
				return false;
			
			return equals(t1.getCause(), t2.getCause()) // Throwable
					&& equals(t1.getClass(), t2.getClass()) // Class
					&& equals(t1.toString(), t2.toString()) // String
					&& equals(t1.getMessage(), t2.getMessage()) // String
					&& equals(t1.getLocalizedMessage(), t2.getLocalizedMessage()) // String
					&& Arrays.equals(t1.getStackTrace(), t2.getStackTrace()); // StackTraceElement[]
		}
		
		private static boolean equals(Object o1, Object o2)
		{
			if (o1 == o2)
				return true;
			
			if (o1 == null || o2 == null)
				return false;
			
			return o1.equals(o2);
		}
		
		@Override
		public int hashCode()
		{
			return hashCode(_level) // Level
					+ hashCode(_className) // String
					+ hashCode(_methodName) // String
					+ hashCode(_message) // String
					+ hashCode(_throwable); // Throwable
		}
		
		private static int hashCode(Throwable t)
		{
			if (t == null)
				return 0;
			
			return hashCode(t.getCause()) // Throwable
					+ hashCode(t.getClass()) // Class
					+ hashCode(t.toString()) // String
					+ hashCode(t.getMessage()) // String
					+ hashCode(t.getLocalizedMessage()) // String
					+ Arrays.hashCode(t.getStackTrace()); // StackTraceElement[]
		}
		
		private static int hashCode(Object o)
		{
			if (o == null)
				return 0;
			
			return o.hashCode();
		}
	}
	
	private final FastMap<MMOLogEntry, Integer> _logEntries = new FastMap<MMOLogEntry, Integer>();
	private final Logger _logger;
	
	public MMOLogger(Class<?> clazz, int flushInterval)
	{
		_logger = Logger.getLogger(clazz.getName());
		
		L2ThreadPool.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run()
			{
				flush();
			}
		}, flushInterval, flushInterval);
	}
	
	public void flush()
	{
		for (;;)
		{
			final MMOLogEntry entry;
			final Integer count;
			
			synchronized (_logEntries)
			{
				final Entry<MMOLogEntry, Integer> first = _logEntries.head().getNext();
				
				if (first == _logEntries.tail())
					break;
				
				entry = first.getKey();
				count = first.getValue();
				
				_logEntries.remove(entry);
			}
			
			final Level level = entry._level;
			final String className = entry._className;
			final String methodName = entry._methodName;
			final String message = entry._message;
			final Throwable throwable = entry._throwable;
			
			if (count == 1)
				_logger.logp(level, className, methodName, message, throwable);
			else
				_logger.logp(level, className, methodName, count + " x " + message, throwable);
		}
	}
	
	private void log(Level level, String message, Throwable throwable)
	{
		if (_logger.isLoggable(level))
		{
			final StackTraceElement caller = new Throwable().getStackTrace()[2];
			
			final String className = caller.getClassName();
			final String methodName = caller.getMethodName();
			
			final MMOLogEntry entry = new MMOLogEntry(level, className, methodName, message, throwable);
			
			synchronized (_logEntries)
			{
				final Integer count = _logEntries.get(entry);
				
				_logEntries.put(entry, count == null ? 1 : count + 1);
			}
		}
	}
	
	public void debug(Object message)
	{
		log(Level.FINE, String.valueOf(message), null);
	}
	
	public void debug(Object message, Throwable throwable)
	{
		log(Level.FINE, String.valueOf(message), throwable);
	}
	
	public void error(Object message)
	{
		log(Level.SEVERE, String.valueOf(message), null);
	}
	
	public void error(Object message, Throwable throwable)
	{
		log(Level.SEVERE, String.valueOf(message), throwable);
	}
	
	public void fatal(Object message)
	{
		log(Level.SEVERE, String.valueOf(message), null);
	}
	
	public void fatal(Object message, Throwable throwable)
	{
		log(Level.SEVERE, String.valueOf(message), throwable);
	}
	
	public void info(Object message)
	{
		log(Level.INFO, String.valueOf(message), null);
	}
	
	public void info(Object message, Throwable throwable)
	{
		log(Level.INFO, String.valueOf(message), throwable);
	}
	
	public void trace(Object message)
	{
		log(Level.FINEST, String.valueOf(message), null);
	}
	
	public void trace(Object message, Throwable throwable)
	{
		log(Level.FINEST, String.valueOf(message), throwable);
	}
	
	public void warn(Object message)
	{
		log(Level.WARNING, String.valueOf(message), null);
	}
	
	public void warn(Object message, Throwable throwable)
	{
		log(Level.WARNING, String.valueOf(message), throwable);
	}
	
	public boolean isDebugEnabled()
	{
		return _logger.isLoggable(Level.FINE);
	}
	
	public boolean isErrorEnabled()
	{
		return _logger.isLoggable(Level.SEVERE);
	}
	
	public boolean isFatalEnabled()
	{
		return _logger.isLoggable(Level.SEVERE);
	}
	
	public boolean isInfoEnabled()
	{
		return _logger.isLoggable(Level.INFO);
	}
	
	public boolean isTraceEnabled()
	{
		return _logger.isLoggable(Level.FINEST);
	}
	
	public boolean isWarnEnabled()
	{
		return _logger.isLoggable(Level.WARNING);
	}
}
