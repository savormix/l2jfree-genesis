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
package com.l2jfree.loginserver.network.legacy.packets;

import com.l2jfree.loginserver.network.legacy.L2GameServer;
import com.l2jfree.network.mmocore.ReceivablePacket;

/**
 * Just a wrapper class for convenience.
 * @author savormix
 */
public abstract class L2GameServerPacket extends ReceivablePacket<L2GameServer, L2GameServerPacket, L2LoginServerPacket>
{
	protected L2GameServerPacket()
	{
		super();
	}
}
