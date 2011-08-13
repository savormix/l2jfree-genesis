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
package com.l2jfree.gameserver.config;

import com.l2jfree.L2Config.ConfigPropertiesLoader;
import com.l2jfree.config.annotation.ConfigClass;
import com.l2jfree.config.annotation.ConfigField;

/**
 * @author NB4L1
 */
@ConfigClass(folderName = "config", fileName = "system")
public final class SystemConfig extends ConfigPropertiesLoader
{
	/** Whether to dump heap after startup */
	@ConfigField(name = "DumpHeapAfterStartup", value = "true", eternal = true, comment = { "Whether to dump heap after server startup or not." })
	public static boolean DUMP_HEAP_AFTER_STARTUP;
	
	/** Whether to dump heap before shutdown */
	@ConfigField(name = "DumpHeapBeforeShutdown", value = "true", eternal = true, comment = { "Whether to dump heap before server shutdown or not." })
	public static boolean DUMP_HEAP_BEFORE_SHUTDOWN;
}
