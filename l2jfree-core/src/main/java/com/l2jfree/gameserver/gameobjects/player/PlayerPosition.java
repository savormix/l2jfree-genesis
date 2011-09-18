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
package com.l2jfree.gameserver.gameobjects.player;

import com.l2jfree.gameserver.gameobjects.CharacterPosition;
import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.network.client.packets.sendable.ValidateLocation.UpdateLocation;
import com.l2jfree.gameserver.sql.PlayerDB;

/**
 * @author NB4L1
 */
public class PlayerPosition extends CharacterPosition
{
	public PlayerPosition(L2Player activeChar)
	{
		super(activeChar);
	}
	
	public void load(PlayerDB playerDB)
	{
		setXYZ(playerDB.x, playerDB.y, playerDB.z);
		
		setHeading(playerDB.heading);
	}
	
	public void store(PlayerDB playerDB)
	{
		playerDB.x = getX();
		playerDB.y = getY();
		playerDB.z = getZ();
		playerDB.heading = getHeading();
	}
	
	@Override
	public final L2Player getActiveChar()
	{
		return (L2Player)super.getActiveChar();
	}
	
	public void setClientXYZ(int clientX, int clientY, int clientZ)
	{
		setClientPosition(clientX, clientY, clientZ, Integer.MAX_VALUE);
	}
	
	public void setClientPosition(int clientX, int clientY, int clientZ, int clientHeading)
	{
		// TODO add some semi-validation for received data from client, BUT DO NOT TAKE IT AS VALID!
		_log.info(getActiveChar() + " clientX: " + clientX + ", clientY: " + clientY + ", clientZ: " + clientZ);
		if (clientHeading != Integer.MAX_VALUE)
			_log.info(getActiveChar() + " clientHeading: " + clientHeading);
		
		// FIXME only temporarily while we don't have proper movement
		setXYZ(clientX, clientY, clientZ);
		if (clientHeading != Integer.MAX_VALUE)
			setHeading(clientHeading);
		
		// TODO: perhaps make an iterative task instead of replying every time
		// even though we have flood protection
		// broadcast?
		getActiveChar().sendPacket(new UpdateLocation(getActiveChar()));
	}
}
