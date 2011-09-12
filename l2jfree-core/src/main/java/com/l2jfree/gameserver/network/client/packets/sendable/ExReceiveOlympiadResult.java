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
public abstract class ExReceiveOlympiadResult extends L2ServerPacket
{
	/**
	 * A nicer name for {@link ExReceiveOlympiadResult}.
	 * 
	 * @author savormix (generated)
	 * @see ExReceiveOlympiadResult
	 */
	public static final class OlympiadMatchResult extends ExReceiveOlympiadResult
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see ExReceiveOlympiadResult#ExReceiveOlympiadResult()
		 */
		public OlympiadMatchResult()
		{
		}
	}
	
	private static final int[] EXT_OPCODES = { 0xd4, 0x00, 0x01, 0x00, 0x00, 0x00 };
	
	/** Constructs this packet. */
	public ExReceiveOlympiadResult()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0xfe;
	}
	
	@Override
	protected int[] getAdditionalOpcodes()
	{
		return EXT_OPCODES;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeD(0); // ??? 0
		buf.writeS(""); // Victor
		buf.writeD(0); // Winner's side
		buf.writeD(0); // Type??
		buf.writeS(""); // Winner
		buf.writeS(""); // Winner's pledge
		buf.writeD(0); // Winner's pledge ID
		buf.writeD(0); // Winner's class
		buf.writeD(0); // Winner's received damage
		buf.writeD(0); // Winner's olympiad points
		buf.writeD(0); // Winner's points in this match
		buf.writeD(0); // Loser's side
		buf.writeD(0); // Type??
		buf.writeS(""); // Loser
		buf.writeS(""); // Loser's pledge
		buf.writeD(0); // Loser's pledge ID
		buf.writeD(0); // Loser's class
		buf.writeD(0); // Loser's received damage
		buf.writeD(0); // Loser's olympiad points
		buf.writeD(0); // Loser's points in this match
	}
}
