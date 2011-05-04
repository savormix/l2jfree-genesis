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
package com.l2jfree.util.logging;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author NB4L1
 */
public final class ErrorLog
{
	private ErrorLog()
	{
	}
	
	public static final class Handler extends FileHandler
	{
		static
		{
			new File("log/error").mkdirs();
		}
		
		public Handler() throws IOException, SecurityException
		{
			super();
		}
	}
	
	public static final class Filter implements java.util.logging.Filter
	{
		@Override
		public boolean isLoggable(LogRecord record)
		{
			return record.getLevel().intValue() > Level.INFO.intValue() || record.getThrown() != null;
		}
	}
	
	public static final class Formatter extends L2RuntimeLogFormatter
	{
	}
}
