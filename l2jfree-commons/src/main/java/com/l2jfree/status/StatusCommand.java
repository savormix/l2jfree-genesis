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
package com.l2jfree.status;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.l2jfree.lang.L2System;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author NB4L1
 */
public abstract class StatusCommand
{
	protected static final L2Logger _log = L2Logger.getLogger(StatusCommand.class);
	
	private final String _description;
	private final String[] _commands;
	
	protected StatusCommand(String description, String... commands)
	{
		_description = description;
		_commands = commands;
	}
	
	protected abstract void useCommand(String command, String params);
	
	protected final String[] getCommands()
	{
		return _commands;
	}
	
	protected final String listCommands()
	{
		return StringUtils.join(getCommands(), "|");
	}
	
	protected final String getDescription()
	{
		return _description;
	}
	
	@SuppressWarnings("static-method")
	protected String getParameterUsage()
	{
		return null;
	}
	
	@SuppressWarnings("static-method")
	protected StatusThread getStatusThread()
	{
		return (StatusThread)Thread.currentThread();
	}
	
	protected final StatusCommand print(Object obj)
	{
		getStatusThread().print(obj);
		
		return this;
	}
	
	protected final StatusCommand println(Object obj)
	{
		getStatusThread().println(obj);
		
		return this;
	}
	
	protected final StatusCommand println()
	{
		getStatusThread().println();
		
		return this;
	}
	
	protected final String readLine() throws IOException
	{
		return getStatusThread().readLine();
	}
	
	protected String getHostAddress()
	{
		return getStatusThread().getSocket().getInetAddress().getHostAddress();
	}
	
	protected final void printMemoryStatistics()
	{
		for (String line : L2System.getMemoryUsageStatistics())
			println(line);
	}
}
