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

/**
 * @author NB4L1
 */
public final class L2Parser
{
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object get(Class<?> type, String value)
	{
		if (type == Boolean.class || type == Boolean.TYPE)
		{
			return L2Parser.getBoolean(value);
		}
		else if (type == Long.class || type == Long.TYPE)
		{
			return L2Parser.getLong(value);
		}
		else if (type == Integer.class || type == Integer.TYPE)
		{
			return L2Parser.getInteger(value);
		}
		else if (type == Short.class || type == Short.TYPE)
		{
			return L2Parser.getShort(value);
		}
		else if (type == Byte.class || type == Byte.TYPE)
		{
			return L2Parser.getByte(value);
		}
		else if (type == Double.class || type == Double.TYPE)
		{
			return L2Parser.getDouble(value);
		}
		else if (type == Float.class || type == Float.TYPE)
		{
			return L2Parser.getFloat(value);
		}
		else if (type == String.class)
		{
			return L2Parser.getString(value);
		}
		else if (type.isEnum())
		{
			return L2Parser.getEnum((Class<? extends Enum>)type, value);
		}
		else
		{
			throw new L2ParserException("Not covered type: " + type + "!");
		}
	}
	
	public static boolean getBoolean(String value)
	{
		if (value == null)
			throw new L2ParserException(Boolean.class);
		
		try
		{
			return Boolean.parseBoolean(value);
		}
		catch (RuntimeException e)
		{
			throw new L2ParserException(Boolean.class, value, e);
		}
	}
	
	public static byte getByte(String value)
	{
		if (value == null)
			throw new L2ParserException(Byte.class);
		
		try
		{
			return Byte.decode(value);
		}
		catch (RuntimeException e)
		{
			throw new L2ParserException(Byte.class, value, e);
		}
	}
	
	public static short getShort(String value)
	{
		if (value == null)
			throw new L2ParserException(Short.class);
		
		try
		{
			return Short.decode(value);
		}
		catch (RuntimeException e)
		{
			throw new L2ParserException(Short.class, value, e);
		}
	}
	
	public static int getInteger(String value)
	{
		if (value == null)
			throw new L2ParserException(Integer.class);
		
		try
		{
			return Integer.decode(value);
		}
		catch (RuntimeException e)
		{
			throw new L2ParserException(Integer.class, value, e);
		}
	}
	
	public static long getLong(String value)
	{
		if (value == null)
			throw new L2ParserException(Long.class);
		
		try
		{
			return Long.decode(value);
		}
		catch (RuntimeException e)
		{
			throw new L2ParserException(Long.class, value, e);
		}
	}
	
	public static float getFloat(String value)
	{
		if (value == null)
			throw new L2ParserException(Float.class);
		
		try
		{
			return Float.parseFloat(value);
		}
		catch (RuntimeException e)
		{
			throw new L2ParserException(Float.class, value, e);
		}
	}
	
	public static double getDouble(String value)
	{
		if (value == null)
			throw new L2ParserException(Double.class);
		
		try
		{
			return Double.parseDouble(value);
		}
		catch (RuntimeException e)
		{
			throw new L2ParserException(Double.class, value, e);
		}
	}
	
	public static String getString(String value)
	{
		if (value == null)
			throw new L2ParserException(String.class);
		
		return String.valueOf(value);
	}
	
	public static <T extends Enum<T>> T getEnum(Class<T> enumClass, String value)
	{
		if (value == null)
			throw new L2ParserException(enumClass);
		
		try
		{
			return Enum.valueOf(enumClass, value);
		}
		catch (RuntimeException e)
		{
			throw new L2ParserException(enumClass, value, e);
		}
	}
	
	public static final class L2ParserException extends RuntimeException
	{
		private static final long serialVersionUID = 1839324679891385619L;
		
		public L2ParserException()
		{
			super();
		}
		
		public L2ParserException(String message)
		{
			super(message);
		}
		
		public L2ParserException(Class<?> requiredType)
		{
			super(requiredType + " value required, but not specified!");
		}
		
		public L2ParserException(Class<?> requiredType, String value, RuntimeException cause)
		{
			super(requiredType + " value required, but found: '" + value + "'!", cause);
		}
	}
}
