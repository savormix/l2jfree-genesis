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

import java.lang.reflect.Array;

import com.l2jfree.lang.L2TextBuilder;

public class DefaultArrayConverter implements Converter
{
	private static final class SingletonHolder
	{
		public static final DefaultArrayConverter INSTANCE = new DefaultArrayConverter();
	}
	
	public static DefaultArrayConverter getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	@Override
	public Object convertFromString(Class<?> type, String value)
	{
		final Class<?> componentType = type.getComponentType();
		
		if (value.isEmpty())
			return Array.newInstance(componentType, 0);
		
		final String[] splitted = value.split(",");
		final Object array = Array.newInstance(componentType, splitted.length);
		
		for (int i = 0; i < splitted.length; i++)
		{
			Array.set(array, i, getElementConverter().convertFromString(componentType, splitted[i]));
		}
		
		return array;
	}
	
	@Override
	public String convertToString(Class<?> type, Object obj)
	{
		final Class<?> componentType = type.getComponentType();
		
		if (obj == null)
			return "";
		
		final int length = Array.getLength(obj);
		final L2TextBuilder tb = L2TextBuilder.newInstance();
		
		for (int i = 0; i < length; i++)
		{
			if (i > 0)
				tb.append(",");
			
			tb.append(getElementConverter().convertToString(componentType, Array.get(obj, i)));
		}
		
		return tb.moveToString();
	}
	
	protected Converter getElementConverter()
	{
		return DefaultConverter.getInstance();
	}
}
