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
package com.l2jfree.gameserver.network.client.packets.sendable;

/**
 * @author savormix (generated)
 */
public abstract class SunRise extends StaticPacket
{
	/**
	 * A nicer name for {@link SunRise}.
	 * 
	 * @author savormix (generated)
	 * @see SunRise
	 */
	public static final class DayTime extends SunRise
	{
		/** This packet. */
		public static final DayTime PACKET = new DayTime();

		/**
		 * Constructs this packet.
		 * 
		 * @see SunRise#SunRise()
		 */
		private DayTime()
		{
		}
	}

	/** Constructs this packet. */
	public SunRise()
	{
	}

	@Override
	protected int getOpcode()
	{
		return 0x12;
	}
}
