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
package com.l2jfree.gameserver.network.client.packets.sendable;

import java.nio.ByteBuffer;

import com.l2jfree.gameserver.network.client.L2CoreClient;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * The original name was <TT>KeyPacket</TT>.
 * @author savormix
 */
public final class ProtocolAnswer extends L2ServerPacket
{
	private static final int CLIENT_KEY_LENGTH = 8;
	
	private final boolean _compatible;
	private final byte[] _key;
	
	/**
	 * Constructs a packet to inform the client about protocol compatibility and
	 * key to be used for further communications.
	 * @param compatible whether protocol versions are compatible
	 * @param key complete cipher's key
	 */
	public ProtocolAnswer(boolean compatible, ByteBuffer key)
	{
		_compatible = compatible;
		_key = new byte[CLIENT_KEY_LENGTH];
		key.get(_key, 0, _key.length);
	}
	
	@Override
	protected int getMainOpcode()
	{
		return 0x2e;
	}
	
	@Override
	protected void writeImpl(L2CoreClient client, MMOBuffer buf)
	{
		buf.writeC(_compatible);
		buf.writeB(_key);
		
		buf.writeD(0x01);
		buf.writeD(0x01);
		buf.writeC(0x01);
		buf.writeD(0x00);
	}
}
