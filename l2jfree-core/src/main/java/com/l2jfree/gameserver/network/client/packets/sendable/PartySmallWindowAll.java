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
import com.l2jfree.network.ClientProtocolVersion;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix (generated)
 */
public abstract class PartySmallWindowAll extends L2ServerPacket
{
	/**
	 * A nicer name for {@link PartySmallWindowAll}.
	 * 
	 * @author savormix (generated)
	 * @see PartySmallWindowAll
	 */
	public static final class PartyInfo extends PartySmallWindowAll
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see PartySmallWindowAll#PartySmallWindowAll()
		 */
		public PartyInfo()
		{
		}
	}
	
	/** Constructs this packet. */
	public PartySmallWindowAll()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x4e;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		final boolean god = client.getVersion().isNewerThanOrEqualTo(ClientProtocolVersion.GODDESS_OF_DESTRUCTION);
		buf.writeD(0); // Leader OID
		buf.writeD(0); // Item distribution
		final int sizeA = 0; // Other members
		buf.writeD(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeD(0); // Member OID
			buf.writeS(""); // Name
			buf.writeD(0); // Current CP
			buf.writeD(0); // Maximum CP
			buf.writeD(0); // Current HP
			buf.writeD(0); // Maximum HP
			buf.writeD(0); // Current MP
			buf.writeD(0); // Maximum MP
			if (god)
				buf.writeD(0); // Vitality
			buf.writeD(0); // Level
			buf.writeD(0); // Class
			buf.writeD(0); // Sex
			buf.writeD(0); // Race
			buf.writeD(0); // ??? 0
			buf.writeD(0); // ??? 0
			if (god)
			{
				buf.writeD(0); // ??? 0
				final int sizeB = 0; // Servitor count
				buf.writeD(sizeB);
				for (int j = 0; j < sizeB; j++)
				{
					buf.writeD(0); // Servitor OID
					buf.writeD(0); // Servitor
					buf.writeD(0); // Servitor's type
					buf.writeS(""); // Servitor's name
					buf.writeD(0); // Servitor's current HP
					buf.writeD(0); // Servitor's maximum HP
					buf.writeD(0); // Servitor's current MP
					buf.writeD(0); // Servitor's maximum MP
					buf.writeD(0); // Servitor's level
				}
			}
			else
			{
				buf.writeD(0); // Servitor OID, branching condition
				// branch with AboveZero
				{
					buf.writeD(0); // Servitor
					buf.writeD(0); // Servitor's type
					buf.writeS(""); // Servitor's name
					buf.writeD(0); // Servitor's current HP
					buf.writeD(0); // Servitor's maximum HP
					buf.writeD(0); // Servitor's current MP
					buf.writeD(0); // Servitor's maximum MP
					buf.writeD(0); // Servitor's level
				}
			}
		}
	}
}
