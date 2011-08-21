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

import com.l2jfree.loginserver.network.client.L2LoginClient;
import com.l2jfree.loginserver.network.client.L2LoginClientState;
import com.l2jfree.loginserver.network.client.L2NoServiceReason;
import com.l2jfree.loginserver.network.client.L2ClientSecurity.SessionKey;
import com.l2jfree.loginserver.network.client.packets.L2ClientPacket;
import com.l2jfree.loginserver.network.client.packets.sendable.LoginFailure;
import com.l2jfree.loginserver.network.client.packets.sendable.ServerList;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix
 */
public final class RequestServerList extends L2ClientPacket
{
	/** Packet's identifier */
	public static final int OPCODE = 0x05;
	
	private long _sessionKey;
	
	@Override
	protected int getMinimumLength()
	{
		return READ_Q;
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		_sessionKey = buf.readQ();
		buf.skipAll();
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		L2LoginClient llc = getClient();
		SessionKey sk = llc.getSessionKey();
		if (sk != null && sk.getActiveKey() == _sessionKey)
		{
			llc.setState(L2LoginClientState.VIEWING_LIST);
			llc.sendPacket(new ServerList());
		}
		else
			llc.close(new LoginFailure(L2NoServiceReason.ACCESS_FAILED_TRY_AGAIN));
	}
}
