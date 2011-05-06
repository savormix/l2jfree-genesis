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

import javolution.text.TextBuilder;

public final class MMOBuffer
{
	private ByteBuffer _buffer;
	
	public MMOBuffer()
	{
	}
	
	public void setByteBuffer(ByteBuffer buffer)
	{
		_buffer = buffer;
	}
	
	public int getAvailableBytes()
	{
		return _buffer.remaining();
	}
	
	// ======================================================================
	// read methods
	
	public void skip(int bytes)
	{
		if (_buffer.remaining() < bytes)
			throw new BufferUnderflowException();
		
		_buffer.position(_buffer.position() + bytes);
	}
	
	public void skipAll()
	{
		_buffer.position(_buffer.limit());
	}
	
	public void readB(byte[] dst)
	{
		_buffer.get(dst);
	}
	
	public void readB(byte[] dst, int offset, int len)
	{
		_buffer.get(dst, offset, len);
	}
	
	public int readC()
	{
		return _buffer.get() & 0xFF;
	}
	
	public int readH()
	{
		return _buffer.getShort() & 0xFFFF;
	}
	
	public int readD()
	{
		return _buffer.getInt();
	}
	
	public long readQ()
	{
		return _buffer.getLong();
	}
	
	public double readF()
	{
		return _buffer.getDouble();
	}
	
	public String readS()
	{
		TextBuilder tb = TextBuilder.newInstance();
		
		for (char c; (c = _buffer.getChar()) != 0;)
			tb.append(c);
		
		String str = tb.toString();
		TextBuilder.recycle(tb);
		return str;
	}
	
	// ======================================================================
	// write methods
	
	public void writeC(boolean value)
	{
		_buffer.put((byte)(value ? 1 : 0));
	}
	
	public void writeC(int value)
	{
		_buffer.put((byte)value);
	}
	
	public void writeH(boolean value)
	{
		_buffer.putShort((short)(value ? 1 : 0));
	}
	
	public void writeH(int value)
	{
		_buffer.putShort((short)value);
	}
	
	public void writeD(boolean value)
	{
		_buffer.putInt(value ? 1 : 0);
	}
	
	public void writeD(int value)
	{
		_buffer.putInt(value);
	}
	
	public void writeD(long value)
	{
		_buffer.putInt(value < Integer.MAX_VALUE ? (int)value : Integer.MAX_VALUE);
	}
	
	public void writeQ(boolean value)
	{
		_buffer.putLong(value ? 1 : 0);
	}
	
	public void writeQ(long value)
	{
		_buffer.putLong(value);
	}
	
	public void writeF(double value)
	{
		_buffer.putDouble(value);
	}
	
	public void writeB(byte[] data)
	{
		_buffer.put(data);
	}
	
	/**
	 * Same as {@link MMOBuffer#writeS(CharSequence)}, except that <code>'\000'</code> won't be written automatically.<br>
	 * So this way there is no need to concat multiple Strings into a single one.
	 * 
	 * @param charSequence
	 */
	public MMOBuffer append(CharSequence charSequence)
	{
		putChars(charSequence);
		
		return this;
	}
	
	public void writeS(CharSequence... charSequences)
	{
		if (charSequences != null)
			for (CharSequence charSequence : charSequences)
				putChars(charSequence);
		
		_buffer.putChar('\000');
	}
	
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
