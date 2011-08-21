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
package com.l2jfree.loginserver.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.io.IOUtils;

import com.l2jfree.Shutdown;
import com.l2jfree.TerminationStatus;
import com.l2jfree.Util;
import com.l2jfree.loginserver.Config;
import com.l2jfree.loginserver.network.client.L2BanReason;
import com.l2jfree.sql.L2Database;
import com.l2jfree.util.HexUtil;

/**
 * @author savormix
 */
public final class L2AccountManager extends Config
{
	private ManagerState _state;
	
	private String _user;
	private String _pass;
	private boolean _super;
	private Date _dob;
	private L2BanReason _ban;
	
	private L2AccountManager()
	{
		_state = ManagerState.INITIAL_CHOICE;
	}
	
	private ManagerState getState()
	{
		return _state;
	}
	
	private void setState(ManagerState state)
	{
		_state = state;
	}
	
	private String getUser()
	{
		return _user;
	}
	
	private void setUser(String user)
	{
		_user = user;
	}
	
	private String getPass()
	{
		return _pass;
	}
	
	private void setPass(String pass)
	{
		_pass = pass;
	}
	
	private boolean isSuper()
	{
		return _super;
	}
	
	private void setSuper(boolean super1)
	{
		_super = super1;
	}
	
	private Date getDob()
	{
		return _dob;
	}
	
	private void setDob(Date dob)
	{
		_dob = dob;
	}
	
	private L2BanReason getBan()
	{
		return _ban;
	}
	
	private void setBan(L2BanReason ban)
	{
		_ban = ban;
	}
	
