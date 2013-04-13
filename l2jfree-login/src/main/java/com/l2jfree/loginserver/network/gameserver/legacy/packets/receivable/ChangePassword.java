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
package com.l2jfree.loginserver.network.gameserver.legacy.packets.receivable;

import java.nio.BufferUnderflowException;

import com.l2jfree.loginserver.network.gameserver.legacy.packets.L2LegacyGameServerPacket;
import com.l2jfree.loginserver.network.gameserver.legacy.packets.sendable.ChangePasswordResponse;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix
 */
@SuppressWarnings("unused")
public class ChangePassword extends L2LegacyGameServerPacket
{
	/** Packet's identifier */
	public static final int OPCODE = 0x0b;
	
	private String _account;
	private String _character;
	private String _current;
	private String _next;
	
	@Override
	protected int getMinimumLength()
	{
		return READ_S + READ_S + READ_S + READ_S;
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		_account = buf.readS();
		_character = buf.readS();
		_current = buf.readS();
		_next = buf.readS();
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		sendPacket(new ChangePasswordResponse(_character, "This feature is not supported."));
		
		_log.info("Character " + _character + " (" + _account + ") attempted password change.");
	}
}
