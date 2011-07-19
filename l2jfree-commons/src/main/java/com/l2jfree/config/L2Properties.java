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
package com.l2jfree.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author Noctarius
 */
public final class L2Properties extends Properties
{
	private static final long serialVersionUID = -4599023842346938325L;
	
	private boolean _warn = true;
	
	public L2Properties()
	{
	}
	
	public L2Properties setLog(boolean warn)
	{
		_warn = warn;
		
		return this;
	}
	
	// ===================================================================================
	
	public L2Properties(String name) throws IOException
	{
		load(new FileInputStream(name));
	}
	
	public L2Properties(File file) throws IOException
	{
		load(new FileInputStream(file));
	}
	
	public L2Properties(InputStream inStream) throws IOException
	{
		load(inStream);
	}
	
	public L2Properties(Reader reader) throws IOException
	{
		load(reader);
	}
	
	public L2Properties(Node node)
	{
		loadAttributes(node);
	}
	
	// ===================================================================================
	
	public void load(String name) throws IOException
	{
		load(new FileInputStream(name));
	}
	
	public void load(File file) throws IOException
	{
		load(new FileInputStream(file));
	}
	
	@Override
	public void load(InputStream inStream) throws IOException
	{
		try
		{
			super.load(inStream);
		}
		finally
		{
			inStream.close();
		}
	}
	
	@Override
	public void load(Reader reader) throws IOException
	{
		try
		{
			super.load(reader);
		}
		finally
		{
			reader.close();
		}
	}
	
	public void loadAttributes(Node node)
	{
		final NamedNodeMap attrs = node.getAttributes();
		
		for (int i = 0; i < attrs.getLength(); i++)
		{
			final Node attr = attrs.item(i);
			
			setProperty(attr.getNodeName(), attr.getNodeValue());
		}
	}
	
	// ===================================================================================
	
	@Override
	public String getProperty(String key)
	{
		String property = super.getProperty(key);
		
		if (property == null)
		{
			if (_warn)
				System.err.println("L2Properties: Missing property for key - " + key);
			
			return null;
		}
		
		return property.trim();
	}
	
	@Override
	public String getProperty(String key, String defaultValue)
	{
		String property = super.getProperty(key, defaultValue);
		
		if (property == null)
		{
			if (_warn)
				System.err.println("L2Properties: Missing defaultValue for key - " + key);
			
			return null;
		}
		
		return property.trim();
	}
	
	// ===================================================================================
	
	public synchronized Object setProperty(String key, Object value)
	{
		return setProperty(key, String.valueOf(value));
	}
	
	// ===================================================================================
	
	@SuppressWarnings( { "unchecked", "rawtypes" })
	public Object getProperty(Class<?> expectedType, ConfigProperty configProperty)
	{
		final String name = configProperty.name();
		final String defaultValue = configProperty.value();
		
		if (expectedType == Boolean.class || expectedType == Boolean.TYPE)
		{
			return getBool(name, defaultValue);
		}
		else if (expectedType == Long.class || expectedType == Long.TYPE)
		{
			return getLong(name, defaultValue);
		}
		else if (expectedType == Integer.class || expectedType == Integer.TYPE)
		{
			return getInteger(name, defaultValue);
		}
		else if (expectedType == Short.class || expectedType == Short.TYPE)
		{
			return getShort(name, defaultValue);
		}
		else if (expectedType == Byte.class || expectedType == Byte.TYPE)
		{
			return getByte(name, defaultValue);
		}
		else if (expectedType == Double.class || expectedType == Double.TYPE)
		{
			return getDouble(name, defaultValue);
		}
		else if (expectedType == Float.class || expectedType == Float.TYPE)
		{
			return getFloat(name, defaultValue);
		}
		else if (expectedType == String.class)
		{
			return getString(name, defaultValue);
		}
		else if (expectedType.isEnum())
		{
			return getEnum(name, (Class<? extends Enum>)expectedType, defaultValue);
		}
		else
		{
			throw new IllegalStateException();
		}
	}
	
	// ===================================================================================
	
	public boolean getBool(String name)
	{
		return getBool(name, null);
	}
	
