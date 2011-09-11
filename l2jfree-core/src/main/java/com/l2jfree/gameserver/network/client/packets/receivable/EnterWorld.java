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
package com.l2jfree.gameserver.network.client.packets.receivable;

import java.nio.BufferUnderflowException;

import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.gameserver.network.client.packets.sendable.UserInfo.MyPlayerInfo;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author hex1r0
 */
public class EnterWorld extends L2ClientPacket
{
	public static final int OPCODE = 0x11;
	
	@Override
	protected int getMinimumLength()
	{
		return 0;
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		// TODO
		buf.skipAll();
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO
		
		sendPacket(new MyPlayerInfo());
		
		// Welcome to Lineage 2, temp solution ^)
		sendPacket(new L2ServerPacket() {
			@Override
			protected void writeImpl(L2Client client, MMOBuffer buf) throws RuntimeException
			{
				buf.writeD(34);
				buf.writeD(0);
			}
			
			@Override
			protected int getOpcode()
			{
				return 0x62;
			}
		});
	}
}
