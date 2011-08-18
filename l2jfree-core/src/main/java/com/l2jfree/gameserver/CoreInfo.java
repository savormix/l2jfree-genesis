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
package com.l2jfree.gameserver;

import org.apache.commons.lang3.ArrayUtils;

import com.l2jfree.CommonsInfo;
import com.l2jfree.util.jar.FormattedVersion;

/**
 * Generic class to provide version info.
 * 
 * @author noctarius
 */
public final class CoreInfo extends CommonsInfo
{
	private CoreInfo()
	{
		// utility class
	}
	
	private static final FormattedVersion CORE_VERSION = new FormattedVersion(GameServer.class);
	
	/** Shows startup and version information. */
	public static void showStartupInfo()
	{
		CommonsInfo.showStartupInfo(CORE_VERSION);
	}
	
	/**
	 * Returns version information string.
	 * 
	 * @return version info
	 */
	public static String getVersionInfo()
	{
		return CORE_VERSION.getVersionInfo();
	}
	
	public static String[] getFullVersionInfo()
	{
		return ArrayUtils.addAll(new String[] { "l2jfree-core    :    " + CORE_VERSION.getFullVersionInfo() },
				CommonsInfo.getFullVersionInfo());
	}
}
