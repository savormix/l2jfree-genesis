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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.l2jfree.loginserver.network.client.L2Account;
import com.l2jfree.loginserver.network.client.L2LoginClient;
import com.l2jfree.loginserver.network.client.L2NoServiceReason;
import com.l2jfree.loginserver.network.client.packets.L2ClientPacket;
import com.l2jfree.loginserver.network.client.packets.sendable.PlayFailure;
import com.l2jfree.loginserver.network.client.packets.sendable.PlaySuccess;
import com.l2jfree.loginserver.network.legacy.L2GameServer;
import com.l2jfree.loginserver.network.legacy.L2LegacyConnections;
import com.l2jfree.loginserver.network.legacy.status.L2LegacyStatus;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;
import com.l2jfree.sql.L2Database;

/**
 * @author savormix
 */
public final class RequestServerLogin extends L2ClientPacket
{
	/** Packet's identifier */
	public static final int OPCODE = 0x02;
	
	private long _sessionKey;
	private int _serverId;
	
	@Override
	protected int getMinimumLength()
	{
		return READ_Q + READ_C;
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		_sessionKey = buf.readQ();
		_serverId = buf.readC();
		buf.skipAll();
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		L2LoginClient llc = getClient();
		if (llc.getSessionKey() != null && llc.getSessionKey().getActiveKey() != _sessionKey)
		{
			llc.close(new PlayFailure(L2NoServiceReason.ACCESS_FAILED_TRY_AGAIN));
			return;
		}
		
		L2GameServer lgs = L2LegacyConnections.getInstance().getById(_serverId);
		if (lgs == null || lgs.getStatus() == L2LegacyStatus.DOWN) // server down
		{
			llc.close(new PlayFailure(L2NoServiceReason.MAINTENANCE_UNDERGOING));
			return;
		}
		
		L2Account acc = llc.getAccount();
		if (acc == null) // should never happen
			llc.close(new PlayFailure(L2NoServiceReason.THERE_IS_A_SYSTEM_ERROR));
		else if (!llc.getAccount().isSuperUser()) // normal account
		{
			if (lgs.getStatus() == L2LegacyStatus.GM_ONLY) // restricted access
				llc.close(new PlayFailure(L2NoServiceReason.MAINTENANCE_UNDERGOING));
			else if (lgs.getOnlineAccounts().size() >= lgs.getMaxPlayers()) // server full
				llc.close(new PlayFailure(L2NoServiceReason.TOO_HIGH_TRAFFIC));
		}
		else
		{
			Connection con = null;
			try
			{
				con = L2Database.getConnection();
				PreparedStatement ps = con
						.prepareStatement("UPDATE account SET lastServerId = ? WHERE username LIKE ?");
				ps.setInt(1, _serverId);
				ps.setString(2, acc.getAccount());
				ps.executeUpdate();
				ps.close();
			}
			catch (SQLException e)
			{
				_log.error("Could not modify account data!", e);
			}
			finally
			{
				L2Database.close(con);
			}
			llc.sendPacket(new PlaySuccess(llc));
		}
	}
}
