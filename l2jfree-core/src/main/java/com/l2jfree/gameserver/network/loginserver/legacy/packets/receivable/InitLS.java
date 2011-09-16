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
import com.l2jfree.gameserver.network.loginserver.legacy.L2LegacyLoginServerState;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.L2LegacyLoginServerPacket;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.sendable.AuthRequest;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.sendable.BlowFishKey;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;
import com.l2jfree.util.Rnd;

/**
 * @author hex1r0
 */
public class InitLS extends L2LegacyLoginServerPacket
{
	public static final int OPCODE = 0x00;
	
	@Override
	protected int getMinimumLength()
	{
		return READ_D + READ_D;
	}
	
	private int _rev;
	private byte[] _keyRSA;
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		_rev = buf.readD();
		int size = buf.readD();
		_keyRSA = buf.readB(size);
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO
		_log.info("InitLS - revision: " + _rev);
		
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
		
		sendPacket(new AuthRequest(ReportedConfig.ID, ReportedConfig.ACCEPT_ALTERNATE_ID, new byte[10],
				NetworkConfig.LISTEN_PORT, true, ReportedConfig.MAX_ONLINE, NetworkConfig.SUBNETS, NetworkConfig.HOSTS));
		getClient().setState(L2LegacyLoginServerState.KEYS_EXCHANGED);
	}
	
	@Override
	protected boolean blockReadingUntilExecutionIsFinished()
	{
		return true;
	}
}
