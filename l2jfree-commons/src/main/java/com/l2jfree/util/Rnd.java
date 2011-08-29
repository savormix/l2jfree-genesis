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
package com.l2jfree.util;

import java.util.List;
import java.util.Random;

import com.l2jfree.lang.L2TextBuilder;

public final class Rnd
{
	private static final class L2Random extends Random
	{
		private static final long serialVersionUID = 2089256427272977088L;
		
		/**
		 * Copied from java.util.Random.
		 */
		private static final long multiplier = 0x5DEECE66DL;
		private static final long addend = 0xBL;
		private static final long mask = (1L << 48) - 1;
		
		private long seed;
		
		@Override
		public synchronized void setSeed(long newSeed)
		{
			seed = (newSeed ^ multiplier) & mask;
			
			super.setSeed(newSeed);
		}
		
		@Override
		protected int next(int bits)
		{
			long nextseed = (seed = ((seed * multiplier + addend) & mask));
			
			return (int)(nextseed >>> (48 - bits));
		}
		
		public double nextDouble(double n)
		{
			return nextDouble() * n;
		}
		
		@Override
		public int nextInt(int n)
		{
			return (int)(nextDouble() * n);
		}
		
		public long nextLong(long n)
		{
			return (long)(nextDouble() * n);
		}
	}
	
	private static final L2Random RND = new L2Random();
	
	/**
	 * Get random number from 0.0 to 1.0
	 * 
	 * @return double
	 */
	public static double nextDouble()
	{
		return RND.nextDouble();
	}
	
	/**
	 * Get random number from 0 to n-1
	 * 
	 * @param n
	 * @return int
	 */
	public static int nextInt(int n)
	{
		return RND.nextInt(n);
	}
	
	/**
	 * Get random number from 0 to n-1
	 * 
	 * @param n
	 * @return int
	 */
	public static int get(int n)
	{
		return RND.nextInt(n);
	}
	
	/**
	 * Get random number from min to max <b>(not max-1)</b>
	 * 
	 * @param min
	 * @param max
	 * @return int
	 */
	public static int get(int min, int max)
	{
		if (min < max)
			return min + RND.nextInt(max - min + 1);
		else
			return max + RND.nextInt(min - max + 1);
	}
	
	/**
	 * Get random number from 0 to n-1
	 * 
	 * @param n
	 * @return long
	 */
	public static long nextLong(long n)
	{
		return RND.nextLong(n);
	}
	
	/**
	 * Get random number from 0 to n-1
	 * 
	 * @param n
	 * @return long
	 */
	public static long get(long n)
	{
		return RND.nextLong(n);
	}
	
	/**
	 * Get random number from min to max <b>(not max-1)</b>
	 * 
	 * @param min
	 * @param max
	 * @return long
	 */
	public static long get(long min, long max)
	{
		if (min < max)
			return min + RND.nextLong(max - min + 1);
		else
			return max + RND.nextLong(min - max + 1);
	}
	
	public static boolean calcChance(int chance, int maxChance)
	{
		return chance > RND.nextInt(maxChance);
	}
	
	public static boolean calcChance(double chance, int maxChance)
	{
		return chance > RND.nextDouble(maxChance);
	}
	
	public static boolean calcChance(double chance)
	{
		return chance > RND.nextDouble();
	}
	
	public static double nextGaussian()
	{
		return RND.nextGaussian();
	}
	
	public static boolean nextBoolean()
	{
		return RND.nextBoolean();
	}
	
	public static byte[] nextBytes(byte[] array)
	{
		RND.nextBytes(array);
		
		return array;
	}
	
	/**
	 * Returns a randomly selected element taken from the given list.
	 * 
	 * @param <T> type of list elements
	 * @param list a list
	 * @return a randomly selected element
	 */
	public static final <T> T get(List<T> list)
	{
		if (list == null || list.size() == 0)
			return null;
		else
			return list.get(get(list.size()));
	}
	
	/**
	 * Returns a randomly selected element taken from the given array.
	 * 
	 * @param <T> type of array elements
	 * @param array an array
	 * @return a randomly selected element
	 */
	public static final <T> T get(T[] array)
	{
		if (array == null || array.length == 0)
			return null;
		else
			return array[get(array.length)];
	}
	
	public static final String DIGITS = "0123456789";
	public static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
	public static final String UPPER_CASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static final String LETTERS = LOWER_CASE_LETTERS + UPPER_CASE_LETTERS;
	public static final String LETTERS_AND_DIGITS = LETTERS + DIGITS;
	
	public static String getString(int length, CharSequence source)
	{
		final L2TextBuilder tb = new L2TextBuilder(length);
		
		for (int i = 0; i < length; i++)
			tb.append(source.charAt(Rnd.get(source.length())));
		
		return tb.moveToString();
	}
}
