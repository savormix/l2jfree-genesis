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
		/**
		 * Constructs this packet.
		 * 
		 * @see StopMove#StopMove(L2Character)
		 */
		public Arrived(L2Character activeChar)
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
	private StopMove(L2Character activeChar)
	{
		_activeChar = activeChar;
		_activeChar.getView().refreshPosition();
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x47;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		final ICharacterView view = _activeChar.getView();
		
		buf.writeD(view.getObjectId()); // Actor OID
		buf.writeD(view.getX()); // Location X
		buf.writeD(view.getY()); // Location Y
		buf.writeD(view.getZ()); // Location Z
		buf.writeD(view.getHeading()); // Heading
	}
}
