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
package com.l2jfree.gameserver.network.loginserver.legacy.packets.sendable;

import java.util.LinkedList;

import com.l2jfree.gameserver.network.loginserver.legacy.L2LegacyLoginServer;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.L2LegacyGameServerPacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author hex1r0
 */
public class ServerStatus extends L2LegacyGameServerPacket
{
	public static enum ServerStatusAttributes
	{
		NONE,
		SERVER_LIST_STATUS,
		SERVER_LIST_CLOCK,
		SERVER_LIST_BRACKETS,
		SERVER_LIST_MAX_PLAYERS,
		TEST_SERVER,
		SERVER_LIST_PVP,
		SERVER_LIST_UNK,
		SERVER_LIST_HIDE_NAME,
		SERVER_AGE_LIMITATION;
		
		private static final ServerStatusAttributes[] VALUES = ServerStatusAttributes.values();
		
		public static ServerStatusAttributes valueOf(int index)
		{
			if (index < 0 || VALUES.length <= index)
				return NONE;
			
			return VALUES[index];
		}
	}
	
	private static final class Attribute
	{
		public final int _id;
		public final int _value;
		
		private Attribute(ServerStatusAttributes type, int value)
		{
			_id = type.ordinal();
			_value = value;
		}
	}
	
	public static final int OPCODE = 0x06;
	
	private final LinkedList<Attribute> _attributes = new LinkedList<ServerStatus.Attribute>();
	
	@Override
	protected int getOpcode()
	{
		return OPCODE;
	}
	
	@Override
	protected void writeImpl(L2LegacyLoginServer client, MMOBuffer buf) throws RuntimeException
	{
		buf.writeD(_attributes.size());
		for (Attribute att : _attributes)
		{
			buf.writeD(att._id);
			buf.writeD(att._value);
		}
	}
	
	public void addAttribute(ServerStatusAttributes type, int value)
	{
		_attributes.add(new Attribute(type, value));
	}
	
	public void addAttribute(ServerStatusAttributes type, boolean value)
	{
		addAttribute(type, value ? 0x01 : 0x00);
	}
	
}
