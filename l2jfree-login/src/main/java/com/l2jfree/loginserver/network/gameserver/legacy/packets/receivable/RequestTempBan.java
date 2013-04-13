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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.l2jfree.loginserver.network.client.L2NoServiceReason;
import com.l2jfree.loginserver.network.gameserver.legacy.packets.L2LegacyGameServerPacket;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;
import com.l2jfree.sql.L2Database;

/**
 * @author savormix
 */
@SuppressWarnings("unused")
public class RequestTempBan extends L2LegacyGameServerPacket
{
	/** Packet's identifier */
	public static final int OPCODE = 0x0a;
	
	private static final DateFormat EXP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	
	private String _account;
	private String _ip;
	private long _expiry;
	private String _reason;
	
	@Override
	protected int getMinimumLength()
	{
		return READ_S + READ_S + READ_Q + READ_C;
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		_account = buf.readS();
		_ip = buf.readS();
		_expiry = buf.readQ();
		if (buf.readC() != 0)
			_reason = buf.readS();
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// temporary ban
		Connection con = null;
		try
		{
			con = L2Database.getConnection();
			PreparedStatement ps =
					con.prepareStatement("UPDATE account SET banReason = ?, banExpiry = ? WHERE username LIKE ?");
			ps.setInt(1, -L2NoServiceReason.ACCESS_FAILED_TRY_AGAIN.getId());
			ps.setLong(2, _expiry);
			ps.setString(3, _account);
			ps.executeUpdate();
			ps.close();
			
			_log.info("Banned " + _account + " until " + EXP_FORMAT.format(new Date(_expiry)));
		}
		catch (SQLException e)
		{
			_log.error("Could not temporarily ban account!", e);
		}
		finally
		{
			L2Database.close(con);
		}
		
		// TODO: ban IP as well?
	}
}
