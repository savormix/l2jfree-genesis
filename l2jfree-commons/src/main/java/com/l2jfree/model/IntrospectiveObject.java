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
package com.l2jfree.model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * This class provides objects with advanced status reporting capabilities
 * and is intended to bet used as the topmost class in a hierarchy.
 * @author savormix
 * @see #toString()
 * @see #toMultiLineString()
 */
public abstract class IntrospectiveObject
{
	private static final int DEFAULT_WIDTH = 80;
	
	/**
	 * Returns a specific class in the hierarchy whose own status reflects
	 * this object's status the best.<BR><BR>
	 * By default, the current class status is reported for simplicity & clarity.
	 * However, if the current class merely implements methods, it may be desirable
	 * to report a class above in the hierarchy.<BR><BR>
	 * If this method returns {@code null}, the complete status of this object
	 * will be reported using {@link #toMultiLineString()}.<BR><BR>
	 * @return a class in hierarchy
	 * @see #toString()
	 */
	protected Class<? extends IntrospectiveObject> getReportedClass()
	{
		return getClass();
	}
	
	/**
	 * Returns the complete status of this object in a multi-line string.
	 * <BR><BR>
	 * The beginning and the end of the string are separated with '=' lines.
	 * First, the object's own class is reported in a canonical form and all
	 * non-static object's own class fields are reported in name = value pairs.
	 * The value is reported using {@link String#valueOf(Object)} except if
	 * {@code this == Object}, when 'this' is reported instead.
	 * Then, the same is done for each superclass in the class hierarchy.
	 * Superclass info is separated by a line of '-'s before the line with the
	 * canonical name.
	 * <BR><BR>
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
	 * @return a textual representation of this object
	 */
	public String toMultiLineString()
	{
		String eol = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < DEFAULT_WIDTH; i++)
			sb.append('=');
		sb.append(eol);
		
		Class<?> c = getClass();
		sb.append("Object's class: ");
		sb.append(c.getCanonicalName());
		sb.append(eol);
		
		writeFields(c, sb, eol);
		
		while ((c = c.getSuperclass()) != null)
		{
			for (int i = 0; i < DEFAULT_WIDTH; i++)
				sb.append('-');
			sb.append(eol);
			
			sb.append("Superclass: ");
			sb.append(c.getCanonicalName());
			sb.append(eol);
			
			writeFields(c, sb, eol);
		}
		
		for (int i = 0; i < DEFAULT_WIDTH; i++)
			sb.append('=');
		//sb.append(eol);
		return sb.toString();
	}
	
	/**
	 * Returns either a single or a multi-line string representing the
	 * actual status of this object.
	 * <BR><BR>
	 * A single line consists of the simple name of the object's reported class
	 * followed by all non-static fields in name = value pairs, separated with
	 * comma followed by a space and enclosed in simple parenthesis. If object's
	 * reported class does not declare any non-static fields, no parenthesis are
	 * dded.
	 * <BR><BR>
	 * Example outputs could be:<BR>
	 * <CODE>
	 * SomeClass<BR>
	 * SomeClass(number = 15, object = this)
	 * </CODE>
	 * <BR><BR>
	 * For information about the multi-line representation, see
	 * {@link #toMultiLineString()}.
	 */
	@Override
	public String toString()
	{
		Class<?> c = getReportedClass();
		if (c == null)
			return toMultiLineString();
		StringBuilder sb = new StringBuilder(c.getSimpleName());
		int open = sb.length();
		sb.append('(');
		if (!writeFields(c, sb, null))
			sb.deleteCharAt(open);
		else
			sb.append(')');
		return sb.toString();
	}
	
	private boolean writeFields(Class<?> c, StringBuilder dest, String eol)
	{
		if (c == null)
			return false;
		boolean init = true;
		for (Field f : c.getDeclaredFields())
		{
			int mod = f.getModifiers();
			if (Modifier.isStatic(mod))
				continue;
			if (init)
				init = false;
			else if (eol == null)
				dest.append(", ");
			dest.append(f.getName());
			dest.append(" = ");
			try
			{
				f.setAccessible(true);
				Object val = f.get(this);
				if (this == val)
					dest.append("this");
				else
					dest.append(val);
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
}
