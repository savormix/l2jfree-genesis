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
package com.l2jfree.loginserver.network.legacy;

import com.l2jfree.util.logging.L2Logger;

/**
 * @author savormix
 *
 */
public class L2LegacySecurity
{
	private static final L2Logger _log = L2Logger.getLogger(L2LegacySecurity.class);
	
	private L2LegacySecurity()
	{
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Returns a singleton object.
	 * @return an instance of this class
	 */
	public static L2LegacySecurity getInstance()
	{
		return SingletonHolder._instance;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		static final L2LegacySecurity _instance = new L2LegacySecurity();
	}
}
