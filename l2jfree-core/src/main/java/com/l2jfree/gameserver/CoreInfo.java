package com.l2jfree.gameserver;

import org.apache.commons.lang.ArrayUtils;

import com.l2jfree.CommonsInfo;
import com.l2jfree.util.jar.FormattedVersion;

/**
 * @author noctarius
 */
public final class CoreInfo extends CommonsInfo
{
	protected CoreInfo()
	{
	}
	
	private static final FormattedVersion CORE_VERSION = new FormattedVersion(GameServer.class);
	
	public static void showStartupInfo()
	{
		CommonsInfo.showStartupInfo(CORE_VERSION);
	}
	
	public static String getVersionInfo()
	{
		return CORE_VERSION.getVersionInfo();
	}
	
	public static String[] getFullVersionInfo()
	{
		return (String[])ArrayUtils.addAll(new String[] { "l2jfree-core :    " + CORE_VERSION.getFullVersionInfo() },
				CommonsInfo.getFullVersionInfo());
	}
}
