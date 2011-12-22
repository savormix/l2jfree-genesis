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
package com.l2jfree.gameserver.network.client.packets.sendable;

import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * Applies to pre-GoD only.
 * 
 * @author savormix (generated)
 * @see com.l2jfree.gameserver.network.client.packets.sendable.ExAcquireSkillInfo.LearnableSkillInfo
 */
public abstract class AcquireSkillInfoPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link AcquireSkillInfoPacket}.
	 * 
	 * @author savormix (generated)
	 * @see AcquireSkillInfoPacket
	 */
	public static final class LearnableSkillInfo extends AcquireSkillInfoPacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see AcquireSkillInfoPacket#AcquireSkillInfoPacket()
		 */
		public LearnableSkillInfo()
		{
		}
	}
	
	/** Constructs this packet. */
	public AcquireSkillInfoPacket()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x91;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeD(0); // Skill
		buf.writeD(0); // Level
		buf.writeD(0); // SP
		buf.writeD(0); // Skill type
		final int sizeA = 0; // Requirement count
		buf.writeD(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeD(0); // Requirement type
			buf.writeD(0); // Item
			buf.writeQ(0L); // Quantity
			buf.writeD(0); // Unk
		}
	}
}
