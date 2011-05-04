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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javolution.text.TextBuilder;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.l2jfree.L2Config;

/**
 * @author NB4L1
 */
public abstract class L2RuntimeLogFormatter extends L2LogFormatter
{
	@Override
	protected final void format0(LogRecord record, TextBuilder tb)
	{
		tb.append(record.getLevel()).append(" ");
		
		appendDate(record, tb);
		
		if (record.getLevel().intValue() > Level.INFO.intValue() || record.getThrown() != null)
			if (!StringUtils.isEmpty(record.getSourceClassName()) && !StringUtils.isEmpty(record.getSourceMethodName()))
				tb.append(record.getSourceClassName()).append(".").append(record.getSourceMethodName()).append("(): ");
		
		appendMessage(record, tb);
		appendThrown(record, tb);
	}
	
	protected final void appendThrown(LogRecord record, TextBuilder tb)
	{
		Throwable throwable = record.getThrown();
		
		if (throwable == null)
		{
			if (record.getLevel().intValue() >= L2Config.EXTENDED_LOG_LEVEL.intValue())
				throwable = new ExtendedLog();
			else if (record.getMessage() != null && record.getMessage().contains("Unevenly distributed hash code - Degraded Preformance"))
				throwable = new ExtendedLog();
		}
		
		if (throwable != null)
		{
			StringWriter sw = null;
			PrintWriter pw = null;
			try
			{
				sw = new StringWriter();
				pw = new PrintWriter(sw);
				
				throwable.printStackTrace(pw);
				
				appendNewline(tb);
				tb.append(sw);
			}
			finally
			{
				IOUtils.closeQuietly(pw);
				IOUtils.closeQuietly(sw);
			}
		}
	}
	
	private static final class ExtendedLog extends Exception
	{
		private static final long serialVersionUID = -8959693629510963880L;
		
		private ExtendedLog()
		{
			super("This is just an extended feature of logging to show the stacktrace! It's not a real exception to report!");
		}
	}
}
