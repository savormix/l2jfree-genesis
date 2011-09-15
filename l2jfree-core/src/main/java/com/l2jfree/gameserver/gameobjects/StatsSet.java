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
package com.l2jfree.gameserver.gameobjects;

import java.lang.reflect.Array;

import javolution.util.FastMap;

/**
 * @author hex1r0
 */
public class StatsSet
{
	private final FastMap<String, Object> _map = new FastMap<String, Object>();
	
	public StatsSet put(String key, Object value)
	{
		_map.put(key, value);
		return this;
	}
	
	public byte getByte(String key)
	{
		checkKey(key, null);
		return (Byte)getValue(key, Byte.class);
	}
	
	public boolean getBool(String key) throws IllegalArgumentException
	{
		checkKey(key, null);
		return (Boolean)getValue(key, Boolean.class);
	}
	
	public boolean getBool(String key, boolean defaultValue) throws IllegalArgumentException
	{
		if (checkKey(key, defaultValue) == Boolean.valueOf(defaultValue))
			return defaultValue;
		
		return (Boolean)getValue(key, Boolean.class);
	}
	
	public byte getByte(String key, byte defaultValue)
	{
		if (checkKey(key, defaultValue) == Byte.valueOf(defaultValue))
			return defaultValue;
		
		return (Byte)getValue(key, Byte.class);
	}
	
	public short getShort(String key)
	{
		checkKey(key, null);
		return (Short)getValue(key, Short.class);
	}
	
	public short getShort(String key, short defaultValue)
	{
		if (checkKey(key, defaultValue) == Short.valueOf(defaultValue))
			return defaultValue;
		
		return (Short)getValue(key, Short.class);
	}
	
	public int getInt(String key)
	{
		checkKey(key, null);
		return (Integer)getValue(key, Integer.class);
	}
	
	public int getInt(String key, int defaultValue)
	{
		if (checkKey(key, defaultValue) == Integer.valueOf(defaultValue))
			return defaultValue;
		
		return (Integer)getValue(key, Integer.class);
	}
	
	public long getLong(String key)
	{
		checkKey(key, null);
		return (Long)getValue(key, Long.class);
	}
	
	public long getLong(String key, long defaultValue)
	{
		if (checkKey(key, defaultValue) == Long.valueOf(defaultValue))
			return defaultValue;
		
		return (Long)getValue(key, Long.class);
	}
	
	public float getFloat(String key)
	{
		checkKey(key, null);
		return (Float)getValue(key, Float.class);
	}
	
	public float getFloat(String key, float defaultValue)
	{
		if (checkKey(key, defaultValue) == Float.valueOf(defaultValue))
			return defaultValue;
		
		return (Float)getValue(key, Float.class);
	}
	
	public double getDouble(String key)
	{
		checkKey(key, null);
		return (Double)getValue(key, Double.class);
	}
	
	public double getDouble(String key, double defaultValue)
	{
		if (checkKey(key, defaultValue) == Double.valueOf(defaultValue))
			return defaultValue;
		
		return (Double)getValue(key, Double.class);
	}
	
	public String getString(String key)
	{
		checkKey(key, null);
		return (String)getValue(key, String.class);
	}
	
	public String getString(String key, String defaultValue)
	{
		if (defaultValue.equals(checkKey(key, defaultValue)))
			return defaultValue;
		
		return (String)getValue(key, String.class);
	}
	
	public boolean[] getBoolArray(String key, String separator)
	{
		checkKey(key, null);
		return (boolean[])getValueAsArray(key, Boolean.class, separator);
	}
	
	public byte[] getByteArray(String key, String separator)
	{
		checkKey(key, null);
		return (byte[])getValueAsArray(key, Byte.class, separator);
	}
	
	public short[] getShortArray(String key, String separator)
	{
		checkKey(key, null);
		return (short[])getValueAsArray(key, Short.class, separator);
	}
	
	public int[] getIntArray(String key, String separator)
	{
		checkKey(key, null);
		return (int[])getValueAsArray(key, Integer.class, separator);
	}
	
	public long[] getLongArray(String key, String separator)
	{
		checkKey(key, null);
		return (long[])getValueAsArray(key, Long.class, separator);
	}
	
	public float[] getFloatArray(String key, String separator)
	{
		checkKey(key, null);
		return (float[])getValueAsArray(key, Float.class, separator);
	}
	
	public double[] getDoubleArray(String key, String separator)
	{
		checkKey(key, null);
		return (double[])getValueAsArray(key, Double.class, separator);
	}
	
	public String[] getStringArray(String key, String separator)
	{
		checkKey(key, null);
		return (String[])getValueAsArray(key, String.class, separator);
	}
	
	private Object checkKey(String key, Object defaultValue) throws IllegalArgumentException
	{
		if (!_map.containsKey(key))
		{
			if (defaultValue == null)
				throw new IllegalArgumentException("Invalid key value: '" + key + "'");
			else
				return defaultValue;
		}
		return null;
	}
	
	private Object getValue(String key, Class<?> clazz) throws IllegalArgumentException
	{
		Object value = _map.get(key);
		if (clazz.isInstance(value))
			return value;
		
		String s = String.valueOf(value);
		try
		{
			if (clazz.isAssignableFrom(Boolean.class))
				return Boolean.parseBoolean(s);
			
			else if (clazz.isAssignableFrom(Short.class))
				return Short.parseShort(s);
			
			else if (clazz.isAssignableFrom(Integer.class))
				return Integer.parseInt(s);
			
			else if (clazz.isAssignableFrom(Long.class))
				return Long.parseLong(s);
			
			else if (clazz.isAssignableFrom(Float.class))
				return Float.parseFloat(s);
			
			else if (clazz.isAssignableFrom(Double.class))
				return Double.parseDouble(s);
			
			else
				return s;
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Expected " + clazz.getSimpleName() + " value, but found: '"
					+ value.toString() + "'");
		}
	}
	
	private Object getValueAsArray(String key, Class<?> clazz, String separator)
	{
		Object value = _map.get(key);
		
		String[] values = String.valueOf(_map.get(key)).split(separator);
		
		Object array = Array.newInstance(clazz, values.length);
		for (int i = 0; i < values.length; i++)
		{
			try
			{
				if (clazz.isAssignableFrom(Boolean.class))
					Array.set(array, i, Boolean.parseBoolean(values[i]));
				
				else if (clazz.isAssignableFrom(Short.class))
					Array.set(array, i, Short.parseShort(values[i]));
				
				else if (clazz.isAssignableFrom(Integer.class))
					Array.set(array, i, Integer.parseInt(values[i]));
				
				else if (clazz.isAssignableFrom(Long.class))
					Array.set(array, i, Long.parseLong(values[i]));
				
				else if (clazz.isAssignableFrom(Float.class))
					Array.set(array, i, Float.parseFloat(values[i]));
				
				else if (clazz.isAssignableFrom(Double.class))
					Array.set(array, i, Double.parseDouble(values[i]));
				
				else
					Array.set(array, i, String.valueOf(values[i]));
			}
			catch (Exception e)
			{
				throw new IllegalArgumentException("Expected " + clazz.getSimpleName() + " value, but found: '"
						+ value.toString() + "'");
			}
		}
		
		return array;
	}
}
