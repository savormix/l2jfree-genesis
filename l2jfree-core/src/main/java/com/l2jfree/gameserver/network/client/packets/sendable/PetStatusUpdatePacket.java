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

import com.l2jfree.ClientProtocolVersion;
import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix (generated)
 */
public abstract class PetStatusUpdatePacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link PetStatusUpdatePacket}.
	 * 
	 * @author savormix (generated)
	 * @see PetStatusUpdatePacket
	 */
	public static final class ServitorUpdate extends PetStatusUpdatePacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see PetStatusUpdatePacket#PetStatusUpdatePacket()
		 */
		public ServitorUpdate()
		{
		}
	}
	
	/** Constructs this packet. */
	public PetStatusUpdatePacket()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0xb6;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeD(0); // Type
		buf.writeD(0); // Servitor OID
		buf.writeD(0); // Location X
		buf.writeD(0); // Location Y
		buf.writeD(0); // Location Z
		buf.writeS(""); // Name
		buf.writeD(0); // Current satiation
		buf.writeD(0); // Maximum satiation
		buf.writeD(0); // Current HP
		buf.writeD(0); // Maximum HP
		buf.writeD(0); // Current MP
		buf.writeD(0); // Maximum MP
		buf.writeD(0); // Level
		buf.writeQ(0L); // XP
		buf.writeQ(0L); // Current level XP
		buf.writeQ(0L); // Next level XP
		if (client.getVersion().isNewerThanOrEqualTo(ClientProtocolVersion.GODDESS_OF_DESTRUCTION))
			buf.writeD(0); // ??? 0
	}
}
