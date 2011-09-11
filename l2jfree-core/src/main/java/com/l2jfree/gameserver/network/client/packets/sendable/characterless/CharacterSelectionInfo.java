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
package com.l2jfree.gameserver.network.client.packets.sendable.characterless;

import java.util.List;

import com.l2jfree.gameserver.config.ReportedConfig;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.gameserver.sql.PlayerDB;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author hex1r0
 * @author NB4L1
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
		 * @param client authorized client
		 * @see CharacterSelectionInfo#CharacterSelectionInfo(L2Client)
		 */
		public AvailableCharacters(L2Client client)
		{
			super(client);
		}
	}
	
	private final String _account;
	private final int _sessionId;
	private final List<PlayerDB> _players;
	
	/**
	 * Constructs this packet.
	 * 
	 * @param client authorized client
	 */
	public CharacterSelectionInfo(L2Client client)
	{
		_sessionId = client.getSessionId(); // TODO: revise session stuff
		_account = client.getAccountName();
		_players = PlayerDB.findByAccount(_account); // FIXME: don't fetch the whole player only the required data
		
		client.definePlayerSlots(_players);
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x09;
	}
	
	@Override
	protected void writeImpl(L2Client client, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeD(_players.size()); // Character count
		buf.writeD(7); // New characters
		buf.writeC(0); // 0
		for (PlayerDB p : _players)
		{
			buf.writeS(p.name); // Character name
			buf.writeD(0); // TODO: Character ID
			buf.writeS(p.accountName); // Account name
			buf.writeD(_sessionId); // Session ID
			buf.writeD(0); // Pledge ID
			buf.writeD(0); // 0
			buf.writeD(p.gender); // Sex
			buf.writeD(p.baseClassId.getRace()); // Race
			buf.writeD(p.baseClassId); // Main class
			buf.writeD(ReportedConfig.ID); // Game server
			buf.writeD(p.x); // Location X
			buf.writeD(p.y); // Location Y
			buf.writeD(p.z); // Location Z
			buf.writeF(100D); // Current HP
			buf.writeF(30D); // Current MP
			buf.writeD(0); // SP
			buf.writeQ(0L); // XP
			buf.writeF(0D); // XP %
			buf.writeD(1); // Level
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
			buf.writeD(p.hairStyle); // Hair style
			buf.writeD(p.hairColor); // Hair color
			buf.writeD(p.face); // Face
			buf.writeF(100D); // Maximum HP
			buf.writeF(30D); // Maximum MP
			buf.writeD(0); // Time of deletion
			buf.writeD(p.activeClassId); // Current class
			buf.writeD(1); // Selected
			buf.writeC(0); // Weapon enchant glow
			buf.writeD(0); // ??? 0
			buf.writeD(0); // Transformation
			buf.writeD(0); // Pet
			buf.writeD(0); // Pet level
			buf.writeD(0); // ???
			buf.writeD(0); // ??? Pet food
			buf.writeF(0D); // Pet maximum HP
			buf.writeF(0D); // Pet current HP
			buf.writeD(20000); // Vitality
		}
	}
}
