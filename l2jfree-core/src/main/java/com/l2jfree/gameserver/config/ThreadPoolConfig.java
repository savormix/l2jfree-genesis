package com.l2jfree.gameserver.config;

import com.l2jfree.L2Config.ConfigPropertiesLoader;
import com.l2jfree.config.annotation.ConfigClass;
import com.l2jfree.config.annotation.ConfigField;

/**
 * @author NB4L1
 */
@ConfigClass(folderName = "config", fileName = "threadpool")
public final class ThreadPoolConfig extends ConfigPropertiesLoader
{
	@ConfigField(name = "ScheduledThreadPoolCount", value = "-1", eternal = true, comment = {
			"Determines the amount of created scheduled thread pools", //
			"If set to -1 (which is recommended), the server will decide the amount depending on the available processors", //
	})
	public static int SCHEDULED_THREAD_POOL_COUNT;
	
	@ConfigField(name = "ThreadsPerScheduledThreadPool", value = "4", eternal = true, comment = {
			"Specifies how many threads will be in a single scheduled pool", //
			"NOTE: fixing your code is always better than increasing the pool size ;)", //
	})
	public static int THREADS_PER_SCHEDULED_THREAD_POOL;
	
	@ConfigField(name = "InstantThreadPoolCount", value = "-1", eternal = true, comment = {
			"Determines the amount of created instant thread pools", //
			"If set to -1 (which is recommended), the server will decide the amount depending on the available processors", //
	})
	public static int INSTANT_THREAD_POOL_COUNT;
	
	@ConfigField(name = "ThreadsPerInstantThreadPool", value = "2", eternal = true, comment = {
			"Specifies how many threads will be in a single instant pool", //
			"NOTE: fixing your code is always better than increasing the pool size ;)", //
	})
	public static int THREADS_PER_INSTANT_THREAD_POOL;
}
