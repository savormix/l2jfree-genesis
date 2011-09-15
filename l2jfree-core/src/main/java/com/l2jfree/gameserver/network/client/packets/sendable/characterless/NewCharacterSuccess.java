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

import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.gameserver.templates.player.ClassId;
import com.l2jfree.gameserver.templates.player.PlayerBaseTemplate;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * Sends available character creation templates to client.
 * 
 * @author savormix
 */
public abstract class NewCharacterSuccess extends L2ServerPacket
{
	/**
	 * A nicer name for {@link NewCharacterSuccess}.
	 * 
	 * @author savormix
	 * @see NewCharacterSuccess
	 */
	public static final class CharacterTemplates extends NewCharacterSuccess
	{
		/** This packet. */
		public static final CharacterTemplates PACKET = new CharacterTemplates();
		
		/**
		 * Constructs this packet.
		 * 
		 * @see NewCharacterSuccess#NewCharacterSuccess()
		 */
		private CharacterTemplates()
		{
		}
	}
	
	private static final int DELIMITER_START = 70;
	private static final int DELIMITER_END = 10;
	
	private static final ClassId[] _classes = new ClassId[] { ClassId.HumanFighter, ClassId.HumanMystic,
			ClassId.ElvenFighter, ClassId.ElvenMystic, ClassId.DarkFighter, ClassId.DarkMystic, ClassId.OrcFighter,
			ClassId.OrcMystic, ClassId.DwarvenFighter, ClassId.MaleSoldier, ClassId.FemaleSoldier };
	
	private static final PlayerBaseTemplate[] _baseTemplates = new PlayerBaseTemplate[_classes.length];
	
	/** Constructs this packet. */
	private NewCharacterSuccess()
	{
		for (int i = 0; i < _baseTemplates.length; i++)
			_baseTemplates[i] = _classes[i].getBaseTemplate();
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x0d;
	}
	
	@Override
	protected void writeImpl(L2Client client, MMOBuffer buf) throws RuntimeException
	{
		buf.writeD(_baseTemplates.length); // Template count
		for (int i = 0; i < _baseTemplates.length; i++)
		{
			buf.writeD(_baseTemplates[i].getRace()); // Race
			buf.writeD(_classes[i]); // Class
			buf.writeD(DELIMITER_START); // Stat delimiter: start
			buf.writeD(_baseTemplates[i].getSTR()); // STR
			buf.writeD(DELIMITER_END); // Stat delimiter: end
			buf.writeD(DELIMITER_START); // Stat delimiter: start
			buf.writeD(_baseTemplates[i].getDEX()); // DEX
			buf.writeD(DELIMITER_END); // Stat delimiter: end
			buf.writeD(DELIMITER_START); // Stat delimiter: start
			buf.writeD(_baseTemplates[i].getCON()); // CON
			buf.writeD(DELIMITER_END); // Stat delimiter: end
			buf.writeD(DELIMITER_START); // Stat delimiter: start
			buf.writeD(_baseTemplates[i].getINT()); // INT
			buf.writeD(DELIMITER_END); // Stat delimiter: end
			buf.writeD(DELIMITER_START); // Stat delimiter: start
			buf.writeD(_baseTemplates[i].getWIT()); // WIT
			buf.writeD(DELIMITER_END); // Stat delimiter: end
			buf.writeD(DELIMITER_START); // Stat delimiter: start
			buf.writeD(_baseTemplates[i].getMEN()); // MEN
			buf.writeD(DELIMITER_END); // Stat delimiter: end
		}
	}
}
