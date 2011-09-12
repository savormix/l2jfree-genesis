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
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author hex1r0
 * @author savormix
 */
public abstract class ExSendManorListPacket extends StaticPacket
{
	/**
	 * A nicer name for {@link ExSendManorListPacket}.
	 * 
	 * @author savormix (generated)
	 * @see ExSendManorListPacket
	 */
	public static final class ManorList extends ExSendManorListPacket
	{
		/** This packet. */
		public static final ManorList PACKET = new ManorList();
		
		/**
		 * Constructs this packet.
		 * 
		 * @see ExSendManorListPacket#ExSendManorListPacket()
		 */
		private ManorList()
		{
		}
	}
	
	private static final int[] EXT_OPCODES = { 0x22, 0x00, };
	
	private final String[] _castles = { "gludio", "dion", "giran", "oren", "aden", "innadrile", "godard", "rune",
			"shuttgart" };
	
	/** Constructs this packet. */
	public ExSendManorListPacket()
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
		buf.writeD(_castles.length);
		for (int i = 0; i < _castles.length; i++)
		{
			buf.writeD(i + 1); // Castle
			buf.writeS(_castles[i]); // Manor
		}
	}
}
