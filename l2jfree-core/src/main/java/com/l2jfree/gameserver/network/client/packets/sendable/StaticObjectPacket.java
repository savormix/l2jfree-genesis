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
public abstract class StaticObjectPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link StaticObjectPacket}.
	 * 
	 * @author savormix (generated)
	 * @see StaticObjectPacket
	 */
	public static final class StaticObjectInfo extends StaticObjectPacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see StaticObjectPacket#StaticObjectPacket()
		 */
		public StaticObjectInfo()
		{
		}
	}

	/** Constructs this packet. */
	public StaticObjectPacket()
	{
	}

	@Override
	protected int getOpcode()
	{
		return 0x9f;
	}

	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeD(0); // Object
		buf.writeD(0); // OID
		buf.writeD(0); // Door
		buf.writeD(0); // Can be targetted
		buf.writeD(0); // Mesh index
		buf.writeD(0); // Closed
		buf.writeD(0); // Attackable
		buf.writeD(0); // Current HP
		buf.writeD(0); // Maximum HP
		buf.writeD(0); // Show HP bar
		buf.writeD(0); // Damage level
	}
}
