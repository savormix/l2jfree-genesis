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

import com.l2jfree.config.L2Parser;

public class DefaultConverter implements Converter
{
	private static final class SingletonHolder
	{
		public static final DefaultConverter INSTANCE = new DefaultConverter();
	}
	
	public static DefaultConverter getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	@Override
	public Object convertFromString(Class<?> type, String value)
	{
		if (type.isArray())
			return getArrayConverter().convertFromString(type, value);
		
		return L2Parser.get(type, value);
	}
	
	@Override
	public String convertToString(Class<?> type, Object obj)
	{
		if (type.isArray())
			return getArrayConverter().convertToString(type, obj);
		
		if (obj == null)
			return "";
		
		return obj.toString();
	}
	
	@SuppressWarnings("static-method")
	protected Converter getArrayConverter()
	{
		return DefaultArrayConverter.getInstance();
	}
}
