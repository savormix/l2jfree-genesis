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
public abstract class ExShowCropInfoPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link ExShowCropInfoPacket}.
	 * 
	 * @author savormix (generated)
	 * @see ExShowCropInfoPacket
	 */
	public static final class CropSaleInfo extends ExShowCropInfoPacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see ExShowCropInfoPacket#ExShowCropInfoPacket()
		 */
		public CropSaleInfo()
		{
		}
	}
	
	private static final int[] EXT_OPCODES = { 0x24, 0x00 };
	
	/** Constructs this packet. */
	public ExShowCropInfoPacket()
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
		buf.writeD(0); // Sales
		final int sizeA = 0; // Crop count
		buf.writeD(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeD(0); // Crop
			buf.writeQ(0L); // Quantity bought
			buf.writeQ(0L); // Total quantity bought
			buf.writeQ(0L); // Price
			buf.writeC(0); // Reward
			buf.writeD(0); // Seed level
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