	public boolean getBool(String name, String deflt)
	{
		String val = getProperty(name, deflt);
		if (val == null)
			throw new IllegalArgumentException("Boolean value required, but not specified");
		
		try
		{
			return Boolean.parseBoolean(val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Boolean value required, but found: " + val);
		}
	}
	
	public boolean getBool(String name, boolean deflt)
	{
		String val = getProperty(name);
		if (val == null)
			return deflt;
		
		try
		{
			return Boolean.parseBoolean(val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Boolean value required, but found: " + val);
		}
	}
	
	public byte getByte(String name)
	{
		return getByte(name, null);
	}
	
	public byte getByte(String name, String deflt)
	{
		String val = getProperty(name, deflt);
		if (val == null)
			throw new IllegalArgumentException("Byte value required, but not specified");
		
		try
		{
			return Byte.decode(val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Byte value required, but found: " + val);
		}
	}
	
	public byte getByte(String name, byte deflt)
	{
		String val = getProperty(name);
		if (val == null)
			return deflt;
		
		try
		{
			return Byte.decode(val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Byte value required, but found: " + val);
		}
	}
	
	public short getShort(String name)
	{
		return getShort(name, null);
	}
	
	public short getShort(String name, String deflt)
	{
		String val = getProperty(name, deflt);
		if (val == null)
			throw new IllegalArgumentException("Short value required, but not specified");
		
		try
		{
			return Short.decode(val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Short value required, but found: " + val);
		}
	}
	
	public short getShort(String name, short deflt)
	{
		String val = getProperty(name);
		if (val == null)
			return deflt;
		
		try
		{
			return Short.decode(val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Short value required, but found: " + val);
		}
	}
	
	public int getInteger(String name)
	{
		return getInteger(name, null);
	}
	
	public int getInteger(String name, String deflt)
	{
		String val = getProperty(name, deflt);
		if (val == null)
			throw new IllegalArgumentException("Integer value required, but not specified");
		
		try
		{
			return Integer.decode(val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Integer value required, but found: " + val);
		}
	}
	
	public int getInteger(String name, int deflt)
	{
		String val = getProperty(name);
		if (val == null)
			return deflt;
		
		try
		{
			return Integer.decode(val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Integer value required, but found: " + val);
		}
	}
	
	public long getLong(String name)
	{
		return getLong(name, null);
	}
	
	public long getLong(String name, String deflt)
	{
		String val = getProperty(name, deflt);
		if (val == null)
			throw new IllegalArgumentException("Integer value required, but not specified");
		
		try
		{
			return Long.decode(val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Integer value required, but found: " + val);
		}
	}
	
	public long getLong(String name, long deflt)
	{
		String val = getProperty(name);
		if (val == null)
			return deflt;
		
		try
		{
			return Long.decode(val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Integer value required, but found: " + val);
		}
	}
	
	public float getFloat(String name)
	{
		return getFloat(name, null);
	}
	
	public float getFloat(String name, String deflt)
	{
		String val = getProperty(name, deflt);
		if (val == null)
			throw new IllegalArgumentException("Float value required, but not specified");
		
		try
		{
			return Float.parseFloat(val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Float value required, but found: " + val);
		}
	}
	
	public float getFloat(String name, float deflt)
	{
		String val = getProperty(name);
		if (val == null)
			return deflt;
		
		try
		{
			return Float.parseFloat(val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Float value required, but found: " + val);
		}
	}
	
	public double getDouble(String name)
	{
		return getDouble(name, null);
	}
	
	public double getDouble(String name, String deflt)
	{
		String val = getProperty(name, deflt);
		if (val == null)
			throw new IllegalArgumentException("Float value required, but not specified");
		
		try
		{
			return Double.parseDouble(val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Float value required, but found: " + val);
		}
	}
	
	public double getDouble(String name, double deflt)
	{
		String val = getProperty(name);
		if (val == null)
			return deflt;
		
		try
		{
			return Double.parseDouble(val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Float value required, but found: " + val);
		}
	}
	
	public String getString(String name)
	{
		return getString(name, null);
	}
	
	public String getString(String name, String deflt)
	{
		String val = getProperty(name, deflt);
		if (val == null)
			throw new IllegalArgumentException("String value required, but not specified");
		
		return String.valueOf(val);
	}
	
	public <T extends Enum<T>> T getEnum(String name, Class<T> enumClass)
	{
		return getEnum(name, enumClass, (String)null);
	}
	
	public <T extends Enum<T>> T getEnum(String name, Class<T> enumClass, String deflt)
	{
		String val = getProperty(name, deflt);
		if (val == null)
			throw new IllegalArgumentException("Enum value of type " + enumClass.getName()
					+ " required, but not specified");
		
		try
		{
			return Enum.valueOf(enumClass, String.valueOf(val));
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Enum value of type " + enumClass.getName() + "required, but found: "
					+ val);
		}
	}
	
	public <T extends Enum<T>> T getEnum(String name, Class<T> enumClass, T deflt)
	{
		String val = getProperty(name);
		if (val == null)
			return deflt;
		
		try
		{
			return Enum.valueOf(enumClass, String.valueOf(val));
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Enum value of type " + enumClass.getName() + "required, but found: "
					+ val);
		}
	}
}
