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

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.l2jfree.L2Config;
import com.l2jfree.gameserver.config.ConfigMarker;
import com.l2jfree.util.logging.ConsoleLog;

/**
 * @author savormix
 * @author NB4L1
 */
public class Config extends L2Config
{
	static
	{
		// TODO to temporarily allow eclipselink to show sqls
		for (Handler h : Logger.getLogger("").getHandlers())
			if (h instanceof ConsoleLog.Handler)
				h.setLevel(Level.FINE);
		//
		
		CoreInfo.showStartupInfo();
		
		L2Config.initApplication(ConfigMarker.class.getPackage(), L2CoreThreadPools.class, L2CoreDataSource.class);
	}
}
