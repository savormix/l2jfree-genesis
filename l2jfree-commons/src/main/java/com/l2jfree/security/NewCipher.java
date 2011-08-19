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

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A blowfish cipher with additional features.
 * Supports custom cyclic 32-bit packet checksum calculation/validation.
 * Supports legacy packet XORing with a specified 32-bit key.
 * <BR><BR>
 * XORing was an application-specific feature, so the replacement methods
 * are not present in this class.
 * <BR><BR>
 * Legacy methods may have out-of-date documentation. See the documentation
 * of the replacement method instead.
 * @see #encipher(ByteBuffer, int, int)
 * @see #decipher(ByteBuffer, int, int)
 * @see #appendChecksum(ByteBuffer, int, int)
 * @see #verifyChecksum(ByteBuffer, int, int)
 * @see #encXORPass(byte[], int, int, int)
 */
public class NewCipher
{
	private final byte[] _blowfishKey;
	
	BlowfishEngine _crypt;
	BlowfishEngine _decrypt;
	
	/**
	 * Constructs a Blowfish cipher.
	 * @param blowfishKey Blowfish key
	 */
	public NewCipher(byte[] blowfishKey)
	{
		_blowfishKey = blowfishKey;
		_crypt = new BlowfishEngine();
		_crypt.init(true, getBlowfishKey());
		_decrypt = new BlowfishEngine();
		_decrypt.init(false, getBlowfishKey());
	}
	
	/**
	 * An inherently unsafe method to initialize a Blowfish cipher.
	 * <BR><BR>
	 * If the given string contains non-ASCII characters, the
	 * cipher may not be initialized properly.
	 * @param key An ASCII string
	 * @see #NewCipher(byte[])
	 */
	@Deprecated
	public NewCipher(String key)
	{
		this(key.getBytes());
	}
	
	/**
	 * Returns the associated Blowfish key.
	 * @return Blowfish key
	 */
	public byte[] getBlowfishKey()
	{
		return _blowfishKey;
	}
	
	/**
	 * Verifies a packet's checksum.
	 * @deprecated Legacy method
	 * @param raw packet's body
	 * @return whether packet integrity is OK or not
	 * @see #verifyChecksum(ByteBuffer, int)
	 */
	@Deprecated
	public static boolean verifyChecksum(byte[] raw)
	{
		return NewCipher.verifyChecksum(raw, 0, raw.length);
	}
	
	/**
	 * Verifies a packet's checksum.
	 * @deprecated Legacy method
	 * @param raw data
	 * @param offset offset to the packet's body
	 * @param size packet's body size
	 * @return whether packet integrity is OK or not
	 * @see #verifyChecksum(ByteBuffer, int, int)
	 */
	@Deprecated
	public static boolean verifyChecksum(byte[] raw, final int offset, final int size)
	{
		return verifyChecksum(ByteBuffer.wrap(raw, offset, size), size);
	}
	
	/**
	 * Verifies a packet's checksum.
	 * <BR><BR>
	 * It is assumed that the packet's body starts at current position.
	 * @param buf byte buffer
	 * @param size packet's body size
	 * @return whether packet integrity is OK or not
	 */
	public static boolean verifyChecksum(ByteBuffer buf, final int size)
	{
		return verifyChecksum(buf, buf.position(), size);
	}
	
	/**
	 * Verifies a packet's checksum.
	 * @param buf byte buffer
	 * @param offset offset to a packet's body
	 * @param size packet's body size
	 * @return whether packet integrity is OK or not
	 */
	public static boolean verifyChecksum(ByteBuffer buf, final int offset, final int size)
	{
		// check if size is multiple of 4 (and > 0)
		if ((size & 3) != 0 || size <= 4)
			return false;
		
		int calculated = 0;
		int end = offset + size - 4; // ignore embedded checksum
		
		int pos;
		for (pos = offset; pos < end; pos += 4)
		{
			int i = buf.getInt(pos);
			calculated ^= i;
		}
		
		return (calculated == buf.getInt(pos));
	}
	
	/**
	 * Calculates and embeds a packet's checksum.
	 * @deprecated Legacy method
	 * @param raw packet's body with padding
	 * @see #appendChecksum(ByteBuffer, int)
	 */
	@Deprecated
	public static void appendChecksum(byte[] raw)
	{
		NewCipher.appendChecksum(raw, 0, raw.length);
	}
	
