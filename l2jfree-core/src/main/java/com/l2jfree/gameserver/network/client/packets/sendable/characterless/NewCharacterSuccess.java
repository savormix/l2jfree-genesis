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
	
	private final ClassId[] _templates; // FIXME: suitable templates
	
	/** Constructs this packet. */
	public NewCharacterSuccess()
	{
		_templates =
				new ClassId[] { ClassId.HumanFighter, ClassId.HumanMystic, ClassId.ElvenFighter, ClassId.ElvenMystic,
						ClassId.DarkFighter, ClassId.DarkMystic, ClassId.OrcFighter, ClassId.OrcMystic,
						ClassId.DwarvenFighter, ClassId.MaleSoldier, ClassId.FemaleSoldier };
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x0d;
	}
	
	@Override
	protected void writeImpl(L2Client client, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeD(_templates.length); // Template count
		for (ClassId temp : _templates)
		{
			buf.writeD(temp.getRace()); // Race
			buf.writeD(temp); // Class
			buf.writeD(DELIMITER_START); // Stat delimiter: start
			buf.writeD(0); // STR
			buf.writeD(DELIMITER_END); // Stat delimiter: end
			buf.writeD(DELIMITER_START); // Stat delimiter: start
			buf.writeD(0); // DEX
			buf.writeD(DELIMITER_END); // Stat delimiter: end
			buf.writeD(DELIMITER_START); // Stat delimiter: start
			buf.writeD(0); // CON
			buf.writeD(DELIMITER_END); // Stat delimiter: end
			buf.writeD(DELIMITER_START); // Stat delimiter: start
			buf.writeD(0); // INT
			buf.writeD(DELIMITER_END); // Stat delimiter: end
			buf.writeD(DELIMITER_START); // Stat delimiter: start
			buf.writeD(0); // WIT
			buf.writeD(DELIMITER_END); // Stat delimiter: end
			buf.writeD(DELIMITER_START); // Stat delimiter: start
			buf.writeD(0); // MEN
			buf.writeD(DELIMITER_END); // Stat delimiter: end
		}
	}
}
