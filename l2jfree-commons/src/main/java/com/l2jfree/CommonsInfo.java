package com.l2jfree;

import com.l2jfree.util.jar.FormattedVersion;

/**
 * @author noctarius
 */
public class CommonsInfo
{
	protected CommonsInfo()
	{
	}
	
	private static final FormattedVersion COMMONS_VERSION = new FormattedVersion(L2Config.class);
	
	public static void showStartupInfo(FormattedVersion version)
	{
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println(" ___       ___           ___");
		System.out.println("/\\_ \\    /'___`\\   __  /'___\\");
		System.out.println("\\//\\ \\  /\\_\\ /\\ \\ /\\_\\/\\ \\__/  _ __    __     __");
		System.out.println("  \\ \\ \\ \\/_/// /__\\/\\ \\ \\ ,__\\/\\`'__\\/'__`\\ /'__`\\");
		System.out.println("   \\_\\ \\_  // /_\\ \\\\ \\ \\ \\ \\_/\\ \\ \\//\\  __//\\  __/");
		System.out.println("   /\\____\\/\\______/_\\ \\ \\ \\_\\  \\ \\_\\\\ \\____\\ \\____\\");
		System.out.println("   \\/____/\\/_____//\\ \\_\\ \\/_/   \\/_/ \\/____/\\/____/");
		System.out.println("                  \\ \\____/");
		System.out.println("                   \\/___/  [starting version: " + version.getVersionNumber() + "]");
	}
	
	public static String[] getFullVersionInfo()
	{
		return new String[] { "l2j-commons  :    " + COMMONS_VERSION.getFullVersionInfo() };
	}
}
