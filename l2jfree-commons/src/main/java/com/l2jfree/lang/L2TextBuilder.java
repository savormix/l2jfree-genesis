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

import java.io.Serializable;

import com.l2jfree.util.ObjectPool;

/**
 * @author NB4L1
 */
public final class L2TextBuilder implements Serializable
{
	private static final long serialVersionUID = -4689463504223014731L;
	
	private StringBuilder _builder;
	
	public L2TextBuilder()
	{
		_builder = POOL.get();
	}
	
	public L2TextBuilder(int capacity)
	{
		_builder = POOL.get();
		_builder.ensureCapacity(capacity);
	}
	
	public L2TextBuilder(String str)
	{
		_builder = POOL.get();
		_builder.append(str);
	}
	
	public L2TextBuilder appendln()
	{
		append("\r\n");
		return this;
	}
	
	public L2TextBuilder appendln(Object obj)
	{
		append(obj);
		appendln();
		return this;
	}
	
	public L2TextBuilder appendNewline()
	{
		append("\r\n");
		return this;
	}
	
	public L2TextBuilder appendNewline(Object obj)
	{
		append(obj);
		appendNewline();
		return this;
	}
	
	/**
	 * Returns the String representation of this object and recycles it. This builder is empty when
	 * the result is returned.
	 * 
	 * @return the built String
	 */
	public String moveToString()
	{
		final String value = toString();
		
		POOL.store(_builder);
		
		_builder = null;
		
		return value;
	}
	
	private static final ObjectPool<StringBuilder> POOL = new ObjectPool<StringBuilder>() {
		@Override
		protected StringBuilder create()
		{
			return new StringBuilder();
		}
		
		@Override
		protected void reset(StringBuilder sb)
		{
			sb.setLength(0);
		}
	};
	
	// ===================================================================================
	// StringBuilder delegate methods
	
	/**
	 * @return int
	 * @see java.lang.StringBuilder#length()
	 */
	public int length()
	{
		return _builder.length();
	}
	
	/**
	 * @return int
	 * @see java.lang.StringBuilder#capacity()
	 */
	public int capacity()
	{
		return _builder.capacity();
	}
	
	/**
	 * @return int
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return _builder.hashCode();
	}
	
	/**
	 * @param minimumCapacity
	 * @see java.lang.StringBuilder#ensureCapacity(int)
	 */
	public void ensureCapacity(int minimumCapacity)
	{
		_builder.ensureCapacity(minimumCapacity);
	}
	
	/**
	 * @see java.lang.StringBuilder#trimToSize()
	 */
	public void trimToSize()
	{
		_builder.trimToSize();
	}
	
	/**
	 * @param newLength
	 * @see java.lang.StringBuilder#setLength(int)
	 */
	public void setLength(int newLength)
	{
		_builder.setLength(newLength);
	}
	
	/**
	 * @param obj
	 * @return boolean
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		return _builder.equals(obj);
	}
	
	/**
	 * @param obj
	 * @return this
	 * @see java.lang.StringBuilder#append(java.lang.Object)
	 */
	public L2TextBuilder append(Object obj)
	{
		_builder.append(obj);
		return this;
	}
	
	/**
	 * @param str
	 * @return this
	 * @see java.lang.StringBuilder#append(java.lang.String)
	 */
	public L2TextBuilder append(String str)
	{
		_builder.append(str);
		return this;
	}
	
	/**
	 * @param sb
	 * @return this
	 * @see java.lang.StringBuilder#append(java.lang.StringBuffer)
	 */
	public L2TextBuilder append(StringBuffer sb)
	{
		_builder.append(sb);
		return this;
	}
	
	/**
	 * @param index
	 * @return char
	 * @see java.lang.StringBuilder#charAt(int)
	 */
	public char charAt(int index)
	{
		return _builder.charAt(index);
	}
	
	/**
	 * @param s
	 * @return this
	 * @see java.lang.StringBuilder#append(java.lang.CharSequence)
	 */
	public L2TextBuilder append(CharSequence s)
	{
		_builder.append(s);
		return this;
	}
	
	/**
	 * @param index
	 * @return int
	 * @see java.lang.StringBuilder#codePointAt(int)
	 */
	public int codePointAt(int index)
	{
		return _builder.codePointAt(index);
	}
	
	/**
	 * @param s
	 * @param start
	 * @param end
	 * @return this
	 * @see java.lang.StringBuilder#append(java.lang.CharSequence, int, int)
	 */
	public L2TextBuilder append(CharSequence s, int start, int end)
	{
		_builder.append(s, start, end);
		return this;
	}
	
	/**
	 * @param str
	 * @return this
	 * @see java.lang.StringBuilder#append(char[])
	 */
	public L2TextBuilder append(char[] str)
	{
		_builder.append(str);
		return this;
	}
	
