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
				new PacketHandlerBuilder<L2Client, L2ClientPacket, L2ServerPacket, L2ClientState>(L2ClientState.class);
		
		dph.addPacket(ProtocolVersion.class, //
				CONNECTED);
		
		dph.addPacket(AuthLogin.RequestAuthorization.class, //
				PROTOCOL_OK);
		dph.addPacket(Logout.class, //
				CHARACTER_MANAGEMENT, CHARACTER_SELECTED, LOGGED_IN);
		dph.addPacket(NewCharacter.RequestNewCharacter.class, //
				CHARACTER_MANAGEMENT);
		dph.addPacket(CharacterDeletePacket.RequestDeleteCharacter.class, //
				CHARACTER_MANAGEMENT);
		dph.addPacket(CharacterSelect.RequestSelectCharacter.class, //
				CHARACTER_MANAGEMENT);
		dph.addPacket(NewCharacterPacket.RequestCharacterTemplates.class, //
				CHARACTER_MANAGEMENT);
		dph.addPacket(CharacterRestorePacket.RequestRestoreCharacter.class, //
				CHARACTER_MANAGEMENT);
		dph.addPacket(NetPing.UptimeResponse.class, //
				CHARACTER_MANAGEMENT, CHARACTER_SELECTED, LOGGED_IN);
		dph.addPacket(EnterWorld.RequestEnterWorld.class, //
				CHARACTER_SELECTED);
		dph.addPacket(RequestManorList.class, //
				LOGGED_IN);
		dph.addPacket(RequestAvailableCharacters.class, //
				CHARACTER_MANAGEMENT);
		dph.addPacket(ExGetOnAirShip.RequestBoardAircraft.class, //
				LOGGED_IN);
		dph.addPacket(Attack.RequestAttack.class, //
				LOGGED_IN);
		dph.addPacket(MoveBackwardToLocation.RequestMovement.class, //
				LOGGED_IN);
		dph.addPacket(Action.RequestInteraction.class, //
				LOGGED_IN);
		dph.addPacket(AttackRequest.RequestAttack.class, //
				LOGGED_IN);
		dph.addPacket(Say2.RequestSendChatMessage.class, //
				LOGGED_IN);
		dph.addPacket(RequestRestart.class, //
				LOGGED_IN);
		dph.addPacket(ValidatePosition.ReportLocation.class, //
				LOGGED_IN);
		
		/*
		try
		{
			for (Class<?> c : ClassFinder.findClasses(Action.class.getPackage().getName()))
				if (L2ClientPacket.class.isAssignableFrom(c))
					if (!Modifier.isAbstract(c.getModifiers()))
						if (Modifier.isPublic(c.getModifiers()))
							dph.addPacket((Class<? extends L2ClientPacket>)c, LOGGED_IN);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		*/
		
		return dph;
	}
}
