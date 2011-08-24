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
package com.l2jfree.lang;

import com.l2jfree.util.Introspection;

/**
 * This class provides objects with advanced status reporting capabilities and is intended to be
 * used as the topmost class in a hierarchy.
 * 
 * @author savormix
 * @see #toString()
 * @see #toMultiLineString()
 */
public abstract class IntrospectiveObject
{
	/**
	 * Returns a specific class in the hierarchy whose own status reflects this object's status the
	 * best.<BR>
	 * <BR>
	 * By default, the current class status is reported for simplicity & clarity. However, if the
	 * current class merely implements methods, it may be desirable to report a class above in the
	 * hierarchy.<BR>
	 * <BR>
	 * If this method returns {@code null}, the complete status of this object will be reported.<BR>
	 * <BR>
	 * 
	 * @return a class in hierarchy
	 * @see #toString()
	 */
	protected Class<? extends IntrospectiveObject> getReportedClass()
	{
		return getClass();
	}
	
	/**
	 * Returns the complete status of this object in a multi-line string.
	 * 
	 * @return a textual representation of this object
	 * @see Introspection#toMultiLineString(Object)
	 */
	public String toMultiLineString()
	{
		return Introspection.toMultiLineString(this);
	}
	
	/**
	 * Returns the a subset of this object's status in a single line string.<BR>
	 * Complete status is reported if this object does not specify a class to be reported.
	 * 
	 * @see Introspection#toString(Object, Class)
	 */
	@Override
	public String toString()
	{
		return Introspection.toString(this, getReportedClass());
	}
}
