package com.l2jfree;

public final class Util
{
	private Util()
	{
	}
	
	public static void printSection(String s)
	{
		s = "={ " + s + " }";
		
		while (s.length() < 160)
			s = "-" + s;
		
		L2Config.out.println(s);
	}
}
