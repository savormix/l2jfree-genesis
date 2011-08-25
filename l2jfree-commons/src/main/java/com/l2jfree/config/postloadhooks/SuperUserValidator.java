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
package com.l2jfree.config.postloadhooks;

import com.l2jfree.Shutdown;
import com.l2jfree.TerminationStatus;

/**
 * @author NB4L1
 */
public final class SuperUserValidator extends TypedPostLoadHook<String>
{
	@Override
	protected void valueLoadedImpl(String user)
	{
		if (user.equalsIgnoreCase("root") || user.equalsIgnoreCase("postgres"))
		{
			System.err.println("L2jFree servers should not use superuser accounts... exiting now!");
			
			Shutdown.exit(TerminationStatus.ENVIRONMENT_SUPERUSER);
		}
	}
	
	@Override
	protected Class<String> getRequiredType()
	{
		return String.class;
	}
}
