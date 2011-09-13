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
package com.l2jfree.gameserver.util;

/**
 * An {@link Integer} equivalent to ensure the distinction from {@link PersistentId}.
 * 
 * @author NB4L1
 */
public final class ObjectId extends Number implements Comparable<ObjectId>
{
	private static final long serialVersionUID = -3234011324288480727L;
	
	private final int _value;
	
	public ObjectId(int value)
	{
		_value = value;
	}
	
	@Override
	public byte byteValue()
	{
		return (byte)_value;
	}
	
	@Override
	public short shortValue()
	{
		return (short)_value;
	}
	
	@Override
	public int intValue()
	{
		return _value;
	}
	
	@Override
	public long longValue()
	{
		return _value;
	}
	
	@Override
	public float floatValue()
	{
		return _value;
	}
	
	@Override
	public double doubleValue()
	{
		return _value;
	}
	
	@Override
	public String toString()
	{
		return String.valueOf(_value);
	}
	
	@Override
	public int hashCode()
	{
		return _value;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof ObjectId))
			return false;
		
		return _value == ((ObjectId)obj)._value;
	}
	
	@Override
	public int compareTo(ObjectId other)
	{
		if (_value < other._value)
			return -1;
		
		if (_value > other._value)
			return 1;
		
		return 0;
	}
}
