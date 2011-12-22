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
 * @see com.l2jfree.gameserver.network.client.packets.sendable.ExAcquirableSkillListByClass.LearnableSkillList
 */
public abstract class AcquireSkillListPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link AcquireSkillListPacket}.
	 * 
	 * @author savormix (generated)
	 * @see AcquireSkillListPacket
	 */
	public static final class LearnableSkillList extends AcquireSkillListPacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see AcquireSkillListPacket#AcquireSkillListPacket()
		 */
		public LearnableSkillList()
		{
		}
	}
	
	/** Constructs this packet. */
	public AcquireSkillListPacket()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x90;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeD(0); // List type, branching condition
		final int sizeA = 0; // Skill count
		buf.writeD(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeD(0); // Skill
			buf.writeD(0); // Level
			buf.writeD(0); // Maximum level
			buf.writeD(0); // SP
			buf.writeD(0); // Requirement
			// branch with PledgeUnitSkills
			{
				buf.writeD(0); // Pledge unit
			}
		}
	}
}
