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
package com.l2jfree.gameserver.network.client.packets.sendable.outgame;

import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.sendable.StaticPacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author hex1r0
 */
public class CharacterDeleteFail extends StaticPacket
{
	public static final CharacterDeleteFail REASON_DELETION_FAILED = new CharacterDeleteFail(0x01);
	public static final CharacterDeleteFail REASON_YOU_MAY_NOT_DELETE_CLAN_MEMBER = new CharacterDeleteFail(0x02);
	public static final CharacterDeleteFail REASON_CLAN_LEADERS_MAY_NOT_BE_DELETED = new CharacterDeleteFail(0x03);
	
	private final int _error;
	
	private CharacterDeleteFail(int error)
	{
		_error = error;
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x1e;
	}
	
	@Override
	protected void writeImpl(L2Client client, MMOBuffer buf) throws RuntimeException
	{
		buf.writeD(_error);
	}
}
