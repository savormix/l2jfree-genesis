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

import com.l2jfree.loginserver.LoginServer;
import com.l2jfree.loginserver.network.client.L2LoginClient;
import com.l2jfree.loginserver.network.client.L2LoginClientState;
import com.l2jfree.loginserver.network.client.L2NoServiceReason;
import com.l2jfree.loginserver.network.client.packets.L2ClientPacket;
import com.l2jfree.loginserver.network.client.packets.sendable.GameGuardSuccess;
import com.l2jfree.loginserver.network.client.packets.sendable.Init;
import com.l2jfree.loginserver.network.client.packets.sendable.LoginFailure;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;
import com.l2jfree.util.logging.L2Logger;

/**
 * Client sends this packet automatically in response to
 * {@link Init} packet. Server replies with <TT>Unknown</TT>
 * or {@link GameGuardSuccess} based on this packet's data.
 * @author savormix
 */
public final class AuthGameGuard extends L2ClientPacket
{
	/** Packet's identifier */
	public static final int OPCODE = 0x07;
	
	private static final L2Logger _log = L2Logger.getLogger(AuthGameGuard.class);
	
	private int _sessionId;
	private long _unk1;
	private long _unk2;
	
	/* (non-Javadoc)
	 * @see com.l2jfree.network.mmocore.ReceivablePacket#getMinimumLength()
	 */
	@Override
	protected int getMinimumLength()
	{
		return 20;
	}
	
	/* (non-Javadoc)
	 * @see com.l2jfree.network.mmocore.ReceivablePacket#read(com.l2jfree.network.mmocore.MMOBuffer)
	 */
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		_sessionId = buf.readD();
		_unk1 = buf.readQ();
		_unk2 = buf.readQ();
		// the rest doesn't make much sense
		buf.skipAll();
	}
	
	/* (non-Javadoc)
	 * @see com.l2jfree.network.mmocore.ReceivablePacket#runImpl()
	 */
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		L2LoginClient llc = getClient();
		if (_unk1 != _unk2)
			_log.warn("Suspicious client activity! " + llc);
		
		if (!LoginServer.SVC_CHECK_GAMEGUARD || llc.getSessionId() == _sessionId)
		{
			llc.setState(L2LoginClientState.GAMEGUARD_PASSED);
			llc.sendPacket(new GameGuardSuccess(_sessionId));
		}
		else
			llc.close(new LoginFailure(L2NoServiceReason.ACCESS_FAILED_TRY_AGAIN));
	}
}
