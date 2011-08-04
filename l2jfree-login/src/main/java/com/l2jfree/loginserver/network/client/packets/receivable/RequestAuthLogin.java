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
import com.l2jfree.loginserver.network.client.L2LoginClient;
import com.l2jfree.loginserver.network.client.L2LoginClientState;
import com.l2jfree.loginserver.network.client.L2NoServiceReason;
import com.l2jfree.loginserver.network.client.packets.L2ClientPacket;
import com.l2jfree.loginserver.network.client.packets.sendable.LoginFailure;
import com.l2jfree.loginserver.network.client.packets.sendable.LoginSuccess;
import com.l2jfree.loginserver.network.client.packets.sendable.ServerList;
import com.l2jfree.loginserver.network.legacy.L2GameServer;
import com.l2jfree.loginserver.network.legacy.L2LegacyConnections;
import com.l2jfree.loginserver.network.legacy.packets.sendable.KickPlayer;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;
import com.l2jfree.sql.L2Database;
import com.l2jfree.util.HexUtil;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author savormix
 *
 */
public final class RequestAuthLogin extends L2ClientPacket
{
	/** Packet's identifier */
	public static final int OPCODE = 0x00;
	
	private static final L2Logger _log = L2Logger.getLogger(RequestAuthLogin.class);
	
	private byte[] _enciphered;
	
	@Override
	protected int getMinimumLength()
	{
		return 128;
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		_enciphered = buf.readB(new byte[getMinimumLength()]);
		buf.skipAll();
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		L2LoginClient llc = getClient();
		byte[] deciphered;
		try
		{
			Cipher rsa = Cipher.getInstance("RSA/ECB/nopadding");
			rsa.init(Cipher.DECRYPT_MODE, llc.getPrivateKey());
			deciphered = rsa.doFinal(_enciphered, 0, getMinimumLength());
		}
		catch (GeneralSecurityException e)
		{
			_log.error("Failed to decipher account credentials!", e);
			llc.close(new LoginFailure(L2NoServiceReason.THERE_IS_A_SYSTEM_ERROR));
			return;
		}
		
		String user = null;
		String password = null;
		try
		{
			user = new String(deciphered, 0x5E, 14, "US-ASCII").trim().toLowerCase();
			password = new String(deciphered, 0x6C, 16, "US-ASCII").trim();
			
			MessageDigest sha = MessageDigest.getInstance("SHA");
			byte[] pass = sha.digest(password.getBytes("US-ASCII"));
			password = HexUtil.bytesToHexString(pass);
		}
		catch (NoSuchAlgorithmException e)
		{
			_log.fatal("SHA1 is not available!", e);
			llc.close(new LoginFailure(L2NoServiceReason.SYSTEM_ERROR));
			Shutdown.exit(TerminationStatus.ENVIRONMENT_MISSING_COMPONENT_OR_SERVICE);
			return;
		}
		catch (UnsupportedEncodingException e)
		{
			_log.fatal("ASCII is not available!", e);
			llc.close(new LoginFailure(L2NoServiceReason.SYSTEM_ERROR));
			Shutdown.exit(TerminationStatus.ENVIRONMENT_MISSING_COMPONENT_OR_SERVICE);
			return;
		}
		
		Connection con = null;
		try
		{
			int ban = -1;
			
			con = L2Database.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT password, superUser, birthDate, banReason, lastServerId FROM account WHERE username LIKE ?");
			ps.setString(1, user);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				if (password.equals(rs.getString("password")))
				{
					ban = rs.getInt("banReason");
					if (ban == 0)
					{
						boolean offline = true;
						for (L2GameServer lgs : L2LegacyConnections.getInstance().getAuthorized())
						{
							if (lgs.getOnlineAccounts().contains(user))
							{
								offline = false;
								lgs.sendPacket(new KickPlayer(user));
								llc.close(new LoginFailure(L2NoServiceReason.ALREADY_IN_USE));
								break;
							}
						}
						
						if (offline)
						{
							L2Account la = new L2Account(user, rs.getBoolean("superUser"),
									rs.getDate("birthDate"), rs.getInt("lastServerId"));
							llc.setAccount(la);
							
							if (ServiceConfig.SVC_SHOW_EULA)
							{
								llc.setState(L2LoginClientState.LOGGED_IN);
								llc.sendPacket(new LoginSuccess(llc));
							}
							else
							{
								llc.setState(L2LoginClientState.VIEWING_LIST);
								llc.sendPacket(new ServerList());
							}
						}
					}
					else // suspended
						llc.close(new LoginFailure(L2BanReason.getById(ban)));
				}
				else // wrong password
					llc.close(new LoginFailure(L2NoServiceReason.PASSWORD_INCORRECT));
			}
			else // no such user
				llc.close(new LoginFailure(L2NoServiceReason.PASSWORD_INCORRECT));
			rs.close();
			ps.close();
			
			if (ban == 0)
			{
				ps = con.prepareStatement("UPDATE account SET lastLogin = ? WHERE username = ?");
				ps.setLong(1, System.currentTimeMillis());
				ps.setString(2, user);
				ps.executeUpdate();
				ps.close();
				try
				{
					ps = con.prepareStatement("INSERT INTO logins (username, ipv4, date_) VALUES (?, ?, ?)");
					ps.setString(1, user);
					ps.setString(2, llc.getHostAddress());
					ps.setDate(3, new Date(System.currentTimeMillis()));
					ps.executeUpdate();
					ps.close();
				}
				catch (SQLException e)
				{
					// one entry per IP per day by default
				}
			}
		}
		catch (SQLException e)
		{
			_log.error("Could not validate login credentials!", e);
			llc.close(new LoginFailure(L2NoServiceReason.THERE_IS_A_SYSTEM_ERROR));
		}
		finally
		{
			L2Database.close(con);
		}
	}
}
