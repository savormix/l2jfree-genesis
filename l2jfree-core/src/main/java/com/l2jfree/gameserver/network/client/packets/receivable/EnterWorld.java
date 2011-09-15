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
package com.l2jfree.gameserver.network.client.packets.receivable;

import java.nio.BufferUnderflowException;
import java.util.Collection;
import java.util.Collections;

import com.l2jfree.gameserver.gameobjects.L2Item;
import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.sendable.EtcStatusUpdatePacket.EtcEffectIcons;
import com.l2jfree.gameserver.network.client.packets.sendable.ExBRExtraUserInfo.EventPlayerInfo;
import com.l2jfree.gameserver.network.client.packets.sendable.ExBRPremiumState.PremiumPlayerInfo;
import com.l2jfree.gameserver.network.client.packets.sendable.ExGetBookMarkInfoPacket.MyTeleportBookmarkList;
import com.l2jfree.gameserver.network.client.packets.sendable.ExQuestItemList.QuestInventory;
import com.l2jfree.gameserver.network.client.packets.sendable.ExSearchOrc.DemandProcessBlock;
import com.l2jfree.gameserver.network.client.packets.sendable.GameGuardQueryPacket.DemandGameGuardStatus;
import com.l2jfree.gameserver.network.client.packets.sendable.HennaInfoPacket.MyHennaList;
import com.l2jfree.gameserver.network.client.packets.sendable.ItemList.MyInventory;
import com.l2jfree.gameserver.network.client.packets.sendable.MagicAndSkillList.MyCharacterInfo;
import com.l2jfree.gameserver.network.client.packets.sendable.QuestListPacket.ActiveQuests;
import com.l2jfree.gameserver.network.client.packets.sendable.SkillListPacket.SkillList;
import com.l2jfree.gameserver.network.client.packets.sendable.SystemMessagePacket.SystemMessage;
import com.l2jfree.gameserver.network.client.packets.sendable.UserInfo.MyPlayerInfo;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author hex1r0
 * @author savormix (generated)
 */
public abstract class EnterWorld extends L2ClientPacket
{
	/**
	 * A nicer name for {@link EnterWorld}.
	 * 
	 * @author savormix (generated)
	 * @see EnterWorld
	 */
	public static final class RequestEnterWorld extends EnterWorld
	{
		// only for convenience
	}
	
	/** Packet's identifier */
	public static final int OPCODE = 0x11;
	
	@Override
	protected int getMinimumLength()
	{
		return 84 + 4 + 4 + 4 + 4 + 4;
	}
	
	/* Fields for storing read data */
	
	@SuppressWarnings("unused")
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		final byte[] bytesA = buf.readB(new byte[84]); // Unk
		final byte[] bytesB = buf.readB(new byte[4]); // Client IP
		final byte[] bytesC = buf.readB(new byte[4]); // Hop 1
		final byte[] bytesD = buf.readB(new byte[4]); // Hop 2
		final byte[] bytesE = buf.readB(new byte[4]); // Hop 3
		final byte[] bytesF = buf.readB(new byte[4]); // Hop 4
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// WARNING: IF ALTERING, PLEASE RETAIN THE ORDER!
		// ALSO, IF REMOVING DUPLICATES, COMMENT THEM OUT INSTEAD.
		
		// TODO: implement
		L2Player activeChar = getClient().getActiveChar();
		// send MacroInfo packets
		sendPacket(MyTeleportBookmarkList.PACKET);
		sendPacket(EtcEffectIcons.PACKET); // with grade penalty levels because skills (including Expertise X) aren't loaded yet
		{
			sendPacket(new DemandGameGuardStatus());
			sendPacket(new DemandProcessBlock());
		}
		// send system messages about game-time dependent skills, like Shadow Sense - either now in effect or no longer in effect
		Collection<?> fakeSkills = Collections.EMPTY_SET;
		sendPacket(new SkillList(fakeSkills)); // without noble/hero skills (temporary skills?; item skills & non-class skills like divine inspiration are included), contact savormix for more info
		sendPacket(EtcEffectIcons.PACKET); // correct icons
		sendPacket(MyHennaList.PACKET);
		sendPacket(new SkillList(fakeSkills)); // with noble/hero skills
		sendPacket(new SkillList(fakeSkills)); // yes, a second time
		// if in pledge
		{
			// send UpdatePledgeMember with self!
			// send all PledgeUnitInfo (-1 to 2002 :P)
			// send PledgeSkillList
		}
		Collection<L2Item> fakeItems = Collections.emptyList();
		sendPacket(new MyInventory(false, fakeItems));
		sendPacket(new QuestInventory(fakeItems));
		// send shortcut list
		// send action list with every single action (189, from 0 to 5016)
		sendPacket(new SkillList(fakeSkills)); // now send with pledge skills AND with armor set skill(s)
		{
			sendPacket(MyPlayerInfo.PACKET);
			sendPacket(new EventPlayerInfo(activeChar));
			sendPacket(new PremiumPlayerInfo(activeChar));
		}
		sendPacket(ActiveQuests.PACKET); // without TW quests
		// DUMP KNOWNLIST HERE
		// NpcInfo, StaticObjectInfo, ItemOnGround, PlayerInfo (+ Sell/Buy/PackageShopInfo) + EventPlayerInfo + Relationships, AgitDecoInfo, also start sending Movement/StatusUpdate packets
		sendPacket(new SystemMessage(34));
		sendPacket(EtcEffectIcons.PACKET); // correct icons (again)
		// send seven signs system message
		sendPacket(MyCharacterInfo.PACKET);
		// send storage slot info
		// if in pledge
		{
			// send clan notice
		}
		// send NevitBlessingInfo
		// send effect icons
		// and more... but it gets rather messy here.
	}
}
