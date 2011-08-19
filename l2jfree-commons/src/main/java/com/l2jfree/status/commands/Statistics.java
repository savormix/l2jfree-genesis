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
package com.l2jfree.status.commands;

import com.l2jfree.L2Config;
import com.l2jfree.lang.L2System;
import com.l2jfree.status.StatusCommand;

/**
 * @author NB4L1
 */
public final class Statistics extends StatusCommand
{
	/** Creates a command handler. */
	public Statistics()
	{
		super("displays basic server statistics", "status", "stats");
	}
	
	@Override
	protected void useCommand(String command, String params)
	{
		println("Server statistics: ");
		println("  --->  Server uptime: " + L2Config.getUptime());
		println("  ---> Active threads: " + Thread.activeCount());
		println("  --->    Memory used: " + L2System.usedMemory());
	}
}
