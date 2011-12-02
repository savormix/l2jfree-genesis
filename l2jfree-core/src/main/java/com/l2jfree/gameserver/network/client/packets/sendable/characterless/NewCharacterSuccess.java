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

import com.l2jfree.ClientProtocolVersion;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.gameserver.templates.L2PlayerTemplate;
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
	
	// FIXME: export to templates for each stat
	private static final int MAX_STAT_VALUE = 70;
	private static final int MIN_STAT_VALUE = 10;
	private static final int MAX_STAT_VALUE_GOD = 99;
	private static final int MIN_STAT_VALUE_GOD = 1;
	
	private static final ClassId[] CLASSES = new ClassId[] { ClassId.HumanFighter, ClassId.HumanMystic,
			ClassId.ElvenFighter, ClassId.ElvenMystic, ClassId.DarkFighter, ClassId.DarkMystic, ClassId.OrcFighter,
			ClassId.OrcMystic, ClassId.DwarvenFighter, ClassId.MaleSoldier, ClassId.FemaleSoldier };
	
	/** Constructs this packet. */
	private NewCharacterSuccess()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x0d;
	}
	
	@Override
	protected void writeImpl(L2Client client, MMOBuffer buf) throws RuntimeException
	{
		final int max, min;
		if (client.getVersion().isNewerThanOrEqualTo(ClientProtocolVersion.GODDESS_OF_DESTRUCTION))
		{
			max = MAX_STAT_VALUE_GOD;
			min = MIN_STAT_VALUE_GOD;
		}
		else
		{
			max = MAX_STAT_VALUE;
			min = MIN_STAT_VALUE;
		}
		
		buf.writeD(CLASSES.length); // Template count
		for (ClassId classId : CLASSES)
		{
			final L2PlayerTemplate template = classId.getTemplate();
			
			buf.writeD(classId.getRace()); // Race
			buf.writeD(classId.getId()); // Class
			buf.writeD(max); // Max STR
			buf.writeD(template.getSTR()); // Base STR
			buf.writeD(min); // Min STR
			buf.writeD(max); // Max DEX
			buf.writeD(template.getDEX()); // Base DEX
			buf.writeD(min); // Min DEX
			buf.writeD(max); // Max CON
			buf.writeD(template.getCON()); // Base CON
			buf.writeD(min); // Min CON
			buf.writeD(max); // Max INT
			buf.writeD(template.getINT()); // Base INT
			buf.writeD(min); // Min INT
			buf.writeD(max); // Max WIT
			buf.writeD(template.getWIT()); // Base WIT
			buf.writeD(min); // Min WIT
			buf.writeD(max); // Max MEN
			buf.writeD(template.getMEN()); // Base MEN
			buf.writeD(min); // Min MEN
		}
	}
}
