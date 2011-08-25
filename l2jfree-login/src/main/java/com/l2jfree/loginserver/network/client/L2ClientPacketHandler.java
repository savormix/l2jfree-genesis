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
import com.l2jfree.network.mmocore.PacketHandler;

/**
 * This class maps Lineage II game client packet opcodes to login server's receivable packets.
 * 
 * @author savormix
 */
public final class L2ClientPacketHandler extends PacketHandler<L2Client, L2ClientPacket, L2ServerPacket>
{
	private L2ClientPacketHandler()
	{
		// singleton
	}
	
	@Override
	public L2ClientPacket handlePacket(ByteBuffer buf, L2Client client, int opcode)
	{
		switch (opcode)
		{
			case AuthGameGuard.OPCODE:
				if (client.stateEquals(L2ClientState.CONNECTED))
					return new AuthGameGuard();
				return invalidState(client, AuthGameGuard.class, opcode);
				
			case RequestAuthLogin.OPCODE:
				if (client.stateEquals(L2ClientState.GAMEGUARD_PASSED))
					return new RequestAuthLogin();
				return invalidState(client, RequestAuthLogin.class, opcode);
				
			case RequestSubmitCardNo.OPCODE:
				if (client.stateEquals(L2ClientState.LOGGED_IN))
					return new RequestSubmitCardNo();
				return invalidState(client, RequestSubmitCardNo.class, opcode);
				
			case RequestServerList.OPCODE:
				if (client.stateEquals(L2ClientState.LOGGED_IN))
					return new RequestServerList();
				return invalidState(client, RequestServerList.class, opcode);
				
			case RequestServerLogin.OPCODE:
				if (client.stateEquals(L2ClientState.VIEWING_LIST))
					return new RequestServerLogin();
				return invalidState(client, RequestServerLogin.class, opcode);
				
			default:
				return unknown(buf, client, opcode);
		}
	}
	
	/**
	 * Returns a singleton object.
	 * 
	 * @return an instance of this class
	 */
	public static L2ClientPacketHandler getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder
	{
		public static final L2ClientPacketHandler INSTANCE = new L2ClientPacketHandler();
	}
}
