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
package com.l2jfree.loginserver.config;

import com.l2jfree.L2Config.ConfigPropertiesLoader;
import com.l2jfree.config.annotation.ConfigClass;
import com.l2jfree.config.annotation.ConfigField;

/**
 * @author NB4L1
 */
@ConfigClass(folderName = "config", fileName = "threadpool")
public final class ThreadPoolConfig extends ConfigPropertiesLoader
{
	/** Number of threads to be used by the scheduled executor service */
	@ConfigField(name = "ThreadsPerScheduledThreadPool", value = "-1", eternal = true, comment = {
			"Specifies how many threads will be in the scheduled pool", //
			"If set to -1 (which is recommended), the server will decide the amount depending on the available processors", //
			"NOTE: fixing your code is always better than increasing the pool size ;)" })
	public static int THREADS_PER_SCHEDULED_THREAD_POOL;
}
