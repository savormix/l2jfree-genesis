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

import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.gameserver.network.client.packets.receivable.Action;
import com.l2jfree.gameserver.network.client.packets.receivable.Attack;
import com.l2jfree.gameserver.network.client.packets.receivable.AttackRequest;
import com.l2jfree.gameserver.network.client.packets.receivable.EnterWorld;
import com.l2jfree.gameserver.network.client.packets.receivable.ExGetOnAirShip;
import com.l2jfree.gameserver.network.client.packets.receivable.Logout;
import com.l2jfree.gameserver.network.client.packets.receivable.MoveBackwardToLocation;
import com.l2jfree.gameserver.network.client.packets.receivable.NetPing;
import com.l2jfree.gameserver.network.client.packets.receivable.RequestManorList;
import com.l2jfree.gameserver.network.client.packets.receivable.RequestRestart;
import com.l2jfree.gameserver.network.client.packets.receivable.Say2;
import com.l2jfree.gameserver.network.client.packets.receivable.ValidatePosition;
import com.l2jfree.gameserver.network.client.packets.receivable.accountless.AuthLogin;
import com.l2jfree.gameserver.network.client.packets.receivable.accountless.ProtocolVersion;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.CharacterDeletePacket;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.CharacterRestorePacket;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.CharacterSelect;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.NewCharacter;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.NewCharacterPacket;
import com.l2jfree.gameserver.network.client.packets.receivable.characterless.RequestAvailableCharacters;
import com.l2jfree.network.mmocore.packethandlers.PacketHandlerBuilder;
import com.l2jfree.network.mmocore.packethandlers.ThreeLevelPacketHandler;

/**
 * @author NB4L1
 */
public final class L2ExperimentalPacketHandler extends
		ThreeLevelPacketHandler<L2Client, L2ClientPacket, L2ServerPacket, L2ClientState>
{
	private static final class SingletonHolder
	{
		static
		{
			try
			{
				INSTANCE = new L2ExperimentalPacketHandler();
			}
			catch (Exception e)
			{
				throw new Error(e);
			}
		}
		
		public static final L2ExperimentalPacketHandler INSTANCE;
	}
	
	public static L2ExperimentalPacketHandler getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private L2ExperimentalPacketHandler() throws Exception
	{
		// singleton
	}
	
	@Override
	public PacketHandlerBuilder<L2Client, L2ClientPacket, L2ServerPacket, L2ClientState> initPacketHandlerBuilder()
			throws Exception
	{
		final PacketHandlerBuilder<L2Client, L2ClientPacket, L2ServerPacket, L2ClientState> dph =
				new PacketHandlerBuilder<L2Client, L2ClientPacket, L2ServerPacket, L2ClientState>(L2ClientState.class,
						L2ClientState.LOGGED_IN);
		
		// CONNECTED
		dph.addPacket(ProtocolVersion.class);
		
		// PROTOCOL_OK
		dph.addPacket(AuthLogin.RequestAuthorization.class);
		
		// CHARACTER_MANAGEMENT
		dph.addPacket(NewCharacter.RequestNewCharacter.class);
		dph.addPacket(CharacterDeletePacket.RequestDeleteCharacter.class);
		dph.addPacket(CharacterSelect.RequestSelectCharacter.class);
		dph.addPacket(NewCharacterPacket.RequestCharacterTemplates.class);
		dph.addPacket(CharacterRestorePacket.RequestRestoreCharacter.class);
		dph.addPacket(RequestAvailableCharacters.class);
		
		// CHARACTER_SELECTED
		dph.addPacket(EnterWorld.RequestEnterWorld.class);
		
		// LOGGED_IN
		dph.addPacket(RequestManorList.class);
		
		dph.addPacket(ExGetOnAirShip.RequestBoardAircraft.class);
		dph.addPacket(Attack.RequestAttack.class);
		dph.addPacket(MoveBackwardToLocation.RequestMovement.class);
		dph.addPacket(Action.RequestInteraction.class);
		dph.addPacket(AttackRequest.RequestAttack.class);
		dph.addPacket(Say2.RequestSendChatMessage.class);
		dph.addPacket(RequestRestart.class);
		dph.addPacket(ValidatePosition.ReportLocation.class);
		
		// CHARACTER_MANAGEMENT, CHARACTER_SELECTED, LOGGED_IN
		dph.addPacket(Logout.class);
		dph.addPacket(NetPing.UptimeResponse.class);
		
		//dph.addPackets(Action.class.getPackage().getName());
		
		return dph;
	}
}
