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
package com.l2jfree.loginserver.network.legacy.packets.receivable;

import java.nio.BufferUnderflowException;

import com.l2jfree.loginserver.network.legacy.packets.L2GameServerPacket;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix
 *
 */
public final class PlayerLogout extends L2GameServerPacket
{
	/** Packet's identifier */
	public static final int OPCODE = 0x03;
	
	private String _account;
	
	@Override
	protected int getMinimumLength()
	{
		return 1;
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException,
			RuntimeException
	{
		_account = buf.readS();
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		getClient().getOnlineAccounts().remove(_account);
	}
}
