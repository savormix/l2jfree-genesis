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
package com.l2jfree.network;

import java.io.UnsupportedEncodingException;

import com.l2jfree.util.logging.L2Logger;

// FIXME new login server/protocol
public abstract class ReceivableBasePacket
{
	protected static final L2Logger _log = L2Logger.getLogger(ReceivableBasePacket.class);
	
	private final byte[] _data;
	private int _off;
	
	protected ReceivableBasePacket(byte[] decrypted)
	{
		_data = decrypted;
		_off = 1;
	}
	
	protected final boolean canRead()
	{
		return _off < _data.length;
	}
	
	protected final boolean canRead(int bytes)
	{
		return _off + bytes <= _data.length;
	}
	
	protected final int readD()
	{
		int result = _data[_off++] & 0xff;
		result |= _data[_off++] << 8 & 0xff00;
		result |= _data[_off++] << 16 & 0xff0000;
		result |= _data[_off++] << 24 & 0xff000000;
		return result;
	}
	
	protected final int readH()
	{
		int result = _data[_off++] & 0xff;
		result |= _data[_off++] << 8 & 0xff00;
		return result;
	}
	
	protected final int readC()
	{
		int result = _data[_off++] & 0xff;
		return result;
	}
	
	protected final double readF()
	{
		long result = _data[_off++] & 0xff;
		result |= _data[_off++] << 8 & 0xff00;
		result |= _data[_off++] << 16 & 0xff0000;
		result |= _data[_off++] << 24 & 0xff000000;
		result |= _data[_off++] << 32 & 0xff00000000l;
		result |= _data[_off++] << 40 & 0xff0000000000l;
		result |= _data[_off++] << 48 & 0xff000000000000l;
		result |= _data[_off++] << 56 & 0xff00000000000000l;
		return Double.longBitsToDouble(result);
	}
	
	protected final String readS()
	{
		String result = null;
		try
		{
			result = new String(_data, _off, _data.length - _off, "UTF-16LE");
			result = result.substring(0, result.indexOf(0x00));
			_off += result.length() * 2 + 2;
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	protected final byte[] readB(int length)
	{
		byte[] result = new byte[length];
		System.arraycopy(_data, _off, result, 0, length);
		_off += length;
		return result;
	}
}
