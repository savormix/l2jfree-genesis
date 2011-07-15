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
package com.l2jfree.network.mmocore;

import java.nio.ByteOrder;

/**
 * @author KenM
 */
public final class MMOConfig
{
	private final String NAME;
	
	private int BUFFER_SIZE = 64 * 1024; // 0xFFFF + 1
	
	private int MAX_SEND_PER_PASS = Integer.MAX_VALUE;
	private int MAX_READ_PER_PASS = Integer.MAX_VALUE;
	
	private int MAX_SEND_BYTE_PER_PASS = Integer.MAX_VALUE;
	private int MAX_READ_BYTE_PER_PASS = Integer.MAX_VALUE;
	
	private int SLEEP_TIME = 10;
	
	private int HELPER_BUFFER_COUNT = 20;
	
	private ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
	
	private int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
	
	public MMOConfig(String name)
	{
		NAME = name;
	}
	
	public String getName()
	{
		return NAME;
	}
	
	/**
	 * To configure the bytebuffers' size used for read-write operations.  (probably never used)
	 * 
	 * @param bufferSize
	 */
	public void setBufferSize(int bufferSize)
	{
		if (bufferSize < 64 * 1024) // 0xFFFF + 1
			throw new IllegalArgumentException();
		
		BUFFER_SIZE = bufferSize;
	}
	
	int getBufferSize()
	{
		return BUFFER_SIZE;
	}
	
	/**
	 * To configure the amount of "helper" bytebuffers kept in cache for further usage.
	 * 
	 * @param helperBufferCount
	 */
	public void setHelperBufferCount(int helperBufferCount)
	{
		HELPER_BUFFER_COUNT = helperBufferCount;
	}
	
	int getHelperBufferCount()
	{
		return HELPER_BUFFER_COUNT;
	}
	
	/**
	 * To configure the byte order used by the bytebuffers. (probably never used)
	 * 
	 * @param byteOrder
	 */
	public void setByteOrder(ByteOrder byteOrder)
	{
		BYTE_ORDER = byteOrder;
	}
	
	ByteOrder getByteOrder()
	{
		return BYTE_ORDER;
	}
	
	/**
	 * Server will try to send maxSendPerPass packets per socket write call however it may send less<br>
	 * if the write buffer was filled before achieving this value.
	 * 
	 * @param maxSendPerPass The maximum number of packets to be sent on a single socket write call
	 */
	public void setMaxSendPerPass(int maxSendPerPass)
	{
		MAX_SEND_PER_PASS = maxSendPerPass;
	}
	
	/**
	 * @return The maximum number of packets sent in an socket write call
	 */
	int getMaxSendPerPass()
	{
		return MAX_SEND_PER_PASS;
	}
	
	/**
	 * Server will try to read maxReadPerPass packets per socket read call however it may read less<br>
	 * if the read buffer was filled before achieving this value.
	 * 
	 * @param maxReadPerPass The maximum number of packets to be read on a single socket read call
	 */
	public void setMaxReadPerPass(int maxReadPerPass)
	{
		MAX_READ_PER_PASS = maxReadPerPass;
	}
	
	int getMaxReadPerPass()
	{
		return MAX_READ_PER_PASS;
	}
	
	/**
	 * Server will try to send maxSendBytePerPass bytes per socket write call however it may send less<br>
	 * if the write buffer was filled before achieving this value.
	 * 
	 * @param maxSendBytePerPass The maximum number of bytes to be sent on a single socket write call
	 */
	public void setMaxSendBytePerPass(int maxSendBytePerPass)
	{
		MAX_SEND_BYTE_PER_PASS = maxSendBytePerPass;
	}
	
	int getMaxSendBytePerPass()
	{
		return MAX_SEND_BYTE_PER_PASS;
	}
	
	/**
	 * Server will try to read maxReadBytePerPass bytes per socket read call however it may read less<br>
	 * if the read buffer was filled before achieving this value.
	 * 
	 * @param maxReadBytePerPass The maximum number of bytes to be read on a single socket read call
	 */
	public void setMaxReadBytePerPass(int maxReadBytePerPass)
	{
		MAX_READ_BYTE_PER_PASS = maxReadBytePerPass;
	}
	
	int getMaxReadBytePerPass()
	{
		return MAX_READ_BYTE_PER_PASS;
	}
	
	/**
	 * Defines how much time (in milis) should the selector sleep, a higher value increases throughput but also<br>
	 * increases latency (to a max of the sleep value itself).<br>
	 * Also an extremely high value(usually > 100) will decrease throughput due to the server<br>
	 * not doing enough sends per second (depends on max sends per pass).<br>
	 * <br>
	 * Recommended values:
	 * <ul>
	 * <li>1 for minimal latency</li>
	 * <li>5-50 for a latency/troughput trade-off based on your needs</li>
	 * </ul>
	 * 
	 * @param sleepTime the sleepTime to set in millisec
	 */
	public void setSelectorSleepTime(int sleepTime)
	{
		SLEEP_TIME = sleepTime;
	}
	
	/**
	 * @return the sleepTime setting for the selector
	 */
	int getSelectorSleepTime()
	{
		return SLEEP_TIME;
	}
	
	/**
	 * To configure the amount of read-write threads.
	 * 
	 * @param threadCount
	 */
	public void setThreadCount(int threadCount)
	{
		THREAD_COUNT = threadCount;
	}
	
	int getThreadCount()
	{
		return THREAD_COUNT;
	}
}
