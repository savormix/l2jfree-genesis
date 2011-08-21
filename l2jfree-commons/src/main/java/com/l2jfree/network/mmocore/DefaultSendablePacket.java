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
 */
public abstract class DefaultSendablePacket<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>>
		extends SendablePacket<T, RP, SP>
{
	private static final int[] EMPTY_ADDITIONAL_OPCODES = new int[0];
	
	/**
	 * Returns this packet's identifier.
	 * 
	 * @return a number from the interval [{@link java.lang.Byte#MIN_VALUE};
	 *         {@link java.lang.Byte#MAX_VALUE}]
	 */
	protected abstract int getOpcode();
	
	/**
	 * Returns this packet's additional identifiers, if any, an empty array otherwise.
	 * 
	 * @return an array filled with numbers from the interval [{@link java.lang.Byte#MIN_VALUE};
	 *         {@link java.lang.Byte#MAX_VALUE}]
	 */
	protected int[] getAdditionalOpcodes()
	{
		return EMPTY_ADDITIONAL_OPCODES;
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
		buf.writeC(getOpcode());
		for (int additionalOpcode : getAdditionalOpcodes())
			buf.writeC(additionalOpcode);
		
		writeImpl(client, buf);
	}
}
