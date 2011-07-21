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

import com.l2jfree.status.StatusCommand;
import com.l2jfree.util.concurrent.L2ThreadPool;

/**
 * @author NB4L1
 */
public final class Purge extends StatusCommand
{
	public Purge()
	{
		super("purges the threadpool", "purge");
	}
	
	@Override
	protected void useCommand(String command, String params)
	{
		L2ThreadPool.purge();
		
		println("Threadpool purged.");
	}
}
