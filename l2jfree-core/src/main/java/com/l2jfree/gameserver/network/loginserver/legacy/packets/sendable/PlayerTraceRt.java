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
 * @author NB4L1
 */
public class PlayerTraceRt extends L2LegacyGameServerPacket
{
	private final String _account;
	private final String _pcIp;
	private final String _hop1;
	private final String _hop2;
	private final String _hop3;
	private final String _hop4;
	
	public PlayerTraceRt(String account, String pcIp, String hop1, String hop2, String hop3, String hop4)
	{
		_account = account;
		_pcIp = pcIp;
		_hop1 = hop1;
		_hop2 = hop2;
		_hop3 = hop3;
		_hop4 = hop4;
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x07;
	}
	
	@Override
	protected void writeImpl(L2LegacyLoginServer client, MMOBuffer buf) throws RuntimeException
	{
		buf.writeS(_account);
		buf.writeS(_pcIp);
		buf.writeS(_hop1);
		buf.writeS(_hop2);
		buf.writeS(_hop3);
		buf.writeS(_hop4);
	}
}
