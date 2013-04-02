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
public class MagicSkillUse extends L2ServerPacket
{
	/** Constructs this packet. */
	public MagicSkillUse()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x48;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		final ClientProtocolVersion cpv = client.getVersion();
		if (cpv.isNewerThanOrEqualTo(ClientProtocolVersion.GODDESS_OF_DESTRUCTION))
			buf.writeD(0); // ??? 0
		buf.writeD(0); // Caster OID
		buf.writeD(0); // Main target OID
		if (cpv.isNewerThanOrEqualTo(ClientProtocolVersion.GODDESS_OF_DESTRUCTION))
			buf.writeC(0); // ??? 0
		buf.writeD(0); // Skill
		buf.writeD(0); // Level
		buf.writeD(0); // Cast time
		if (cpv.isNewerThanOrEqualTo(ClientProtocolVersion.GODDESS_OF_DESTRUCTION))
			buf.writeD(-1); // ??? -1/0
		buf.writeD(0); // Cooldown
		buf.writeD(0); // Caster X
		buf.writeD(0); // Caster Y
		buf.writeD(0); // Caster Z
		final int sizeA = 0; // ???
		buf.writeD(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeH(0); // ??? 0
		}
		buf.writeD(0); // Main target X
		buf.writeD(0); // Main target Y
		buf.writeD(0); // Main target Z
	}
}
