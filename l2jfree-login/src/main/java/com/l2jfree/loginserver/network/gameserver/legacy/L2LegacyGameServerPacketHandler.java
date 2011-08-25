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
package com.l2jfree.loginserver.network.gameserver.legacy;

import java.nio.ByteBuffer;

import com.l2jfree.loginserver.network.gameserver.legacy.packets.L2LegacyGameServerPacket;
import com.l2jfree.loginserver.network.gameserver.legacy.packets.L2LegacyLoginServerPacket;
import com.l2jfree.loginserver.network.gameserver.legacy.packets.receivable.BlowfishKey;
import com.l2jfree.loginserver.network.gameserver.legacy.packets.receivable.ChangeAccessLevel;
import com.l2jfree.loginserver.network.gameserver.legacy.packets.receivable.GameServerAuth;
import com.l2jfree.loginserver.network.gameserver.legacy.packets.receivable.PlayerAuthRequest;
import com.l2jfree.loginserver.network.gameserver.legacy.packets.receivable.PlayerLogout;
import com.l2jfree.loginserver.network.gameserver.legacy.packets.receivable.PlayerTraceRt;
import com.l2jfree.loginserver.network.gameserver.legacy.packets.receivable.PlayersInGame;
import com.l2jfree.loginserver.network.gameserver.legacy.packets.receivable.ServerStatus;
import com.l2jfree.network.mmocore.PacketHandler;

/**
 * @author savormix
 */
public final class L2LegacyGameServerPacketHandler extends PacketHandler<L2LegacyGameServer, L2LegacyGameServerPacket, L2LegacyLoginServerPacket>
{
	private L2LegacyGameServerPacketHandler()
	{
		// singleton
	}
	
	@Override
	public L2LegacyGameServerPacket handlePacket(ByteBuffer buf, L2LegacyGameServer client, int opcode)
	{
		switch (opcode)
		{
			case BlowfishKey.OPCODE:
				if (client.stateEquals(L2LegacyGameServerState.CONNECTED))
					return new BlowfishKey();
				return invalidState(client, BlowfishKey.class, opcode);
				
			case GameServerAuth.OPCODE:
				if (client.stateEquals(L2LegacyGameServerState.KEYS_EXCHANGED))
					return new GameServerAuth();
				return invalidState(client, GameServerAuth.class, opcode);
				
			default:
				if (!client.stateEquals(L2LegacyGameServerState.AUTHED))
					return invalidState(client, opcode);
				
				switch (opcode)
				{
					case PlayersInGame.OPCODE:
						return new PlayersInGame();
						
					case PlayerLogout.OPCODE:
						return new PlayerLogout();
						
					case ChangeAccessLevel.OPCODE:
						return new ChangeAccessLevel();
						
					case PlayerAuthRequest.OPCODE:
						return new PlayerAuthRequest();
						
					case ServerStatus.OPCODE:
						return new ServerStatus();
						
					case PlayerTraceRt.OPCODE:
						return new PlayerTraceRt();
						
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
	public static L2LegacyGameServerPacketHandler getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder
	{
		public static final L2LegacyGameServerPacketHandler INSTANCE = new L2LegacyGameServerPacketHandler();
	}
}
