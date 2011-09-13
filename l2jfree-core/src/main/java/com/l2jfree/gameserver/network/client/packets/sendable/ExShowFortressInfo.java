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
import com.l2jfree.gameserver.network.client.packets.receivable.RequestAllFortressInfo.RequestFortressList;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix (generated)
 * @see RequestFortressList
 */
public abstract class ExShowFortressInfo extends StaticPacket
{
	/**
	 * A nicer name for {@link ExShowFortressInfo}.
	 * 
	 * @author savormix (generated)
	 * @see ExShowFortressInfo
	 */
	public static final class FortressList extends ExShowFortressInfo
	{
		public static final FortressList PACKET = new FortressList();
		
		/**
		 * Constructs this packet.
		 * 
		 * @see ExShowFortressInfo#ExShowFortressInfo()
		 */
		private FortressList()
		{
		}
	}
	
	private static final int[] EXT_OPCODES = { 0x15, 0x00 };
	
	/** Constructs this packet. */
	public ExShowFortressInfo()
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
		// Unowned fortress example:
		//Fortress (D): Antharas' Fortress (116)
		//Owner pledge (S): 
		//Under siege (D): No (0)
		//Owned for (D): N/A (0)
		
		// Owned fortress example:
		//Fortress (D): Hunter's Fortress (118)
		//Owner pledge (S): Eximius
		//Under siege (D): No (0)
		//Owned for (D): 4 days, 7 hours, 45 minutes, 59 seconds (373559)
		
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		final int sizeA = 21; // Fort count
		buf.writeD(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeD(101 + i); // Fortress
			buf.writeS(""); // Owner pledge
			buf.writeD(0); // Under siege
			buf.writeD(0); // Owned for
		}
	}
}
