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
import static com.l2jfree.gameserver.network.client.L2ClientState.CHARACTER_SELECTED;
import static com.l2jfree.gameserver.network.client.L2ClientState.CONNECTED;
import static com.l2jfree.gameserver.network.client.L2ClientState.LOGGED_IN;
import static com.l2jfree.gameserver.network.client.L2ClientState.PROTOCOL_OK;

import java.nio.ByteBuffer;

import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.gameserver.network.client.packets.receivable.EnterWorld;
import com.l2jfree.gameserver.network.client.packets.receivable.ExGetOnAirShip;
import com.l2jfree.gameserver.network.client.packets.receivable.Logout;
import com.l2jfree.gameserver.network.client.packets.receivable.MoveBackwardToLocation;
import com.l2jfree.gameserver.network.client.packets.receivable.NetPing;
import com.l2jfree.gameserver.network.client.packets.receivable.RequestManorList;
import com.l2jfree.gameserver.network.client.packets.receivable.RequestRestart;
import com.l2jfree.gameserver.network.client.packets.receivable.ValidatePosition;
import com.l2jfree.gameserver.network.client.packets.receivable.accountless.AuthLogin;
import com.l2jfree.gameserver.network.client.packets.receivable.accountless.ProtocolVersion;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.CharacterDeletePacket;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.CharacterRestorePacket;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.CharacterSelect;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.NewCharacter;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.NewCharacterPacket;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.RequestAvailableCharacters;
import com.l2jfree.network.mmocore.PacketHandler;
import com.l2jfree.util.HexUtil;

/**
 * @author savormix
 * @author hex1r0
 */
public final class L2ClientPacketHandler extends PacketHandler<L2Client, L2ClientPacket, L2ServerPacket>
{
	private L2ClientPacketHandler()
	{
		// singleton
		
		validateSharedOpcodes(0x36, RequestAvailableCharacters.OPCODE_2, RequestAvailableCharacters.class,
				ExGetOnAirShip.OPCODE_2, ExGetOnAirShip.class);
	}
	
	private static void validateSharedOpcodes(int expectedOpcode, int opcode1, Class<? extends L2ClientPacket> clazz1,
			int opcode2, Class<? extends L2ClientPacket> clazz2)
	{
		if (expectedOpcode == opcode1 && expectedOpcode == opcode2 && opcode1 == opcode2)
			return;
		
		System.err.println("Previously shared opcodes (opcode: 0x" + HexUtil.fillHex(expectedOpcode, 2) + ") for "
				+ clazz1.getName() + " (opcode: 0x" + HexUtil.fillHex(opcode1, 2) + ")" + " and " //
				+ clazz2.getName() + " (opcode: 0x" + HexUtil.fillHex(opcode2, 2) + ") aren't shared anymore!");
	}
	
	@Override
	public L2ClientPacket handlePacket(ByteBuffer buf, L2Client client, final int opcode)
	{
		switch (opcode)
		{
			case ProtocolVersion.OPCODE: // 0x0e
				if (client.stateEquals(CONNECTED))
					return new ProtocolVersion();
				return invalidState(client, ProtocolVersion.class, opcode);
				
			case AuthLogin.OPCODE: // 0x2b
				if (client.stateEquals(PROTOCOL_OK))
					return new AuthLogin.RequestAuthorization();
				return invalidState(client, AuthLogin.class, opcode);
				
			case Logout.OPCODE: // 0x00
				if (client.stateEquals(CHARACTER_MANAGEMENT, CHARACTER_SELECTED, LOGGED_IN))
					return new Logout();
				return invalidState(client, Logout.class, opcode);
				
			case NewCharacter.OPCODE: // 0x0c
				if (client.stateEquals(CHARACTER_MANAGEMENT))
					return new NewCharacter.RequestNewCharacter();
				return invalidState(client, NewCharacter.class, opcode);
				
			case CharacterDeletePacket.OPCODE: // 0x0d
				if (client.stateEquals(CHARACTER_MANAGEMENT))
					return new CharacterDeletePacket.RequestDeleteCharacter();
				return invalidState(client, CharacterDeletePacket.class, opcode);
				
			case CharacterSelect.OPCODE: // 0x12
				if (client.stateEquals(CHARACTER_MANAGEMENT))
					return new CharacterSelect.RequestSelectCharacter();
				return invalidState(client, CharacterSelect.class, opcode);
				
			case NewCharacterPacket.OPCODE: // 0x13
				if (client.stateEquals(CHARACTER_MANAGEMENT))
					return new NewCharacterPacket.RequestCharacterTemplates();
				return invalidState(client, NewCharacterPacket.class, opcode);
				
			case CharacterRestorePacket.OPCODE: // 0x7b
				if (client.stateEquals(CHARACTER_MANAGEMENT))
					return new CharacterRestorePacket.RequestRestoreCharacter();
				return invalidState(client, CharacterRestorePacket.class, opcode);
				
			case NetPing.OPCODE: // 0xb1
				if (client.stateEquals(CHARACTER_MANAGEMENT, CHARACTER_SELECTED, LOGGED_IN))
					return new NetPing.UptimeResponse();
				return invalidState(client, NetPing.class, opcode);
				
			case EnterWorld.OPCODE: // 0x11
				if (client.stateEquals(CHARACTER_SELECTED))
					return new EnterWorld.RequestEnterWorld();
				return invalidState(client, EnterWorld.class, opcode);
				
			case 0xd0:
			{
				if (buf.remaining() < 2)
					return underflow(buf, client, opcode);
				
				final int opcode2 = buf.getChar();
				
				switch (opcode2)
				{
					case RequestManorList.OPCODE_2: // 0x01
						if (client.stateEquals(LOGGED_IN))
							return new RequestManorList();
						return invalidState(client, RequestManorList.class, opcode, opcode2);
						
					case 0x36:
						// RequestAvailableCharacters.OPCODE_2 // 0x36
						if (client.stateEquals(CHARACTER_MANAGEMENT))
							return new RequestAvailableCharacters();
						// ExGetOnAirShip.OPCODE_2 // 0x36
						else if (client.stateEquals(LOGGED_IN))
							return new ExGetOnAirShip.RequestBoardAircraft();
						return invalidState(client, opcode, opcode2);
						
					default:
						return unknown(buf, client, opcode, opcode2);
				}
			}
			
			case MoveBackwardToLocation.OPCODE: // 0x0f
				if (client.stateEquals(LOGGED_IN))
					return new MoveBackwardToLocation.RequestMovement();
				return invalidState(client, MoveBackwardToLocation.class, opcode);
				
			case RequestRestart.OPCODE: // 0x57
				if (client.stateEquals(LOGGED_IN))
					return new RequestRestart();
				return invalidState(client, RequestRestart.class, opcode);
				
			case ValidatePosition.OPCODE: // 0x59
				if (client.stateEquals(LOGGED_IN))
					return new ValidatePosition.ReportLocation();
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
