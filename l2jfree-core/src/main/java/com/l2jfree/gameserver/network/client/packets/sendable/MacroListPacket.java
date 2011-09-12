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
public abstract class MacroListPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link MacroListPacket}.
	 * 
	 * @author savormix (generated)
	 * @see MacroListPacket
	 */
	public static final class MacroInfo extends MacroListPacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see MacroListPacket#MacroListPacket()
		 */
		public MacroInfo()
		{
		}
	}
	
	/** Constructs this packet. */
	public MacroListPacket()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0xe8;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeD(0); // Revision
		buf.writeC(0); // 0
		buf.writeC(0); // Total macros
		final int sizeA = 0; // Definition
		buf.writeC(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeD(0); // Macro ID
			buf.writeS(""); // Name
			buf.writeS(""); // Description
			buf.writeS(""); // Acronym
			buf.writeC(0); // Icon
			final int sizeB = 0; // Command count
			buf.writeC(sizeB);
			for (int j = 0; j < sizeB; j++)
			{
				buf.writeC(0); // Sequence number
				buf.writeC(0); // Type
				buf.writeD(0); // Skill
				buf.writeC(0); // Shortcut
				buf.writeS(""); // Command
			}
		}
	}
}
