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

import com.l2jfree.gameserver.gameobjects.L2Character;
import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.gameobjects.components.interfaces.ICharacterView;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix (generated)
 * @author hex1r0
 */
public abstract class MoveToLocation extends L2ServerPacket
{
	/**
	 * A nicer name for {@link MoveToLocation}.
	 * 
	 * @author savormix (generated)
	 * @see MoveToLocation
	 */
	public static final class Move extends MoveToLocation
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see MoveToLocation#MoveToLocation(L2Character)
		 */
		public Move(L2Character activeChar)
		{
			super(activeChar);
		}
	}
	
	private final L2Character _activeChar;
	
	/**
	 * Constructs this packet.
	 * 
	 * @param activeChar
	 */
	private MoveToLocation(L2Character activeChar)
	{
		_activeChar = activeChar;
		_activeChar.getView().refreshPosition();
		_activeChar.getView().refreshDestination();
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x2f;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		final ICharacterView view = _activeChar.getView();
		
		buf.writeD(view.getObjectId()); // Actor OID
		buf.writeD(view.getDestinationX()); // Destination X
		buf.writeD(view.getDestinationY()); // Destination Y
		buf.writeD(view.getDestinationZ()); // Destination Z
		buf.writeD(view.getX()); // Current X
		buf.writeD(view.getY()); // Current Y
		buf.writeD(view.getZ()); // Current Z
	}
}
