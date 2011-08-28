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

import java.io.File;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

import com.l2jfree.lang.L2System;
import com.l2jfree.util.logging.L2Logger;

public class Version
{
	private String _revisionNumber = "exported";
	private String _versionNumber = "-1";
	private String _buildJdk = "";
	private long _buildTime = -1;
	
	private static final L2Logger _log = L2Logger.getLogger(Version.class);
	
	static
	{
		L2System.milliTime();
	}
	
	public Version()
	{
	}
	
	public Version(Class<?> c)
	{
		loadInformation(c);
	}
	
	public void loadInformation(Class<?> c)
	{
		File jarName = null;
		try
		{
			jarName = Locator.getClassSource(c);
			
			if (!jarName.isFile())
				return;
			
			JarFile jarFile = new JarFile(jarName);
			
			Attributes attrs = jarFile.getManifest().getMainAttributes();
			
			setBuildTime(attrs);
			
			setBuildJdk(attrs);
			
			setRevisionNumber(attrs);
			
			setVersionNumber(attrs);
		}
		catch (Exception e) // IOException | RuntimeException
		{
			if (_log.isErrorEnabled())
				_log.error("Unable to get Soft information\nFile name '"
								+ (jarName == null ? "null" : jarName.getAbsolutePath()) + "' isn't a valid jar", e);
		}
		
	}
	
	/**
	 * @param attrs
	 */
	private void setVersionNumber(Attributes attrs)
	{
		String versionNumber = attrs.getValue("Implementation-Version");
		if (versionNumber != null)
		{
			_versionNumber = versionNumber;
		}
		else
		{
			_versionNumber = "-1";
		}
	}
	
	/**
	 * @param attrs
	 */
	private void setRevisionNumber(Attributes attrs)
	{
		String revisionNumber = attrs.getValue("Implementation-Build");
		if (revisionNumber != null)
		{
			_revisionNumber = revisionNumber;
		}
		else
		{
			_revisionNumber = "-1";
		}
	}
	
	/**
	 * @param attrs
	 */
	private void setBuildJdk(Attributes attrs)
	{
		String buildJdk = attrs.getValue("Build-Jdk");
		if (buildJdk != null)
		{
			_buildJdk = buildJdk;
		}
		else
		{
			buildJdk = attrs.getValue("Created-By");
			if (buildJdk != null)
			{
				_buildJdk = buildJdk;
			}
			else
			{
				_buildJdk = "-1";
			}
		}
	}
	
	/**
	 * @param attrs
	 */
	private void setBuildTime(Attributes attrs)
	{
		String buildTime = attrs.getValue("Implementation-Time");
		if (buildTime != null)
		{
			_buildTime = Long.parseLong(buildTime);
		}
		else
		{
			_buildTime = -1;
		}
	}
	
	public String getRevisionNumber()
	{
		return _revisionNumber;
	}
	
	public String getVersionNumber()
	{
		return _versionNumber;
	}
	
	public String getBuildJdk()
	{
		return _buildJdk;
	}
	
	public long getBuildTime()
	{
		return _buildTime;
	}
}
