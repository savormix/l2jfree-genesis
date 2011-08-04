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
package com.l2jfree.util.jar;

import java.util.Date;

/**
 * @author NB4L1
 */
public class FormattedVersion extends Version
{
	private final String _versionInfo;
	private final String _fullVersionInfo;
	
	public FormattedVersion(Class<?> c)
	{
		super(c);
		
		_versionInfo = String.format("%-6s [ %4s ]", getVersionNumber(), getRevisionNumber());
		_fullVersionInfo = getVersionInfo() + " - " + getBuildJdk() + " - " + new Date(getBuildTime());
	}
	
	public String getVersionInfo()
	{
		return _versionInfo;
	}
	
	public String getFullVersionInfo()
	{
		return _fullVersionInfo;
	}
}
