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
package com.l2jfree.gameserver.network.client.packets.receivable;

import java.nio.BufferUnderflowException;

import com.l2jfree.ClientProtocolVersion;
import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix (generated)
 */
public abstract class SetPrivateStoreListBuy extends L2ClientPacket
{
	/**
	 * A nicer name for {@link SetPrivateStoreListBuy}.
	 * 
	 * @author savormix (generated)
	 * @see SetPrivateStoreListBuy
	 */
	public static final class RequestStartBuyShop extends SetPrivateStoreListBuy
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see SetPrivateStoreListBuy#SetPrivateStoreListBuy()
		 */
		public RequestStartBuyShop()
		{
		}
	}
	
	/** Packet's identifier */
	public static final int OPCODE = 0x9a;
	
	/** Constructs this packet. */
	public SetPrivateStoreListBuy()
	{
	}
	
	@Override
	protected int getMinimumLength()
	{
		return READ_D;
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		final boolean god = getClient().getVersion().isNewerThanOrEqualTo(ClientProtocolVersion.GODDESS_OF_DESTRUCTION);
		final int sizeA = buf.readD(); // Offer count
		for (int i = 0; i < sizeA; i++)
		{
			buf.readD(); // Item
			buf.readH(); // Enchant level
			buf.readH(); // Name exists
			buf.readQ(); // Quantity
			buf.readQ(); // Price/unit
			buf.readH(); // Attack element
			buf.readH(); // Attack element power
			buf.readH(); // Fire defense
			buf.readH(); // Water defense
			buf.readH(); // Wind defense
			buf.readH(); // Earth defense
			buf.readH(); // Holy defense
			buf.readH(); // Dark defense
			if (god)
				buf.readD(); // ??? 0
		}
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO: implement
	}
}
