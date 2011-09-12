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
 * @author savormix (generated)
 */
public abstract class PledgeSkillListPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link PledgeSkillListPacket}.
	 * 
	 * @author savormix (generated)
	 * @see PledgeSkillListPacket
	 */
	public static final class PledgeSkillList extends PledgeSkillListPacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see PledgeSkillListPacket#PledgeSkillListPacket()
		 */
		public PledgeSkillList()
		{
		}
	}
	
	private static final int[] EXT_OPCODES = { 0x3a, 0x00, };
	
	/** Constructs this packet. */
	public PledgeSkillListPacket()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0xfe;
	}
	
	@Override
	protected int[] getAdditionalOpcodes()
	{
		return EXT_OPCODES;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		final int sizeA = 0; // Pledge skill count
		buf.writeD(sizeA);
		final int sizeB = 0; // Unit skill count
		buf.writeD(sizeB);
		for (int j = 0; j < sizeA; j++)
		{
			buf.writeD(0); // Skill
			buf.writeD(0); // Level
		}
		for (int i = 0; i < sizeB; i++)
		{
			buf.writeD(0); // Unit
			buf.writeD(0); // Skill
			buf.writeD(0); // Level
		}
	}
}