	/**
	 * Launches the interactive account manager.
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args)
	{
		// TODO rework this crap
		Util.printSection("Account Management");
		
		_log.info("Please choose:");
		//_log.info("list - list registered accounts");
		_log.info("reg - register a new account");
		_log.info("rem - remove a registered account");
		_log.info("prom - promote a registered account");
		_log.info("dem - demote a registered account");
		_log.info("ban - ban a registered account");
		_log.info("unban - unban a registered account");
		_log.info("quit - exit this application");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		L2AccountManager acm = new L2AccountManager();
		
		String line;
		try
		{
			while ((line = br.readLine()) != null)
			{
				line = line.trim();
				Connection con = null;
				switch (acm.getState())
				{
					case USER_NAME:
						line = line.toLowerCase();
						try
						{
							con = L2Database.getConnection();
							PreparedStatement ps = con
									.prepareStatement("SELECT superuser FROM account WHERE username LIKE ?");
							ps.setString(1, line);
							ResultSet rs = ps.executeQuery();
							if (!rs.next())
							{
								acm.setUser(line);
								
								_log.info("Desired password:");
								acm.setState(ManagerState.PASSWORD);
							}
							else
							{
								_log.info("User name already in use.");
								acm.setState(ManagerState.INITIAL_CHOICE);
							}
							rs.close();
							ps.close();
						}
						catch (SQLException e)
						{
							_log.error("Could not access database!", e);
							acm.setState(ManagerState.INITIAL_CHOICE);
						}
						finally
						{
							L2Database.close(con);
						}
						break;
					case PASSWORD:
						try
						{
							MessageDigest sha = MessageDigest.getInstance("SHA");
							byte[] pass = sha.digest(line.getBytes("US-ASCII"));
							acm.setPass(HexUtil.bytesToHexString(pass));
						}
						catch (NoSuchAlgorithmException e)
						{
							_log.fatal("SHA1 is not available!", e);
							Shutdown.exit(TerminationStatus.ENVIRONMENT_MISSING_COMPONENT_OR_SERVICE);
						}
						catch (UnsupportedEncodingException e)
						{
							_log.fatal("ASCII is not available!", e);
							Shutdown.exit(TerminationStatus.ENVIRONMENT_MISSING_COMPONENT_OR_SERVICE);
						}
						_log.info("Super user: [y/n]");
						acm.setState(ManagerState.SUPERUSER);
						break;
					case SUPERUSER:
						try
						{
							if (line.length() != 1)
								throw new IllegalArgumentException("One char required.");
							else if (line.charAt(0) == 'y')
								acm.setSuper(true);
							else if (line.charAt(0) == 'n')
								acm.setSuper(false);
							else
								throw new IllegalArgumentException("Invalid choice.");
							
							_log.info("Date of birth: [yyyy-mm-dd]");
							acm.setState(ManagerState.DOB);
						}
						catch (IllegalArgumentException e)
						{
							_log.info("[y/n]?");
						}
						break;
					case DOB:
						try
						{
							Date d = Date.valueOf(line);
							if (d.after(new Date(System.currentTimeMillis())))
								throw new IllegalArgumentException("Future date specified.");
							acm.setDob(d);
							
							_log.info("Ban reason ID or nothing:");
							acm.setState(ManagerState.SUSPENDED);
						}
						catch (IllegalArgumentException e)
						{
							_log.info("[yyyy-mm-dd] in the past:");
						}
						break;
					case SUSPENDED:
						try
						{
							if (line.length() > 0)
							{
								int id = Integer.parseInt(line);
								acm.setBan(L2BanReason.getById(id));
							}
							else
								acm.setBan(null);
							
							try
							{
								con = L2Database.getConnection();
								PreparedStatement ps = con
										.prepareStatement("INSERT INTO account (username, password, superuser, birthDate, banReason) VALUES (?, ?, ?, ?, ?)");
								ps.setString(1, acm.getUser());
								ps.setString(2, acm.getPass());
								ps.setBoolean(3, acm.isSuper());
								ps.setDate(4, acm.getDob());
								L2BanReason lbr = acm.getBan();
								if (lbr == null)
									ps.setNull(5, Types.INTEGER);
								else
									ps.setInt(5, lbr.getId());
								ps.executeUpdate();
								_log.info("Account " + acm.getUser() + " has been registered.");
								ps.close();
							}
							catch (SQLException e)
							{
								_log.error("Could not register an account!", e);
							}
							finally
							{
								L2Database.close(con);
							}
							acm.setState(ManagerState.INITIAL_CHOICE);
						}
						catch (NumberFormatException e)
						{
							_log.info("Ban reason ID or nothing:");
						}
						break;
					case REMOVE:
						acm.setUser(line.toLowerCase());
						try
						{
							con = L2Database.getConnection();
							PreparedStatement ps = con.prepareStatement("DELETE FROM account WHERE username LIKE ?");
							ps.setString(1, acm.getUser());
							int cnt = ps.executeUpdate();
							if (cnt > 0)
								_log.info("Account " + acm.getUser() + " has been removed.");
							else
								_log.info("Account " + acm.getUser() + " does not exist!");
							ps.close();
						}
						catch (SQLException e)
						{
							_log.error("Could not remove an account!", e);
						}
						finally
						{
							L2Database.close(con);
						}
						acm.setState(ManagerState.INITIAL_CHOICE);
						break;
					case PROMOTE:
						acm.setUser(line.toLowerCase());
						try
						{
							con = L2Database.getConnection();
							PreparedStatement ps = con
									.prepareStatement("UPDATE account SET superuser = ? WHERE username LIKE ?");
							ps.setBoolean(1, true);
							ps.setString(2, acm.getUser());
							int cnt = ps.executeUpdate();
							if (cnt > 0)
								_log.info("Account " + acm.getUser() + " has been promoted.");
							else
								_log.info("Account " + acm.getUser() + " does not exist!");
							ps.close();
						}
						catch (SQLException e)
						{
							_log.error("Could not promote an account!", e);
						}
						finally
						{
							L2Database.close(con);
						}
						acm.setState(ManagerState.INITIAL_CHOICE);
						break;
					case DEMOTE:
						acm.setUser(line.toLowerCase());
						try
						{
							con = L2Database.getConnection();
							PreparedStatement ps = con
									.prepareStatement("UPDATE account SET superuser = ? WHERE username LIKE ?");
							ps.setBoolean(1, false);
							ps.setString(2, acm.getUser());
							int cnt = ps.executeUpdate();
							if (cnt > 0)
								_log.info("Account " + acm.getUser() + " has been demoted.");
							else
								_log.info("Account " + acm.getUser() + " does not exist!");
							ps.close();
						}
						catch (SQLException e)
						{
							_log.error("Could not demote an account!", e);
						}
						finally
						{
							L2Database.close(con);
						}
						acm.setState(ManagerState.INITIAL_CHOICE);
						break;
					case UNBAN:
						acm.setUser(line.toLowerCase());
						try
						{
							con = L2Database.getConnection();
							PreparedStatement ps = con
									.prepareStatement("UPDATE account SET banReason = ? WHERE username LIKE ?");
							ps.setNull(1, Types.INTEGER);
							ps.setString(2, acm.getUser());
							int cnt = ps.executeUpdate();
							if (cnt > 0)
								_log.info("Account " + acm.getUser() + " has been unbanned.");
							else
								_log.info("Account " + acm.getUser() + " does not exist!");
							ps.close();
						}
						catch (SQLException e)
						{
							_log.error("Could not demote an account!", e);
						}
						finally
						{
							L2Database.close(con);
						}
						acm.setState(ManagerState.INITIAL_CHOICE);
						break;
					case BAN:
						line = line.toLowerCase();
						try
						{
							con = L2Database.getConnection();
							PreparedStatement ps = con
									.prepareStatement("SELECT superuser FROM account WHERE username LIKE ?");
							ps.setString(1, line);
							ResultSet rs = ps.executeQuery();
							if (rs.next())
							{
								acm.setUser(line);
								
								_log.info("Ban reason ID:");
								acm.setState(ManagerState.REASON);
							}
							else
							{
								_log.info("Account does not exist.");
								acm.setState(ManagerState.INITIAL_CHOICE);
							}
							rs.close();
							ps.close();
						}
						catch (SQLException e)
						{
							_log.error("Could not access database!", e);
							acm.setState(ManagerState.INITIAL_CHOICE);
						}
						finally
						{
							L2Database.close(con);
						}
						break;
					case REASON:
						try
						{
							int ban = Integer.parseInt(line);
							con = L2Database.getConnection();
							PreparedStatement ps = con
									.prepareStatement("UPDATE account SET banReason = ? WHERE username LIKE ?");
							ps.setInt(1, ban);
							ps.setString(2, acm.getUser());
							ps.executeUpdate();
							_log.info("Account " + acm.getUser() + " has been banned.");
							ps.close();
						}
						catch (NumberFormatException e)
						{
							_log.info("Ban reason ID:");
						}
						catch (SQLException e)
						{
							_log.error("Could not ban an account!", e);
						}
						finally
						{
							L2Database.close(con);
						}
						acm.setState(ManagerState.INITIAL_CHOICE);
						break;
					default:
						line = line.toLowerCase();
						if (line.equals("reg"))
						{
							_log.info("Desired user name:");
							acm.setState(ManagerState.USER_NAME);
						}
						else if (line.equals("rem"))
						{
							_log.info("User name:");
							acm.setState(ManagerState.REMOVE);
						}
						else if (line.equals("prom"))
						{
							_log.info("User name:");
							acm.setState(ManagerState.PROMOTE);
						}
						else if (line.equals("dem"))
						{
							_log.info("User name:");
							acm.setState(ManagerState.DEMOTE);
						}
						else if (line.equals("unban"))
						{
							_log.info("User name:");
							acm.setState(ManagerState.UNBAN);
						}
						else if (line.equals("ban"))
						{
							_log.info("User name:");
							acm.setState(ManagerState.BAN);
						}
						else if (line.equals("quit"))
							Shutdown.exit(TerminationStatus.MANUAL_SHUTDOWN);
						else
							_log.info("Incorrect command.");
						break;
				}
			}
		}
		catch (IOException e)
		{
			_log.fatal("Could not process input!", e);
		}
		finally
		{
			IOUtils.closeQuietly(br);
		}
	}
	
	private enum ManagerState
	{
		INITIAL_CHOICE,
		USER_NAME,
		PASSWORD,
		SUPERUSER,
		DOB,
		SUSPENDED,
		
		OFFSET,
		COUNT,
		
		REMOVE,
		PROMOTE,
		DEMOTE,
		UNBAN,
		
		BAN,
		REASON,
	}
}
