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
package com.l2jfree;

/**
 * @author NB4L1
 */
// dates can be wrong
public enum ClientProtocolVersion
{
	FREYA(216), // 2010-08-24
	HIGH_FIVE(267), // 2011-02-15
	HIGH_FIVE_UPDATE_1(268), // 2011-03-15
	HIGH_FIVE_UPDATE_2(271), // 2011-05-25
	HIGH_FIVE_UPDATE_3(273); // 2011-06-08
	
	private int _version;
	
	private ClientProtocolVersion(int version)
	{
		_version = version;
	}
	
	public int getVersion()
	{
		return _version;
	}
}
