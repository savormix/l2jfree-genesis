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
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix (generated)
 */
public abstract class HennaInfoPacket extends StaticPacket
{
	/**
	 * A nicer name for {@link HennaInfoPacket}.
	 * 
	 * @author savormix (generated)
	 * @see HennaInfoPacket
	 */
	public static final class MyHennaList extends HennaInfoPacket
	{
		public static final MyHennaList PACKET = new MyHennaList();
		
		/**
		 * Constructs this packet.
		 * 
		 * @see HennaInfoPacket#HennaInfoPacket()
		 */
		private MyHennaList()
		{
		}
	}
	
	/** Constructs this packet. */
	public HennaInfoPacket()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0xe5;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeC(0); // INT modifier
		buf.writeC(0); // STR modifier
		buf.writeC(0); // CON modifier
		buf.writeC(0); // MEN modifier
		buf.writeC(0); // DEX modifier
		buf.writeC(0); // WIT modifier
		// lock for read before writing this
		buf.writeD(3); // Available slots
		final int sizeA = 0; // Used slots
		buf.writeD(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeD(0); // Henna
			buf.writeD(1); // 1
		}
	}
}
