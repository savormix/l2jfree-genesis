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
package com.l2jfree.gameserver.network.client;

import com.l2jfree.gameserver.network.client.packets.receivable.NetPing.UptimeResponse;
import com.l2jfree.gameserver.network.client.packets.sendable.NetPingPacket.ServerUptime;
import com.l2jfree.util.concurrent.AbstractIterativePeriodicTaskManager;

/**
 * Sends {@link ServerUptime} to test whether a client is responding.<BR>
 * <BR>
 * The implementation is incomplete.
 * 
 * @author savormix
 * @see ServerUptime
 * @see UptimeResponse
 */
public final class L2ClientNetPing extends AbstractIterativePeriodicTaskManager<IL2Client>
{
	private static final int PERIOD = 30 * 1000;
	
	private L2ClientNetPing()
	{
		super(PERIOD);
	}
	
	@Override
	protected void callTask(IL2Client task)
	{
		// FIXME reactivate later
		//task.sendPacket(ServerUptime.PACKET);
	}
	
	@Override
	protected String getCalledMethodName()
	{
		return "sendPacket(ServerUptime.PACKET)";
	}
	
	public static L2ClientNetPing getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		public static final L2ClientNetPing INSTANCE = new L2ClientNetPing();
	}
}
