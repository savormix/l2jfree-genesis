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
package com.l2jfree.loginserver.network.legacy.status;

/**
 * @author savormix
 *
 */
public enum L2LegacyManagedState
{
	/** Online status */
	STATUS(1),
	/** Server types */
	TYPE(2),
	/** Square brackets */
	BRACKETS(3),
	/** Maximum online players */
	MAX_PLAYERS(4),
	/** Minimal player age */
	AGE_LIMIT(5);
	
	private final Integer _id;
	
	private L2LegacyManagedState(int id)
	{
		_id = id;
	}
	
	/**
	 * Returns the state's type ID.
	 * @return state ID
	 */
	public Integer getId()
	{
		return _id;
	}
}
