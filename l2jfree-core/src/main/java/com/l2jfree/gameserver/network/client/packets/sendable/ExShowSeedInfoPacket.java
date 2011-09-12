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
public abstract class ExShowSeedInfoPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link ExShowSeedInfoPacket}.
	 * 
	 * @author savormix (generated)
	 * @see ExShowSeedInfoPacket
	 */
	public static final class SeedSaleInfo extends ExShowSeedInfoPacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see ExShowSeedInfoPacket#ExShowSeedInfoPacket()
		 */
		public SeedSaleInfo()
		{
		}
	}
	
	private static final int[] EXT_OPCODES = { 0x23, 0x00, };
	
	/** Constructs this packet. */
	public ExShowSeedInfoPacket()
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
		buf.writeC(0); // 0
		buf.writeD(0); // Castle
		buf.writeD(0); // 0
		final int sizeA = 0; // Seed count
		buf.writeD(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeD(0); // Seed
			buf.writeQ(0L); // Remaining in stock
			buf.writeQ(0L); // Today's stock
			buf.writeQ(0L); // Price
			buf.writeD(0); // Level
			final int sizeB = 0; // Reward 1 items
			buf.writeC(sizeB);
			for (int j = 0; j < sizeA; j++)
			{
				buf.writeD(0); // Reward 1
			}
			final int sizeC = 0; // Reward 2 items
			buf.writeC(sizeC);
			for (int j = 0; j < sizeB; j++)
			{
				buf.writeD(0); // Reward 2
			}
		}
	}
}
