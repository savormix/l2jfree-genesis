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
import com.l2jfree.loginserver.network.client.L2NoServiceReason;
import com.l2jfree.loginserver.network.client.packets.L2ClientPacket;
import com.l2jfree.loginserver.network.client.packets.sendable.PlayFailure;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author savormix
 *
 */
public final class RequestServerLogin extends L2ClientPacket
{
	private static final L2Logger _log = L2Logger.getLogger(RequestServerLogin.class);
	/** Packet's identifier */
	public static final int OPCODE = 0x02;
	
	private long _sessionKey;
	private int _serverId;
	
	/* (non-Javadoc)
	 * @see com.l2jfree.network.mmocore.ReceivablePacket#getMinimumLength()
	 */
	@Override
	protected int getMinimumLength()
	{
		return 9;
	}
	
	/* (non-Javadoc)
	 * @see com.l2jfree.network.mmocore.ReceivablePacket#read(com.l2jfree.network.mmocore.MMOBuffer)
	 */
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException,
			RuntimeException
	{
		_sessionKey = buf.readQ();
		_serverId = buf.readC();
		buf.skipAll();
	}
	
	/* (non-Javadoc)
	 * @see com.l2jfree.network.mmocore.ReceivablePacket#runImpl()
	 */
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		_log.info("Logging into server " + _serverId);
		L2LoginClient llc = getClient();
		if (llc.getActiveSessionKey() == null || llc.getActiveSessionKey() == _sessionKey)
			llc.close(new PlayFailure(L2NoServiceReason.INCORRECT_COUPON_FOR_SERVER));
		else
			llc.close(new PlayFailure(L2NoServiceReason.ACCESS_FAILED_TRY_AGAIN));
	}
}
