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
public abstract class GMViewPledgeInfoPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link GMViewPledgeInfoPacket}.
	 * 
	 * @author savormix (generated)
	 * @see GMViewPledgeInfoPacket
	 */
	public static final class ViewPledgeUnitInfo extends GMViewPledgeInfoPacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see GMViewPledgeInfoPacket#GMViewPledgeInfoPacket()
		 */
		public ViewPledgeUnitInfo()
		{
		}
	}

	/** Constructs this packet. */
	public GMViewPledgeInfoPacket()
	{
	}

	@Override
	protected int getOpcode()
	{
		return 0x96;
	}

	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeS(""); // Pledge member
		buf.writeD(0); // Pledge ID
		buf.writeD(0); // Unit
		buf.writeS(""); // Name
		buf.writeS(""); // Leader name
		buf.writeD(0); // Crest ID
		buf.writeD(0); // Level
		buf.writeD(0); // Castle
		buf.writeD(0); // Hideout
		buf.writeD(0); // Fortress
		buf.writeD(0); // Rank
		buf.writeD(0); // Reputation
		buf.writeD(0); // 0
		buf.writeD(0); // 0
		buf.writeD(0); // Alliance ID
		buf.writeS(""); // Alliance name
		buf.writeD(0); // Alliance crest ID
		buf.writeD(0); // At war
		buf.writeD(0); // territ castle??
		final int sizeA = 0; // Member count
		buf.writeD(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeS(""); // Name
			buf.writeD(0); // Level
			buf.writeD(0); // Class
			buf.writeD(0); // Sex
			buf.writeD(0); // Race
			buf.writeD(0); // Member OID
			buf.writeD(0); // Apprentice
		}
	}
}
