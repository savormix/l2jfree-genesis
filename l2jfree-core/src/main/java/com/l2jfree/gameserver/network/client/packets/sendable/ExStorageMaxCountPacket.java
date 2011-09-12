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
public abstract class ExStorageMaxCountPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link ExStorageMaxCountPacket}.
	 * 
	 * @author savormix (generated)
	 * @see ExStorageMaxCountPacket
	 */
	public static final class StorageSlotInfo extends ExStorageMaxCountPacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see ExStorageMaxCountPacket#ExStorageMaxCountPacket()
		 */
		public StorageSlotInfo()
		{
		}
	}
	
	private static final int[] EXT_OPCODES = { 0x2f, 0x00 };
	
	/** Constructs this packet. */
	public ExStorageMaxCountPacket()
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
		buf.writeD(0); // Inventory slots
		buf.writeD(0); // Warehouse slots
		buf.writeD(0); // Clan warehouse slots
		buf.writeD(0); // Selling store slots
		buf.writeD(0); // Buying store slots
		buf.writeD(0); // Dwarven recipe book slots
		buf.writeD(0); // Common recipe book slots
		buf.writeD(0); // Extra inventory slots (belt?)
		buf.writeD(0); // Quest inventory slots
	}
}
