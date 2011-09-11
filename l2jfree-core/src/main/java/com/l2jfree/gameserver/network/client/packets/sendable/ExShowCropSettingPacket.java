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
public abstract class ExShowCropSettingPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link ExShowCropSettingPacket}.
	 * 
	 * @author savormix (generated)
	 * @see ExShowCropSettingPacket
	 */
	public static final class CropSaleSetup extends ExShowCropSettingPacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see ExShowCropSettingPacket#ExShowCropSettingPacket()
		 */
		public CropSaleSetup()
		{
		}
	}

	private static final int[] EXT_OPCODES = {
		0x2b,
		0x00,
	};

	/** Constructs this packet. */
	public ExShowCropSettingPacket()
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
		buf.writeD(0); // Castle
		final int sizeA = 0; // Crop count
		buf.writeD(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeD(0); // Crop
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
			buf.writeQ(0L); // Maximum quantity
			buf.writeQ(0L); // Production price
			buf.writeQ(0L); // Minimum price
			buf.writeQ(0L); // Maximum price
			buf.writeQ(0L); // Today's quantity
			buf.writeQ(0L); // Today's price
			buf.writeC(0); // Today's reward
			buf.writeQ(0L); // Next quantity
			buf.writeQ(0L); // Next price
			buf.writeC(0); // Next reward
		}
	}
}
