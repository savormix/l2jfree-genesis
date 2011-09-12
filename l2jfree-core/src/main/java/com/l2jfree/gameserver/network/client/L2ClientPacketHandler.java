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
import com.l2jfree.gameserver.network.client.packets.receivable.EnterWorld.RequestEnterWorld;
import com.l2jfree.gameserver.network.client.packets.receivable.ExGetOnAirShip;
import com.l2jfree.gameserver.network.client.packets.receivable.Logout;
import com.l2jfree.gameserver.network.client.packets.receivable.RequestManorList;
import com.l2jfree.gameserver.network.client.packets.receivable.RequestRestart;
import com.l2jfree.gameserver.network.client.packets.receivable.ValidatePosition;
import com.l2jfree.gameserver.network.client.packets.receivable.ValidatePosition.ReportLocation;
import com.l2jfree.gameserver.network.client.packets.receivable.accountless.AuthLogin;
import com.l2jfree.gameserver.network.client.packets.receivable.accountless.AuthLogin.RequestAuthorization;
import com.l2jfree.gameserver.network.client.packets.receivable.accountless.ProtocolVersion;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.CharacterDeletePacket;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.CharacterDeletePacket.RequestDeleteCharacter;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.CharacterRestorePacket;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.CharacterRestorePacket.RequestRestoreCharacter;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.CharacterSelect;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.CharacterSelect.RequestSelectCharacter;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.NewCharacter;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.NewCharacter.RequestNewCharacter;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.NewCharacterPacket;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.NewCharacterPacket.RequestCharacterTemplates;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.RequestAvailableCharacters;
import com.l2jfree.network.mmocore.PacketHandler;

/**
 * @author savormix
 * @author hex1r0
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
				
			case AuthLogin.OPCODE:
				if (client.stateEquals(PROTOCOL_OK))
					return new RequestAuthorization();
				return invalidState(client, AuthLogin.class, opcode);
				
			case Logout.OPCODE:
				if (client.stateEquals(CHARACTER_MANAGEMENT) || client.stateEquals(LOGGED_IN))
					return new Logout();
				return invalidState(client, Logout.class, opcode);
				
			case NewCharacter.OPCODE:
				if (client.stateEquals(CHARACTER_MANAGEMENT))
					return new RequestNewCharacter();
				return invalidState(client, NewCharacter.class, opcode);
				
			case CharacterDeletePacket.OPCODE:
				if (client.stateEquals(CHARACTER_MANAGEMENT))
					return new RequestDeleteCharacter();
				return invalidState(client, CharacterDeletePacket.class, opcode);
				
			case CharacterSelect.OPCODE:
				if (client.stateEquals(CHARACTER_MANAGEMENT))
					return new RequestSelectCharacter();
				return invalidState(client, CharacterSelect.class, opcode);
				
			case NewCharacterPacket.OPCODE:
				if (client.stateEquals(CHARACTER_MANAGEMENT))
					return new RequestCharacterTemplates();
				return invalidState(client, NewCharacterPacket.class, opcode);
				
			case CharacterRestorePacket.OPCODE:
				if (client.stateEquals(CHARACTER_MANAGEMENT))
					return new RequestRestoreCharacter();
				return invalidState(client, CharacterRestorePacket.class, opcode);
				
			case 0xd0:
			{
				if (buf.remaining() < 2)
					return underflow(buf, client, opcode);
				
				final int opcode2 = buf.getChar();
				
				switch (opcode2)
				{
					case RequestManorList.OPCODE_2:
						if (client.stateEquals(LOGGED_IN))
							return new RequestManorList();
						return invalidState(client, RequestManorList.class, opcode, opcode2);
						
					case ExGetOnAirShip.OPCODE_2:
						if (client.stateEquals(CHARACTER_MANAGEMENT))
							return new RequestAvailableCharacters();
						//else if (client.stateEquals(LOGGED_IN))
						//return new ();
						return invalidState(client, RequestManorList.class, opcode, opcode2);
					default:
						return unknown(buf, client, opcode, opcode2);
				}
			}
			
			case EnterWorld.OPCODE:
				if (client.stateEquals(LOGGED_IN))
					return new RequestEnterWorld();
				return invalidState(client, EnterWorld.class, opcode);
				
			case RequestRestart.OPCODE:
				if (client.stateEquals(LOGGED_IN))
					return new RequestRestart();
				return invalidState(client, RequestRestart.class, opcode);
				
			case ValidatePosition.OPCODE:
				if (client.stateEquals(LOGGED_IN))
					return new ReportLocation();
				return invalidState(client, ValidatePosition.class, opcode);
				
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
