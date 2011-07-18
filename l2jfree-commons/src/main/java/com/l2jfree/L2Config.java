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
package com.l2jfree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.LogManager;

import javolution.text.TextBuilder;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.l2jfree.config.ConfigProperty;
import com.l2jfree.config.L2Properties;
import com.l2jfree.io.BufferedRedirectingOutputStream;
import com.l2jfree.lang.L2Math;
import com.l2jfree.lang.L2TextBuilder;
import com.l2jfree.util.HandlerRegistry;
import com.l2jfree.util.L2FastSet;
import com.l2jfree.util.logging.L2Logger;

// TODO do we need commons logging?
// TODO fill /doc folder
/**
 */
public abstract class L2Config
{
	/** Application's launch timestamp */
	public static final long SERVER_STARTED = System.currentTimeMillis();
	
	public static String getUptime()
	{
		final long uptimeInSec = (System.currentTimeMillis() - SERVER_STARTED) - 1000;
		
		final long s = uptimeInSec / 1 % 60;
		final long m = uptimeInSec / 60 % 60;
		final long h = uptimeInSec / 3600 % 24;
		final long d = uptimeInSec / 86400;
		
		final L2TextBuilder tb = L2TextBuilder.newInstance();
		
		if (d > 0)
			tb.append(d + " day(s), ");
		
		if (h > 0 || tb.length() != 0)
			tb.append(h + " hour(s), ");
		
		if (m > 0 || tb.length() != 0)
			tb.append(m + " minute(s), ");
		
		if (s > 0 || tb.length() != 0)
			tb.append(s + " second(s)");
		
		return tb.moveToString();
	}
	
	/** Logging configuration file */
	public static final String LOG_FILE = "./config/logging.properties";
	/** Telnet configuration file */
	// TODO move from here
	public static final String TELNET_FILE = "./config/telnet.properties";
	
	/**
	 * Defines the type of log entries that should be followed by a complete stack trace, regardless if an exception is attached.
	 */
	public static Level EXTENDED_LOG_LEVEL = Level.OFF;
	
	protected static final L2Logger _log;
	
	/** A stream where normal log messages are printed. */
	public static final PrintStream out = System.out;
	/** A stream where error messages are printed. */
	public static final PrintStream err = System.err;
	
