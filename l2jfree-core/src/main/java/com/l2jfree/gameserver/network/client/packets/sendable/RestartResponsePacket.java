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

import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * Sent in response to
 * {@link com.l2jfree.gameserver.network.client.packets.receivable.RequestRestart}.
 * 
 * @author savormix
 */
public abstract class RestartResponsePacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link RestartResponsePacket}.
	 * 
	 * @author savormix
	 * @see RestartResponsePacket
	 */
	public static final class RestartResult extends RestartResponsePacket
	{
		public static final RestartResult ALLOWED = new RestartResult(true);
		public static final RestartResult DENIED = new RestartResult(false);
		
		/**
		 * Constructs this packet.
		 * 
		 * @param result response to request
		 * @see RestartResponsePacket#RestartResponsePacket(boolean)
		 */
		private RestartResult(boolean result)
		{
			super(result);
		}
	}
	
	private final boolean _result;
	
	/**
	 * Constructs this packet.
	 * 
	 * @param result response to request
	 */
	public RestartResponsePacket(boolean result)
	{
		_result = result;
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x71;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		buf.writeD(_result); // Can return to character selection
	}
}
