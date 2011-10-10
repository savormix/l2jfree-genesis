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
 * Sent to the player that is expelled from party.
 * 
 * @author savormix (generated)
 */
public abstract class SetOustPartyMemberPacket extends StaticPacket
{
	/**
	 * A nicer name for {@link SetOustPartyMemberPacket}.
	 * 
	 * @author savormix (generated)
	 * @see SetOustPartyMemberPacket
	 */
	public static final class ExpelledFromParty extends SetOustPartyMemberPacket
	{
		/** This packet. */
		public static final ExpelledFromParty PACKET = new ExpelledFromParty();
		
		/**
		 * Constructs this packet.
		 * 
		 * @see SetOustPartyMemberPacket#SetOustPartyMemberPacket()
		 */
		private ExpelledFromParty()
		{
		}
	}
	
	/** Constructs this packet. */
	public SetOustPartyMemberPacket()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x3d;
	}
}
