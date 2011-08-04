package com.l2jfree.loginserver;

import org.apache.commons.lang.ArrayUtils;

import com.l2jfree.CommonsInfo;
import com.l2jfree.util.jar.FormattedVersion;

/**
 * @author noctarius
 */
public final class LoginInfo extends CommonsInfo
{
	protected LoginInfo()
	{
	}
	
	private static final FormattedVersion LOGIN_VERSION = new FormattedVersion(LoginServer.class);
	
	public static void showStartupInfo()
	{
		CommonsInfo.showStartupInfo(LOGIN_VERSION);
	}
	
	public static String getVersionInfo()
	{
		return LOGIN_VERSION.getVersionInfo();
	}
	
	public static String[] getFullVersionInfo()
	{
		return (String[])ArrayUtils.addAll(new String[] { "l2jfree-login :    " + LOGIN_VERSION.getFullVersionInfo() },
				CommonsInfo.getFullVersionInfo());
	}
}
