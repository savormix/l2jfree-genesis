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
package com.l2jfree.network.mmocore;

import java.nio.ByteBuffer;

/**
 * Handles incoming packets.
 * @param <T> connection
 * @param <RP> receivable packet
 * @param <SP> sendable packet
 * @author KenM
 */
public interface IPacketHandler<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>>
{
	/**
	 * Chooses a suitable packet handler.
	 * @param buf a byte buffer containing any further opcodes, and packet data
	 * @param client a client that received the packet
	 * @param opcode the first byte (opcode) of the packet
	 * @return a recognized packet that represents the packet body or null otherwise
	 */
	public RP handlePacket(ByteBuffer buf, T client, int opcode);
}
