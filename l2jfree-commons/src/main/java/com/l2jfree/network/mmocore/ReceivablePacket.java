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

import java.nio.BufferUnderflowException;

import com.l2jfree.network.mmocore.FloodManager.ErrorMode;

/**
 * @param <T> Connection
 * @param <RP> Receivable packet
 * @param <SP> Sendable packet
 * @author KenM
 */
public abstract class ReceivablePacket<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>>
		extends AbstractPacket implements Runnable
{
	protected ReceivablePacket()
	{
		super();
	}
	
	private T _client;
	
	final void setClient(T client)
	{
		_client = client;
	}
	
	/**
	 * Returns the client that received this packet.
	 * @return packet receiver
	 */
	public final T getClient()
	{
		return _client;
	}
	
	/**
	 * Specifies the minimum length of a valid packet in bytes.
	 * <BR><BR>
	 * The main purpose of this value is to help identify
	 * malformed packets and/or outdated packet formats.
	 * @return size of a smallest valid packet
	 */
	protected abstract int getMinimumLength();
	
	/**
	 * Extract data from a network packet.
	 * <BR><BR>
	 * All bytes should be read, even if they have
	 * no use when processing the packet.
	 * @param buf a byte buffer containing packet data
	 * @return whether to process this packet (true) or ignore it
	 * @throws BufferUnderflowException if packet does not match the expected format
	 * @throws RuntimeException if a generic failure occurs while reading
	 */
	protected abstract boolean read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException;
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final void run()
	{
		try
		{
			runImpl();
		}
		catch (InvalidPacketException e)
		{
			getClient().getMMOController().report(ErrorMode.FAILED_RUNNING, getClient(), (RP)this, e);
		}
		catch (RuntimeException e)
		{
			getClient().getMMOController().report(ErrorMode.FAILED_RUNNING, getClient(), (RP)this, e);
		}
	}
	
	protected abstract void runImpl() throws InvalidPacketException, RuntimeException;
	
	protected final void sendPacket(SP sp)
	{
		getClient().sendPacket(sp);
	}
}
