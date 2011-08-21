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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.l2jfree.loginserver.network.legacy.packets.L2GameServerPacket;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;
import com.l2jfree.sql.L2Database;

/**
 * @author savormix
 */
public final class ChangeAccessLevel extends L2GameServerPacket
{
	/** Packet's identifier */
	public static final int OPCODE = 0x04;
	
	private String _account;
	private int _level;
	
	@Override
	protected int getMinimumLength()
	{
		return READ_D + READ_S;
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		_level = buf.readD();
		_account = buf.readS();
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		Connection con = null;
		try
		{
			con = L2Database.getConnection();
			PreparedStatement ps = con
					.prepareStatement("UPDATE account SET banReason = ?, superUser = ? WHERE username LIKE ?");
			if (_level < 0)
				ps.setInt(1, -_level);
			else
				ps.setNull(1, Types.INTEGER);
			ps.setBoolean(2, _level > 0);
			ps.setString(3, _account);
			ps.executeUpdate();
			ps.close();
		}
		catch (SQLException e)
		{
			_log.error("Could not change account access level!", e);
		}
		finally
		{
			L2Database.close(con);
		}
	}
}
