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

import java.io.UnsupportedEncodingException;
import java.nio.BufferUnderflowException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.Cipher;

import com.l2jfree.Shutdown;
import com.l2jfree.TerminationStatus;
import com.l2jfree.loginserver.config.ServiceConfig;
import com.l2jfree.loginserver.network.client.L2Account;
import com.l2jfree.loginserver.network.client.L2BanReason;
import com.l2jfree.loginserver.network.client.L2Client;
import com.l2jfree.loginserver.network.client.L2ClientState;
import com.l2jfree.loginserver.network.client.L2NoServiceReason;
import com.l2jfree.loginserver.network.client.packets.L2ClientPacket;
import com.l2jfree.loginserver.network.client.packets.sendable.LoginFailure;
import com.l2jfree.loginserver.network.client.packets.sendable.LoginSuccess;
import com.l2jfree.loginserver.network.client.packets.sendable.ServerList;
import com.l2jfree.loginserver.network.gameserver.legacy.L2LegacyGameServer;
import com.l2jfree.loginserver.network.gameserver.legacy.L2LegacyGameServerController;
import com.l2jfree.loginserver.network.gameserver.legacy.packets.sendable.KickPlayer;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;
import com.l2jfree.sql.L2Database;
import com.l2jfree.util.HexUtil;

/**
 * @author savormix
 */
public final class RequestAuthLogin extends L2ClientPacket
{
	/** Packet's identifier */
	public static final int OPCODE = 0x00;
	
	private static final int BLOCK_SIZE = 128;
	
	private byte[] _userBlock;
	private byte[] _passBlock;
	
