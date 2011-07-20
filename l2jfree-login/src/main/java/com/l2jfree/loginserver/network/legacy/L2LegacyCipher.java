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
package com.l2jfree.loginserver.network.legacy;

import java.io.IOException;

import com.l2jfree.network.mmocore.DataSizeHolder;
import com.l2jfree.security.NewCipher;
import com.l2jfree.util.HexUtil;

/**
 * @author savormix
 *
 */
public final class L2LegacyCipher
{
	private static final byte[] STARTER_BLOWFISH_KEY = HexUtil.HexStringToBytes(
			"5F 3B 76 2E 5D 30 35 2D 33 31 21 7C 2B 2D 25 78 54 21 5E 5B 24 00"
	);
	
	private NewCipher _cipher;
	
	/** Creates a cipher to secure communications. */
	public L2LegacyCipher()
	{
		_cipher = new NewCipher(STARTER_BLOWFISH_KEY);
	}
	
	/**
	 * Deciphers a block of <TT>size</TT> bytes starting at <TT>raw[offset]</TT>.
	 * @param raw byte array
	 * @param offset array offset
	 * @param size number of bytes to decipher
	 * @return whether data was successfully deciphered
	 * @throws IOException invalid <TT>size</TT> value
	 */
	public boolean decipher(byte[] raw, final int offset, DataSizeHolder size) throws IOException
	{
		final int dataSize = size.getSize();
		// remove checksum
		size.decreaseSize(4);
		
		getBlowfishCipher().decrypt(raw, offset, dataSize);
		return NewCipher.verifyChecksum(raw, offset, dataSize);
	}
	
	/**
	 * Enciphers a block of <TT>size</TT> bytes starting at <TT>raw[offset]</TT>.
	 * @param raw byte array
	 * @param offset array offset
	 * @param size number of bytes to encipher
	 * @return size after encryption
	 * @throws IOException invalid <TT>size</TT> value
	 */
	public int encipher(byte[] raw, final int offset, int size) throws IOException
	{
		// reserve checksum
		size += 4;
		// padding
		size += 8 - size % 8;
		
		NewCipher.appendChecksum(raw, offset, size);
		getBlowfishCipher().crypt(raw, offset, size);
		return size;
	}
	
	/**
	 * Returns the underlying Blowfish cipher.
	 * @return a Blowfish cipher
	 */
	public NewCipher getBlowfishCipher()
	{
		return _cipher;
	}
	
	/**
	 * Changes the Blowfish key.
	 * @param key Blowfish key
	 */
	public void setBlowfishKey(byte[] key)
	{
		_cipher = new NewCipher(key);
	}
}
