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

import org.apache.commons.lang.StringUtils;

import com.l2jfree.status.StatusCommand;
import com.l2jfree.util.concurrent.RunnableStatsManager;
import com.l2jfree.util.concurrent.RunnableStatsManager.SortBy;

/**
 * @author NB4L1
 */
public final class ClassStats extends StatusCommand
{
	public ClassStats()
	{
		super("dump runnable stats", "class");
	}
	
	@Override
	protected void useCommand(String command, String params)
	{
		SortBy sortBy = null;
		
		if (!params.isEmpty())
			sortBy = SortBy.valueOf(params.toUpperCase());
		
		RunnableStatsManager.dumpClassStats(sortBy);
		
		println("Runnable stats dumped...");
	}
	
	@Override
	protected String getParameterUsage()
	{
		return "[" + StringUtils.join(SortBy.values(), "|").toLowerCase() + "]";
	}
}
