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

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import com.l2jfree.util.logging.L2Logger;

// FIXME new login server/protocol
public abstract class SendableBasePacket
{
	protected static final L2Logger _log = L2Logger.getLogger(SendableBasePacket.class);
	
	private final ByteArrayOutputStream _bao = new ByteArrayOutputStream();
	
	protected SendableBasePacket(int opCode)
	{
		writeC(opCode);
	}
	
	protected final void writeD(int value)
	{
		_bao.write(value & 0xff);
		_bao.write(value >> 8 & 0xff);
		_bao.write(value >> 16 & 0xff);
		_bao.write(value >> 24 & 0xff);
	}
	
	protected final void writeH(int value)
	{
		_bao.write(value & 0xff);
		_bao.write(value >> 8 & 0xff);
	}
	
	protected final void writeC(int value)
	{
		_bao.write(value & 0xff);
	}
	
	protected final void writeF(double org)
	{
		long value = Double.doubleToRawLongBits(org);
		_bao.write((int)(value & 0xff));
		_bao.write((int)(value >> 8 & 0xff));
		_bao.write((int)(value >> 16 & 0xff));
		_bao.write((int)(value >> 24 & 0xff));
		_bao.write((int)(value >> 32 & 0xff));
		_bao.write((int)(value >> 40 & 0xff));
		_bao.write((int)(value >> 48 & 0xff));
		_bao.write((int)(value >> 56 & 0xff));
	}
	
	protected final void writeS(String text)
	{
		if (text != null)
		{
			try
			{
				final byte[] bytes = text.getBytes("UTF-16LE");
				
				_bao.write(bytes, 0, bytes.length);
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		
		_bao.write(0);
		_bao.write(0);
	}
	
	protected final void writeB(byte[] bytes)
	{
		_bao.write(bytes, 0, bytes.length);
	}
	
	public byte[] getContent()
	{
		writeD(0x00); // reserve for checksum
		
		int padding = _bao.size() % 8;
		if (padding != 0)
			for (int i = padding; i < 8; i++)
				writeC(0x00);
		
		return _bao.toByteArray();
	}
}
