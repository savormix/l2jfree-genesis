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
package com.l2jfree.loginserver.network.client.packets.receivable;

import java.nio.BufferUnderflowException;

import com.l2jfree.loginserver.config.ServiceConfig;
import com.l2jfree.loginserver.network.client.L2Client;
import com.l2jfree.loginserver.network.client.L2ClientState;
import com.l2jfree.loginserver.network.client.L2NoServiceReason;
import com.l2jfree.loginserver.network.client.packets.L2ClientPacket;
import com.l2jfree.loginserver.network.client.packets.sendable.GameGuardSuccess;
import com.l2jfree.loginserver.network.client.packets.sendable.Init;
import com.l2jfree.loginserver.network.client.packets.sendable.LoginFailure;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * Client sends this packet automatically in response to {@link Init} packet. Server replies with
 * <TT>Unknown</TT> or {@link GameGuardSuccess} based on this packet's data.
 * 
 * @author savormix
 */
public final class AuthGameGuard extends L2ClientPacket
{
	/** Packet's identifier */
	public static final int OPCODE = 0x07;
	
	private int _sessionId;
	
	@Override
	protected int getMinimumLength()
	{
		return READ_D + READ_Q + READ_Q; // session ID and two longs
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		_sessionId = buf.readD();
		buf.readQ();
		buf.readQ();
		buf.skipAll();
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		final L2Client client = getClient();
		if (!ServiceConfig.CHECK_GAMEGUARD || client.getSessionId() == _sessionId)
		{
			client.setState(L2ClientState.GAMEGUARD_PASSED);
			client.sendPacket(new GameGuardSuccess(_sessionId));
		}
		else
			client.close(new LoginFailure(L2NoServiceReason.ACCESS_FAILED_TRY_AGAIN));
	}
}
