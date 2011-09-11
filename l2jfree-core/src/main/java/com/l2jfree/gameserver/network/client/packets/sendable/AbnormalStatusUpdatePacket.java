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
public abstract class AbnormalStatusUpdatePacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link AbnormalStatusUpdatePacket}.
	 * 
	 * @author savormix (generated)
	 * @see AbnormalStatusUpdatePacket
	 */
	public static final class EffectIcons extends AbnormalStatusUpdatePacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see AbnormalStatusUpdatePacket#AbnormalStatusUpdatePacket()
		 */
		public EffectIcons()
		{
		}
	}

	/** Constructs this packet. */
	public AbnormalStatusUpdatePacket()
	{
	}

	@Override
	protected int getOpcode()
	{
		return 0x85;
	}

	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		final int sizeA = 0; // Effect count
		buf.writeH(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeD(0); // Skill
			buf.writeH(0); // Level
			buf.writeD(0); // Time left
		}
	}
}