	/**
	 * @param str
	 * @param offset
	 * @param len
	 * @return this
	 * @see java.lang.StringBuilder#append(char[], int, int)
	 */
	public L2TextBuilder append(char[] str, int offset, int len)
	{
		_builder.append(str, offset, len);
		return this;
	}
	
	/**
	 * @param b
	 * @return this
	 * @see java.lang.StringBuilder#append(boolean)
	 */
	public L2TextBuilder append(boolean b)
	{
		_builder.append(b);
		return this;
	}
	
	/**
	 * @param c
	 * @return this
	 * @see java.lang.StringBuilder#append(char)
	 */
	public L2TextBuilder append(char c)
	{
		_builder.append(c);
		return this;
	}
	
	/**
	 * @param i
	 * @return this
	 * @see java.lang.StringBuilder#append(int)
	 */
	public L2TextBuilder append(int i)
	{
		_builder.append(i);
		return this;
	}
	
	/**
	 * @param index
	 * @return int
	 * @see java.lang.StringBuilder#codePointBefore(int)
	 */
	public int codePointBefore(int index)
	{
		return _builder.codePointBefore(index);
	}
	
	/**
	 * @param lng
	 * @return this
	 * @see java.lang.StringBuilder#append(long)
	 */
	public L2TextBuilder append(long lng)
	{
		_builder.append(lng);
		return this;
	}
	
	/**
	 * @param f
	 * @return this
	 * @see java.lang.StringBuilder#append(float)
	 */
	public L2TextBuilder append(float f)
	{
		_builder.append(f);
		return this;
	}
	
	/**
	 * @param d
	 * @return this
	 * @see java.lang.StringBuilder#append(double)
	 */
	public L2TextBuilder append(double d)
	{
		_builder.append(d);
		return this;
	}
	
	/**
	 * @param codePoint
	 * @return this
	 * @see java.lang.StringBuilder#appendCodePoint(int)
	 */
	public L2TextBuilder appendCodePoint(int codePoint)
	{
		_builder.appendCodePoint(codePoint);
		return this;
	}
	
	/**
	 * @param start
	 * @param end
	 * @return this
	 * @see java.lang.StringBuilder#delete(int, int)
	 */
	public L2TextBuilder delete(int start, int end)
	{
		_builder.delete(start, end);
		return this;
	}
	
	/**
	 * @param index
	 * @return this
	 * @see java.lang.StringBuilder#deleteCharAt(int)
	 */
	public L2TextBuilder deleteCharAt(int index)
	{
		_builder.deleteCharAt(index);
		return this;
	}
	
	/**
	 * @param start
	 * @param end
	 * @param str
	 * @return this
	 * @see java.lang.StringBuilder#replace(int, int, java.lang.String)
	 */
	public L2TextBuilder replace(int start, int end, String str)
	{
		_builder.replace(start, end, str);
		return this;
	}
	
	/**
	 * @param beginIndex
	 * @param endIndex
	 * @return int
	 * @see java.lang.StringBuilder#codePointCount(int, int)
	 */
	public int codePointCount(int beginIndex, int endIndex)
	{
		return _builder.codePointCount(beginIndex, endIndex);
	}
	
	/**
	 * @param index
	 * @param str
	 * @param offset
	 * @param len
	 * @return this
	 * @see java.lang.StringBuilder#insert(int, char[], int, int)
	 */
	public L2TextBuilder insert(int index, char[] str, int offset, int len)
	{
		_builder.insert(index, str, offset, len);
		return this;
	}
	
	/**
	 * @param offset
	 * @param obj
	 * @return this
	 * @see java.lang.StringBuilder#insert(int, java.lang.Object)
	 */
	public L2TextBuilder insert(int offset, Object obj)
	{
		_builder.insert(offset, obj);
		return this;
	}
	
	/**
	 * @param offset
	 * @param str
	 * @return this
	 * @see java.lang.StringBuilder#insert(int, java.lang.String)
	 */
	public L2TextBuilder insert(int offset, String str)
	{
		_builder.insert(offset, str);
		return this;
	}
	
	/**
	 * @param offset
	 * @param str
	 * @return this
	 * @see java.lang.StringBuilder#insert(int, char[])
	 */
	public L2TextBuilder insert(int offset, char[] str)
	{
		_builder.insert(offset, str);
		return this;
	}
	
	/**
	 * @param dstOffset
	 * @param s
	 * @return this
	 * @see java.lang.StringBuilder#insert(int, java.lang.CharSequence)
	 */
	public L2TextBuilder insert(int dstOffset, CharSequence s)
	{
		_builder.insert(dstOffset, s);
		return this;
	}
	
