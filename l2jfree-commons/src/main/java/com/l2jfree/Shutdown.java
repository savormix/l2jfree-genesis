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

public final class Shutdown
{
	private Shutdown()
	{
	}
	
	public static void shutdown(String initiator)
	{
		try
		{
			System.out.println(initiator + " issued SHUTDOWN command!");
		}
		finally
		{
			Runtime.getRuntime().exit(0);
		}
	}
	
	public static void restart(String initiator)
	{
		try
		{
			System.out.println(initiator + " issued RESTART command!");
		}
		finally
		{
			Runtime.getRuntime().exit(2);
		}
	}
	
	public static void halt(String initiator)
	{
		try
		{
			System.out.println(initiator + " issued HALT command!");
		}
		finally
		{
			Runtime.getRuntime().halt(2);
		}
	}
}
