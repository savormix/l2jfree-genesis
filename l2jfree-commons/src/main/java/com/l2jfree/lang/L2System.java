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
package com.l2jfree.lang;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanServer;

import org.apache.commons.io.FileUtils;

import com.sun.management.HotSpotDiagnosticMXBean;

import com.l2jfree.L2Config;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author NB4L1
 */
public final class L2System
{
	private static final L2Logger _log = L2Logger.getLogger(L2System.class);
	
	private L2System()
	{
	}
	
	private static final long ZERO = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1);
	
	public static long milliTime()
	{
		return System.currentTimeMillis() - ZERO;
	}
	
	/**
	 * If you are TOTALLY sure, that your server has got constant cpu clock speed,<br>
	 * then you can use the more accurate nano based time measurement.<br>
	 * <br>
	 * In case just remove the comment from below, and add around the method above.
	 */
	/*
	private static final long ZERO = System.nanoTime() - TimeUnit.DAYS.toNanos(1);
	
	public static long milliTime()
	{
		return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - ZERO);
	}
	*/

	/**
	 * Copy of HashMap.hash().
	 */
	public static int hash(int h)
	{
		h ^= (h >>> 20) ^ (h >>> 12);
		return h ^ (h >>> 7) ^ (h >>> 4);
	}
	
	public static boolean equals(Object o1, Object o2)
	{
		return o1 == null ? o2 == null : o1.equals(o2);
	}
	
	public static String[] getMemoryUsageStatistics()
	{
		double max = Runtime.getRuntime().maxMemory() / 1024; // maxMemory is the upper limit the jvm can use
		double allocated = Runtime.getRuntime().totalMemory() / 1024; //totalMemory the size of the current allocation pool
		double nonAllocated = max - allocated; //non allocated memory till jvm limit
		double cached = Runtime.getRuntime().freeMemory() / 1024; // freeMemory the unused memory in the allocation pool
		double used = allocated - cached; // really used memory
		double useable = max - used; //allocated, but non-used and non-allocated memory
		
		SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
		DecimalFormat df = new DecimalFormat(" (0.0000'%')");
		DecimalFormat df2 = new DecimalFormat(" # 'KB'");
		
		return new String[] {
				"+----",// ...
				"| Global Memory Informations at " + sdf.format(new Date()) + ":", // ...
				"|    |", // ...
				"| Allowed Memory:" + df2.format(max),
				"|    |= Allocated Memory:" + df2.format(allocated) + df.format(allocated / max * 100),
				"|    |= Non-Allocated Memory:" + df2.format(nonAllocated) + df.format(nonAllocated / max * 100),
				"| Allocated Memory:" + df2.format(allocated),
				"|    |= Used Memory:" + df2.format(used) + df.format(used / max * 100),
				"|    |= Unused (cached) Memory:" + df2.format(cached) + df.format(cached / max * 100),
				"| Useable Memory:" + df2.format(useable) + df.format(useable / max * 100), // ...
				"+----" };
	}
	
	public static long usedMemory()
	{
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576;
	}
	
	public static void dumpHeap(boolean live)
	{
		try
		{
			FileUtils.forceMkdir(new File("log/heapdumps"));
			
			final L2TextBuilder tb = L2TextBuilder.newInstance();
			tb.append("log/heapdumps/HeapDump_");
			tb.append(new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()));
			tb.append("_uptime-").append(L2Config.getShortUptime());
			if (live)
				tb.append("_live");
			tb.append(".hprof");
			
			final String dumpFile = tb.moveToString();
			
			HotSpotDiagnosticMXBeanHolder.INSTANCE.dumpHeap(dumpFile, live);
			
			_log.info("L2System: JVM Heap successfully dumped to `" + dumpFile + "`!");
		}
		catch (Exception e)
		{
			_log.warn("L2System: Failed to dump heap:", e);
		}
	}
	
	private static final class HotSpotDiagnosticMXBeanHolder
	{
		static
		{
			try
			{
				final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
				final String mXBeanName = "com.sun.management:type=HotSpotDiagnostic";
				final Class<HotSpotDiagnosticMXBean> mXBeanInterface = HotSpotDiagnosticMXBean.class;
				
				INSTANCE = ManagementFactory.newPlatformMXBeanProxy(mBeanServer, mXBeanName, mXBeanInterface);
			}
			catch (Exception e)
			{
				throw new Error(e);
			}
		}
		
		public static final HotSpotDiagnosticMXBean INSTANCE;
	}
}
