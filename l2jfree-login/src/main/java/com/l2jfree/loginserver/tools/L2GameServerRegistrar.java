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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import com.l2jfree.Shutdown;
import com.l2jfree.TerminationStatus;
import com.l2jfree.Util;
import com.l2jfree.loginserver.Config;
import com.l2jfree.sql.L2Database;
import com.l2jfree.util.HexUtil;
import com.l2jfree.util.Rnd;

/**
 * @author savormix
 *
 */
public final class L2GameServerRegistrar extends Config
{
	private static final int BYTES = 64;
	
	private RegistrationState _state;
	
	private int _id;
	private String _auth;
	private boolean _trusted;
	
	private L2GameServerRegistrar()
	{
		_state = RegistrationState.INITIAL_CHOICE;
	}
	
	private RegistrationState getState()
	{
		return _state;
	}
	
	private void setState(RegistrationState state)
	{
		_state = state;
	}
	
	private int getId()
	{
		return _id;
	}
	
	private void setId(int id)
	{
		_id = id;
	}
	
	private String getAuth()
	{
		return _auth;
	}
	
	private void setAuth(String auth)
	{
		_auth = auth;
	}
	
	private boolean isTrusted()
	{
		return _trusted;
	}
	
	private void setTrusted(boolean trusted)
	{
		_trusted = trusted;
	}
	
