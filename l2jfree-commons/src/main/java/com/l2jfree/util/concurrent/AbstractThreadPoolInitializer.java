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
package com.l2jfree.util.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author NB4L1
 */
public abstract class AbstractThreadPoolInitializer implements ThreadPoolInitializer
{
	private final List<ScheduledThreadPoolExecutor> _scheduledPools = new ArrayList<ScheduledThreadPoolExecutor>();
	private final List<ThreadPoolExecutor> _instantPools = new ArrayList<ThreadPoolExecutor>();
	private final List<ThreadPoolExecutor> _longRunningPools = new ArrayList<ThreadPoolExecutor>();
	
	protected final void addScheduledPool(ScheduledThreadPoolExecutor pool)
	{
		_scheduledPools.add(pool);
	}
	
	protected final void addInstantPool(ThreadPoolExecutor pool)
	{
		_instantPools.add(pool);
	}
	
	protected final void addLongRunningPool(ThreadPoolExecutor pool)
	{
		_longRunningPools.add(pool);
	}
	
	@Override
	public abstract void initThreadPool() throws Exception;
	
	@Override
	public final ScheduledThreadPoolExecutor[] getScheduledPools()
	{
		return _scheduledPools.toArray(new ScheduledThreadPoolExecutor[_scheduledPools.size()]);
	}
	
	@Override
	public final ThreadPoolExecutor[] getInstantPools()
	{
		return _instantPools.toArray(new ThreadPoolExecutor[_instantPools.size()]);
	}
	
	@Override
	public final ThreadPoolExecutor[] getLongRunningPools()
	{
		return _longRunningPools.toArray(new ThreadPoolExecutor[_longRunningPools.size()]);
	}
}
