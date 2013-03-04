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

/**
 * @author NB4L1
 * @param <T>
 * @param <RP>
 * @param <SP>
 */
public abstract class DefaultSendablePacket<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>>
		extends SendablePacket<T, RP, SP>
{
	private static final int[] EMPTY_ADDITIONAL_OPCODES = new int[0];
	
	/**
	 * Returns this packet's identifier.<BR>It is assumed that the same opcode
	 * can safely be used for all supported clients.
	 * 
	 * @return a number from the interval [{@link java.lang.Byte#MIN_VALUE};
	 *         {@link java.lang.Byte#MAX_VALUE}]
	 */
	@SuppressWarnings("static-method")
	protected int getOpcode() {
		throw new AbstractMethodError();
	}
	
	/**
	 * Returns a suitable packet's identifier for the specified client.
	 * 
	 * @param client packet receiver
	 * @return a number from the interval [{@link java.lang.Byte#MIN_VALUE};
	 *         {@link java.lang.Byte#MAX_VALUE}]
	 */
	protected int getOpcode(T client) {
		return getOpcode();
	}
	
	/**
	 * Returns this packet's additional identifiers, if any; an empty array otherwise.<BR>
	 * It is assumed that the same opcodes can safely be used for all supported clients.
	 * 
	 * @return an array filled with numbers from the interval [{@link java.lang.Byte#MIN_VALUE};
	 *         {@link java.lang.Byte#MAX_VALUE}]
	 */
	@SuppressWarnings("static-method")
	protected int[] getAdditionalOpcodes() {
		return EMPTY_ADDITIONAL_OPCODES;
	}
	
	/**
	 * Returns suitable packet's additional identifiers, if any, for the specified client; an empty array otherwise.
	 * 
	 * @param client packet receiver
	 * @return an array filled with numbers from the interval [{@link java.lang.Byte#MIN_VALUE};
	 *         {@link java.lang.Byte#MAX_VALUE}]
	 */
	protected int[] getAdditionalOpcodes(T client)
	{
		return getAdditionalOpcodes();
	}
	
	/**
	 * Embed data into a network packet.
	 * 
	 * @param client packet sender
	 * @param buf buffer for packet's data
	 * @throws RuntimeException if a generic failure occurs while writing
	 */
	protected abstract void writeImpl(T client, MMOBuffer buf) throws RuntimeException;
	
	@Override
	protected final void write(T client, MMOBuffer buf) throws RuntimeException
	{
		buf.writeC(getOpcode(client));
		for (int additionalOpcode : getAdditionalOpcodes(client))
			buf.writeC(additionalOpcode);
		
		writeImpl(client, buf);
	}
}
