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
public abstract class AgitDecoInfoPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link AgitDecoInfoPacket}.
	 * 
	 * @author savormix (generated)
	 * @see AgitDecoInfoPacket
	 */
	public static final class AgitDecoInfo extends AgitDecoInfoPacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see AgitDecoInfoPacket#AgitDecoInfoPacket()
		 */
		public AgitDecoInfo()
		{
		}
	}
	
	/** Constructs this packet. */
	public AgitDecoInfoPacket()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0xfd;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		// dupes no longer apply in GoD
		buf.writeD(0); // Hideout
		buf.writeC(0); // HP restoration level
		buf.writeC(0); // MP restoration level
		buf.writeC(0); // MP restoration level (dupe)
		buf.writeC(0); // XP restoration level
		buf.writeC(0); // Teleport selection level
		buf.writeC(0); // 0
		buf.writeC(0); // Curtain level
		buf.writeC(0); // Item creation level
		buf.writeC(0); // Support magic level
		buf.writeC(0); // Support magic level (dupe)
		buf.writeC(0); // Front plateform level
		buf.writeC(0); // Item creation level (dupe)
		buf.writeD(0); // 0
		buf.writeD(0); // 0
		buf.writeD(0); // ??? 0
		buf.writeD(0); // ??? 0
		buf.writeD(0); // ??? 0
	}
}
