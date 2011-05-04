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
package com.l2jfree.config.converters;

@SuppressWarnings("unchecked")
public final class DefaultConverter implements Converter
{
	public static final DefaultConverter INSTANCE = new DefaultConverter();
	
	@Override
	public Object convertFromString(Class<?> type, String value)
	{
		if (type.isArray())
			return DefaultArrayConverter.INSTANCE.convertFromString(type, value);
		
		if (type == Boolean.class || type == Boolean.TYPE)
		{
			return Boolean.parseBoolean(value);
		}
		else if (type == Long.class || type == Long.TYPE)
		{
			return Long.decode(value);
		}
		else if (type == Integer.class || type == Integer.TYPE)
		{
			return Integer.decode(value);
		}
		else if (type == Short.class || type == Short.TYPE)
		{
			return Short.decode(value);
		}
		else if (type == Byte.class || type == Byte.TYPE)
		{
			return Byte.decode(value);
		}
		else if (type == Double.class || type == Double.TYPE)
		{
			return Double.parseDouble(value);
		}
		else if (type == Float.class || type == Float.TYPE)
		{
			return Float.parseFloat(value);
		}
		else if (type == String.class)
		{
			return value;
		}
		else if (type.isEnum())
		{
			return Enum.valueOf((Class<? extends Enum>)type, value);
		}
		else
		{
			throw new IllegalArgumentException("Not covered type: " + type + "!");
		}
	}
	
	@Override
	public String convertToString(Class<?> type, Object obj)
	{
		if (type.isArray())
			return DefaultArrayConverter.INSTANCE.convertToString(type, obj);
		
		if (obj == null)
			return null;
		
		return obj.toString();
	}
}
