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
package com.l2jfree.loginserver.network.gameserver.legacy.packets.sendable;

import java.security.interfaces.RSAPublicKey;

import com.l2jfree.loginserver.network.gameserver.legacy.L2LegacyGameServer;
import com.l2jfree.loginserver.network.gameserver.legacy.packets.L2LegacyLoginServerPacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix
 */
public final class InitLS extends L2LegacyLoginServerPacket
{
	private static final int LEGACY_PROTOCOL_REVISION = 0x0104;
	
	private final byte[] _publicKey;
	
	/**
	 * Constructs the packet to transmit the public key.
	 * 
	 * @param publicKey public key
	 */
	public InitLS(RSAPublicKey publicKey)
	{
		_publicKey = publicKey.getModulus().toByteArray();
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x00;
	}
	
	@Override
	protected void writeImpl(L2LegacyGameServer client, MMOBuffer buf)
	{
		buf.writeD(LEGACY_PROTOCOL_REVISION);
		buf.writeD(_publicKey.length);
		buf.writeB(_publicKey);
	}
}
