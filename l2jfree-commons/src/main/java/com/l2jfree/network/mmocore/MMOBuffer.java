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
package com.l2jfree.network.mmocore;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import com.l2jfree.lang.L2Math;

/**
 * A wrapper class around a {@link ByteBuffer} to implement the "usual" API for reading-writing
 * data.
 * 
 * @author NB4L1
 */
public final class MMOBuffer
{
	private final StringBuilder _sb;
	private ByteBuffer _buffer;
	
	/**
	 * Creates a {@link java.nio.ByteBuffer} wrapper with additional read methods. <BR>
	 * <BR>
	 * Use {@link #setByteBuffer(ByteBuffer)} to dynamically wrap buffers.
	 */
	public MMOBuffer()
	{
		_sb = new StringBuilder();
	}
	
	/**
	 * Attaches a byte buffer to be wrapped.
	 * 
	 * @param buffer a byte buffer
	 */
	public void setByteBuffer(ByteBuffer buffer)
	{
		_buffer = buffer;
	}
	
	/**
	 * Returns the number of available bytes in the wrapped buffer.
	 * 
	 * @return number of bytes available
	 * @see java.nio.ByteBuffer#remaining()
	 */
	public int getAvailableBytes()
	{
		return _buffer.remaining();
	}
	
	/**
	 * Discards the following <TT>byteCount</TT> bytes.
	 * 
	 * @param byteCount number of bytes to skip
	 */
	public void skip(int byteCount)
	{
		if (_buffer.remaining() < byteCount)
			throw new BufferUnderflowException();
		
		_buffer.position(_buffer.position() + byteCount);
	}
	
	/** Discards all remaining bytes. */
	public void skipAll()
	{
		_buffer.position(_buffer.limit());
	}
	
	/**
	 * Reads <TT>dst.length</TT> bytes into the given array.
	 * 
	 * @param dst a byte array
	 * @return <TT>dst</TT>
	 * @see java.nio.ByteBuffer#get(byte[])
	 */
	public byte[] readB(byte[] dst)
	{
		_buffer.get(dst);
		return dst;
	}
	
	/**
	 * Reads <TT>len</TT> bytes into the given array starting at <TT>dst[offset]</TT>.
	 * 
	 * @param dst a byte array
	 * @param offset array offset
	 * @param len number of bytes to read
	 * @see java.nio.ByteBuffer#get(byte[], int, int)
	 */
	public void readB(byte[] dst, int offset, int len)
	{
		_buffer.get(dst, offset, len);
	}
	
	/**
	 * Reads one byte (char in C) as a signed integer.
	 * 
	 * @return a number from the interval [{@link java.lang.Byte#MIN_VALUE};
	 *         {@link java.lang.Byte#MAX_VALUE}]
	 */
	public int readC()
	{
		return _buffer.get();
	}
	
	/**
	 * Reads one byte (unsigned char in C) as an unsigned integer.
	 * 
	 * @return a number from the interval [0; 255]
	 */
	public int readUC()
	{
		return _buffer.get() & 0xFF;
	}
	
	/**
	 * Reads two bytes (a word) as a signed integer.
	 * 
	 * @return a number from the interval [{@link java.lang.Short#MIN_VALUE};
	 *         {@link java.lang.Short#MAX_VALUE}]
	 */
	public int readH()
	{
		return _buffer.getShort();
	}
	
	/**
	 * Reads two bytes (a word) as an unsigned integer.
	 * 
	 * @return a number from the interval [0; 65535]
	 */
	public int readUH()
	{
		return _buffer.getChar();
	}
	
	/**
	 * Reads four bytes (two words, a dword) as a signed integer.
	 * 
	 * @return a number from the interval [{@link java.lang.Integer#MIN_VALUE};
	 *         {@link java.lang.Integer#MAX_VALUE}]
	 */
	public int readD()
	{
		return _buffer.getInt();
	}
	
	/**
	 * Reads eight bytes (four words, a qword) as a signed integer.
	 * 
	 * @return a number from the interval [{@link java.lang.Long#MIN_VALUE};
	 *         {@link java.lang.Long#MAX_VALUE}]
	 */
	public long readQ()
	{
		return _buffer.getLong();
	}
	
	/**
	 * Reads eight bytes as a signed floating point number.
	 * 
	 * @return a number from the interval [{@link java.lang.Double#MIN_VALUE};
	 *         {@link java.lang.Double#MAX_VALUE}]
	 */
	public double readF()
	{
		return _buffer.getDouble();
	}
	
	/**
	 * Reads an UTF-16 encoded C string. <BR>
	 * <BR>
	 * <I>NOTE: The read string remains in memory until this method is called again.</I>
	 * 
	 * @return a string
	 */
	public String readS()
	{
		// clears the builder
		_sb.setLength(0);
		
		for (char c; (c = _buffer.getChar()) != 0;)
			_sb.append(c);
		
		return _sb.toString();
	}
	
	/**
	 * Writes <TT>count</TT> null bytes.
	 * 
	 * @param count byte count
	 */
	public void write0(int count)
	{
		writeB(new byte[count]);
	}
	
	/**
	 * Writes a boolean as a single byte.
	 * 
	 * @param value <TT>true</TT> or <TT>false</TT>
	 */
	public void writeC(boolean value)
	{
		_buffer.put((byte)(value ? 1 : 0));
	}
	
