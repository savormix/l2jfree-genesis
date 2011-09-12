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
public abstract class ExHeroListPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link ExHeroListPacket}.
	 * 
	 * @author savormix (generated)
	 * @see ExHeroListPacket
	 */
	public static final class HeroList extends ExHeroListPacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see ExHeroListPacket#ExHeroListPacket()
		 */
		public HeroList()
		{
		}
	}
	
	private static final int[] EXT_OPCODES = { 0x79, 0x00 };
	
	/** Constructs this packet. */
	public ExHeroListPacket()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0xfe;
	}
	
	@Override
	protected int[] getAdditionalOpcodes()
	{
		return EXT_OPCODES;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		final int sizeA = 0; // Hero count
		buf.writeD(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeS(""); // Name
			buf.writeD(0); // Class
			buf.writeS(""); // Pledge
			buf.writeD(0); // Pledge crest ID
			buf.writeS(""); // Alliance
			buf.writeD(0); // Alliance crest ID
			buf.writeD(0); // Count
		}
	}
}
