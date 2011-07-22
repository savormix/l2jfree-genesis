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
package com.l2jfree.loginserver.network.client;

import java.util.Date;

/**
 * @author savormix
 *
 */
public class L2Account
{
	private final String _account;
	private final boolean _superUser;
	private final Date _dateOfBirth;
	private final int _lastServerId;
	
	/**
	 * Creates an account to attach to a client connection wrapper.
	 * @param account Account name
	 * @param superUser Account privileges
	 * @param dateOfBirth Account holder's DoB
	 * @param lastServerId Last game server
	 */
	public L2Account(String account, boolean superUser, Date dateOfBirth, int lastServerId)
	{
		_account = account;
		_superUser = superUser;
		_dateOfBirth = dateOfBirth;
		_lastServerId = lastServerId;
	}
	
	/**
	 * Returns the associated account name.
	 * @return account name
	 */
	public String getAccount()
	{
		return _account;
	}
	
	/**
	 * Returns whether this client can login to any online server.
	 * @return whether the associated account has superuser privileges
	 */
	public boolean isSuperUser()
	{
		return _superUser;
	}
	
	/**
	 * Returns account holder's birth date.
	 * @return holder's DoB
	 */
	public Date getDateOfBirth()
	{
		return _dateOfBirth;
	}
	
	/**
	 * Returns the ID of a game server into which the associated account logged last.
	 * @return last game server ID
	 */
	public int getLastServerId()
	{
		return _lastServerId;
	}
}
