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
import com.l2jfree.gameserver.util.ObjectId;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix (generated)
 */
public abstract class ExBRExtraUserInfo extends L2ServerPacket
{
	/**
	 * A nicer name for {@link ExBRExtraUserInfo}.
	 * 
	 * @author savormix (generated)
	 * @see ExBRExtraUserInfo
	 */
	public static final class EventPlayerInfo extends ExBRExtraUserInfo
	{
		/**
		 * Constructs this packet.
		 * 
		 * @param player a player's character
		 * @see ExBRExtraUserInfo#ExBRExtraUserInfo(L2Player)
		 */
		public EventPlayerInfo(L2Player player)
		{
			super(player);
		}
	}
	
	private static final int[] EXT_OPCODES = { 0xda, 0x00 };
	
	// XXX: perhaps use view later
	private final ObjectId _playerObjectId;
	private final int _effect;
	private final boolean _active;
	
	/**
	 * Constructs this packet.
	 * 
	 * @param player a player's character
	 */
	public ExBRExtraUserInfo(L2Player player)
	{
		this(player.getObjectId(), 0, false);
	}
	
	private ExBRExtraUserInfo(ObjectId playerObjectId, int effect, boolean active)
	{
		_playerObjectId = playerObjectId;
		_effect = effect;
		_active = active;
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
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeD(_playerObjectId); // Actor OID
		buf.writeD(_effect); // Event effect
		buf.writeC(_active); // Active
	}
}
