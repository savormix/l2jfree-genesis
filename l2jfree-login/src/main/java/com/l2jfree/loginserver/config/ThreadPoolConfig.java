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
			"NOTE: fixing your code is always better than increasing the pool size ;)", //
	})
	public static int THREADS_PER_SCHEDULED_THREAD_POOL;
}
