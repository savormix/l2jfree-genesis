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
package com.l2jfree.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;

import org.apache.commons.lang3.StringUtils;

/**
 * This class provides advanced object status reporting capabilities. <BR>
 * <BR>
 * Allows inspecting objects that do not extend the {@code IntrospectiveObject} class.
 * 
 * @author savormix
 */
public final class Introspection
{
	private static final int DEFAULT_WIDTH = 80;
	
	private Introspection()
	{
		// utility class
	}
	
	/**
	 * Returns a single line string representing a subset of this object's status. <BR>
	 * <BR>
	 * The string consists of the simple name of the given class followed by all non-static fields
	 * of that class in name = value pairs, separated with comma followed by a space and enclosed in
	 * simple parenthesis. If that class does not declare any non-static fields, no parenthesis are
	 * added. <BR>
	 * If the given class is {@code null}, then the complete status is reported, as in
	 * {@link #toString(Object)}. <BR>
	 * <BR>
	 * Example outputs could be:<BR>
	 * <CODE>
	 * SomeClass<BR>
	 * SomeClass(number = 15, object = this)
	 * </CODE>
	 * 
	 * @param o an object (of type {@code T})
	 * @param c {@code Class<T>} or {@code Class<? super T>}
	 * @return a textual representation of the given object
	 */
	public static String toString(Object o, Class<?> c)
	{
		if (o == null)
			return "null".intern();
		Class<?> actual = (c == null ? o.getClass() : c);
		StringBuilder sb = new StringBuilder(actual.getSimpleName());
		int open = sb.length();
		sb.append('(');
		boolean written = writeFields(actual, o, sb, null, true);
		if (c == null)
			while ((actual = actual.getSuperclass()) != null)
				if (writeFields(actual, o, sb, null, !written))
					written = true;
		if (!written)
			sb.deleteCharAt(open);
		else
			sb.append(')');
		return sb.toString();
	}
	
	/**
	 * Returns a single line string representing the complete status of this object. <BR>
	 * <BR>
	 * The string consists of the simple name of the object's reported class followed by all
	 * non-static fields in name = value pairs, separated with comma followed by a space and
	 * enclosed in simple parenthesis. If all classes in hierarchy do not declare any non-static
	 * fields, no parenthesis are added. <BR>
	 * <BR>
	 * Example outputs could be:<BR>
	 * <CODE>
	 * SomeClass<BR>
	 * SomeClass(number = 15, object = this)
	 * </CODE>
	 * 
	 * @param o an object
	 * @return a textual representation of the given object
	 */
	public static String toString(Object o)
	{
		return toString(o, null);
	}
	
	/**
	 * Returns the complete status of the given object in a multi-line string. <BR>
	 * <BR>
	 * The beginning and the end of the string are separated with '=' lines. First, the object's own
	 * class is reported in a canonical form and all non-static object's own class fields are
	 * reported in name = value pairs. The value is reported using {@link String#valueOf(Object)}
	 * except if {@code o == Object}, when 'this' is reported instead. Then, the same is done for
	 * each superclass in the class hierarchy. Superclass info is separated by a line of '-'s before
	 * the line with the canonical name. <BR>
	 * <BR>
	 * An example output could be:<BR>
	 * <CODE>
	 * =================================================<BR>
	 * Object's class: com.l2jfree.model.SomeClass<BR>
	 * number = 15<BR>
	 * object = this<BR>
	 * -------------------------------------------------<BR>
	 * Superclass: com.l2jfree.model.IntrospectiveObject<BR>
	 * -------------------------------------------------<BR>
	 * Superclass: java.lang.Object<BR>
	 * =================================================<BR>
	 * </CODE>
	 * 
	 * @param o an object
	 * @return a textual representation of the given object
	 */
	public static String toMultiLineString(Object o)
	{
		String eol = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < DEFAULT_WIDTH; i++)
			sb.append('=');
		sb.append(eol);
		
		Class<?> c = o.getClass();
		sb.append("Object's class: ");
		sb.append(c.getCanonicalName());
		sb.append(eol);
		
		writeFields(c, o, sb, eol, true);
		
		while ((c = c.getSuperclass()) != null)
		{
			for (int i = 0; i < DEFAULT_WIDTH; i++)
				sb.append('-');
			sb.append(eol);
			
			sb.append("Superclass: ");
			sb.append(c.getCanonicalName());
			sb.append(eol);
			
			writeFields(c, o, sb, eol, true);
		}
		
		for (int i = 0; i < DEFAULT_WIDTH; i++)
			sb.append('=');
		return sb.toString();
	}
	
	private static boolean writeFields(Class<?> c, Object accessor, StringBuilder dest, String eol, boolean init)
	{
		if (c == null)
			throw new IllegalArgumentException("No class specified.");
		else if (!c.isInstance(accessor))
			throw new IllegalArgumentException(accessor + " is not a " + c.getCanonicalName());
		for (Field f : c.getDeclaredFields())
		{
			int mod = f.getModifiers();
			if (Modifier.isStatic(mod))
				continue;
			if (init)
				init = false;
			else if (eol == null)
				dest.append(", ");
			String fieldName = null;
			final Column column = f.getAnnotation(Column.class);
			if (column != null)
				fieldName = column.name();
			if (StringUtils.isEmpty(fieldName))
				fieldName = f.getName();
			dest.append(fieldName);
			dest.append(" = ");
			try
			{
				f.setAccessible(true);
				Object val = f.get(accessor);
				if (accessor == val)
					dest.append("this");
				else
					deepToString(val, dest, null);
			}
			catch (Exception e)
			{
				dest.append("???");
			}
			finally
			{
				try
				{
					f.setAccessible(false);
				}
				catch (Exception e)
				{
					// ignore
				}
			}
			if (eol != null)
				dest.append(eol);
		}
		return !init;
	}
	
	private static void deepToString(Object obj, StringBuilder dest, Set<Object> dejaVu) throws SecurityException,
			NoSuchMethodException
	{
		if (obj == null)
		{
			dest.append("null");
			return;
		}
		
		if (obj.getClass().isArray())
		{
			final int length = Array.getLength(obj);
			
			if (length == 0)
			{
				dest.append("[]");
				return;
			}
			
			if (dejaVu == null)
				dejaVu = new HashSet<Object>();
			dejaVu.add(obj);
			
			dest.append('[');
			for (int i = 0; i < length; i++)
			{
				if (i != 0)
					dest.append(", ");
				
				final Object element = Array.get(obj, i);
				
				if (dejaVu.contains(element))
					dest.append("[...]");
				else
					deepToString(element, dest, dejaVu);
			}
			dest.append(']');
			
			dejaVu.remove(obj);
		}
		else
		{
			if (obj.getClass().getMethod("toString").getDeclaringClass() == Object.class)
				dest.append(Introspection.toString(obj));
			else
				dest.append(obj.toString());
		}
	}
}
