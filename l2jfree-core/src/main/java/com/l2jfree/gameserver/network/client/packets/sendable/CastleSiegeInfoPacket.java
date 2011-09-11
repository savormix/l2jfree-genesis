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
public abstract class CastleSiegeInfoPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link CastleSiegeInfoPacket}.
	 * 
	 * @author savormix (generated)
	 * @see CastleSiegeInfoPacket
	 */
	public static final class SiegeInfo extends CastleSiegeInfoPacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see CastleSiegeInfoPacket#CastleSiegeInfoPacket()
		 */
		public SiegeInfo()
		{
		}
	}

	/** Constructs this packet. */
	public CastleSiegeInfoPacket()
	{
	}

	@Override
	protected int getOpcode()
	{
		return 0xc9;
	}

	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeD(0); // Pledge base
		buf.writeD(0); // Owner's view
		buf.writeD(0); // Owner pledge ID
		buf.writeS(""); // Owner pledge name
		buf.writeS(""); // Owner pledge leader
		buf.writeD(0); // Owner alliance ID
		buf.writeS(""); // Owner alliance name
		buf.writeD(0); // Current server time
		buf.writeD(0); // Siege time
		final int sizeA = 0; // Possibility count
		buf.writeD(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeD(0); // Possible siege time
		}
	}
}
