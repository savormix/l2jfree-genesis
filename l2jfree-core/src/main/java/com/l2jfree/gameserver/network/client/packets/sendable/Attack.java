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
public class Attack extends L2ServerPacket
{
	/** Constructs this packet. */
	public Attack()
	{
	}

	@Override
	protected int getOpcode()
	{
		return 0x33;
	}

	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeD(0); // Attacker OID
		buf.writeD(0); // Main target OID
		buf.writeD(0); // Damage to main target
		buf.writeC(0); // Damage modifiers
		buf.writeD(0); // Attacker X
		buf.writeD(0); // Attacker Y
		buf.writeD(0); // Attacker Z
		final int sizeA = 0; // Additional hit count
		buf.writeH(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeD(0); // Target OID
			buf.writeD(0); // Damage
			buf.writeC(0); // Damage modifiers
		}
		buf.writeD(0); // Main target X
		buf.writeD(0); // Main target Y
		buf.writeD(0); // Main target Z
	}
}
