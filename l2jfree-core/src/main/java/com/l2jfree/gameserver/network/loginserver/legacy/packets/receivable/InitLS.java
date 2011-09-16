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
package com.l2jfree.gameserver.network.loginserver.legacy.packets.receivable;

import java.math.BigInteger;
import java.nio.BufferUnderflowException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPublicKeySpec;

import com.l2jfree.gameserver.config.NetworkConfig;
import com.l2jfree.gameserver.config.ReportedConfig;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.L2LegacyLoginServerPacket;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.sendable.BlowFishKey;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.sendable.RequestAuth;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;
import com.l2jfree.util.Rnd;

/**
 * @author hex1r0
 */
public class InitLS extends L2LegacyLoginServerPacket
{
	public static final int OPCODE = 0;
	
	private int _rev;
	private byte[] _keyRSA;
	
	@Override
	protected int getMinimumLength()
	{
		return 5;
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		_rev = buf.readD();
		int size = buf.readD();
		_keyRSA = new byte[size];
		buf.readB(_keyRSA);
	}
	
	public int getRevision()
	{
		return _rev;
	}
	
	public byte[] getRSAKey()
	{
		return _keyRSA;
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		RSAPublicKey publicKey;
		try
		{
			KeyFactory kfac = KeyFactory.getInstance("RSA");
			BigInteger modulus = new BigInteger(_keyRSA);
			RSAPublicKeySpec kspec1 = new RSAPublicKeySpec(modulus, RSAKeyGenParameterSpec.F4);
			publicKey = (RSAPublicKey)kfac.generatePublic(kspec1);
		}
		catch (GeneralSecurityException e)
		{
			_log.warn("Troubles while init the public key send by login");
			return;
		}
		
		byte[] blowfishKey = Rnd.nextBytes(new byte[40]);
		
		sendPacket(new BlowFishKey(blowfishKey, publicKey));
		
		sendPacket(new RequestAuth(ReportedConfig.ID, ReportedConfig.ACCEPT_ALTERNATE_ID, new byte[10],
				NetworkConfig.LISTEN_PORT, true, ReportedConfig.MAX_ONLINE, NetworkConfig.SUBNETS, NetworkConfig.HOSTS));
	}
}
