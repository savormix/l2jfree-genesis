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
