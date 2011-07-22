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

import com.l2jfree.util.logging.L2Logger;

/**
 * @author savormix
 *
 */
public enum L2LegacyStatus
{
	/** Online */
	AUTO(0, "Auto"),
	/** Online */
	GOOD(1, "Good"),
	/** Online */
	NORMAL(2, "Normal"),
	/** Online */
	FULL(3, "Full"),
	/** Offline */
	DOWN(4, "Down"),
	/** Online for GMs */
	GM_ONLY(5, "GM only");
	
	private static final L2Logger _log = L2Logger.getLogger(L2LegacyStatus.class);
	
	private final int _id;
	private final String _status;
	
	private L2LegacyStatus(int id, String status)
	{
		_id = id;
		_status = status;
	}
	
	/**
	 * Returns status value ID.
	 * @return status ID
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * Returns displayable status string.
	 * @return status string
	 */
	public String getStatus()
	{
		return _status;
	}
	
	/**
	 * Returns a legacy status with the given ID.
	 * @param id status ID
	 * @return legacy status
	 */
	public static L2LegacyStatus getById(int id)
	{
		for (L2LegacyStatus lls : values())
			if (lls.getId() == id)
				return lls;
		
		_log.warn("Unknown legacy status ID: " + id);
		return AUTO;
	}
}
