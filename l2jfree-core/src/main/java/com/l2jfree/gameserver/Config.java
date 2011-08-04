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
package com.l2jfree.gameserver;

import com.l2jfree.L2Config;
import com.l2jfree.Shutdown;
import com.l2jfree.TerminationStatus;
import com.l2jfree.sql.L2Database;
import com.l2jfree.sql.L2DatabaseInstaller;
import com.l2jfree.util.concurrent.L2ThreadPool;

/**
 * @author savormix
 * @author NB4L1
 */
public class Config extends L2Config
{
	static
	{
		try
		{
			registerConfigClasses("com.l2jfree.gameserver.config");
		}
		catch (Exception e)
		{
			_log.fatal("Could not load configuration classes!", e);
			Shutdown.exit(TerminationStatus.RUNTIME_INVALID_CONFIGURATION);
		}
		
		try
		{
			L2Config.loadConfigs();
		}
		catch (Exception e)
		{
			_log.fatal("Could not load configuration files!", e);
			Shutdown.exit(TerminationStatus.RUNTIME_INVALID_CONFIGURATION);
		}
		
		try
		{
			L2ThreadPool.initThreadPools(new L2CoreThreadPools());
		}
		catch (Exception e)
		{
			_log.fatal("Could not initialize thread pools!", e);
			Shutdown.exit(TerminationStatus.RUNTIME_INITIALIZATION_FAILURE);
		}
		
		try
		{
			L2Database.setDataSource("default", new L2CoreDataSource());
		}
		catch (Exception e)
		{
			_log.fatal("Could not initialize DB connections!", e);
			Shutdown.exit(TerminationStatus.RUNTIME_INITIALIZATION_FAILURE);
		}
		
		try
		{
			L2DatabaseInstaller.check();
		}
		catch (Exception e)
		{
			_log.fatal("Could not initialize DB tables!", e);
			Shutdown.exit(TerminationStatus.RUNTIME_INITIALIZATION_FAILURE);
		}
	}
	
	protected Config()
	{
		super();
	}
}
