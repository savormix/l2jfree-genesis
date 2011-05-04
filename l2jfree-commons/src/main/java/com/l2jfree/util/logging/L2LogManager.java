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
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author NB4L1
 */
// TODO there must be a more elegant way to use a subclass instead of a Logger instance itselfg
public final class L2LogManager extends LogManager
{
	static
	{
		new File("log").mkdirs();
	}
	
	public L2LogManager()
	{
	}
	
	@Override
	public void reset() throws SecurityException
	{
		for (StackTraceElement ste : Thread.currentThread().getStackTrace())
			if ("java.util.logging.LogManager$Cleaner".equals(ste.getClassName()))
				return;
		
		super.reset();
	}
	
	@Override
	public synchronized boolean addLogger(Logger logger)
	{
		if (!(logger instanceof L2Logger))
			logger = new L2Logger(logger.getName(), logger.getResourceBundleName());
		
		return super.addLogger(logger);
	}
	
	@Override
	public synchronized Logger getLogger(String name)
	{
		Logger logger = super.getLogger(name);
		if (logger == null)
		{
			logger = new L2Logger(name, null);
			addLogger(logger);
			logger = getLogger(name);
		}
		return logger;
	}
}
