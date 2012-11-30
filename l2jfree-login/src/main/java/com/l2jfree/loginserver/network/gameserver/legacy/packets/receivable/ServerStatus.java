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
package com.l2jfree.loginserver.network.gameserver.legacy.packets.receivable;

import java.nio.BufferUnderflowException;
import java.util.Map;
import java.util.Map.Entry;

import javolution.util.FastMap;

import com.l2jfree.loginserver.network.gameserver.L2GameServerType;
import com.l2jfree.loginserver.network.gameserver.legacy.L2LegacyGameServer;
import com.l2jfree.loginserver.network.gameserver.legacy.packets.L2LegacyGameServerPacket;
import com.l2jfree.loginserver.network.gameserver.legacy.status.L2LegacyAgeLimit;
import com.l2jfree.network.legacy.ServerStatusAttributes;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix
 */
public final class ServerStatus extends L2LegacyGameServerPacket
{
	/** Packet's identifier */
	public static final int OPCODE = 0x06;
	
	private final Map<Integer, Integer> _status;
	
	/** Constructs this packet. */
	public ServerStatus()
	{
		_status = new FastMap<Integer, Integer>();
	}
	
	@Override
	protected int getMinimumLength()
	{
		// a valid packet of 4 bytes does not need to be handled
		return READ_D + (READ_D + READ_D);
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		int size = buf.readD();
		for (int i = 0; i < size; i++)
		{
			int type = buf.readD();
			int value = buf.readD();
			_status.put(type, value);
		}
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		final L2LegacyGameServer lgs = getClient();
		for (ServerStatusAttributes llms : ServerStatusAttributes.VALUES)
		{
			Integer val = _status.remove(llms.ordinal());
			if (val == null)
				continue;
			
			switch (llms)
			{
				case SERVER_LIST_STATUS:
					lgs.setStatus(com.l2jfree.network.legacy.ServerStatus.VALUES.valueOf(val));
					break;
				case SERVER_LIST_UNK:
					lgs.setTypes(L2GameServerType.UNKNOWN.set(lgs.getTypes(), val != 0));
					break;
				case SERVER_LIST_CLOCK:
					lgs.setTypes(L2GameServerType.RELAX.set(lgs.getTypes(), val != 0));
					break;
				case SERVER_LIST_HIDE_NAME:
					lgs.setTypes(L2GameServerType.UNNAMED.set(lgs.getTypes(), val != 0));
					break;
				case TEST_SERVER:
					lgs.setTypes(L2GameServerType.TEST.set(lgs.getTypes(), val != 0));
					break;
				case SERVER_LIST_PVP:
					lgs.setPvp(val != 0);
					break;
				case SERVER_LIST_BRACKETS:
					lgs.setBrackets(val != 0);
					break;
				case SERVER_LIST_MAX_PLAYERS:
					lgs.setMaxPlayers(val);
					break;
				case SERVER_AGE_LIMITATION:
					lgs.setAge(val);
					if (!L2LegacyAgeLimit.isDisplayed(lgs))
						_log.info("Game server on ID " + lgs.getId() + " specified an invisible age limit.");
					break;
				default:
					_log.warn("Unhandled legacy managed state: " + llms);
					break;
			}
		}
		
		for (Entry<Integer, Integer> e : _status.entrySet())
			_log.warn("Unknown legacy managed state! Type: " + e.getKey() + " value: " + e.getValue());
	}
}
