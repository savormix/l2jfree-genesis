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
import com.l2jfree.gameserver.gameobjects.L2Object;
import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.gameobjects.components.interfaces.ICharacterView;
import com.l2jfree.gameserver.gameobjects.components.interfaces.IObjectView;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix (generated)
 */
public abstract class MoveToPawn extends L2ServerPacket
{
	/**
	 * A nicer name for {@link MoveToPawn}.
	 * 
	 * @author savormix (generated)
	 * @see MoveToPawn
	 */
	public static final class Follow extends MoveToPawn
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see MoveToPawn#MoveToPawn(L2Character, L2Object, int)
		 */
		public Follow(L2Character activeChar, L2Object target, int distance)
		{
			super(activeChar, target, distance);
		}
	}
	
	private final L2Character _activeChar;
	private final L2Object _target;
	private final int _distance;
	
	/**
	 * Constructs this packet.
	 * 
	 * @param activeChar
	 * @param target
	 * @param distance
	 */
	public MoveToPawn(L2Character activeChar, L2Object target, int distance)
	{
		_activeChar = activeChar;
		_activeChar.getView().refreshPosition();
		_target = target;
		_target.getView().refreshPosition();
		_distance = distance;
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x72;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		final ICharacterView view = _activeChar.getView();
		final IObjectView targetView = _target.getView();
		
		buf.writeD(view.getObjectId()); // Follower OID
		buf.writeD(targetView.getObjectId()); // Target OID
		buf.writeD(_distance); // Follow distance
		buf.writeD(view.getX()); // Current follower X
		buf.writeD(view.getY()); // Current follower Y
		buf.writeD(view.getZ()); // Current follower Z
		buf.writeD(targetView.getX()); // Current target X
		buf.writeD(targetView.getY()); // Current target Y
		buf.writeD(targetView.getZ()); // Current target Z
	}
}
