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
package com.l2jfree.gameserver.network.client;

import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.util.concurrent.L2ThreadPool;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author NB4L1
 */
public final class Disconnection
{
	private static final L2Logger _log = L2Logger.getLogger(Disconnection.class);
	
	private final L2Client _client;
	private final L2Player _activeChar;
	
	public Disconnection(L2Client client)
	{
		this(client, null);
	}
	
	public Disconnection(L2Player activeChar)
	{
		this(null, activeChar);
	}
	
	public Disconnection(L2Client client, L2Player activeChar)
	{
		// 
		if (client != null)
			_client = client;
		else if (activeChar != null)
			_client = activeChar.getClient() instanceof L2Client ? (L2Client)activeChar.getClient() : null;
		else
			_client = null;
		
		// 
		if (activeChar != null)
			_activeChar = activeChar;
		else if (client != null)
			_activeChar = client.getActiveChar();
		else
			_activeChar = null;
		
		// 
		if (_activeChar != null)
			_activeChar.setClient(null);
	}
	
	public Disconnection store()
	{
		try
		{
			if (_activeChar != null)
			{
				// FIXME
				//_activeChar.store();
			}
		}
		catch (RuntimeException e)
		{
			_log.warn("", e);
		}
		
		return this;
	}
	
	public Disconnection storeAndRemoveFromWorld()
	{
		try
		{
			if (_activeChar != null)
				_activeChar.removeFromWorld();
		}
		catch (RuntimeException e)
		{
			_log.warn("", e);
		}
		
		return this;
	}
	
	public Disconnection close(boolean toLoginScreen)
	{
		if (_client != null)
			_client.close(toLoginScreen);
		
		return this;
	}
	
	public void defaultSequence(boolean toLoginScreen)
	{
		close(toLoginScreen).storeAndRemoveFromWorld();
	}
	
	public void onDisconnection()
	{
		if (_activeChar != null)
		{
			L2ThreadPool.schedule(new Runnable() {
				@Override
				public void run()
				{
					storeAndRemoveFromWorld();
				}
			}, /* TODO */0);
		}
	}
}