	/**
	 * Calculates and embeds a packet's checksum.
	 * @deprecated Legacy method
	 * @param raw data
	 * @param offset offset to a packet's body
	 * @param size packet's body size
	 * @see #appendChecksum(ByteBuffer, int, int)
	 */
	@Deprecated
	public static void appendChecksum(byte[] raw, final int offset, final int size)
	{
		appendChecksum(ByteBuffer.wrap(raw, offset, size), size);
	}
	
	/**
	 * Calculates and embeds a packet's checksum.<BR>
	 * Buffer's position will not be changed.
	 * <BR><BR>
	 * It is assumed that the packet's body starts at current position.
	 * @param buf byte buffer
	 * @param size packet's body size
	 */
	public static void appendChecksum(ByteBuffer buf, final int size)
	{
		appendChecksum(buf, buf.position(), size);
	}
	
	/**
	 * Calculates and embeds a packet's checksum.<BR>
	 * Buffer's position will not be changed.
	 * @param buf byte buffer
	 * @param offset offset to a packet's body
	 * @param size packet's body size
	 */
	public static void appendChecksum(ByteBuffer buf, final int offset, final int size)
	{
		int checksum = 0;
		int end = offset + size - 4; // ignore reserved bytes
		
		int pos;
		for (pos = offset; pos < end; pos += 4)
		{
			int i = buf.getInt(pos);
			checksum ^= i;
		}
		
		buf.putInt(pos, checksum);
	}
	
	/**
	 * Packet is first XOR encoded with <code>key</code> Then, the last 4 bytes are overwritten with the the XOR "key". Thus this assume that there is enough room for the key to fit without overwriting data.
	 * 
	 * @deprecated Legacy method
	 * @param raw The raw bytes to be encrypted
	 * @param key The 4 bytes (int) XOR key
	 */
	@Deprecated
	public static void encXORPass(byte[] raw, int key)
	{
		NewCipher.encXORPass(raw, 0, raw.length, key);
	}
	
	/**
	 * Packet is first XOR encoded with <code>key</code> Then, the last 4 bytes are overwritten with the the XOR "key". Thus this assume that there is enough room for the key to fit without overwriting data.
	 * 
	 * @deprecated Legacy method
	 * @param raw The raw bytes to be encrypted
	 * @param offset The begining of the data to be encrypted
	 * @param size Length of the data to be encrypted
	 * @param key The 4 bytes (int) XOR key
	 */
	@Deprecated
	public static void encXORPass(byte[] raw, final int offset, final int size, int key)
	{
		int stop = size - 8;
		int pos = 4 + offset;
		int edx;
		int ecx = key; // Initial xor key
		
		while (pos < stop)
		{
			edx = (raw[pos] & 0xFF);
			edx |= (raw[pos + 1] & 0xFF) << 8;
			edx |= (raw[pos + 2] & 0xFF) << 16;
			edx |= (raw[pos + 3] & 0xFF) << 24;
			
			ecx += edx;
			
			edx ^= ecx;
			
			raw[pos++] = (byte)(edx & 0xFF);
			raw[pos++] = (byte)(edx >> 8 & 0xFF);
			raw[pos++] = (byte)(edx >> 16 & 0xFF);
			raw[pos++] = (byte)(edx >> 24 & 0xFF);
		}

		raw[pos++] = (byte)(ecx & 0xFF);
		raw[pos++] = (byte)(ecx >> 8 & 0xFF);
		raw[pos++] = (byte)(ecx >> 16 & 0xFF);
		raw[pos++] = (byte)(ecx >> 24 & 0xFF);
	}
	
	/**
	 * Deciphers given byte array in blocks of 8 bytes using a Blowfish key.
	 * <BR><BR>
	 * If the last block contains less than 8 bytes, they are not deciphered.
	 * @deprecated Legacy method
	 * @param raw data
	 * @return deciphered data
	 * @throws IOException not thrown by this method
	 * @see #decipher(ByteBuffer, int)
	 */
	@Deprecated
	public byte[] decrypt(byte[] raw) throws IOException
	{
		byte[] result = new byte[raw.length];
		int count = raw.length / 8;
		
		for (int i = 0; i < count; i++)
		{
			_decrypt.processBlock(raw, i * 8, result, i * 8);
		}
		
		return result;
	}
	
