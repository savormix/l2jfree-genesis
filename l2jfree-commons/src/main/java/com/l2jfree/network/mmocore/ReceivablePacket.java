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
 * @author KenM
 */
public abstract class ReceivablePacket<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>>
		extends AbstractPacket implements Runnable
{
	protected ReceivablePacket()
	{
	}
	
	private T _client;
	
	final void setClient(T client)
	{
		_client = client;
	}
	
	public final T getClient()
	{
		return _client;
	}
	
	/** Should be overridden. */
	protected int getMinimumLength()
	{
		return 0;
	}
	
	protected abstract boolean read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException;
	
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
			getClient().getSelectorThread().report(ErrorMode.FAILED_RUNNING, getClient(), (RP)this, e);
		}
		catch (RuntimeException e)
		{
			getClient().getSelectorThread().report(ErrorMode.FAILED_RUNNING, getClient(), (RP)this, e);
		}
	}
	
	protected abstract void runImpl() throws InvalidPacketException, RuntimeException;
	
	protected final void sendPacket(SP sp)
	{
		getClient().sendPacket(sp);
	}
}
