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
package com.l2jfree.loginserver.network.legacy;

import java.nio.ByteBuffer;

import com.l2jfree.loginserver.network.legacy.packets.L2GameServerPacket;
import com.l2jfree.loginserver.network.legacy.packets.L2LoginServerPacket;
import com.l2jfree.loginserver.network.legacy.packets.receivable.BlowfishKey;
import com.l2jfree.loginserver.network.legacy.packets.receivable.ChangeAccessLevel;
import com.l2jfree.loginserver.network.legacy.packets.receivable.GameServerAuth;
import com.l2jfree.loginserver.network.legacy.packets.receivable.PlayerAuthRequest;
import com.l2jfree.loginserver.network.legacy.packets.receivable.PlayerLogout;
import com.l2jfree.loginserver.network.legacy.packets.receivable.PlayerTraceRt;
import com.l2jfree.loginserver.network.legacy.packets.receivable.PlayersInGame;
import com.l2jfree.loginserver.network.legacy.packets.receivable.ServerStatus;
import com.l2jfree.network.mmocore.IPacketHandler;
import com.l2jfree.util.HexUtil;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author savormix
 *
 */
public final class L2LegacyPackets implements IPacketHandler<L2GameServer, L2GameServerPacket, L2LoginServerPacket>
{
	private static final L2Logger _log = L2Logger.getLogger(L2LegacyPackets.class);
	
	private L2LegacyPackets()
	{
		// singleton
	}
	
	@Override
	public L2GameServerPacket handlePacket(ByteBuffer buf, L2GameServer client,
			int opcode)
	{
		switch (opcode)
		{
		case BlowfishKey.OPCODE:
			if (client.getState() == L2LegacyState.CONNECTED)
				return new BlowfishKey();
			break;
		case GameServerAuth.OPCODE:
			if (client.getState() == L2LegacyState.KEYS_EXCHANGED)
				return new GameServerAuth();
			break;
		default:
			if (client.getState() != L2LegacyState.AUTHED)
				break;
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
			}
			// unknown packet
			_log.info("Unknown legacy packet: 0x" + HexUtil.fillHex(opcode, 2));
			return null;
		}
		// invalid state
		_log.info("Legacy packet in invalid state: 0x" + HexUtil.fillHex(opcode, 2));
		return null;
	}
	
	/**
	 * Returns a singleton object.
	 * @return an instance of this class
	 */
	public static L2LegacyPackets getInstance()
	{
		return SingletonHolder._instance;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final L2LegacyPackets _instance = new L2LegacyPackets();
	}
}
