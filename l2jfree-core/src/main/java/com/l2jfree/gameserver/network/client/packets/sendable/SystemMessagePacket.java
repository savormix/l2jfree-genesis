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
public abstract class SystemMessagePacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link SystemMessagePacket}.
	 * 
	 * @author savormix (generated)
	 * @see SystemMessagePacket
	 */
	public static final class SystemMessage extends SystemMessagePacket
	{
		/**
		 * Constructs this packet as an immutable system message.
		 * 
		 * @param immutableMessageId message ID
		 * @see SystemMessagePacket#SystemMessagePacket(int)
		 */
		public SystemMessage(int immutableMessageId)
		{
			super(immutableMessageId);
		}
		
		public static SystemMessage valueOf(String message)
		{
			// FIXME
			//SystemMessage sm = new SystemMessage(SystemMessageId.S1);
			//sm.addString(msg);
			//return sm;
			_log.info("SystemMessage: " + message);
			return null;
		}
	}
	
	private final int _messageId;
	
	// TODO: implement 4 real
	
	/**
	 * Constructs this packet as an immutable system message.
	 * 
	 * @param immutableMessageId message ID
	 */
	public SystemMessagePacket(int immutableMessageId)
	{
		_messageId = immutableMessageId;
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x62;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeD(_messageId); // Message
		final int sizeA = 0; // Parameter count
		buf.writeD(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeD(0); // Parameter, branching condition
			// branch with param.StringValue
			{
				buf.writeS(""); // Value
			}
			// branch with param.Player
			{
				buf.writeS(""); // Player
			}
			// branch with param.Item
			{
				buf.writeD(0); // Item
			}
			// branch with param.Item2
			{
				buf.writeD(0); // Item
				buf.writeD(0); // ??? 0
			}
			// branch with param.PledgeBase
			{
				buf.writeD(0); // Real estate
			}
			// branch with param.NumberValue
			{
				buf.writeD(0); // Value
			}
			// branch with param.Npc
			{
				buf.writeD(0); // NPC
			}
			// branch with param.Element
			{
				buf.writeD(0); // Element
			}
			// branch with param.Fstring
			{
				buf.writeD(0); // Value
			}
			// branch with param.Sysstring
			{
				buf.writeD(0); // Value
			}
			// branch with param.Instance
			{
				buf.writeD(0); // Instance
			}
			// branch with param.Quantity
			{
				buf.writeQ(0L); // Quantity
			}
			// branch with param.Skill
			{
				buf.writeD(0); // Skill
				buf.writeD(0); // Level
			}
			// branch with param.Zone
			{
				buf.writeD(0); // Location X
				buf.writeD(0); // Location Y
				buf.writeD(0); // Location Z
			}
			// branch with param.Damage (GoD only)
			{
				buf.writeD(0); // Target OID
				buf.writeD(0); // Attacker OID
				buf.writeD(0); // HP change
			}
		}
	}
}
