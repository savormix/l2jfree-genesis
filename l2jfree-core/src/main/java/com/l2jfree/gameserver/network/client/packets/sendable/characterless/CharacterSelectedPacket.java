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

import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.gameobjects.ObjectPosition;
import com.l2jfree.gameserver.gameobjects.player.PlayerAppearance;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.gameserver.templates.L2PlayerTemplate;
import com.l2jfree.gameserver.templates.player.PlayerBaseTemplate;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * Sent to client after a character is selected by pressing the 'Start' button.
 * 
 * @author hex1r0
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
		 * @param obfusKey client packet opcode obfuscation key
		 * @see CharacterSelectedPacket#CharacterSelectedPacket(int)
		 */
		public SelectedCharacterInfo(int obfusKey)
		{
			super(obfusKey);
		}
	}
	
	private final int _obfusKey;
	
	/**
	 * Constructs this packet.
	 * 
	 * @param obfusKey client packet opcode obfuscation key
	 */
	public CharacterSelectedPacket(int obfusKey)
	{
		_obfusKey = obfusKey;
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
		
		// server sends this packet right AFTER an active char has been selected
		if (activeChar == null)
		{
			_log.fatal("Is server just sending random packets?");
			client.closeNow();
			return;
		}
		
		// FIXME: use views to provide data reliably or no longer applicable?
		final PlayerAppearance appearance = activeChar.getAppearance();
		final ObjectPosition position = activeChar.getPosition();
		final L2PlayerTemplate template = activeChar.getTemplate();
		final PlayerBaseTemplate baseTemplate = template.getPlayerBaseTemplate(appearance.getGender());
		
		buf.writeS(activeChar.getName()); // Name
		buf.writeD(0); // TODO: Character ID
		buf.writeS(activeChar.getTitle()); // Title
		buf.writeD(client.getSessionId()); // Session ID
		buf.writeD(0); // Pledge ID
		buf.writeD(0); // 0
		buf.writeD(baseTemplate.getGender()); // Sex
		buf.writeD(baseTemplate.getRace()); // Race
		buf.writeD(template.getClassId()); // Main class
		buf.writeD(1); // Selected??
		buf.writeD(position.getX()); // Location X
		buf.writeD(position.getY()); // Location Y
		buf.writeD(position.getZ()); // Location Z
		buf.writeF(100D); // Current HP
		buf.writeF(30D); // Current MP
		buf.writeD(0); // SP
		buf.writeQ(0L); // XP
		buf.writeD(1); // Level
		buf.writeD(0); // Karma
		buf.writeD(0); // PK Count
		buf.writeD(1); // INT
		buf.writeD(1); // STR
		buf.writeD(1); // CON
		buf.writeD(1); // MEN
		buf.writeD(1); // DEX
		buf.writeD(1); // WIT
		buf.writeD(0); // Game time
		buf.writeD(0); // 0
		buf.writeD(template.getClassId()); // Current class
		buf.writeD(0); // 0
		buf.writeD(0); // 0
		buf.writeD(0); // 0
		buf.writeD(0); // 0
		buf.writeB(new byte[64]); // Unk
		buf.writeD(_obfusKey); // In-world obfuscation key
	}
}
