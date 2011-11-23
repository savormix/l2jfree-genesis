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

import com.l2jfree.lang.L2TextBuilder;
import com.l2jfree.network.mmocore.FloodManager.ErrorMode;
import com.l2jfree.util.HexUtil;

/**
 * Handles incoming packets.
 * 
 * @param <T> connection
 * @param <RP> receivable packet
 * @param <SP> sendable packet
 * @author KenM
 */
public abstract class PacketHandler<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>>
{
	/**
	 * Chooses a suitable packet handler.
	 * 
	 * @param buf a byte buffer containing any further opcodes, and packet data
	 * @param client a client that received the packet
	 * @param opcode the first byte (opcode) of the packet
	 * @return a recognized packet that represents the packet body or null otherwise
	 */
	public abstract RP handlePacket(ByteBuffer buf, T client, int opcode);
	
	protected final RP underflow(ByteBuffer buf, T client, int opcode, int... additionalOpcodes)
	{
		logUnexpectedBehaviour("Unknown packet", buf, client, null, opcode, additionalOpcodes);
		
		client.getMMOController().report(ErrorMode.BUFFER_UNDER_FLOW, client, null, null);
		return null;
	}
	
	protected final RP unknown(ByteBuffer buf, T client, int opcode, int... additionalOpcodes)
	{
		logUnexpectedBehaviour("Unknown packet", buf, client, null, opcode, additionalOpcodes);
		
		client.getMMOController().report(ErrorMode.INVALID_OPCODE, client, null, null);
		return null;
	}
	
	protected final RP invalidState(T client, int opcode, int... additionalOpcodes)
	{
		return invalidState(client, null, opcode, additionalOpcodes);
	}
	
	protected final RP invalidState(T client, Class<? extends RP> packetClazz, int opcode, int... additionalOpcodes)
	{
		logUnexpectedBehaviour("Packet in invalid state", null, client, packetClazz, opcode, additionalOpcodes);
		
		client.getMMOController().report(ErrorMode.INVALID_STATE, client, null, null);
		return null;
	}
	
	private void logUnexpectedBehaviour(String message, ByteBuffer buf, T client, Class<? extends RP> packetClazz,
			int opcode, int... additionalOpcodes)
	{
		final L2TextBuilder sb = new L2TextBuilder();
		
		sb.append(getClass().getSimpleName()).append(": ").append(message).append(": ");
		
		if (packetClazz != null)
			sb.append(packetClazz.getName()).append(", ");
		
		sb.append("0x").append(HexUtil.fillHex(opcode, 2));
		for (int additionalOpcode : additionalOpcodes)
			sb.append(" : 0x").append(HexUtil.fillHex(additionalOpcode, 2));
		
		sb.append(", Client: ").append(client);
		sb.append("\r\n");
		
		// FIXME reactivate on live
		/*
		if (buf != null)
		{
			byte[] array = new byte[buf.remaining()];
			buf.get(array);
			sb.append(HexUtil.printData(array));
			sb.append("\r\n");
		}
		*/
		
		MMOController._log.info(sb.moveToString());
	}
}
