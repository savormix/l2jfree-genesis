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

import java.util.ArrayList;
import java.util.Collection;

import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.gameserver.util.ObjectId;
import com.l2jfree.network.ClientProtocolVersion;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix (generated)
 * @author hex1r0
 */
public class StatusUpdate extends L2ServerPacket
{
	private static final class Attribute
	{
		private final byte id;
		private final int value;
		
		private Attribute(byte pId, int pValue)
		{
			id = pId;
			value = pValue;
		}
	}
	
	public static final byte LEVEL = 0x01;
	public static final byte EXP = 0x02;
	public static final byte STR = 0x03;
	public static final byte DEX = 0x04;
	public static final byte CON = 0x05;
	public static final byte INT = 0x06;
	public static final byte WIT = 0x07;
	public static final byte MEN = 0x08;
	
	public static final byte CUR_HP = 0x09;
	public static final byte MAX_HP = 0x0a;
	public static final byte CUR_MP = 0x0b;
	public static final byte MAX_MP = 0x0c;
	
	public static final byte SP = 0x0d;
	public static final byte CUR_LOAD = 0x0e;
	public static final byte MAX_LOAD = 0x0f;
	
	public static final byte P_ATK = 0x11;
	public static final byte ATK_SPD = 0x12;
	public static final byte P_DEF = 0x13;
	public static final byte EVASION = 0x14;
	public static final byte ACCURACY = 0x15;
	public static final byte CRITICAL = 0x16;
	public static final byte M_ATK = 0x17;
	public static final byte CAST_SPD = 0x18;
	public static final byte M_DEF = 0x19;
	public static final byte PVP_FLAG = 0x1a;
	public static final byte KARMA = 0x1b;
	
	public static final byte CUR_CP = 0x21;
	public static final byte MAX_CP = 0x22;
	
	private final ObjectId _objectId;
	private final Collection<Attribute> _attributes;
	
	/**
	 * Constructs this packet.
	 * 
	 * @param activeChar
	 */
	public StatusUpdate(L2Player activeChar)
	{
		_objectId = activeChar.getObjectId();
		_attributes = new ArrayList<Attribute>(4);
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x18;
	}
	
	public void addAttribute(byte id, int level)
	{
		_attributes.add(new Attribute(id, level));
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		buf.writeD(_objectId); // Actor OID
		if (client.getVersion().isNewerThanOrEqualTo(ClientProtocolVersion.GODDESS_OF_DESTRUCTION))
		{
			buf.writeD(0); // Other actor's? OID
			buf.writeD(0); // Regeneration
		}
		buf.writeD(_attributes.size()); // Update count
		for (final Attribute a : _attributes)
		{
			buf.writeD(a.id); // Attribute
			buf.writeD(a.value); // Value
		}
	}
}
