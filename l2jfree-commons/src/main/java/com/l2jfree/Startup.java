package com.l2jfree;

import java.util.Set;

import com.l2jfree.util.L2FastSet;

/**
 * @author NB4L1
 */
public final class Startup
{
	private static Set<StartupHook> _startupHooks = new L2FastSet<StartupHook>();
	
	/**
	 * While application is loading, returns {@code false}. After the application finishes loading, returns {@code true}.<BR>
	 * <BR>
	 * If calling this method resulted in {@code true}, all following invocations are guaranteed to result in {@code true}.
	 * 
	 * @return whether the application has finished loading
	 */
	public synchronized static boolean isLoaded()
	{
		return _startupHooks == null;
	}
	
	/**
	 * Adds a hook to be executed after the application loads.
	 * 
	 * @param hook The hook to be attached
	 */
	public synchronized static void addStartupHook(StartupHook hook)
	{
		if (_startupHooks != null)
			_startupHooks.add(hook);
		else
			hook.onStartup();
	}
	
	/** Executes startup hooks. */
	public synchronized static void onStartup()
	{
		final Set<StartupHook> startupHooks = _startupHooks;
		
		_startupHooks = null;
		
		for (StartupHook hook : startupHooks)
			hook.onStartup();
	}
	
	/**
	 * This interface allows the implementing class to be attached as a startup hook.
	 */
	public interface StartupHook
	{
		/**
		 * This method is called on an attached startup hook when/if the application has finished loading.
		 */
		public void onStartup();
	}
}
