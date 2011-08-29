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
import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javolution.util.FastList;

import org.apache.commons.io.FileUtils;

import com.l2jfree.L2Config;
import com.l2jfree.util.concurrent.RunnableStatsManager;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author NB4L1
 */
public abstract class L2Thread extends Thread
{
	protected static final L2Logger _log = L2Logger.getLogger(L2Thread.class);
	
	protected L2Thread()
	{
		super();
	}
	
	protected L2Thread(String name)
	{
		super(name);
	}
	
	private volatile boolean _isAlive = true;
	
	public final void shutdown()
	{
		try
		{
			onShutdown();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		
		try
		{
			_isAlive = false;
			
			join();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
	
	protected void onShutdown()
	{
	}
	
	@Override
	public final void run()
	{
		try
		{
			while (_isAlive)
			{
				final long begin = System.nanoTime();
				
				try
				{
					runTurn();
				}
				finally
				{
					RunnableStatsManager.handleStats(getClass(), System.nanoTime() - begin);
				}
				
				try
				{
					sleepTurn();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		finally
		{
			onFinally();
		}
	}
	
	protected abstract void runTurn();
	
	protected abstract void sleepTurn() throws InterruptedException;
	
	protected void onFinally()
	{
	}
	
	public static List<String> getStats(Thread t)
	{
		List<String> list = new FastList<String>();
		
		list.add(t.toString() + " - ID: " + t.getId());
		list.add(" * State: " + t.getState());
		list.add(" * Alive: " + t.isAlive());
		list.add(" * Daemon: " + t.isDaemon());
		list.add(" * Interrupted: " + t.isInterrupted());
		for (ThreadInfo info : ManagementFactory.getThreadMXBean().getThreadInfo(new long[] { t.getId() }, true, true))
		{
			for (MonitorInfo monitorInfo : info.getLockedMonitors())
			{
				list.add("==========");
				list.add(" * Locked monitor: " + monitorInfo);
				list.add("\t[" + monitorInfo.getLockedStackDepth() + ".]: at " + monitorInfo.getLockedStackFrame());
			}
			
			for (LockInfo lockInfo : info.getLockedSynchronizers())
			{
				list.add("==========");
				list.add(" * Locked synchronizer: " + lockInfo);
			}
			
			list.add("==========");
			for (StackTraceElement trace : info.getStackTrace())
				list.add("\tat " + trace);
		}
		
		return list;
	}
	
	public static List<String> getStats()
	{
		List<String> list = new FastList<String>();
		
		list.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").format(new Date()));
		list.add("");
		list.add("## Java Platform Information ##");
		list.add("Java Runtime Name: " + System.getProperty("java.runtime.name"));
		list.add("Java Version: " + System.getProperty("java.version"));
		list.add("Java Class Version: " + System.getProperty("java.class.version"));
		list.add("");
		list.add("## Virtual Machine Information ##");
		list.add("VM Name: " + System.getProperty("java.vm.name"));
		list.add("VM Version: " + System.getProperty("java.vm.version"));
		list.add("VM Vendor: " + System.getProperty("java.vm.vendor"));
		list.add("VM Info: " + System.getProperty("java.vm.info"));
		list.add("");
		list.add("## OS Information ##");
		list.add("Name: " + System.getProperty("os.name"));
		list.add("Architeture: " + System.getProperty("os.arch"));
		list.add("Version: " + System.getProperty("os.version"));
		list.add("");
		list.add("## Runtime Information ##");
		list.add("CPU Count: " + Runtime.getRuntime().availableProcessors());
		list.add("");
		for (String line : L2System.getMemoryUsageStatistics())
			list.add(line);
		list.add("");
		list.add("## Class Path Information ##\n");
		for (String lib : System.getProperty("java.class.path").split(File.pathSeparator))
			if (!list.contains(lib))
				list.add(lib);
		list.add("");
		
		Set<Thread> threads = new TreeSet<Thread>(new Comparator<Thread>() {
			@Override
			public int compare(Thread t1, Thread t2)
			{
				if (t1.isDaemon() != t2.isDaemon())
					return Boolean.valueOf(t1.isDaemon()).compareTo(t2.isDaemon());
				
				final StackTraceElement[] st1 = t1.getStackTrace();
				final StackTraceElement[] st2 = t2.getStackTrace();
				
				for (int i = 1;; i++)
				{
					final int i1 = st1.length - i;
					final int i2 = st2.length - i;
					
					if (i1 < 0 || i2 < 0)
						break;
					
					final int compare = st1[i1].toString().compareToIgnoreCase(st2[i2].toString());
					
					if (compare != 0)
						return compare;
				}
				
				if (st1.length != st2.length)
					return Integer.valueOf(st1.length).compareTo(st2.length);
				
				return Long.valueOf(t1.getId()).compareTo(t2.getId());
			}
		});
		threads.addAll(Thread.getAllStackTraces().keySet());
		list.add("## " + threads.size() + " thread(s) ##");
		list.add("=================================================");
		
		int i = 1;
		for (Thread thread : threads)
		{
			list.add("");
			list.add(i++ + ".");
			list.addAll(getStats(thread));
		}
		
		return list;
	}
	
	public static void dumpThreads()
	{
		try
		{
			FileUtils.forceMkdir(new File("log/threadstats"));
			
			final L2TextBuilder tb = new L2TextBuilder();
			tb.append("log/threadstats/ThreadStats_");
			tb.append(new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()));
			tb.append("_uptime-").append(L2Config.getShortUptime());
			tb.append(".log");
			
			final String dumpFile = tb.moveToString();
			
			FileUtils.writeLines(new File(dumpFile), getStats());
			
			_log.info("L2Thread: Thread stats successfully dumped to `" + dumpFile + "`!");
		}
		catch (Exception e)
		{
			_log.warn("", e);
		}
	}
}
