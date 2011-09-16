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
package com.l2jfree.gameserver.network.loginserver.legacy.packets.receivable;

import java.nio.BufferUnderflowException;

import com.l2jfree.gameserver.config.ReportedConfig;
import com.l2jfree.gameserver.network.loginserver.legacy.L2LegacyLoginServerState;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.L2LegacyLoginServerPacket;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.sendable.ServerStatus;
import com.l2jfree.network.legacy.ServerStatusAttributes;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author hex1r0
 */
public class AuthResponse extends L2LegacyLoginServerPacket
{
	public static final int OPCODE = 0x02;
	
	@Override
	protected int getMinimumLength()
	{
		return READ_C + READ_S;
	}
	
	private int _serverId;
	private String _serverName;
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		_serverId = buf.readC();
		_serverName = buf.readS();
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		getClient().setState(L2LegacyLoginServerState.AUTHED);
		
		_log.info("Registered on login server as " + _serverId + " : " + _serverName);
		ServerStatus serverStatus = new ServerStatus();
		serverStatus.addAttribute(ServerStatusAttributes.SERVER_LIST_STATUS, 0);
		serverStatus.addAttribute(ServerStatusAttributes.SERVER_LIST_CLOCK, false);
		serverStatus.addAttribute(ServerStatusAttributes.SERVER_LIST_BRACKETS, ReportedConfig.BRACKETS);
		serverStatus.addAttribute(ServerStatusAttributes.TEST_SERVER, false);
		sendPacket(serverStatus);
	}
}
