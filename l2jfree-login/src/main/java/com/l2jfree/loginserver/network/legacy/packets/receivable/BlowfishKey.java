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
package com.l2jfree.loginserver.network.legacy.packets.receivable;

import java.nio.BufferUnderflowException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;

import com.l2jfree.loginserver.network.legacy.L2GameServer;
import com.l2jfree.loginserver.network.legacy.L2LegacyState;
import com.l2jfree.loginserver.network.legacy.packets.L2GameServerPacket;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix
 *
 */
public final class BlowfishKey extends L2GameServerPacket
{
	/** Packet's identifier */
	public static final int OPCODE = 0x00;
	
	private byte[] _enciphered;
	
	@Override
	protected int getMinimumLength()
	{
		return 5;
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException,
			RuntimeException
	{
		int size = buf.readD();
		_enciphered = buf.readB(new byte[size]);
		
		// Must stall any packets queued for read!
		L2GameServer lgs = getClient();
		byte[] padded;
		try
		{
			Cipher rsa = Cipher.getInstance("RSA/ECB/nopadding");
			rsa.init(Cipher.DECRYPT_MODE, getClient().getPrivateKey());
			padded = rsa.doFinal(_enciphered);
		}
		catch (GeneralSecurityException e)
		{
			_log.error("Failed to decipher the Blowfish key!", e);
			// game server already changed the key
			lgs.closeNow();
			return;
		}
		int i;
		for (i = 0; i < padded.length; i++)
			if (padded[i] != 0)
				break;
		byte[] key = new byte[padded.length - i];
		System.arraycopy(padded, i, key, 0, padded.length - i);
		
		lgs.initCipher(key);
		lgs.setState(L2LegacyState.KEYS_EXCHANGED);
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// time-critical packet
	}
}
