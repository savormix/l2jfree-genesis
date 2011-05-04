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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import javolution.text.TextBuilder;

import com.l2jfree.lang.L2TextBuilder;

/**
 * @author NB4L1
 */
public abstract class L2LogFormatter extends Formatter
{
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM HH:mm:ss,SSS");
	
	@Override
	public final String format(LogRecord record)
	{
		L2TextBuilder tb = L2TextBuilder.newInstance();
		
		format0(record, tb);
		
		appendNewline(tb);
		
		return tb.moveToString();
	}
	
	protected abstract void format0(LogRecord record, TextBuilder tb);
	
	protected final void appendDate(LogRecord record, TextBuilder tb)
	{
		tb.append("[").append(DATE_FORMAT.format(new Date(record.getMillis()))).append("] ");
	}
	
	protected final void appendMessage(LogRecord record, TextBuilder tb)
	{
		tb.append(record.getMessage());
	}
	
	protected final void appendParameters(LogRecord record, TextBuilder tb, String separator, boolean before)
	{
		if (record.getParameters() != null)
		{
			for (Object parameter : record.getParameters())
			{
				if (parameter == null)
					continue;
				
				if (before)
				{
					tb.append(separator);
					tb.append(parameter);
				}
				else
				{
					tb.append(parameter);
					tb.append(separator);
				}
			}
		}
	}
	
	protected final void appendNewline(TextBuilder tb)
	{
		tb.append("\r\n");
	}
}
