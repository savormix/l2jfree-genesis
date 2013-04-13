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
package com.l2jfree.loginserver.account;

import javolution.util.FastMap;

import com.l2jfree.loginserver.network.gameserver.legacy.L2LegacyGameServer;
import com.l2jfree.loginserver.network.gameserver.legacy.L2LegacyGameServerController;
import com.l2jfree.loginserver.network.gameserver.legacy.packets.sendable.RequestCharacters;

/**
 * @author savormix
 */
public class AccountCharacterManager
{
	private final FastMap<String, AccountCharacterInfo> _accounts;
	
	public AccountCharacterManager()
	{
		_accounts = FastMap.newInstance();
		_accounts.setShared(true);
	}
	
	public void updateAccount(String account, Integer server)
	{
		final L2LegacyGameServerController ctrl = L2LegacyGameServerController.getInstance();
		if (server != null)
		{
			final L2LegacyGameServer gs = ctrl.getById(server);
			if (gs != null)
				gs.sendPacket(new RequestCharacters(account));
			
			return;
		}
		
		if (_accounts.containsKey(account))
			return;
		
		for (final L2LegacyGameServer gs : ctrl.getAuthorized())
			gs.sendPacket(new RequestCharacters(account));
	}
	
	public void updateAccount(String account, int server, int characters, long[] delTime)
	{
		AccountCharacterInfo aci = getCharacterInfo(account);
		CharactersOnServer cos = aci.getCharacters().get(server);
		if (cos == null)
		{
			cos = new CharactersOnServer(characters, delTime);
			aci.getCharacters().put(server, cos);
		}
		else
			cos.update(characters, delTime);
	}
	
	public AccountCharacterInfo getCharacterInfo(String account)
	{
		AccountCharacterInfo aci = _accounts.get(account);
		if (aci == null)
		{
			aci = new AccountCharacterInfo();
			
			final AccountCharacterInfo old = _accounts.putIfAbsent(account, aci);
			if (old != null)
				aci = old;
		}
		return aci;
	}
	
	public static AccountCharacterManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder
	{
		private static final AccountCharacterManager INSTANCE = new AccountCharacterManager();
	}
}
