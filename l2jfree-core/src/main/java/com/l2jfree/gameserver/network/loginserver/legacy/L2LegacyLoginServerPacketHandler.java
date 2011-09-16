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
import com.l2jfree.gameserver.network.loginserver.legacy.packets.receivable.InitLS;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.receivable.LoginServerFail;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.receivable.RegistrationSucceed;
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
				return new InitLS();
				
			case LoginServerFail.OPCODE:
				return new LoginServerFail();
				
			case RegistrationSucceed.OPCODE:
				return new RegistrationSucceed();
				
			default:
				return unknown(buf, client, opcode);
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
