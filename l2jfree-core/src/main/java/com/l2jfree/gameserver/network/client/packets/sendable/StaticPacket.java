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

import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;

/**
 * A wrapper/marker class for packets with minimal mutability. All meaningful mutations are
 * instanced in advance.<BR>
 * <BR>
 * Subclasses of this packet must meet the following criteria:
 * <UL>
 * <LI>Private constructor</LI>
 * <LI>Public static final field <TT>PACKET</TT> of type <TT>&lt;? extends StaticPacket&gt;</TT></LI>
 * OR
 * <LI>Public static final fields of type <TT>&lt;? extends StaticPacket&gt;</TT></LI>
 * </UL>
 */
public abstract class StaticPacket extends L2ServerPacket
{
	// marker class
}
