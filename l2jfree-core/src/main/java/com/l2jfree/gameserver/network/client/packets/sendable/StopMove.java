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
import com.l2jfree.gameserver.gameobjects.components.interfaces.IPlayerView;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix (generated)
 * @author hex1r0
 */
public abstract class StopMove extends L2ServerPacket
{
	/**
	 * A nicer name for {@link StopMove}.
	 * 
	 * @author savormix (generated)
	 * @see StopMove
	 */
	public static final class Arrived extends StopMove
	{
		/** This packet. */
		public static final Arrived PACKET = new Arrived();
		
		/**
		 * Constructs this packet.
		 * 
		 * @see StopMove#StopMove()
		 */
		private Arrived()
		{
		}
	}
	
	/** Constructs this packet. */
	public StopMove()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x47;
	}
	
	@Override
	public void prepareToSend(L2Client client, L2Player activeChar)
	{
		activeChar.getView().refreshPosition();
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		
		final IPlayerView view = activeChar.getView();
		
		buf.writeD(view.getObjectId()); // Actor OID
		buf.writeD(view.getX()); // Location X
		buf.writeD(view.getY()); // Location Y
		buf.writeD(view.getZ()); // Location Z
		buf.writeD(view.getHeading()); // Heading
	}
}
