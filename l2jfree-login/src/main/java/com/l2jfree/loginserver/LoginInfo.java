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
package com.l2jfree.loginserver;

import org.apache.commons.lang3.ArrayUtils;

import com.l2jfree.CommonsInfo;
import com.l2jfree.util.jar.FormattedVersion;

/**
 * Generic class to provide version info.
 * 
 * @author noctarius
 */
public final class LoginInfo extends CommonsInfo
{
	private LoginInfo()
	{
		// utility class
	}
	
	private static final FormattedVersion LOGIN_VERSION = new FormattedVersion(LoginServer.class);
	
	/** Shows startup and version information. */
	public static void showStartupInfo()
	{
		CommonsInfo.showStartupInfo(LOGIN_VERSION);
	}
	
	/**
	 * Returns version information string.
	 * 
	 * @return version info
	 */
	public static String getVersionInfo()
	{
		return LOGIN_VERSION.getVersionInfo();
	}
	
	public static String[] getFullVersionInfo()
	{
		return ArrayUtils.addAll(new String[] { "l2jfree-login   :    " + LOGIN_VERSION.getFullVersionInfo() },
				CommonsInfo.getFullVersionInfo());
	}
}
