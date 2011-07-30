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
package com.l2jfree.gameserver.network.client;

import static com.l2jfree.gameserver.network.client.L2CoreClientState.CONNECTED;

import java.nio.ByteBuffer;

import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.gameserver.network.client.packets.receivable.ProtocolVersion;
import com.l2jfree.network.mmocore.IPacketHandler;
import com.l2jfree.util.HexUtil;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author savormix
 *
 */
public final class L2ClientPackets implements IPacketHandler<L2CoreClient, L2ClientPacket, L2ServerPacket>
{
	private static final L2Logger _log = L2Logger.getLogger(L2ClientPackets.class);
	
	private L2ClientPackets()
	{
		// singleton
	}
	
	@Override
	public L2ClientPacket handlePacket(ByteBuffer buf, L2CoreClient client, int opcode)
	{
		switch (opcode)
		{
		// TODO Auto-generated method stub
		case ProtocolVersion.OPCODE:
			if (isCorrectState(client, CONNECTED))
				return new ProtocolVersion();
			break;
		default:
			_log.info("Unhandled client packet: 0x" + HexUtil.fillHex(opcode, 2));
			return null;
		}
		_log.info("Packet in invalid state: 0x" + HexUtil.fillHex(opcode, 2));
		return null;
	}
	
	private boolean isCorrectState(L2CoreClient client, L2CoreClientState expectedState,
			L2CoreClientState... additionalStates)
	{
		if (client == null)
			return false;
		
		L2CoreClientState state = client.getState();
		if (state == expectedState)
			return true;
		
		for (L2CoreClientState lccs : additionalStates)
			if (state == lccs)
				return true;
		
		return false;
	}
	
	/**
	 * Returns a singleton object.
	 * @return an instance of this class
	 */
	public static L2ClientPackets getInstance()
	{
		return SingletonHolder._instance;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final L2ClientPackets _instance = new L2ClientPackets();
	}
}
