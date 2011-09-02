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
package com.l2jfree.config.model;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.l2jfree.config.L2Properties;
import com.l2jfree.config.annotation.ConfigClass;
import com.l2jfree.config.annotation.ConfigField;
import com.l2jfree.util.EnumValues;
import com.l2jfree.util.logging.L2Logger;

public final class ConfigClassInfo
{
	private static final L2Logger _log = L2Logger.getLogger(ConfigClassInfo.class);
	
	private static final Map<Class<?>, ConfigClassInfo> _ConfigClasses = new HashMap<Class<?>, ConfigClassInfo>();
	
	public synchronized static ConfigClassInfo valueOf(Class<?> clazz) throws InstantiationException,
			IllegalAccessException
	{
		ConfigClassInfo info = _ConfigClasses.get(clazz);
		
		if (info == null)
			_ConfigClasses.put(clazz, info = new ConfigClassInfo(clazz));
		
		return info;
	}
	
	private final Class<?> _clazz;
	private final ConfigClass _configClass;
	private final List<ConfigFieldInfo> _infos = new ArrayList<ConfigFieldInfo>();
	
	private ConfigClassInfo(Class<?> clazz) throws InstantiationException, IllegalAccessException
	{
		_clazz = clazz;
		_configClass = _clazz.getAnnotation(ConfigClass.class);
		
		final Map<String, ConfigGroup> activeGroups = new HashMap<String, ConfigGroup>();
		
		for (Field field : _clazz.getFields())
		{
			final ConfigField configField = field.getAnnotation(ConfigField.class);
			
			if (configField == null)
				continue;
			
			if (!Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()))
			{
				_log.warn("Invalid modifiers for " + field);
				continue;
			}
			
			final ConfigFieldInfo info = new ConfigFieldInfo(field);
			
			_infos.add(info);
			
			if (info.getConfigGroupBeginning() != null)
			{
				final ConfigGroup group = new ConfigGroup();
				
				activeGroups.put(info.getConfigGroupBeginning().name(), group);
				
				info.setBeginningGroup(group);
			}
			
			for (ConfigGroup group : activeGroups.values())
				group.add(info);
			
			if (info.getConfigGroupEnding() != null)
			{
				final ConfigGroup group = activeGroups.remove(info.getConfigGroupEnding().name());
				
				info.setEndingGroup(group);
			}
		}
		
		if (!activeGroups.isEmpty())
			_log.warn("Invalid config grouping!");
		
		store();
		
		try
		{
			// just in case it's missing
			if (!getConfigFile().exists())
				store(getConfigFile(), null);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public ConfigClass getConfigClass()
	{
		return _configClass;
	}
	
	public File getConfigFile()
	{
		return new File(_configClass.folderName(), _configClass.fileName() + ".properties");
	}
	
	public File getModifiedConfigFile()
	{
		return new File(_configClass.folderName() + "/modified", _configClass.fileName() + ".properties");
	}
	
	public File getDefaultConfigFile()
	{
		return new File(_configClass.folderName() + "/default", _configClass.fileName() + ".properties");
	}
	
	public File getFullConfigFile()
	{
		return new File(_configClass.folderName() + "/full", _configClass.fileName() + ".properties");
	}
	
	public synchronized void load() throws IOException
	{
		final L2Properties properties = new L2Properties().setLog(false);
		
		load(properties);
	}
	
	public synchronized void load(L2Properties properties) throws IOException
	{
		// if config file is missing it will use default values everywhere
		if (getConfigFile().exists())
			properties.load(getConfigFile());
		
		for (ConfigFieldInfo info : _infos)
			info.setCurrentValue(properties);
	}
	
	public synchronized void store()
	{
		for (PrintMode mode : PrintMode.VALUES)
			store(mode);
	}
	
	public synchronized void store(PrintMode mode)
	{
		File configFile = null;
		switch (mode)
		{
			case MODIFIED:
				configFile = getModifiedConfigFile();
				break;
			case DEFAULT:
				configFile = getDefaultConfigFile();
				break;
			case FULL:
			default:
				configFile = getFullConfigFile();
				break;
		}
		
		try
		{
			store(configFile, mode);
		}
		catch (IOException e)
		{
			_log.warn("Couldn't save " + mode + " config file to '" + configFile + "'!", e);
		}
	}
	
	private void store(File configFile, PrintMode mode) throws IOException
	{
		if (!configFile.getParentFile().exists())
			if (!configFile.getParentFile().mkdirs())
				throw new IOException("Couldn't create required folder structure for " + configFile);
		
		PrintWriter pw = null;
		try
		{
			pw = new PrintWriter(configFile);
			
			if (mode != null)
			{
				pw.println("################################################################################");
				switch (mode)
				{
					case MODIFIED:
						pw.println("# This file should be modified in order to influence config variables.");
						pw.println("# Contains only config variables differing from their default values.");
						break;
					case DEFAULT:
						pw.println("# This file exists only for informational purposes.");
						pw.println("# Contains every config variable with their default values.");
						break;
					case FULL:
						pw.println("# This file exists only for informational purposes.");
						pw.println("# Contains every config variable with their current values.");
						break;
				}
				pw.println("################################################################################");
				pw.println();
			}
			
			print(pw, mode);
		}
		finally
		{
			IOUtils.closeQuietly(pw);
		}
	}
	
	public synchronized void print(OutputStream out, PrintMode mode)
	{
		print(new PrintWriter(out, true), mode);
	}
	
	public synchronized void print(PrintWriter out, PrintMode mode)
	{
		if (!ArrayUtils.isEmpty(getConfigClass().comment()))
		{
			for (String line : getConfigClass().comment())
				out.println("# " + line);
			out.println();
		}
		
		for (ConfigFieldInfo info : _infos)
			info.print(out, mode);
	}
	
	public List<ConfigFieldInfo> getConfigFieldInfos()
	{
		return _infos;
	}
	
	public enum PrintMode
	{
		MODIFIED,
		FULL,
		DEFAULT;
		
		public static final EnumValues<PrintMode> VALUES = new EnumValues<PrintMode>(PrintMode.class);
	}
}
