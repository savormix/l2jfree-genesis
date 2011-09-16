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
package com.l2jfree.gameserver.network.loginserver.legacy.packets.sendable;

import com.l2jfree.gameserver.network.loginserver.legacy.L2LegacyLoginServer;
import com.l2jfree.gameserver.network.loginserver.legacy.packets.L2LegacyGameServerPacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author hex1r0
 */
public class AuthRequest extends L2LegacyGameServerPacket
{
	/** Packet's identifier */
	public static final int OPCODE = 0x01;
	
	private final int _serverId;
	private final boolean _acceptAlternateId;
	private final byte[] _hexId;
	private final int _port;
	private final boolean _reserveHost;
	private final int _maxPlayers;
	private final String[] _subnets;
	private final String[] _hosts;
	
	public AuthRequest(int serverId, boolean acceptAlternateId, byte[] hexId, int port, boolean reserveHost,
			int maxPlayers, String[] subnets, String[] hosts)
	{
		_serverId = serverId;
		_acceptAlternateId = acceptAlternateId;
		_hexId = hexId;
		_port = port;
		_reserveHost = reserveHost;
		_maxPlayers = maxPlayers;
		_subnets = subnets;
		_hosts = hosts;
	}
	
	@Override
	protected int getOpcode()
	{
		return OPCODE;
	}
	
	@Override
	protected void writeImpl(L2LegacyLoginServer client, MMOBuffer buf) throws RuntimeException
	{
		buf.writeC(_serverId);
		buf.writeC(_acceptAlternateId ? 0x01 : 0x00);
		buf.writeC(_reserveHost ? 0x01 : 0x00);
		buf.writeH(_port);
		buf.writeD(_maxPlayers);
		buf.writeD(_hexId.length);
		buf.writeB(_hexId);
		buf.writeD(_subnets.length);
		for (int i = 0; i < _subnets.length; i++)
		{
			buf.writeS(_subnets[i]);
			buf.writeS(_hosts[i]);
		}
	}
}
