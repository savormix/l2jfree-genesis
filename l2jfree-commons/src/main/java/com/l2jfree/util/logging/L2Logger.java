package com.l2jfree.util.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements an API similar to the well-known commons-logging style.
 * 
 * @author NB4L1
 */
public class L2Logger extends Logger
{
	public static L2Logger getLogger(Class<?> clazz)
	{
		return getLogger(clazz.getName());
	}
	
	public static L2Logger getLogger(String name)
	{
		return (L2Logger)Logger.getLogger(name);
	}
	
	public L2Logger(String name, String resourceBundleName)
	{
		super(name, resourceBundleName);
	}
	
	private void log0(Level level, String msg, Throwable ex)
	{
		if (isLoggable(level))
		{
			// Hack (?) to get the stack trace.
			StackTraceElement locations[] = new Throwable().getStackTrace();
			
			// Caller will be the third element
			String cname = "unknown";
			String method = "unknown";
			if (locations != null && locations.length > 2)
			{
				StackTraceElement caller = locations[2];
				cname = caller.getClassName();
				method = caller.getMethodName();
			}
			
			if (ex == null)
				logp(level, cname, method, msg);
			else
				logp(level, cname, method, msg, ex);
		}
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.FINE</code>.
	 * 
	 * @param message to log
	 */
	public void debug(Object message)
	{
		log0(Level.FINE, String.valueOf(message), null);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.FINE</code>.
	 * 
	 * @param message to log
	 * @param exception log this cause
	 */
	public void debug(Object message, Throwable exception)
	{
		log0(Level.FINE, String.valueOf(message), exception);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.SEVERE</code>.
	 * 
	 * @param message to log
	 */
	public void error(Object message)
	{
		log0(Level.SEVERE, String.valueOf(message), null);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.SEVERE</code>.
	 * 
	 * @param message to log
	 * @param exception log this cause
	 */
	public void error(Object message, Throwable exception)
	{
		log0(Level.SEVERE, String.valueOf(message), exception);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.SEVERE</code>.
	 * 
	 * @param message to log
	 */
	public void fatal(Object message)
	{
		log0(Level.SEVERE, String.valueOf(message), null);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.SEVERE</code>.
	 * 
	 * @param message to log
	 * @param exception log this cause
	 */
	public void fatal(Object message, Throwable exception)
	{
		log0(Level.SEVERE, String.valueOf(message), exception);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.INFO</code>.
	 * 
	 * @param message to log
	 */
	public void info(Object message)
	{
		log0(Level.INFO, String.valueOf(message), null);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.INFO</code>.
	 * 
	 * @param message to log
	 * @param exception log this cause
	 */
	public void info(Object message, Throwable exception)
	{
		log0(Level.INFO, String.valueOf(message), exception);
	}
	
	/**
	 * Is debug logging currently enabled?
	 */
	public boolean isDebugEnabled()
	{
		return isLoggable(Level.FINE);
	}
	
	/**
	 * Is error logging currently enabled?
	 */
	public boolean isErrorEnabled()
	{
		return isLoggable(Level.SEVERE);
	}
	
	/**
	 * Is fatal logging currently enabled?
	 */
	public boolean isFatalEnabled()
	{
		return isLoggable(Level.SEVERE);
	}
	
	/**
	 * Is info logging currently enabled?
	 */
	public boolean isInfoEnabled()
	{
		return isLoggable(Level.INFO);
	}
	
	/**
	 * Is trace logging currently enabled?
	 */
	public boolean isTraceEnabled()
	{
		return isLoggable(Level.FINEST);
	}
	
	/**
	 * Is warn logging currently enabled?
	 */
	public boolean isWarnEnabled()
	{
		return isLoggable(Level.WARNING);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.FINEST</code>.
	 * 
	 * @param message to log
	 * @see org.apache.commons.logging.Log#trace(Object)
	 */
	public void trace(Object message)
	{
		log0(Level.FINEST, String.valueOf(message), null);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.FINEST</code>.
	 * 
	 * @param message to log
	 * @param exception log this cause
	 */
	public void trace(Object message, Throwable exception)
	{
		log0(Level.FINEST, String.valueOf(message), exception);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.WARNING</code>.
	 * 
	 * @param message to log
	 */
	public void warn(Object message)
	{
		log0(Level.WARNING, String.valueOf(message), null);
	}
	
	/**
	 * Logs a message with <code>java.util.logging.Level.WARNING</code>.
	 * 
	 * @param message to log
	 * @param exception log this cause
	 */
	public void warn(Object message, Throwable exception)
	{
		log0(Level.WARNING, String.valueOf(message), exception);
	}
}