	static
	{
		Locale.setDefault(Locale.ENGLISH);
		
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			/* (non-Javadoc)
			 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
			 */
			@Override
			public void uncaughtException(Thread t, Throwable e)
			{
				System.err.print("Exception in thread \"" + t.getName() + "\" ");
				
				e.printStackTrace(System.err);
				
				// restart automatically
				if (e instanceof Error && !(e instanceof StackOverflowError))
					Shutdown.halt(TerminationStatus.RUNTIME_UNCAUGHT_ERROR);
			}
		});
		
		if (System.getProperty("user.name").equals("root") && System.getProperty("user.home").equals("/root"))
		{
			System.out.print("L2jFree servers should not run under root-account ... exited.");
			Shutdown.exit(TerminationStatus.ENVIRONMENT_SUPERUSER);
		}
		
		final Map<String, List<String>> libs = new HashMap<String, List<String>>();
		
		final Set<File> files = new HashSet<File>();
		
		for (String classPath : System.getProperty("java.class.path").split(File.pathSeparator))
		{
			final File classPathFile = new File(classPath);
			
			if (classPathFile.isDirectory())
			{
				for (File f : classPathFile.listFiles())
					files.add(f);
			}
			else
				files.add(classPathFile);
		}
		
		boolean shouldExit = false;
		
		for (File f : files)
		{
			if (!f.getName().endsWith("jar"))
				continue;
			
			final TextBuilder sb = TextBuilder.newInstance();
			
			final StringTokenizer st = new StringTokenizer(f.getName(), "-");
			
			tokenizer: while (st.hasMoreTokens())
			{
				final String token = st.nextToken();
				
				boolean numberOnly = true;
				
				for (int i = 0; i < token.length(); i++)
				{
					char c = token.charAt(i);
					
					if (numberOnly && c == '.')
						break tokenizer;
					
					numberOnly &= Character.isDigit(c);
				}
				
				if (sb.length() != 0)
					sb.append("-");
				
				sb.append(token);
			}
			
			String str = sb.toString();
			TextBuilder.recycle(sb);
			List<String> list = libs.get(str);
			
			if (list == null)
				libs.put(str, list = new ArrayList<String>());
			else
				shouldExit = true;
			
			list.add(f.getName());
		}
		
		if (shouldExit)
		{
			System.out.println("Server should not run with classpath conflicts! "
					+ "(rename/remove possible conflicting classpath entries)");
			
			for (Map.Entry<String, List<String>> entry : libs.entrySet())
				if (entry.getValue().size() > 1)
					for (String name : entry.getValue())
						System.out.println("\t'" + name + "'");
			
			Shutdown.exit(TerminationStatus.ENVIRONMENT_CP_CONFLICT);
		}
		
		System.setProperty("line.separator", "\r\n");
		System.setProperty("file.encoding", "UTF-8");
		System.setProperty("java.util.logging.manager", "com.l2jfree.util.logging.L2LogManager");
		
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(LOG_FILE);
			
			LogManager.getLogManager().readConfiguration(fis);
		}
		catch (Exception e)
		{
			try
			{
				// if failed to load 'logging.properties' from config then try to load from resources
				LogManager.getLogManager().readConfiguration(
						ClassLoader.getSystemResourceAsStream("logging.properties"));
			}
			catch (Exception e1)
			{
				try
				{
					// if failed to load 'logging.properties', then load default logging parameters
					LogManager.getLogManager().readConfiguration();
				}
				catch (Exception e2)
				{
					throw new Error(e2);
				}
			}
		}
		finally
		{
			IOUtils.closeQuietly(fis);
		}
		
		_log = L2Logger.getLogger(L2Config.class);
		_log.info("logging initialized");
		
		System.setOut(new PrintStream(new BufferedRedirectingOutputStream() {
			/* (non-Javadoc)
			 * @see com.l2jfree.io.BufferedRedirectingOutputStream#handleLine(java.lang.String)
			 */
			@Override
			protected void handleLine(String line)
			{
				final StackTraceElement caller = getCaller();
				
				if (caller == null)
					_log.logp(Level.INFO, "", "", line);
				else
					_log.logp(Level.INFO, caller.getClassName(), caller.getMethodName(), line);
			}
		}));
		
		System.setErr(new PrintStream(new BufferedRedirectingOutputStream() {
			/* (non-Javadoc)
			 * @see com.l2jfree.io.BufferedRedirectingOutputStream#handleLine(java.lang.String)
			 */
			@Override
			protected void handleLine(String line)
			{
				final StackTraceElement caller = getCaller();
				
				if (caller == null)
					_log.logp(Level.WARNING, "", "", line);
				else
					_log.logp(Level.WARNING, caller.getClassName(), caller.getMethodName(), line);
			}
		}));
		
		Shutdown.initShutdownHook();
	}
	
	private static StackTraceElement getCaller()
	{
		StackTraceElement[] stack = new Throwable().getStackTrace();
		
		for (int i = stack.length - 1; i >= 0; i--)
		{
			if (stack[i].getClassName().startsWith("java.io.") || stack[i].getMethodName().equals("printStackTrace"))
				return stack[L2Math.limit(0, i + 1, stack.length - 1)];
			
			if (stack[i].getMethodName().equals("dispatchUncaughtException"))
				break;
		}
		
		return null;
	}
	
	/**
	 * @return internet addresses that are allowed to connect via telnet
	 */
	// TODO move to telnet related classes
	public static Set<String> getAllowedTelnetHostAddresses()
	{
		final Set<String> set = new HashSet<String>();
		
		try
		{
			set.add(InetAddress.getLocalHost().getHostAddress());
		}
		catch (Exception e)
		{
			_log.warn("", e);
		}
		
		try
		{
			for (String host : new L2Properties(L2Config.TELNET_FILE).getProperty("ListOfHosts").split(","))
			{
				try
				{
					set.add(InetAddress.getByName(host.trim()).getHostAddress());
				}
				catch (Exception e)
				{
					_log.warn("", e);
				}
			}
		}
		catch (Exception e)
		{
			_log.warn("", e);
		}
		
		return set;
	}
	
	// TODO make sure to fit new config scheme, and move to config related classes
	private static final HandlerRegistry<String, ConfigLoader> _loaders = new HandlerRegistry<String, ConfigLoader>(
			true) {
		@Override
		public String standardizeKey(String key)
		{
			return key.trim().toLowerCase();
		}
	};
	
	protected static void registerConfig(ConfigLoader loader)
	{
		_loaders.register(loader.getName(), loader);
	}
	
	/**
	 * Load all available configuration files.
	 * 
	 * @throws Exception if any config failed to load
	 */
	public static void loadConfigs() throws Exception
	{
		for (ConfigLoader loader : _loaders.getHandlers().values())
			loader.load();
	}
	
	/**
	 * Load the specified configuration file.
	 * 
	 * @param name Configuration name
	 * @return the outcome of this call in a string
	 * @throws Exception if the specified config could not be loaded
	 */
	public static String loadConfig(String name) throws Exception
	{
		final ConfigLoader loader = _loaders.get(name);
		
		if (loader == null)
			throw new Exception();
		
		try
		{
			loader.load();
			return "'" + loader.getName() + "' reloaded!";
		}
		catch (Exception e)
		{
			return e.getMessage();
		}
	}
	
	/**
	 * @return all available configuration names
	 */
	public static String getLoaderNames()
	{
		return StringUtils.join(_loaders.getHandlers().keySet().iterator(), "|");
	}
	
	protected static abstract class ConfigLoader
	{
		protected abstract String getName();
		
		protected abstract void load() throws Exception;
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public final int hashCode()
		{
			return getClass().hashCode();
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public final boolean equals(Object obj)
		{
			return getClass().equals(obj.getClass());
		}
	}
	
	protected static abstract class ConfigFileLoader extends ConfigLoader
	{
		protected abstract String getFileName();
		
		/* (non-Javadoc)
		 * @see com.l2jfree.L2Config.ConfigLoader#load()
		 */
		@Override
		protected final void load() throws Exception
		{
			_log.info("loading '" + getFileName() + "'");
			
			try
			{
				loadReader(new BufferedReader(new FileReader(getFileName())));
			}
			catch (Exception e)
			{
				_log.fatal("Failed to load '" + getFileName() + "'!", e);
				
				throw new Exception("Failed to load '" + getFileName() + "'!");
			}
		}
		
		protected abstract void loadReader(BufferedReader reader) throws Exception;
	}
	
	protected static abstract class ConfigPropertiesLoader extends ConfigFileLoader
	{
		/* (non-Javadoc)
		 * @see com.l2jfree.L2Config.ConfigFileLoader#getFileName()
		 */
		@Override
		protected final String getFileName()
		{
			return "./config/" + getName().trim() + ".properties";
		}
		
		protected Class<?>[] getAnnotatedClasses()
		{
			return new Class<?>[] { getClass().getEnclosingClass() };
		}
		
		/* (non-Javadoc)
		 * @see com.l2jfree.L2Config.ConfigFileLoader#loadReader(java.io.BufferedReader)
		 */
		@Override
		protected final void loadReader(BufferedReader reader) throws Exception
		{
			final L2Properties properties = new L2Properties(reader);
			
			for (Class<?> cl : getAnnotatedClasses())
			{
				for (Field field : cl.getFields())
				{
					final ConfigProperty configProperty = field.getAnnotation(ConfigProperty.class);
					
					if (configProperty == null || !configProperty.loader().equals(getName()))
						continue;
					
					if (!Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()))
					{
						_log.warn("Invalid modifiers for " + field);
						continue;
					}
					
					field.set(null, properties.getProperty(field.getType(), configProperty));
				}
			}
			
			loadImpl(properties);
		}
		
		protected abstract void loadImpl(L2Properties properties);
	}
	
	private static Set<StartupHook> _startupHooks = new L2FastSet<StartupHook>();
	
	/**
	 * While application is loading, returns {@code false}. After the application finishes loading, returns {@code true}.<BR>
	 * <BR>
	 * If calling this method resulted in {@code true}, all following invocations are guaranteed to result in {@code true}.
	 * 
	 * @return whether the application has finished loading
	 */
	public synchronized static boolean isLoaded()
	{
		return _startupHooks == null;
	}
	
	/**
	 * Adds a hook to be executed after the application loads.
	 * 
	 * @param hook The hook to be attached
	 */
	public synchronized static void addStartupHook(StartupHook hook)
	{
		if (_startupHooks != null)
			_startupHooks.add(hook);
		else
			hook.onStartup();
	}
	
	/** Executes startup hooks. */
	public synchronized static void onStartup()
	{
		final Set<StartupHook> startupHooks = _startupHooks;
		
		_startupHooks = null;
		
		for (StartupHook hook : startupHooks)
			hook.onStartup();
	}
	
	/**
	 * This interface allows the implementing class to be attached as a startup hook.
	 */
	public interface StartupHook
	{
		/**
		 * This method is called on an attached startup hook when/if the application has finished loading.
		 */
		public void onStartup();
	}
}
