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
package com.l2jfree.gameserver.network.client.packets;

import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.gameobjects.player.PlayerView;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.network.mmocore.DefaultSendablePacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * Just for convenience.
 * 
 * @author savormix
 */
public abstract class L2ServerPacket extends DefaultSendablePacket<L2Client, L2ClientPacket, L2ServerPacket>
{
	/**
	 * @param client
	 * @param activeChar
	 * @return true, if the client can receive this packet, false otherwise
	 */
	@SuppressWarnings("static-method")
	public boolean canBeSentTo(L2Client client, L2Player activeChar)
	{
		// allow at default
		return true;
	}
	
	/**
	 * @param client
	 * @param activeChar
	 */
	public void prepareToSend(L2Client client, L2Player activeChar)
	{
		// do nothing at default
	}
	
	/**
	 * @param client
	 * @param activeChar
	 */
	public void packetSent(L2Client client, L2Player activeChar)
	{
		// do nothing at default
	}
	
	@Override
	protected void writeImpl(L2Client client, MMOBuffer buf) throws RuntimeException
	{
		writeImpl(client, client.getActiveChar(), buf);
	}
	
	/**
	 * Embed data into a network packet.
	 * 
	 * @param client packet sender
	 * @param activeChar
	 * @param buf buffer for packet's data
	 * @throws RuntimeException if a generic failure occurs while writing
	 */
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// do nothing at default
	}
	
	// -- methods for convenience 
	
	protected static final void writeElements(PlayerView view, MMOBuffer buf)
	{
		buf.writeH(view.getAttackElementType()); // Attack element
		buf.writeH(view.getAttackElementPower()); // Attack element power
		buf.writeH(view.getFireElementDefence()); // Fire defense
		buf.writeH(view.getWaterElementDefence()); // Water defense
		buf.writeH(view.getWindElementDefence()); // Wind defense
		buf.writeH(view.getEarthElementDefence()); // Earth defense
		buf.writeH(view.getHolyElementDefence()); // Holy defense
		buf.writeH(view.getDarkElementDefence()); // Dark defense
	}
	
	protected static final void writeSlotObjectIds(PlayerView view, boolean withAccessory, MMOBuffer buf)
	{
		for (int slot : PlayerView.getSlots(withAccessory))
			buf.writeD(view.getSlotObjectId(slot));
	}
	
	protected static final void writeSlotItemDisplayIds(PlayerView view, boolean withAccessory, MMOBuffer buf)
	{
		for (int slot : PlayerView.getSlots(withAccessory))
			buf.writeD(view.getSlotItemDisplayId(slot));
	}
	
	protected static final void writeSlotAugmentationIds(PlayerView view, boolean withAccessory, MMOBuffer buf)
	{
		for (int slot : PlayerView.getSlots(withAccessory))
			buf.writeD(view.getSlotAugmentationId(slot));
	}
	
	protected static void writeCubics(PlayerView view, MMOBuffer buf)
	{
		buf.writeH(view.getCubicData().length); // Cubic count
		for (int finalCubic : view.getCubicData())
			buf.writeH(finalCubic); // Cubic
	}
}
