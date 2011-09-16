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
package com.l2jfree.gameserver.network.loginserver.legacy;

import java.nio.ByteBuffer;

import com.l2jfree.gameserver.network.loginserver.legacy.packets.L2LegacyGameServerPacket;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.L2LegacyLoginServerPacket;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.receivable.AuthResponse;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.receivable.InitLS;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.receivable.KickPlayer;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.receivable.LoginServerFail;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.receivable.PlayerAuthResponse;
import com.l2jfree.network.mmocore.PacketHandler;

/**
 * @author hex1r0
 */
public final class L2LegacyLoginServerPacketHandler extends
		PacketHandler<L2LegacyLoginServer, L2LegacyLoginServerPacket, L2LegacyGameServerPacket>
{
	private L2LegacyLoginServerPacketHandler()
	{
		// singleton
	}
	
	@Override
	public L2LegacyLoginServerPacket handlePacket(ByteBuffer buf, L2LegacyLoginServer client, int opcode)
	{
		switch (opcode)
		{
			case InitLS.OPCODE:
				if (client.stateEquals(L2LegacyLoginServerState.CONNECTED))
					return new InitLS();
				return invalidState(client, InitLS.class, opcode);
				
			case LoginServerFail.OPCODE:
				if (client.stateEquals(L2LegacyLoginServerState.KEYS_EXCHANGED))
					return new LoginServerFail();
				return invalidState(client, LoginServerFail.class, opcode);
				
			case AuthResponse.OPCODE:
				if (client.stateEquals(L2LegacyLoginServerState.KEYS_EXCHANGED))
					return new AuthResponse();
				return invalidState(client, AuthResponse.class, opcode);
				
			default:
				if (!client.stateEquals(L2LegacyLoginServerState.AUTHED))
					return invalidState(client, opcode);
				
				switch (opcode)
				{
					case PlayerAuthResponse.OPCODE:
						return new PlayerAuthResponse();
						
					case KickPlayer.OPCODE:
						return new KickPlayer();
						
					default:
						return unknown(buf, client, opcode);
				}
		}
	}
	
	/**
	 * Returns a singleton object.
	 * 
	 * @return an instance of this class
	 */
	public static L2LegacyLoginServerPacketHandler getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder
	{
		public static final L2LegacyLoginServerPacketHandler INSTANCE = new L2LegacyLoginServerPacketHandler();
	}
}
