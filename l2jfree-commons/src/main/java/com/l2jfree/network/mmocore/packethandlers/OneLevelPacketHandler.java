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
package com.l2jfree.network.mmocore.packethandlers;

import java.nio.ByteBuffer;

import com.l2jfree.network.mmocore.MMOConnection;
import com.l2jfree.network.mmocore.PacketHandler;
import com.l2jfree.network.mmocore.ReceivablePacket;
import com.l2jfree.network.mmocore.SendablePacket;

/**
 * @author NB4L1
 * @param <T>
 * @param <RP>
 * @param <SP>
 * @param <S>
 */
public abstract class OneLevelPacketHandler<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>, S extends Enum<S>>
		extends PacketHandler<T, RP, SP>
{
	private final PacketDefinition<T, RP, SP, S>[][] _table;
	
	protected OneLevelPacketHandler(PacketDefinition<T, RP, SP, S>[][] table)
	{
		_table = table;
	}
	
	@Override
	public final RP handlePacket(ByteBuffer buf, T client, final int opcode1)
	{
		if (_table[opcode1] == null)
		{
			return unknown(buf, client, opcode1);
		}
		else if (_table[opcode1].length == 1)
		{
			final PacketDefinition<T, RP, SP, S> packetDefinition = _table[opcode1][client.getState().ordinal()];
			
			if (packetDefinition != null)
				return packetDefinition.newInstance();
			
			return invalidState(client, opcode1);
		}
		else
		{
			throw new Error();
		}
	}
}
