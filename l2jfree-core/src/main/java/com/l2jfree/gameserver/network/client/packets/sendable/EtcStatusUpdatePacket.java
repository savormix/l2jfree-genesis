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
public abstract class EtcStatusUpdatePacket extends StaticPacket
{
	/**
	 * A nicer name for {@link EtcStatusUpdatePacket}.
	 * 
	 * @author savormix (generated)
	 * @see EtcStatusUpdatePacket
	 */
	public static final class EtcEffectIcons extends EtcStatusUpdatePacket
	{
		public static final EtcEffectIcons PACKET = new EtcEffectIcons();
		
		/**
		 * Constructs this packet.
		 * 
		 * @see EtcStatusUpdatePacket#EtcStatusUpdatePacket()
		 */
		private EtcEffectIcons()
		{
		}
	}
	
	/** Constructs this packet. */
	public EtcStatusUpdatePacket()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0xf9;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeD(0); // Charges
		buf.writeD(0); // Weight penalty level
		buf.writeD(0); // Blocking all chat
		buf.writeD(0); // In a dangerous area
		buf.writeD(0); // Grade penalty level: weapon
		buf.writeD(0); // Grade penalty level: armor
		buf.writeD(0); // Charm of courage
		buf.writeD(0); // Death penalty level
		buf.writeD(0); // Souls
	}
}
