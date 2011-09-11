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
public abstract class AllianceInfoPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link AllianceInfoPacket}.
	 * 
	 * @author savormix (generated)
	 * @see AllianceInfoPacket
	 */
	public static final class AllianceInfo extends AllianceInfoPacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see AllianceInfoPacket#AllianceInfoPacket()
		 */
		public AllianceInfo()
		{
		}
	}

	/** Constructs this packet. */
	public AllianceInfoPacket()
	{
	}

	@Override
	protected int getOpcode()
	{
		return 0xb5;
	}

	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeS(""); // Name
		buf.writeD(0); // Total members
		buf.writeD(0); // Online
		buf.writeS(""); // Leader pledge
		buf.writeS(""); // Leader
		final int sizeA = 0; // Allied pledges
		buf.writeD(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeS(""); // Pledge
			buf.writeD(0); // ??? 0
			buf.writeD(0); // Pledge level
			buf.writeS(""); // Pledge leader
			buf.writeD(0); // Pledge members
			buf.writeD(0); // Online
		}
	}
}
