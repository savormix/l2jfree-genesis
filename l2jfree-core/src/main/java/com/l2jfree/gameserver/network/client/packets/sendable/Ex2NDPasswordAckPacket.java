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
public abstract class Ex2NDPasswordAckPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link Ex2NDPasswordAckPacket}.
	 * 
	 * @author savormix (generated)
	 * @see Ex2NDPasswordAckPacket
	 */
	public static final class CharacterAuthorizationSetupResult extends Ex2NDPasswordAckPacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see Ex2NDPasswordAckPacket#Ex2NDPasswordAckPacket()
		 */
		public CharacterAuthorizationSetupResult()
		{
		}
	}
	
	private static final int[] EXT_OPCODES = { 0xe7, 0x00 };
	
	/** Constructs this packet. */
	public Ex2NDPasswordAckPacket()
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
		buf.writeC(0); // Hidden
		buf.writeD(0); // Result
		buf.writeD(0); // 0
	}
}
