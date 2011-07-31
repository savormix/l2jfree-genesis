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

import java.nio.ByteBuffer;

import com.l2jfree.loginserver.network.client.packets.L2ClientPacket;
import com.l2jfree.loginserver.network.client.packets.L2ServerPacket;
import com.l2jfree.loginserver.network.client.packets.receivable.AuthGameGuard;
import com.l2jfree.loginserver.network.client.packets.receivable.RequestAuthLogin;
import com.l2jfree.loginserver.network.client.packets.receivable.RequestServerList;
import com.l2jfree.loginserver.network.client.packets.receivable.RequestServerLogin;
import com.l2jfree.loginserver.network.client.packets.receivable.RequestSubmitCardNo;
import com.l2jfree.network.mmocore.IPacketHandler;
import com.l2jfree.util.HexUtil;
import com.l2jfree.util.logging.L2Logger;

/**
 * This class maps Lineage II game client packet opcodes to
 * login server's receivable packets.
 * @author savormix
 */
public final class L2ClientPackets implements IPacketHandler<L2LoginClient, L2ClientPacket, L2ServerPacket>
{
	private static final L2Logger _log = L2Logger.getLogger(L2ClientPackets.class);
	
	private L2ClientPackets()
	{
		// singleton
	}
	
	@Override
	public L2ClientPacket handlePacket(ByteBuffer buf, L2LoginClient client,
			int opcode)
	{
		switch (opcode)
		{
		case AuthGameGuard.OPCODE:
			if (client.getState() == L2LoginClientState.CONNECTED)
				return new AuthGameGuard();
			break;
		case RequestAuthLogin.OPCODE:
			if (client.getState() == L2LoginClientState.GAMEGUARD_PASSED)
				return new RequestAuthLogin();
			break;
		case RequestSubmitCardNo.OPCODE:
			if (client.getState() == L2LoginClientState.LOGGED_IN)
				return new RequestSubmitCardNo();
			break;
		case RequestServerList.OPCODE:
			if (client.getState() == L2LoginClientState.LOGGED_IN)
				return new RequestServerList();
			break;
		case RequestServerLogin.OPCODE:
			if (client.getState() == L2LoginClientState.VIEWING_LIST)
				return new RequestServerLogin();
			break;
		default:
			// unknown packet
			_log.info("Unknown client packet: 0x" + HexUtil.fillHex(opcode, 2));
			return null;
		}
		// invalid state
		_log.info("Client packet in invalid state: 0x" + HexUtil.fillHex(opcode, 2));
		return null;
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
