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
import com.l2jfree.gameserver.gameobjects.components.interfaces.IPlayerView;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
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
	public void prepareToSend(L2Client client, L2Player activeChar)
	{
		// hex1r0: can be null? this packets is sent from CharacterSelect.RequestSelectCharacter, 
		// so all required null checks have to be done there 
		activeChar.getView().refresh();
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		
		// hex1r0: can be null? this packets is sent from CharacterSelect.RequestSelectCharacter, 
		// so all required null checks have to be done there
		// server sends this packet right AFTER an active char has been selected
		if (activeChar == null)
		{
			_log.fatal("Is server just sending random packets?");
			client.closeNow();
			return;
		}
		
		final IPlayerView view = activeChar.getView();
		
		buf.writeS(view.getName()); // Name
		buf.writeD(view.getCharacterId()); // Character ID
		buf.writeS(view.getTitle()); // Title
		buf.writeD(client.getSessionId()); // Session ID
		buf.writeD(view.getPledgeId()); // Pledge ID
		buf.writeD(0); // 0
		buf.writeD(view.getGender()); // Sex
		buf.writeD(view.getRace()); // Race
		buf.writeD(view.getMainClassId()); // Main class
		buf.writeD(1); // Selected??
		buf.writeD(view.getX()); // Location X
		buf.writeD(view.getY()); // Location Y
		buf.writeD(view.getZ()); // Location Z
		buf.writeF(view.getCurrentHP()); // Current HP
		buf.writeF(view.getCurrentMP()); // Current MP
		buf.writeD(view.getSP()); // SP
		buf.writeQ(view.getExp()); // XP
		buf.writeD(view.getLevel()); // Level
		buf.writeD(view.getKarma()); // Karma
		buf.writeD(view.getPkCount()); // PK Count
		buf.writeD(view.getINT()); // INT
		buf.writeD(view.getSTR()); // STR
		buf.writeD(view.getCON()); // CON
		buf.writeD(view.getMEN()); // MEN
		buf.writeD(view.getDEX()); // DEX
		buf.writeD(view.getWIT()); // WIT
		buf.writeD(0); // Game time
		buf.writeD(0); // 0
		buf.writeD(view.getActiveClassId()); // Current class
		buf.writeD(0); // 0
		buf.writeD(0); // 0
		buf.writeD(0); // 0
		buf.writeD(0); // 0
		buf.writeB(new byte[64]); // Unk
		buf.writeD(_obfusKey); // In-world obfuscation key
	}
}
