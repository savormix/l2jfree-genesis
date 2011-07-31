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
package com.l2jfree.security;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author savormix
 *
 */
public class CoreCipher
{
	private static final int VALID_KEY_LENGTH = 16;
	
	private final ByteBuffer _encKey;
	private final ByteBuffer _decKey;
	
	/**
	 * Constructs a cipher.<BR>
	 * The given buffer's position and mark will be modified.
	 * @param key a [read-only] cipher key
	 * @throws IllegalArgumentException if <TT>key.length != 16</TT>
	 */
	public CoreCipher(byte[] key) throws IllegalArgumentException
	{
		if (key.length != VALID_KEY_LENGTH)
			throw new IllegalArgumentException("Invalid key.");
		
		// Reordering must be here, as the cipher depends upon it
		_encKey = ByteBuffer.allocateDirect(VALID_KEY_LENGTH);
		_encKey.order(ByteOrder.LITTLE_ENDIAN).put(key).clear();
		_decKey = ByteBuffer.allocateDirect(VALID_KEY_LENGTH);
		_decKey.order(ByteOrder.LITTLE_ENDIAN).put(key).clear();
	}
	
	/**
	 * Returns the current encryption key.
	 * @return cipher's key
	 */
	public ByteBuffer getKey()
	{
		return _encKey;
	}
	
	/**
	 * Enciphers buffer's contents using the given key.<BR>
	 * Buffer's position will not be changed.
	 * <BR><BR>
	 * It is assumed that the packet's body starts at current position.
	 * @param buf a byte buffer
	 * @param size packet's size
	 */
	public void encipher(ByteBuffer buf, final int size)
	{
		encipher(buf, buf.position(), size);
	}
	
	/**
	 * Enciphers buffer's contents using the given key.<BR>
	 * Buffer's position will not be changed.
	 * @param buf a byte buffer
	 * @param offset offset to packet's body
	 * @param size packet's size
	 */
	public void encipher(ByteBuffer buf, final int offset, final int size)
	{
		int temp = 0;
		for (int i = 0; i < size; i++)
		{
			final int pos = offset + i;
			int temp2 = buf.get(pos) & 0xFF;
			temp = temp2 ^ _encKey.get(i & 15) ^ temp;
			buf.put(pos, (byte) temp);
		}
		
		_encKey.putInt(8, _encKey.getInt(8) + size);
	}
	
	/**
	 * Deciphers buffer's contents using the given key.<BR>
	 * Buffer's position will not be changed.
	 * <BR><BR>
	 * It is assumed that the packet's body starts at current position.
	 * @param buf a byte buffer
	 * @param size packet's size
	 */
	public void decipher(ByteBuffer buf, final int size)
	{
		decipher(buf, buf.position(), size);
	}
	
	/**
	 * Deciphers buffer's contents using the given key.<BR>
	 * Buffer's position will not be changed.
	 * @param buf a byte buffer
	 * @param offset offset to packet's body
	 * @param size packet's size
	 */
	public void decipher(ByteBuffer buf, final int offset, final int size)
	{
		int temp = 0;
		for (int i = 0; i < size; i++)
		{
			final int pos = offset + i;
			int temp2 = buf.get(pos) & 0xFF;
			buf.put(pos, (byte) (temp2 ^ _decKey.get(i & 15) ^ temp));
			temp = temp2;
		}
		
		_decKey.putInt(8, _decKey.getInt(8) + size);
	}
}