	/**
	 * @param index
	 * @param codePointOffset
	 * @return int
	 * @see java.lang.StringBuilder#offsetByCodePoints(int, int)
	 */
	public int offsetByCodePoints(int index, int codePointOffset)
	{
		return _builder.offsetByCodePoints(index, codePointOffset);
	}
	
	/**
	 * @param dstOffset
	 * @param s
	 * @param start
	 * @param end
	 * @return this
	 * @see java.lang.StringBuilder#insert(int, java.lang.CharSequence, int, int)
	 */
	public L2TextBuilder insert(int dstOffset, CharSequence s, int start, int end)
	{
		_builder.insert(dstOffset, s, start, end);
		return this;
	}
	
	/**
	 * @param offset
	 * @param b
	 * @return this
	 * @see java.lang.StringBuilder#insert(int, boolean)
	 */
	public L2TextBuilder insert(int offset, boolean b)
	{
		_builder.insert(offset, b);
		return this;
	}
	
	/**
	 * @param offset
	 * @param c
	 * @return this
	 * @see java.lang.StringBuilder#insert(int, char)
	 */
	public L2TextBuilder insert(int offset, char c)
	{
		_builder.insert(offset, c);
		return this;
	}
	
	/**
	 * @param offset
	 * @param i
	 * @return this
	 * @see java.lang.StringBuilder#insert(int, int)
	 */
	public L2TextBuilder insert(int offset, int i)
	{
		_builder.insert(offset, i);
		return this;
	}
	
	/**
	 * @param srcBegin
	 * @param srcEnd
	 * @param dst
	 * @param dstBegin
	 * @see java.lang.StringBuilder#getChars(int, int, char[], int)
	 */
	public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin)
	{
		_builder.getChars(srcBegin, srcEnd, dst, dstBegin);
	}
	
	/**
	 * @param offset
	 * @param l
	 * @return this
	 * @see java.lang.StringBuilder#insert(int, long)
	 */
	public L2TextBuilder insert(int offset, long l)
	{
		_builder.insert(offset, l);
		return this;
	}
	
	/**
	 * @param offset
	 * @param f
	 * @return this
	 * @see java.lang.StringBuilder#insert(int, float)
	 */
	public L2TextBuilder insert(int offset, float f)
	{
		_builder.insert(offset, f);
		return this;
	}
	
	/**
	 * @param offset
	 * @param d
	 * @return this
	 * @see java.lang.StringBuilder#insert(int, double)
	 */
	public L2TextBuilder insert(int offset, double d)
	{
		_builder.insert(offset, d);
		return this;
	}
	
	/**
	 * @param str
	 * @return int
	 * @see java.lang.StringBuilder#indexOf(java.lang.String)
	 */
	public int indexOf(String str)
	{
		return _builder.indexOf(str);
	}
	
	/**
	 * @param str
	 * @param fromIndex
	 * @return int
	 * @see java.lang.StringBuilder#indexOf(java.lang.String, int)
	 */
	public int indexOf(String str, int fromIndex)
	{
		return _builder.indexOf(str, fromIndex);
	}
	
	/**
	 * @param str
	 * @return int
	 * @see java.lang.StringBuilder#lastIndexOf(java.lang.String)
	 */
	public int lastIndexOf(String str)
	{
		return _builder.lastIndexOf(str);
	}
	
	/**
	 * @param str
	 * @param fromIndex
	 * @return int
	 * @see java.lang.StringBuilder#lastIndexOf(java.lang.String, int)
	 */
	public int lastIndexOf(String str, int fromIndex)
	{
		return _builder.lastIndexOf(str, fromIndex);
	}
	
	/**
	 * @return this
	 * @see java.lang.StringBuilder#reverse()
	 */
	public L2TextBuilder reverse()
	{
		_builder.reverse();
		return this;
	}
	
	/**
	 * @return String
	 * @see java.lang.StringBuilder#toString()
	 */
	@Override
	public String toString()
	{
		return _builder.toString();
	}
	
	/**
	 * @param index
	 * @param ch
	 * @see java.lang.StringBuilder#setCharAt(int, char)
	 */
	public void setCharAt(int index, char ch)
	{
		_builder.setCharAt(index, ch);
	}
	
	/**
	 * @param start
	 * @return String
	 * @see java.lang.StringBuilder#substring(int)
	 */
	public String substring(int start)
	{
		return _builder.substring(start);
	}
	
	/**
	 * @param start
	 * @param end
	 * @return CharSequence
	 * @see java.lang.StringBuilder#subSequence(int, int)
	 */
	public CharSequence subSequence(int start, int end)
	{
		return _builder.subSequence(start, end);
	}
	
	/**
	 * @param start
	 * @param end
	 * @return String
	 * @see java.lang.StringBuilder#substring(int, int)
	 */
	public String substring(int start, int end)
	{
		return _builder.substring(start, end);
	}
}
