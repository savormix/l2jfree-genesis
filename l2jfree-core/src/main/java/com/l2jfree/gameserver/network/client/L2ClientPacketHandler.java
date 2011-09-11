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

import static com.l2jfree.gameserver.network.client.L2ClientState.CHARACTER_MANAGEMENT;
import static com.l2jfree.gameserver.network.client.L2ClientState.CONNECTED;
import static com.l2jfree.gameserver.network.client.L2ClientState.LOGGED_IN;
import static com.l2jfree.gameserver.network.client.L2ClientState.PROTOCOL_OK;

import java.nio.ByteBuffer;

import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.gameserver.network.client.packets.receivable.EnterWorld;
import com.l2jfree.gameserver.network.client.packets.receivable.Logout;
import com.l2jfree.gameserver.network.client.packets.receivable.RequestRestart;
import com.l2jfree.gameserver.network.client.packets.receivable.outgame.ProtocolVersion;
import com.l2jfree.gameserver.network.client.packets.receivable.outgame.RequestAuthorization;
import com.l2jfree.gameserver.network.client.packets.receivable.outgame.RequestCharacterCreate;
import com.l2jfree.gameserver.network.client.packets.receivable.outgame.RequestCharacterCreationScreen;
import com.l2jfree.gameserver.network.client.packets.receivable.outgame.RequestCharacterDelete;
import com.l2jfree.gameserver.network.client.packets.receivable.outgame.RequestCharacterPreviousState;
import com.l2jfree.gameserver.network.client.packets.receivable.outgame.RequestCharacterRestore;
import com.l2jfree.gameserver.network.client.packets.receivable.outgame.RequestCharacterSelect;
import com.l2jfree.network.mmocore.PacketHandler;

/**
 * @author savormix
 */
public final class L2ClientPacketHandler extends PacketHandler<L2Client, L2ClientPacket, L2ServerPacket>
{
	private L2ClientPacketHandler()
	{
		// singleton
	}
	
	@Override
	public L2ClientPacket handlePacket(ByteBuffer buf, L2Client client, final int opcode)
	{
		switch (opcode)
		{
			case ProtocolVersion.OPCODE:
				if (client.stateEquals(CONNECTED))
					return new ProtocolVersion();
				return invalidState(client, ProtocolVersion.class, opcode);
				
			case RequestAuthorization.OPCODE:
				if (client.stateEquals(PROTOCOL_OK))
					return new RequestAuthorization();
				return invalidState(client, RequestAuthorization.class, opcode);
				
			case Logout.OPCODE:
				if (client.stateEquals(CHARACTER_MANAGEMENT) || client.stateEquals(LOGGED_IN))
					return new Logout();
				return invalidState(client, Logout.class, opcode);
				
			case RequestCharacterCreate.OPCODE:
				if (client.stateEquals(CHARACTER_MANAGEMENT))
					return new RequestCharacterCreate();
				return invalidState(client, RequestCharacterCreate.class, opcode);
				
			case RequestCharacterDelete.OPCODE:
				if (client.stateEquals(CHARACTER_MANAGEMENT))
					return new RequestCharacterDelete();
				return invalidState(client, RequestCharacterDelete.class, opcode);
				
			case RequestCharacterSelect.OPCODE:
				if (client.stateEquals(CHARACTER_MANAGEMENT))
					return new RequestCharacterSelect();
				return invalidState(client, RequestCharacterSelect.class, opcode);
				
			case RequestCharacterCreationScreen.OPCODE:
				if (client.stateEquals(CHARACTER_MANAGEMENT))
					return new RequestCharacterCreationScreen();
				return invalidState(client, RequestCharacterCreationScreen.class, opcode);
				
			case RequestCharacterRestore.OPCODE:
				if (client.stateEquals(CHARACTER_MANAGEMENT))
					return new RequestCharacterRestore();
				return invalidState(client, RequestCharacterRestore.class, opcode);
				
			case 0xd0:
				if (buf.remaining() < 2)
					return underflow(buf, client, opcode);
				
				final int opcode2 = buf.getShort() & 0xffff;
				
				switch (opcode2)
				{
					case RequestCharacterPreviousState.OPCODE:
						if (client.stateEquals(CHARACTER_MANAGEMENT))
							return new RequestCharacterPreviousState();
						return invalidState(client, RequestCharacterPreviousState.class, opcode, opcode2);
						
					default:
						return unknown(buf, client, opcode, opcode2);
				}
				
			case EnterWorld.OPCODE:
				if (client.stateEquals(LOGGED_IN))
					return new EnterWorld();
				return invalidState(client, EnterWorld.class, opcode);
				
			case RequestRestart.OPCODE:
				if (client.stateEquals(LOGGED_IN))
					return new RequestRestart();
				return invalidState(client, RequestRestart.class, opcode);
				
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