	/**
	 * Writes a signed integer as a single byte. <BR>
	 * <BR>
	 * If the given value is out of bounds, it is truncated.
	 * 
	 * @param value a number from the interval [{@link java.lang.Byte#MIN_VALUE};
	 *            {@link java.lang.Byte#MAX_VALUE}]
	 */
	public void writeC(int value)
	{
		_buffer.put((byte)value);
	}
	
	/**
	 * Writes a boolean as a word (two bytes).
	 * 
	 * @param value <TT>true</TT> or <TT>false</TT>
	 */
	public void writeH(boolean value)
	{
		_buffer.putShort((short)(value ? 1 : 0));
	}
	
	/**
	 * Writes a signed integer as a word (two bytes). <BR>
	 * <BR>
	 * If the given value is out of bounds, it is truncated.
	 * 
	 * @param value a number from the interval [{@link java.lang.Short#MIN_VALUE};
	 *            {@link java.lang.Short#MAX_VALUE}]
	 */
	public void writeH(int value)
	{
		_buffer.putShort((short)value);
	}
	
	/**
	 * Writes a boolean as a dword (four bytes).
	 * 
	 * @param value <TT>true</TT> or <TT>false</TT>
	 */
	public void writeD(boolean value)
	{
		_buffer.putInt(value ? 1 : 0);
	}
	
	/**
	 * Writes a signed integer as a dword (four bytes).
	 * 
	 * @param value a number from the interval [{@link java.lang.Integer#MIN_VALUE};
	 *            {@link java.lang.Integer#MAX_VALUE}]
	 */
	public void writeD(int value)
	{
		_buffer.putInt(value);
	}
	
	/**
	 * Writes a signed integer as a dword (four bytes). If the given value is not from the interval [{@link java.lang.Integer#MIN_VALUE};
	 * {@link java.lang.Integer#MAX_VALUE}], then it is increased/decreased to fit.
	 * 
	 * @param value a number from the interval [{@link java.lang.Long#MIN_VALUE};
	 *            {@link java.lang.Long#MAX_VALUE}]
	 */
	public void writeD(long value)
	{
		_buffer.putInt(L2Math.limit(Integer.MIN_VALUE, value, Integer.MAX_VALUE));
	}
	
	/**
	 * Writes a boolean as a qword (eight bytes).
	 * 
	 * @param value <TT>true</TT> or <TT>false</TT>
	 */
	public void writeQ(boolean value)
	{
		_buffer.putLong(value ? 1 : 0);
	}
	
	/**
	 * Writes a signed integer as a qword (eight bytes).
	 * 
	 * @param value a number from the interval [{@link java.lang.Long#MIN_VALUE};
	 *            {@link java.lang.Long#MAX_VALUE}]
	 */
	public void writeQ(long value)
	{
		_buffer.putLong(value);
	}
	
	/**
	 * Writes a signed floating point number into eight bytes.
	 * 
	 * @param value a number from the interval [{@link java.lang.Double#MIN_VALUE};
	 *            {@link java.lang.Double#MAX_VALUE}]
	 */
	public void writeF(double value)
	{
		_buffer.putDouble(value);
	}
	
	/**
	 * Writes <TT>data.length</TT> bytes from the given array.
	 * 
	 * @param data a byte array
	 */
	public void writeB(byte[] data)
	{
		_buffer.put(data);
	}
	
	/**
	 * Writes <TT>length</TT> bytes from the given array starting at the given <TT>offset</TT>.
	 * 
	 * @param data
	 * @param offset
	 * @param length
	 */
	public void writeB(byte[] data, int offset, int length)
	{
		_buffer.put(data, offset, length);
	}
	
	/**
	 * Writes an unterminated character sequence. <BR>
	 * <BR>
	 * A convenience method to write a part of the string to avoid instantiating numerous objects
	 * when concatenating. <BR>
	 * <BR>
	 * You <U>MUST</U> terminate this character sequence manually.
	 * 
	 * @param charSequence a character sequence
	 * @return <TT>this</TT>
	 * @see #writeS(CharSequence)
	 * @see #writeS(CharSequence[])
	 */
	public MMOBuffer append(CharSequence charSequence)
	{
		putChars(charSequence);
		
		return this;
	}
	
	/**
	 * Writes a C string. <BR>
	 * <BR>
	 * Given character sequences are written one after another in the given order. <BR>
	 * <BR>
	 * A convenience method to write all parts of a string without concatenating.
	 * 
	 * @param charSequences parts of a string in the correct order
	 * @see #append(CharSequence)
	 * @see #writeS(CharSequence)
	 */
	public void writeS(CharSequence... charSequences)
	{
		if (charSequences != null)
			for (CharSequence charSequence : charSequences)
				putChars(charSequence);
		
		_buffer.putChar('\000');
	}
	
	/**
	 * Writes a C string.
	 * 
	 * @param charSequence a character sequence
	 * @see #append(CharSequence)
	 * @see #writeS(CharSequence[])
	 */
	public void writeS(CharSequence charSequence)
	{
		putChars(charSequence);
		
		_buffer.putChar('\000');
	}
	
	private void putChars(CharSequence charSequence)
	{
		if (charSequence == null)
			return;
		
		final int length = charSequence.length();
		for (int i = 0; i < length; i++)
			_buffer.putChar(charSequence.charAt(i));
	}
}
