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
 * Baseclass of {@link MMOController} associated threads. which are responsible for various tasks.
 * 
 * @author NB4L1
 */
abstract class WorkerThread<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>>
		extends Thread
{
	private final MMOController<T, RP, SP> _mmoController;
	
	private volatile boolean _shutdown;
	
	protected WorkerThread(MMOController<T, RP, SP> mmoController)
	{
		setName(mmoController.getName() + "-" + getClass().getCanonicalName());
		
		_mmoController = mmoController;
	}
	
	protected final MMOController<T, RP, SP> getMMOController()
	{
		return _mmoController;
	}
	
	@Override
	public abstract void run();
	
	public final void shutdown()
	{
		try
		{
			_shutdown = true;
			
			join();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
	
	final boolean isShuttingDown()
	{
		return _shutdown;
	}
}
