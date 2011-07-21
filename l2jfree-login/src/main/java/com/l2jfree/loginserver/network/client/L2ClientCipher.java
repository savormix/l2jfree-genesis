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
package com.l2jfree.loginserver.network.client;

import java.io.IOException;

import com.l2jfree.network.mmocore.DataSizeHolder;
import com.l2jfree.security.NewCipher;
import com.l2jfree.util.HexUtil;
import com.l2jfree.util.Rnd;

/**
 * @author savormix
 *
 */
public final class L2ClientCipher
{
	private static final byte[] ONE_TIME_BLOWFISH_KEY = HexUtil.HexStringToBytes(
			"6b 60 cb 5b 82 ce 90 b1 cc 2b 6c 55 6c 6c 6c 6c"
	);
	
	private final NewCipher _cipher;
	private boolean _firstTime;
	
	/**
	 * Creates a cipher to secure communications.
	 * @param blowfishKey a Blowfish key
	 */
	public L2ClientCipher(byte[] blowfishKey)
	{
		_cipher = new NewCipher(blowfishKey);
		_firstTime = true;
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
		// ignore padding
		size.setMaxPadding(8);
		
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
		
		if (isFirstTime())
		{
			// reserve for XOR "key"
			size += 4;
			
			// padding
			size += 8 - size % 8;
			NewCipher.encXORPass(raw, offset, size, Rnd.nextInt(Integer.MAX_VALUE));
			getOneTimeCipher().crypt(raw, offset, size);
			
			_firstTime = false;
		}
		else
		{
			// padding
			size += 8 - size % 8;
			NewCipher.appendChecksum(raw, offset, size);
			getBlowfishCipher().crypt(raw, offset, size);
		}
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
	
	private boolean isFirstTime()
	{
		return _firstTime;
	}
	
	private NewCipher getOneTimeCipher()
	{
		return new NewCipher(ONE_TIME_BLOWFISH_KEY);
	}
}
