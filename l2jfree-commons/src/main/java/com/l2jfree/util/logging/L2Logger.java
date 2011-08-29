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
package com.l2jfree.util.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Implements an API similar to the well-known commons-logging style.
 * 
 * @author NB4L1
 */
public final class L2Logger
{
	public static L2Logger getLogger(Class<?> clazz)
	{
		return new L2Logger(clazz.getName());
	}
	
	public static L2Logger getLogger(String name)
	{
		return new L2Logger(name);
	}
	
	private final Logger _logger;
	
	private L2Logger(String name)
	{
		_logger = Logger.getLogger(name);
	}
	
	/**
	 * Log a LogRecord.
	 * 
	 * @param record the LogRecord to be published
	 */
	public void log(LogRecord record)
	{
		_logger.log(record);
	}
	
	private void log0(Level level, StackTraceElement caller, Object message, Throwable exception)
	{
		if (!_logger.isLoggable(level))
			return;
		
		if (caller == null)
			caller = new Throwable().getStackTrace()[2];
		
		_logger.logp(level, caller.getClassName(), caller.getMethodName(), String.valueOf(message), exception);
	}
	
	/**
	 * Logs a message.
	 * 
	 * @param level log at this level
	 * @param message to log
	 */
	public void log(Level level, Object message)
	{
		log0(level, null, message, null);
	}
	
	/**
	 * Logs a message.
	 * 
	 * @param level log at this level
	 * @param message to log
	 * @param exception log this cause
	 */
	public void log(Level level, Object message, Throwable exception)
	{
		log0(level, null, message, exception);
	}
	
	/**
	 * Logs a message.
	 * 
	 * @param level log at this level
	 * @param caller location of code that issued the logging request
	 * @param message to log
	 */
	public void logp(Level level, StackTraceElement caller, Object message)
	{
		log0(level, caller, message, null);
	}
	
	/**
	 * Logs a message.
	 * 
	 * @param level log at this level
	 * @param caller location of code that issued the logging request
	 * @param message to log
	 * @param exception log this cause
	 */
	public void logp(Level level, StackTraceElement caller, Object message, Throwable exception)
	{
		log0(level, caller, message, exception);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.FINE</code>.
	 * 
	 * @param message to log
	 */
	public void debug(Object message)
	{
		log0(Level.FINE, null, message, null);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.FINE</code>.
	 * 
	 * @param message to log
	 * @param exception log this cause
	 */
	public void debug(Object message, Throwable exception)
	{
		log0(Level.FINE, null, message, exception);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.SEVERE</code>.
	 * 
	 * @param message to log
	 */
	public void error(Object message)
	{
		log0(Level.SEVERE, null, message, null);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.SEVERE</code>.
	 * 
	 * @param message to log
	 * @param exception log this cause
	 */
	public void error(Object message, Throwable exception)
	{
		log0(Level.SEVERE, null, message, exception);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.SEVERE</code>.
	 * 
	 * @param message to log
	 */
	public void fatal(Object message)
	{
		log0(Level.SEVERE, null, message, null);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.SEVERE</code>.
	 * 
	 * @param message to log
	 * @param exception log this cause
	 */
	public void fatal(Object message, Throwable exception)
	{
		log0(Level.SEVERE, null, message, exception);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.INFO</code>.
	 * 
	 * @param message to log
	 */
	public void info(Object message)
	{
		log0(Level.INFO, null, message, null);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.INFO</code>.
	 * 
	 * @param message to log
	 * @param exception log this cause
	 */
	public void info(Object message, Throwable exception)
	{
		log0(Level.INFO, null, message, exception);
	}
	
	/**
	 * Is logging currently enabled at the given level?
	 * 
	 * @param level the level to check
	 * @return boolean
	 */
	public boolean isLoggable(Level level)
	{
		return _logger.isLoggable(level);
	}
	
	/**
	 * Is debug logging currently enabled?
	 * 
	 * @return boolean
	 */
	public boolean isDebugEnabled()
	{
		return isLoggable(Level.FINE);
	}
	
	/**
	 * Is error logging currently enabled?
	 * 
	 * @return boolean
	 */
	public boolean isErrorEnabled()
	{
		return isLoggable(Level.SEVERE);
	}
	
	/**
	 * Is fatal logging currently enabled?
	 * 
	 * @return boolean
	 */
	public boolean isFatalEnabled()
	{
		return isLoggable(Level.SEVERE);
	}
	
	/**
	 * Is info logging currently enabled?
	 * 
	 * @return boolean
	 */
	public boolean isInfoEnabled()
	{
		return isLoggable(Level.INFO);
	}
	
	/**
	 * Is trace logging currently enabled?
	 * 
	 * @return boolean
	 */
	public boolean isTraceEnabled()
	{
		return isLoggable(Level.FINEST);
	}
	
	/**
	 * Is warn logging currently enabled?
	 * 
	 * @return boolean
	 */
	public boolean isWarnEnabled()
	{
		return isLoggable(Level.WARNING);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.FINEST</code>.
	 * 
	 * @param message to log
	 */
	public void trace(Object message)
	{
		log0(Level.FINEST, null, message, null);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.FINEST</code>.
	 * 
	 * @param message to log
	 * @param exception log this cause
	 */
	public void trace(Object message, Throwable exception)
	{
		log0(Level.FINEST, null, message, exception);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.WARNING</code>.
	 * 
	 * @param message to log
	 */
	public void warn(Object message)
	{
		log0(Level.WARNING, null, message, null);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.WARNING</code>.
	 * 
	 * @param message to log
	 * @param exception log this cause
	 */
	public void warn(Object message, Throwable exception)
	{
		log0(Level.WARNING, null, message, exception);
	}
}