	/**
	 * Launches the interactive game server registration.
	 * @param args ignored
	 */
	public static void main(String[] args)
	{
		// TODO rework this crap
		Util.printSection("Game Server Registration");
		_log.info("Please choose:");
		_log.info("list - list registered game servers");
		_log.info("reg - register a game server");
		_log.info("rem - remove a registered game server");
		_log.info("hexid - generate a legacy hexid file");
		_log.info("quit - exit this application");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		L2GameServerRegistrar reg = new L2GameServerRegistrar();
		
		String line;
		try
		{
			RegistrationState next = RegistrationState.INITIAL_CHOICE;
			while ((line = br.readLine()) != null)
			{
				line = line.trim().toLowerCase();
				switch (reg.getState())
				{
				case GAMESERVER_ID:
					try
					{
						int id = Integer.parseInt(line);
						if (id < 1 || id > 127)
							throw new IllegalArgumentException("ID must be in [1;127].");
						reg.setId(id);
						reg.setState(next);
					}
					catch (RuntimeException e)
					{
						_log.info("You must input a number between 1 and 127");
					}
					
					if (reg.getState() == RegistrationState.ALLOW_BANS)
					{
						Connection con = null;
						try
						{
							con = L2Database.getConnection();
							PreparedStatement ps = con.prepareStatement("SELECT allowBans FROM gameserver WHERE id = ?");
							ps.setInt(1, reg.getId());
							ResultSet rs = ps.executeQuery();
							if (rs.next())
							{
								_log.info("A game server is already registered on ID " + reg.getId());
								reg.setState(RegistrationState.INITIAL_CHOICE);
							}
							else
								_log.info("Allow account bans from this game server? [y/n]:");
							ps.close();
						}
						catch (SQLException e)
						{
							_log.error("Could not remove a game server!", e);
						}
						finally
						{
							L2Database.close(con);
						}
					}
					else if (reg.getState() == RegistrationState.REMOVE)
					{
						Connection con = null;
						try
						{
							con = L2Database.getConnection();
							PreparedStatement ps = con.prepareStatement("DELETE FROM gameserver WHERE id = ?");
							ps.setInt(1, reg.getId());
							int cnt = ps.executeUpdate();
							if (cnt == 0)
								_log.info("No game server registered on ID " + reg.getId());
							else
								_log.info("Game server removed.");
							ps.close();
						}
						catch (SQLException e)
						{
							_log.error("Could not remove a game server!", e);
						}
						finally
						{
							L2Database.close(con);
						}
						reg.setState(RegistrationState.INITIAL_CHOICE);
					}
					else if (reg.getState() == RegistrationState.GENERATE)
					{
						Connection con = null;
						try
						{
							con = L2Database.getConnection();
							PreparedStatement ps = con.prepareStatement("SELECT authData FROM gameserver WHERE id = ?");
							ps.setInt(1, reg.getId());
							ResultSet rs = ps.executeQuery();
							
							if (rs.next())
							{
								reg.setAuth(rs.getString("authData"));
								byte[] b = HexUtil.HexStringToBytes(reg.getAuth());
								
								Properties pro = new Properties();
								pro.setProperty("ServerID", String.valueOf(reg.getId()));
								pro.setProperty("HexID", HexUtil.hexToString(b));
								
								BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream("hexid.txt"));
								pro.store(os, "the hexID to auth into login");
								IOUtils.closeQuietly(os);
								_log.info("hexid.txt has been generated.");
							}
							else
								_log.info("No game server registered on ID " + reg.getId());
							
							rs.close();
							ps.close();
						}
						catch (SQLException e)
						{
							_log.error("Could not generate hexid.txt!", e);
						}
						finally
						{
							L2Database.close(con);
						}
						reg.setState(RegistrationState.INITIAL_CHOICE);
					}
					break;
				case ALLOW_BANS:
					try
					{
						if (line.length() != 1)
							throw new IllegalArgumentException("One char required.");
						else if (line.charAt(0) == 'y')
							reg.setTrusted(true);
						else if (line.charAt(0) == 'n')
							reg.setTrusted(false);
						else
							throw new IllegalArgumentException("Invalid choice.");
						
						byte[] auth = Rnd.nextBytes(new byte[BYTES]);
						reg.setAuth(HexUtil.bytesToHexString(auth));
						
						Connection con = null;
						try
						{
							con = L2Database.getConnection();
							PreparedStatement ps = con.prepareStatement("INSERT INTO gameserver (id, authData, allowBans) VALUES (?, ?, ?)");
							ps.setInt(1, reg.getId());
							ps.setString(2, reg.getAuth());
							ps.setBoolean(3, reg.isTrusted());
							ps.executeUpdate();
							ps.close();
							
							_log.info("Registered game server on ID " + reg.getId());
							_log.info("The authorization string is:");
							_log.info(reg.getAuth());
							_log.info("Use it when registering this login server.");
							_log.info("If you need a legacy hexid file, use the 'hexid' command.");
						}
						catch (SQLException e)
						{
							_log.error("Could not register gameserver!", e);
						}
						finally
						{
							L2Database.close(con);
						}
						
						reg.setState(RegistrationState.INITIAL_CHOICE);
					}
					catch (IllegalArgumentException e)
					{
						_log.info("[y/n]?");
					}
					break;
				default:
					if (line.equals("list"))
					{
						Connection con = null;
						try
						{
							con = L2Database.getConnection();
							PreparedStatement ps = con.prepareStatement("SELECT id, allowBans FROM gameserver");
							ResultSet rs = ps.executeQuery();
							while (rs.next())
								_log.info("ID: " + rs.getInt("id") + ", trusted: " + rs.getBoolean("allowBans"));
							rs.close();
							ps.close();
						}
						catch (SQLException e)
						{
							_log.error("Could not register gameserver!", e);
						}
						finally
						{
							L2Database.close(con);
						}
						reg.setState(RegistrationState.INITIAL_CHOICE);
					}
					else if (line.equals("reg"))
					{
						_log.info("Enter the desired ID:");
						reg.setState(RegistrationState.GAMESERVER_ID);
						next = RegistrationState.ALLOW_BANS;
					}
					else if (line.equals("rem"))
					{
						_log.info("Enter game server ID:");
						reg.setState(RegistrationState.GAMESERVER_ID);
						next = RegistrationState.REMOVE;
					}
					else if (line.equals("hexid"))
					{
						_log.info("Enter game server ID:");
						reg.setState(RegistrationState.GAMESERVER_ID);
						next = RegistrationState.GENERATE;
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
	
	private enum RegistrationState
	{
		INITIAL_CHOICE,
		GAMESERVER_ID,
		ALLOW_BANS,
		
		REMOVE,
		GENERATE,
	}
}
