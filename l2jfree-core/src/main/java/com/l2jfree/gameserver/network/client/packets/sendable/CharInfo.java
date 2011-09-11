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
public abstract class CharInfo extends L2ServerPacket
{
	/**
	 * A nicer name for {@link CharInfo}.
	 * 
	 * @author savormix (generated)
	 * @see CharInfo
	 */
	public static final class PlayerInfo extends CharInfo
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see CharInfo#CharInfo()
		 */
		public PlayerInfo()
		{
		}
	}

	/** Constructs this packet. */
	public CharInfo()
	{
	}

	@Override
	protected int getOpcode()
	{
		return 0x31;
	}

	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeD(0); // Location X
		buf.writeD(0); // Location Y
		buf.writeD(0); // Location Z
		buf.writeD(0); // Vehicle OID
		buf.writeD(0); // Player OID
		buf.writeS(""); // Name
		buf.writeD(0); // Race
		buf.writeD(0); // Sex
		buf.writeD(0); // Main class
		buf.writeD(0); // Shirt
		buf.writeD(0); // Helmet
		buf.writeD(0); // Main weapon
		buf.writeD(0); // Shield/support weapon
		buf.writeD(0); // Gloves
		buf.writeD(0); // Chest armor
		buf.writeD(0); // Leg armor
		buf.writeD(0); // Boots
		buf.writeD(0); // Cloak
		buf.writeD(0); // Two-handed weapon
		buf.writeD(0); // 1st hair item
		buf.writeD(0); // 2nd hair item
		buf.writeD(0); // Right bracelet
		buf.writeD(0); // Left bracelet
		buf.writeD(0); // 1st talisman
		buf.writeD(0); // 2nd talisman
		buf.writeD(0); // 3rd talisman
		buf.writeD(0); // 4th talisman
		buf.writeD(0); // 5th talisman
		buf.writeD(0); // 6th talisman
		buf.writeD(0); // Belt
		buf.writeD(0); // Shirt augmentation
		buf.writeD(0); // Helmet augmentation
		buf.writeD(0); // Main weapon augmentation
		buf.writeD(0); // Shield/support weapon augmentation
		buf.writeD(0); // Gloves augmentation
		buf.writeD(0); // Chest armor augmentation
		buf.writeD(0); // Leg armor augmentation
		buf.writeD(0); // Boots augmentation
		buf.writeD(0); // Cloak augmentation
		buf.writeD(0); // Two-handed weapon augmentation
		buf.writeD(0); // 1st hair item augmentation
		buf.writeD(0); // 2nd hair item augmentation
		buf.writeD(0); // Right bracelet augmentation
		buf.writeD(0); // Left bracelet augmentation
		buf.writeD(0); // 1st talisman augmentation
		buf.writeD(0); // 2nd talisman augmentation
		buf.writeD(0); // 3rd talisman augmentation
		buf.writeD(0); // 4th talisman augmentation
		buf.writeD(0); // 5th talisman augmentation
		buf.writeD(0); // 6th talisman augmentation
		buf.writeD(0); // Belt augmentation
		buf.writeD(0); // Talisman slots
		buf.writeD(0); // Can equip cloak
		buf.writeD(0); // In PvP
		buf.writeD(0); // Karma
		buf.writeD(0); // Casting speed
		buf.writeD(0); // Attack speed
		buf.writeD(0); // 0
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
		buf.writeD(0); // Hair style
		buf.writeD(0); // Hair color
		buf.writeD(0); // Face
		buf.writeS(""); // Title
		buf.writeD(0); // Pledge ID
		buf.writeD(0); // Pledge crest ID
		buf.writeD(0); // Alliance ID
		buf.writeD(0); // Alliance crest ID
		buf.writeC(0); // Waiting
		buf.writeC(0); // Moving
		buf.writeC(0); // In combat
		buf.writeC(0); // Lying dead
		buf.writeC(0); // Invisible
		buf.writeC(0); // Mount type
		buf.writeC(0); // Private store
		final int sizeA = 0; // Cubic count
		buf.writeH(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeH(0); // Cubic
		}
		buf.writeC(0); // Looking for party
		buf.writeD(0); // Abnormal effect
		buf.writeC(0); // Flying with mount
		buf.writeH(0); // Evaluation score
		buf.writeD(0); // Mount
		buf.writeD(0); // Current class
		buf.writeD(0); // 0
		buf.writeC(0); // Weapon enchant glow
		buf.writeC(0); // Duel team
		buf.writeD(0); // Pledge insignia ID
		buf.writeC(0); // Noble
		buf.writeC(0); // Hero
		buf.writeC(0); // Fishing
		buf.writeD(0); // Fishing lure X
		buf.writeD(0); // Fishing lure Y
		buf.writeD(0); // Fishing lure Z
		buf.writeD(0); // Name color
		buf.writeD(0); // Heading
		buf.writeD(0); // Pledge rank
		buf.writeD(0); // Pledge unit
		buf.writeD(0); // Title color
		buf.writeD(0); // Cursed weapon level
		buf.writeD(0); // Pledge reputation
		buf.writeD(0); // Transformation
		buf.writeD(0); // Agathion
		buf.writeD(0); // ???
		buf.writeD(0); // Special effect
	}
}
