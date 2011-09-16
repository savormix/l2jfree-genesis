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
package com.l2jfree.gameserver.network.loginserver.legacy.packets.sendable;

import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

import com.l2jfree.gameserver.network.loginserver.legacy.L2LegacyLoginServer;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.L2LegacyGameServerPacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author hex1r0
 */
public class BlowFishKey extends L2LegacyGameServerPacket
{
	/** Packet's identifier */
	public static final int OPCODE = 0x00;
	
	private final byte[] _blowfishKey;
	private byte[] _encryptedKey;
	
	public BlowFishKey(byte[] blowfishKey, RSAPublicKey publicKey)
	{
		_blowfishKey = blowfishKey;
		try
		{
			Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
			rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
			_encryptedKey = rsaCipher.doFinal(blowfishKey);
		}
		catch (GeneralSecurityException e)
		{
			_log.fatal("Error While encrypting blowfish key for transmision (Crypt error)", e);
		}
	}
	
	@Override
	protected int getOpcode()
	{
		return OPCODE;
	}
	
	@Override
	protected void writeImpl(L2LegacyLoginServer client, MMOBuffer buf)
	{
		buf.writeD(_encryptedKey.length);
		buf.writeB(_encryptedKey);
		
		client.setNewBlowfishKey(_blowfishKey);
	}
}
