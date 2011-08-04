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

import java.text.NumberFormat;
import java.util.Locale;

import com.l2jfree.lang.L2System;

/**
 */
public final class Util
{
	private Util()
	{
		// utility class
	}
	
	/**
	 * Prints a named section to log.
	 * 
	 * @param s Section name
	 */
	public static void printSection(String s)
	{
		s = "={ " + s + " }";
		
		while (s.length() < 160)
			s = "-" + s;
		
		L2Config.err.println(s);
	}
	
	// some sys info utils
	public static int getAvailableProcessors()
	{
		Runtime rt = Runtime.getRuntime();
		return rt.availableProcessors();
	}
	
	public static String getOSName()
	{
		return System.getProperty("os.name");
	}
	
	public static String getOSVersion()
	{
		return System.getProperty("os.version");
	}
	
	public static String getOSArch()
	{
		return System.getProperty("os.arch");
	}
	
	public static String[] getMemUsage()
	{
		return L2System.getMemoryUsageStatistics();
	}
	
	/**
	 * Returns a number formatted with "," delimiter
	 * 
	 * @param value
	 * @return String formatted number
	 */
	public static String formatNumber(long value)
	{
		return NumberFormat.getInstance(Locale.ENGLISH).format(value);
	}
}
