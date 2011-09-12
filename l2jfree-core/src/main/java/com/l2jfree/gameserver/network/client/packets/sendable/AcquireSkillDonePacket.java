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
public abstract class AcquireSkillDonePacket extends StaticPacket
{
	/**
	 * A nicer name for {@link AcquireSkillDonePacket}.
	 * 
	 * @author savormix (generated)
	 * @see AcquireSkillDonePacket
	 */
	public static final class SkillLearned extends AcquireSkillDonePacket
	{
		/** This packet. */
		public static final SkillLearned PACKET = new SkillLearned();
		
		/**
		 * Constructs this packet.
		 * 
		 * @see AcquireSkillDonePacket#AcquireSkillDonePacket()
		 */
		private SkillLearned()
		{
		}
	}
	
	/** Constructs this packet. */
	public AcquireSkillDonePacket()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x94;
	}
}
