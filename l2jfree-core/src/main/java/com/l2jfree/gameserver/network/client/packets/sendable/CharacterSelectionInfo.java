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
public abstract class CharacterSelectionInfo extends L2ServerPacket
{
	/**
	 * A nicer name for {@link CharacterSelectionInfo}.
	 * 
	 * @author savormix (generated)
	 * @see CharacterSelectionInfo
	 */
	public static final class AvailableCharacters extends CharacterSelectionInfo
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see CharacterSelectionInfo#CharacterSelectionInfo()
		 */
		public AvailableCharacters()
		{
		}
	}

	/** Constructs this packet. */
	public CharacterSelectionInfo()
	{
	}

	@Override
	protected int getOpcode()
	{
		return 0x09;
	}

	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		final int sizeA = 0; // Character count
		buf.writeD(sizeA);
		buf.writeD(0); // New characters
		buf.writeC(0); // 0
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeS(""); // Character name
			buf.writeD(0); // Character ID
			buf.writeS(""); // Account name
			buf.writeD(0); // Session ID
			buf.writeD(0); // Pledge ID
			buf.writeD(0); // 0
			buf.writeD(0); // Sex
			buf.writeD(0); // Race
			buf.writeD(0); // Main class
			buf.writeD(0); // Game server
			buf.writeD(0); // Location X
			buf.writeD(0); // Location Y
			buf.writeD(0); // Location Z
			buf.writeF(0D); // Current HP
			buf.writeF(0D); // Current MP
			buf.writeD(0); // SP
			buf.writeQ(0L); // XP
			buf.writeF(0D); // XP %
			buf.writeD(0); // Level
			buf.writeD(0); // Karma
			buf.writeD(0); // PK Count
			buf.writeD(0); // PvP Count
			buf.writeD(0); // ??? 0
			buf.writeD(0); // ??? 0
			buf.writeD(0); // ??? 0
			buf.writeD(0); // ??? 0
			buf.writeD(0); // ??? 0
			buf.writeD(0); // ??? 0
			buf.writeD(0); // ??? 0
			buf.writeD(0); // Shirt
			buf.writeD(0); // Right earring
			buf.writeD(0); // Left earring
			buf.writeD(0); // Necklace
			buf.writeD(0); // Right ring
			buf.writeD(0); // Left ring
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
			buf.writeD(0); // Hair style
			buf.writeD(0); // Hair color
			buf.writeD(0); // Face
			buf.writeF(0D); // Maximum HP
			buf.writeF(0D); // Maximum MP
			buf.writeD(0); // Time of deletion
			buf.writeD(0); // Current class
			buf.writeD(0); // Selected
			buf.writeC(0); // Weapon enchant glow
			buf.writeD(0); // ??? 0
			buf.writeD(0); // Transformation
			buf.writeD(0); // Pet
			buf.writeD(0); // Pet level
			buf.writeD(0); // ???
			buf.writeD(0); // ??? Pet food
			buf.writeF(0D); // Pet maximum HP
			buf.writeF(0D); // Pet current HP
			buf.writeD(0); // Vitality
		}
	}
}
