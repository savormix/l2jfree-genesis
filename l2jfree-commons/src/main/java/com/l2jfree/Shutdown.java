package com.l2jfree;

public final class Shutdown
{
	private Shutdown()
	{
	}
	
	public static void shutdown(String initiator)
	{
		try
		{
			System.out.println(initiator + " issued SHUTDOWN command!");
		}
		finally
		{
			Runtime.getRuntime().exit(0);
		}
	}
	
	public static void restart(String initiator)
	{
		try
		{
			System.out.println(initiator + " issued RESTART command!");
		}
		finally
		{
			Runtime.getRuntime().exit(2);
		}
	}
	
	public static void halt(String initiator)
	{
		try
		{
			System.out.println(initiator + " issued HALT command!");
		}
		finally
		{
			Runtime.getRuntime().halt(2);
		}
	}
}
