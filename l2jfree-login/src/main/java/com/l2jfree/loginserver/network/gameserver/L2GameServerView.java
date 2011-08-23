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
package com.l2jfree.loginserver.network.gameserver;

import com.l2jfree.util.logging.L2Logger;

/**
 * @author savormix
 */
public abstract class L2GameServerView implements Comparable<L2GameServerView>
{
	/** Default logger for game server attribute views. */
	protected static final L2Logger _log = L2Logger.getLogger(L2GameServerView.class);
	/** Avoid redirecting to local server while this one is offline */
	private static final byte[] UNKNOWN_IPV4 = { 0, 0, 0, 0 };
	
	private int _id;
	private final byte[] _ipv4;
	private int _port;
	private int _age;
	private boolean _pvp;
	private int _onlinePlayers;
	private int _maxPlayers;
	private boolean _online;
	private int _types;
	private boolean _brackets;
	
	/** Creates a default (uninitialized) game server view. */
	protected L2GameServerView()
	{
		_ipv4 = new byte[4];
	}
	
	/** Update this view. */
	public abstract void update();
	
	@Override
	public int compareTo(L2GameServerView lgsv)
	{
		final int id;
		if (lgsv == null)
			id = 0;
		else
			id = lgsv.getId();
		return getId() - id;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof L2GameServerView)
			return getId() == ((L2GameServerView)o).getId();
		else
			return false;
	}
	
	@Override
	public int hashCode()
	{
		return getId() * 32;
	}
	
	/**
	 * Returns the ID assigned to this game server.
	 * 
	 * @return ID
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * Returns game server's listening IP.
	 * 
	 * @return IPv4 listening address
	 */
	public byte[] getIpv4()
	{
		return _ipv4;
	}
	
	/**
	 * Returns game server's listening port.
	 * 
	 * @return listening port
	 */
	public int getPort()
	{
		return _port;
	}
	
	/**
	 * Returns player age requirement.
	 * 
	 * @return minimal player age
	 */
	public int getAge()
	{
		return _age;
	}
	
	/**
	 * Returns whether PvP is allowed.
	 * 
	 * @return is PvP allowed
	 */
	public boolean isPvp()
	{
		return _pvp;
	}
	
	/**
	 * Returns online player count.
	 * 
	 * @return online player count
	 */
	public int getOnlinePlayers()
	{
		return _onlinePlayers;
	}
	
	/**
	 * Returns maximum online player count.
	 * 
	 * @return maximum online players
	 */
	public int getMaxPlayers()
	{
		return _maxPlayers;
	}
	
	/**
	 * Returns server status.
	 * 
	 * @return is online
	 */
	public boolean isOnline()
	{
		return _online;
	}
	
	/**
	 * Returns all types of this game server.
	 * 
	 * @return game server types
	 */
	public int getTypes()
	{
		return _types;
	}
	
	/**
	 * Returns whether to lead game server's name with square brackets.
	 * 
	 * @return show square brackets
	 */
	public boolean isBrackets()
	{
		return _brackets;
	}
	
	/**
	 * Changes game server ID.
	 * @param id new ID
	 */
	protected void setId(int id)
	{
		_id = id;
	}
	
	/**
	 * Changes listening port.
	 * @param port new port
	 */
	protected void setPort(int port)
	{
		_port = port;
	}
	
	/**
	 * Changes age restriction.
	 * @param age minimum age
	 */
	protected void setAge(int age)
	{
		_age = age;
	}
	
	/**
	 * Changes PvP status
	 * @param pvp whether PvP is allowed
	 */
	protected void setPvp(boolean pvp)
	{
		_pvp = pvp;
	}
	
	/**
	 * Changes online player count.
	 * @param onlinePlayers online player count.
	 */
	protected void setOnlinePlayers(int onlinePlayers)
	{
		_onlinePlayers = onlinePlayers;
	}
	
	/**
	 * Changes maximum online player count.
	 * @param maxPlayers maximum player count
	 */
	protected void setMaxPlayers(int maxPlayers)
	{
		_maxPlayers = maxPlayers;
	}
	
	/**
	 * Changes game server status.
	 * @param online whether on or off
	 */
	protected void setOnline(boolean online)
	{
		_online = online;
	}
	
	/**
	 * Change game server types.
	 * @param types server types
	 */
	protected void setTypes(int types)
	{
		_types = types;
	}
	
	/**
	 * Changes bracket status.
	 * @param brackets whether to show brackets
	 */
	protected void setBrackets(boolean brackets)
	{
		_brackets = brackets;
	}
	
	/**
	 * Returns 0.0.0.0 (uninitialized IP).
	 * @return 0.0.0.0
	 */
	protected static final byte[] getDefaultIp()
	{
		return UNKNOWN_IPV4;
	}
}
