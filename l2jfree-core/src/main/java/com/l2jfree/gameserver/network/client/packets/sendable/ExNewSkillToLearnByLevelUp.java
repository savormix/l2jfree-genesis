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
 * @since Goddess of Destruction
 */
public abstract class ExNewSkillToLearnByLevelUp extends StaticPacket
{
	/**
	 * A nicer name for {@link ExNewSkillToLearnByLevelUp}.
	 * 
	 * @author savormix (generated)
	 * @see ExNewSkillToLearnByLevelUp
	 */
	public static final class UnknownFEFC extends ExNewSkillToLearnByLevelUp
	{
		/** This packet. */
		public static final UnknownFEFC PACKET = new UnknownFEFC();
		
		/**
		 * Constructs this packet.
		 * 
		 * @see ExNewSkillToLearnByLevelUp#ExNewSkillToLearnByLevelUp()
		 */
		private UnknownFEFC()
		{
		}
	}
	
	private static final int[] EXT_OPCODES = { 0xfc, 0x00 };
	
	/** Constructs this packet. */
	public ExNewSkillToLearnByLevelUp()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0xfe;
	}
	
	@Override
	protected int[] getAdditionalOpcodes()
	{
		return EXT_OPCODES;
	}
}
