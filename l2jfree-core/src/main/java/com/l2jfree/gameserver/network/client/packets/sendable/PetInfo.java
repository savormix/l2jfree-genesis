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
public abstract class PetInfo extends L2ServerPacket
{
	/**
	 * A nicer name for {@link PetInfo}.
	 * 
	 * @author savormix (generated)
	 * @see PetInfo
	 */
	public static final class ServitorInfo extends PetInfo
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see PetInfo#PetInfo()
		 */
		public ServitorInfo()
		{
		}
	}

	/** Constructs this packet. */
	public PetInfo()
	{
	}

	@Override
	protected int getOpcode()
	{
		return 0xb2;
	}

	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeD(0); // Type
		buf.writeD(0); // Servitor OID
		buf.writeD(0); // Servitor
		buf.writeD(0); // Attackable
		buf.writeD(0); // Location X
		buf.writeD(0); // Location Y
		buf.writeD(0); // Location Z
		buf.writeD(0); // Heading
		buf.writeD(0); // 0
		buf.writeD(0); // Casting speed
		buf.writeD(0); // Attack speed
		buf.writeD(0); // Running speed (on ground)
		buf.writeD(0); // Walking speed (on ground)
		buf.writeD(0); // Running speed (in water)
		buf.writeD(0); // Walking speed (in water)
		buf.writeD(0); // Running speed (in air) ???
		buf.writeD(0); // Walking speed (in air) ???
		buf.writeD(0); // Running speed (in air) while mounted?
		buf.writeD(0); // Walking speed (in air) while mounted?
		buf.writeF(0D); // Movement speed multiplier
		buf.writeF(0D); // Attack speed multiplier
		buf.writeF(0D); // Collision radius
		buf.writeF(0D); // Collision height
		buf.writeD(0); // Main weapon
		buf.writeD(0); // Chest armor
		buf.writeD(0); // Shield/support weapon
		buf.writeC(0); // Owner online
		buf.writeC(0); // Moving
		buf.writeC(0); // In combat
		buf.writeC(0); // Lying dead
		buf.writeC(0); // Status
		buf.writeD(0); // ???
		buf.writeS(""); // Name
		buf.writeD(0); // ???
		buf.writeS(""); // Title
		buf.writeD(0); // 1
		buf.writeD(0); // In PvP
		buf.writeD(0); // Karma
		buf.writeD(0); // Current satiation
		buf.writeD(0); // Maximum satiation
		buf.writeD(0); // Current HP
		buf.writeD(0); // Maximum HP
		buf.writeD(0); // Current MP
		buf.writeD(0); // Maximum MP
		buf.writeD(0); // SP
		buf.writeD(0); // Level
		buf.writeQ(0L); // XP
		buf.writeQ(0L); // Current level XP
		buf.writeQ(0L); // Next level XP
		buf.writeD(0); // Current carried weight
		buf.writeD(0); // Maximum carried weight
		buf.writeD(0); // P. Atk.
		buf.writeD(0); // P. Def.
		buf.writeD(0); // M. Atk.
		buf.writeD(0); // M. Def.
		buf.writeD(0); // Accuracy
		buf.writeD(0); // Evasion
		buf.writeD(0); // Critical
		buf.writeD(0); // Actual movement speed
		buf.writeD(0); // Attack speed
		buf.writeD(0); // Casting speed
		buf.writeD(0); // Abnormal effect
		buf.writeH(0); // Mountable
		buf.writeC(0); // Weapon enchant glow
		buf.writeH(0); // ??? 0
		buf.writeC(0); // Duel team
		buf.writeD(0); // Soulshot usage
		buf.writeD(0); // Spiritshot usage
		buf.writeD(0); // Ability level
		buf.writeD(0); // Special effect
	}
}
