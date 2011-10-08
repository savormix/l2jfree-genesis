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
package com.l2jfree.gameserver.util;

import com.l2jfree.gameserver.gameobjects.L2Character;
import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.gameserver.world.L2World;
import com.l2jfree.lang.L2Math;

public final class Broadcast
{
	private Broadcast()
	{
		// utility class
	}
	
	public static void toKnowingPlayers(L2Character activeChar, L2ServerPacket packet)
	{
		for (L2Player player : activeChar.getKnownList().getKnowingPlayers())
			if (player != null)
				player.sendPacket(packet);
	}
	
	public static void toKnowingPlayersInRadius(L2Character activeChar, L2ServerPacket packet, int radius)
	{
		if (radius < 0)
		{
			toKnowingPlayers(activeChar, packet);
			return;
		}
		
		for (L2Player player : activeChar.getKnownList().getKnowingPlayers())
			if (player != null)
				if (L2Math.isDistanceLessThan(activeChar.getPosition(), player.getPosition(), radius))
					player.sendPacket(packet);
	}
	
	public static void toSelfAndKnowingPlayers(L2Character activeChar, L2ServerPacket packet)
	{
		if (activeChar instanceof L2Player)
			((L2Player)activeChar).sendPacket(packet);
		
		toKnowingPlayers(activeChar, packet);
	}
	
	public static void toSelfAndKnowingPlayersInRadius(L2Character activeChar, L2ServerPacket packet, int radius)
	{
		if (activeChar instanceof L2Player)
			((L2Player)activeChar).sendPacket(packet);
		
		toKnowingPlayersInRadius(activeChar, packet, radius);
	}
	
	public static void toAllOnlinePlayers(L2ServerPacket packet)
	{
		for (L2Player player : L2World.getPlayers())
			if (player != null)
				player.sendPacket(packet);
	}
}