	/**
	 * Deciphers given byte array in blocks of 8 bytes using a Blowfish key.
	 * <BR><BR>
	 * If the last block contains less than 8 bytes, they are not deciphered.
	 * @deprecated Legacy method
	 * @param raw data
	 * @param offset offset to packet's body
	 * @param size packet's body size
	 * @throws IOException not thrown by this method
	 * @see #decipher(ByteBuffer, int, int)
	 */
	@Deprecated
	public void decrypt(byte[] raw, final int offset, final int size) throws IOException
	{
		byte[] result = new byte[size];
		int count = size / 8;
		
		for (int i = 0; i < count; i++)
		{
			_decrypt.processBlock(raw, offset + i * 8, result, i * 8);
		}
		// can the crypt and decrypt go direct to the array
		System.arraycopy(result, 0, raw, offset, size);
	}
	
	/**
	 * Enciphers given byte array in blocks of 8 bytes using a Blowfish key.
	 * <BR><BR>
	 * If the last block contains less than 8 bytes, they are not enciphered.
	 * @deprecated Legacy method
	 * @param raw data
	 * @return deciphered data
	 * @throws IOException not thrown by this method
	 * @see #encipher(ByteBuffer, int)
	 */
	@Deprecated
	public byte[] crypt(byte[] raw) throws IOException
	{
		int count = raw.length / 8;
		byte[] result = new byte[raw.length];
		
		for (int i = 0; i < count; i++)
		{
			_crypt.processBlock(raw, i * 8, result, i * 8);
		}
		
		return result;
	}
	
	/**
	 * Enciphers given byte array in blocks of 8 bytes using a Blowfish key.
	 * <BR><BR>
	 * If the last block contains less than 8 bytes, they are not enciphered.
	 * @deprecated Legacy method
	 * @param raw data
	 * @param offset offset to packet's body
	 * @param size packet's body size
	 * @throws IOException not thrown by this method
	 * @see #encipher(ByteBuffer, int, int)
	 */
	@Deprecated
	public void crypt(byte[] raw, final int offset, final int size) throws IOException
	{
		int count = size / 8;
		byte[] result = new byte[size];
		
		for (int i = 0; i < count; i++)
		{
			_crypt.processBlock(raw, offset + i * 8, result, i * 8);
		}
		// can the crypt and decrypt go direct to the array
		System.arraycopy(result, 0, raw, offset, size);
	}
	
	/**
	 * Enciphers buffer's contents in blocks of 8 bytes using a Blowfish key.<BR>
	 * Buffer's position will not be changed.
	 * <BR><BR>
	 * If the last block contains less than 8 bytes, they are not enciphered.
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
	 * Enciphers buffer's contents in blocks of 8 bytes using a Blowfish key.<BR>
	 * Buffer's position will not be changed.
	 * <BR><BR>
	 * If the last block contains less than 8 bytes, they are not enciphered.
	 * @param buf a byte buffer
	 * @param offset offset to packet's body
	 * @param size packet's size
	 */
	public void encipher(ByteBuffer buf, final int offset, final int size)
	{
		int count = size / 8;
		for (int i = 0; i < count; i++)
			_crypt.processBlock(buf, offset + i * 8);
	}
	
	/**
	 * Deciphers buffer's contents in blocks of 8 bytes using a Blowfish key.<BR>
	 * Buffer's position will not be changed.
	 * <BR><BR>
	 * If the last block contains less than 8 bytes, they are not deciphered.
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
	 * Deciphers buffer's contents in blocks of 8 bytes using a Blowfish key.<BR>
	 * Buffer's position will not be changed.
	 * <BR><BR>
	 * If the last block contains less than 8 bytes, they are not deciphered.
	 * @param buf a byte buffer
	 * @param offset offset to packet's body
	 * @param size packet's size
	 */
	public void decipher(ByteBuffer buf, final int offset, final int size)
	{
		int count = size / 8;
		for (int i = 0; i < count; i++)
			_decrypt.processBlock(buf, offset + i * 8);
	}
}