	@Override
	protected int getMinimumLength()
	{
		return BLOCK_SIZE;
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		_userBlock = buf.readB(new byte[BLOCK_SIZE]);
		if (buf.getAvailableBytes() >= BLOCK_SIZE)
			_passBlock = buf.readB(new byte[BLOCK_SIZE]);
		buf.skipAll();
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		final L2Client client = getClient();
		try
		{
			Cipher rsa = Cipher.getInstance("RSA/ECB/nopadding");
			rsa.init(Cipher.DECRYPT_MODE, client.getPrivateKey());
			_userBlock = rsa.doFinal(_userBlock, 0, BLOCK_SIZE);
			if (_passBlock != null)
				_passBlock = rsa.doFinal(_passBlock, 0, BLOCK_SIZE);
		}
		catch (GeneralSecurityException e)
		{
			_log.error("Failed to decipher account credentials!", e);
			client.close(new LoginFailure(L2NoServiceReason.THERE_IS_A_SYSTEM_ERROR));
			return;
		}
		
		String user = null;
		String password = null;
		try
		{
			if (_passBlock != null)
			{
				user = new String(_userBlock, 0x4E, 50, "US-ASCII").trim().toLowerCase();
				password = new String(_passBlock, 0x5C, 16, "US-ASCII").trim();
			}
			else
			{
				user = new String(_userBlock, 0x5E, 14, "US-ASCII").trim().toLowerCase();
				password = new String(_userBlock, 0x6C, 16, "US-ASCII").trim();
			}
			
			MessageDigest sha = MessageDigest.getInstance("SHA");
			byte[] pass = sha.digest(password.getBytes("US-ASCII"));
			password = HexUtil.bytesToHexString(pass);
		}
		catch (NoSuchAlgorithmException e)
		{
			_log.fatal("SHA1 is not available!", e);
			client.close(new LoginFailure(L2NoServiceReason.SYSTEM_ERROR));
			Shutdown.exit(TerminationStatus.ENVIRONMENT_MISSING_COMPONENT_OR_SERVICE);
			return;
		}
		catch (UnsupportedEncodingException e)
		{
			_log.fatal("ASCII is not available!", e);
			client.close(new LoginFailure(L2NoServiceReason.SYSTEM_ERROR));
			Shutdown.exit(TerminationStatus.ENVIRONMENT_MISSING_COMPONENT_OR_SERVICE);
			return;
		}
		
		Connection con = null;
		try
		{
			int ban = -1;
			
			con = L2Database.getConnection();
			PreparedStatement ps =
					con.prepareStatement("SELECT password, superUser, birthDate, banReason, lastServerId FROM account WHERE username LIKE ?");
			ps.setString(1, user);
			ResultSet rs = ps.executeQuery();
			
			if (!rs.next())
			{
				if (ServiceConfig.AUTO_CREATE_ACCOUNTS)
				{
					boolean isSuperUser = true; // TODO
					Date birthDate = null;
					PreparedStatement ps2 =
							con.prepareStatement("INSERT INTO account(username, password, superUser, birthDate, banReason, lastServerId) VALUES (?, ?, ?, ?, ?, ?)");
					ps2.setString(1, user);
					ps2.setString(2, password);
					ps2.setBoolean(3, true);
					ps2.setDate(4, birthDate);
					ps2.setInt(5, 0);
					ps2.setInt(6, 0);
					if (ps2.executeUpdate() > 0)
					{
						_log.info("New account " + user + " created");
						requestLogin(con, user, isSuperUser, birthDate, 0);
					}
					ps2.close();
				}
				else
					// no such user
					client.close(new LoginFailure(L2NoServiceReason.PASSWORD_INCORRECT));
			}
			else
			{
				if (password.equals(rs.getString("password")))
				{
					ban = rs.getInt("banReason");
					if (ban == 0)
					{
						boolean offline = true;
						for (L2LegacyGameServer lgs : L2LegacyGameServerController.getInstance().getAuthorized())
						{
							if (lgs.getOnlineAccounts().contains(user))
							{
								offline = false;
								lgs.sendPacket(new KickPlayer(user));
								client.close(new LoginFailure(L2NoServiceReason.ALREADY_IN_USE));
								break;
							}
						}
						
						if (offline)
							requestLogin(con, user, rs.getBoolean("superUser"), rs.getDate("birthDate"),
									rs.getInt("lastServerId"));
					}
					else
						// suspended
						client.close(new LoginFailure(L2BanReason.getById(ban)));
				}
				else
					// wrong password
					client.close(new LoginFailure(L2NoServiceReason.PASSWORD_INCORRECT));
			}
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			_log.error("Could not validate login credentials!", e);
			client.close(new LoginFailure(L2NoServiceReason.THERE_IS_A_SYSTEM_ERROR));
		}
		finally
		{
			L2Database.close(con);
		}
	}
	
	private void requestLogin(Connection con, String user, boolean isSuperUser, Date birthDate, int lastServerId)
			throws SQLException
	{
		L2Account la = new L2Account(user, isSuperUser, birthDate, lastServerId);
		getClient().setAccount(la);
		
		if (ServiceConfig.SHOW_EULA)
		{
			getClient().setState(L2ClientState.LOGGED_IN);
			getClient().sendPacket(new LoginSuccess(getClient()));
		}
		else
		{
			getClient().setState(L2ClientState.VIEWING_LIST);
			getClient().sendPacket(new ServerList());
		}
		
		PreparedStatement ps = con.prepareStatement("UPDATE account SET lastLogin = ? WHERE username = ?");
		ps.setLong(1, System.currentTimeMillis());
		ps.setString(2, user);
		ps.executeUpdate();
		ps.close();
		try
		{
			ps = con.prepareStatement("INSERT INTO logins (username, ipv4, date_) VALUES (?, ?, ?)");
			ps.setString(1, user);
			ps.setString(2, getClient().getHostAddress());
			ps.setDate(3, new Date(System.currentTimeMillis()));
			ps.executeUpdate();
			ps.close();
		}
		catch (SQLException e)
		{
			// one entry per IP per day by default
		}
	}
	
	@Override
	protected boolean blockReadingUntilExecutionIsFinished()
	{
		return true;
	}
}
