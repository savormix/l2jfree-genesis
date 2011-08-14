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
import java.util.logging.Level;
import java.util.logging.LogRecord;

import com.l2jfree.lang.L2System;
import com.l2jfree.util.logging.L2Logger;

/**
 */
public final class Util
{
	private static final L2Logger _log = L2Logger.getLogger(Util.class);
	
	private Util()
	{
		// utility class
	}
	
	/**
	 * Prints a named section to log.
	 * 
	 * @param sectionName Section name
	 */
	public static void printSection(String sectionName)
	{
		final StringBuilder sb = new StringBuilder(160);
		
		for (int i = 0; i < (160 - 3 - sectionName.length() - 2); i++)
			sb.append('-');
		
		sb.append("={ ").append(sectionName).append(" }");
		
		_log.log(new PrintSectionLogRecord(Level.INFO, sb.toString()));
	}
	
	/** Helps to bypass the log formatters. */
	public static final class PrintSectionLogRecord extends LogRecord
	{
		private static final long serialVersionUID = 8499381472396828554L;
		
		public PrintSectionLogRecord(Level level, String msg)
		{
			super(Level.INFO, msg);
		}
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
