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
public abstract class CharacterSelectedPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link CharacterSelectedPacket}.
	 * 
	 * @author savormix (generated)
	 * @see CharacterSelectedPacket
	 */
	public static final class SelectedCharacterInfo extends CharacterSelectedPacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see CharacterSelectedPacket#CharacterSelectedPacket()
		 */
		public SelectedCharacterInfo()
		{
		}
	}

	/** Constructs this packet. */
	public CharacterSelectedPacket()
	{
	}

	@Override
	protected int getOpcode()
	{
		return 0x0b;
	}

	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeS(""); // Name
		buf.writeD(0); // Character ID
		buf.writeS(""); // Title
		buf.writeD(0); // Session ID
		buf.writeD(0); // Pledge ID
		buf.writeD(0); // 0
		buf.writeD(0); // Sex
		buf.writeD(0); // Race
		buf.writeD(0); // Main class
		buf.writeD(0); // Selected??
		buf.writeD(0); // Location X
		buf.writeD(0); // Location Y
		buf.writeD(0); // Location Z
		buf.writeF(0D); // Current HP
		buf.writeF(0D); // Current MP
		buf.writeD(0); // SP
		buf.writeQ(0L); // XP
		buf.writeD(0); // Level
		buf.writeD(0); // Karma
		buf.writeD(0); // PK Count
		buf.writeD(0); // INT
		buf.writeD(0); // STR
		buf.writeD(0); // CON
		buf.writeD(0); // MEN
		buf.writeD(0); // DEX
		buf.writeD(0); // WIT
		buf.writeD(0); // Game time
		buf.writeD(0); // 0
		buf.writeD(0); // Current class
		buf.writeD(0); // 0
		buf.writeD(0); // 0
		buf.writeD(0); // 0
		buf.writeD(0); // 0
		buf.writeB(new byte[64]); // Unk
		buf.writeD(0); // In-world obfuscation key
	}
}
