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
@SuppressWarnings("javadoc")
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
	 * @return
	 * @see java.lang.StringBuilder#length()
	 */
	public int length()
	{
		return _builder.length();
	}
	
	/**
	 * @return
	 * @see java.lang.StringBuilder#capacity()
	 */
	public int capacity()
	{
		return _builder.capacity();
	}
	
	/**
	 * @return
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
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		return _builder.equals(obj);
	}
	
	/**
	 * @param obj
	 * @return
	 * @see java.lang.StringBuilder#append(java.lang.Object)
	 */
	public StringBuilder append(Object obj)
	{
		return _builder.append(obj);
	}
	
	/**
	 * @param str
	 * @return
	 * @see java.lang.StringBuilder#append(java.lang.String)
	 */
	public StringBuilder append(String str)
	{
		return _builder.append(str);
	}
	
	/**
	 * @param sb
	 * @return
	 * @see java.lang.StringBuilder#append(java.lang.StringBuffer)
	 */
	public StringBuilder append(StringBuffer sb)
	{
		return _builder.append(sb);
	}
	
	/**
	 * @param index
	 * @return
	 * @see java.lang.StringBuilder#charAt(int)
	 */
	public char charAt(int index)
	{
		return _builder.charAt(index);
	}
	
	/**
	 * @param s
	 * @return
	 * @see java.lang.StringBuilder#append(java.lang.CharSequence)
	 */
	public StringBuilder append(CharSequence s)
	{
		return _builder.append(s);
	}
	
	/**
	 * @param index
	 * @return
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
	 * @return
	 * @see java.lang.StringBuilder#append(java.lang.CharSequence, int, int)
	 */
	public StringBuilder append(CharSequence s, int start, int end)
	{
		return _builder.append(s, start, end);
	}
	
	/**
	 * @param str
	 * @return
	 * @see java.lang.StringBuilder#append(char[])
	 */
	public StringBuilder append(char[] str)
	{
		return _builder.append(str);
	}
	
	/**
	 * @param str
	 * @param offset
	 * @param len
	 * @return
	 * @see java.lang.StringBuilder#append(char[], int, int)
	 */
	public StringBuilder append(char[] str, int offset, int len)
	{
		return _builder.append(str, offset, len);
	}
	
	/**
	 * @param b
	 * @return
	 * @see java.lang.StringBuilder#append(boolean)
	 */
	public StringBuilder append(boolean b)
	{
		return _builder.append(b);
	}
	
	/**
	 * @param c
	 * @return
	 * @see java.lang.StringBuilder#append(char)
	 */
	public StringBuilder append(char c)
	{
		return _builder.append(c);
	}
	
	/**
	 * @param i
	 * @return
	 * @see java.lang.StringBuilder#append(int)
	 */
	public StringBuilder append(int i)
	{
		return _builder.append(i);
	}
	
	/**
	 * @param index
	 * @return
	 * @see java.lang.StringBuilder#codePointBefore(int)
	 */
	public int codePointBefore(int index)
	{
		return _builder.codePointBefore(index);
	}
	
	/**
	 * @param lng
	 * @return
	 * @see java.lang.StringBuilder#append(long)
	 */
	public StringBuilder append(long lng)
	{
		return _builder.append(lng);
	}
	
	/**
	 * @param f
	 * @return
	 * @see java.lang.StringBuilder#append(float)
	 */
	public StringBuilder append(float f)
	{
		return _builder.append(f);
	}
	
	/**
	 * @param d
	 * @return
	 * @see java.lang.StringBuilder#append(double)
	 */
	public StringBuilder append(double d)
	{
		return _builder.append(d);
	}
	
	/**
	 * @param codePoint
	 * @return
	 * @see java.lang.StringBuilder#appendCodePoint(int)
	 */
	public StringBuilder appendCodePoint(int codePoint)
	{
		return _builder.appendCodePoint(codePoint);
	}
	
	/**
	 * @param start
	 * @param end
	 * @return
	 * @see java.lang.StringBuilder#delete(int, int)
	 */
	public StringBuilder delete(int start, int end)
	{
		return _builder.delete(start, end);
	}
	
	/**
	 * @param index
	 * @return
	 * @see java.lang.StringBuilder#deleteCharAt(int)
	 */
	public StringBuilder deleteCharAt(int index)
	{
		return _builder.deleteCharAt(index);
	}
	
	/**
	 * @param start
	 * @param end
	 * @param str
	 * @return
	 * @see java.lang.StringBuilder#replace(int, int, java.lang.String)
	 */
	public StringBuilder replace(int start, int end, String str)
	{
		return _builder.replace(start, end, str);
	}
	
	/**
	 * @param beginIndex
	 * @param endIndex
	 * @return
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
	 * @return
	 * @see java.lang.StringBuilder#insert(int, char[], int, int)
	 */
	public StringBuilder insert(int index, char[] str, int offset, int len)
	{
		return _builder.insert(index, str, offset, len);
	}
	
	/**
	 * @param offset
	 * @param obj
	 * @return
	 * @see java.lang.StringBuilder#insert(int, java.lang.Object)
	 */
	public StringBuilder insert(int offset, Object obj)
	{
		return _builder.insert(offset, obj);
	}
	
	/**
	 * @param offset
	 * @param str
	 * @return
	 * @see java.lang.StringBuilder#insert(int, java.lang.String)
	 */
	public StringBuilder insert(int offset, String str)
	{
		return _builder.insert(offset, str);
	}
	
	/**
	 * @param offset
	 * @param str
	 * @return
	 * @see java.lang.StringBuilder#insert(int, char[])
	 */
	public StringBuilder insert(int offset, char[] str)
	{
		return _builder.insert(offset, str);
	}
	
	/**
	 * @param dstOffset
	 * @param s
	 * @return
	 * @see java.lang.StringBuilder#insert(int, java.lang.CharSequence)
	 */
	public StringBuilder insert(int dstOffset, CharSequence s)
	{
		return _builder.insert(dstOffset, s);
	}
	
	/**
	 * @param index
	 * @param codePointOffset
	 * @return
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
	 * @return
	 * @see java.lang.StringBuilder#insert(int, java.lang.CharSequence, int, int)
	 */
	public StringBuilder insert(int dstOffset, CharSequence s, int start, int end)
	{
		return _builder.insert(dstOffset, s, start, end);
	}
	
	/**
	 * @param offset
	 * @param b
	 * @return
	 * @see java.lang.StringBuilder#insert(int, boolean)
	 */
	public StringBuilder insert(int offset, boolean b)
	{
		return _builder.insert(offset, b);
	}
	
	/**
	 * @param offset
	 * @param c
	 * @return
	 * @see java.lang.StringBuilder#insert(int, char)
	 */
	public StringBuilder insert(int offset, char c)
	{
		return _builder.insert(offset, c);
	}
	
	/**
	 * @param offset
	 * @param i
	 * @return
	 * @see java.lang.StringBuilder#insert(int, int)
	 */
	public StringBuilder insert(int offset, int i)
	{
		return _builder.insert(offset, i);
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
	 * @return
	 * @see java.lang.StringBuilder#insert(int, long)
	 */
	public StringBuilder insert(int offset, long l)
	{
		return _builder.insert(offset, l);
	}
	
	/**
	 * @param offset
	 * @param f
	 * @return
	 * @see java.lang.StringBuilder#insert(int, float)
	 */
	public StringBuilder insert(int offset, float f)
	{
		return _builder.insert(offset, f);
	}
	
	/**
	 * @param offset
	 * @param d
	 * @return
	 * @see java.lang.StringBuilder#insert(int, double)
	 */
	public StringBuilder insert(int offset, double d)
	{
		return _builder.insert(offset, d);
	}
	
	/**
	 * @param str
	 * @return
	 * @see java.lang.StringBuilder#indexOf(java.lang.String)
	 */
	public int indexOf(String str)
	{
		return _builder.indexOf(str);
	}
	
	/**
	 * @param str
	 * @param fromIndex
	 * @return
	 * @see java.lang.StringBuilder#indexOf(java.lang.String, int)
	 */
	public int indexOf(String str, int fromIndex)
	{
		return _builder.indexOf(str, fromIndex);
	}
	
	/**
	 * @param str
	 * @return
	 * @see java.lang.StringBuilder#lastIndexOf(java.lang.String)
	 */
	public int lastIndexOf(String str)
	{
		return _builder.lastIndexOf(str);
	}
	
	/**
	 * @param str
	 * @param fromIndex
	 * @return
	 * @see java.lang.StringBuilder#lastIndexOf(java.lang.String, int)
	 */
	public int lastIndexOf(String str, int fromIndex)
	{
		return _builder.lastIndexOf(str, fromIndex);
	}
	
	/**
	 * @return
	 * @see java.lang.StringBuilder#reverse()
	 */
	public StringBuilder reverse()
	{
		return _builder.reverse();
	}
	
	/**
	 * @return
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
	 * @return
	 * @see java.lang.StringBuilder#substring(int)
	 */
	public String substring(int start)
	{
		return _builder.substring(start);
	}
	
	/**
	 * @param start
	 * @param end
	 * @return
	 * @see java.lang.StringBuilder#subSequence(int, int)
	 */
	public CharSequence subSequence(int start, int end)
	{
		return _builder.subSequence(start, end);
	}
	
	/**
	 * @param start
	 * @param end
	 * @return
	 * @see java.lang.StringBuilder#substring(int, int)
	 */
	public String substring(int start, int end)
	{
		return _builder.substring(start, end);
	}
}
