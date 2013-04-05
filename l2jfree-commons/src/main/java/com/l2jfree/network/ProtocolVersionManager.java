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
package com.l2jfree.network;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages Lineage II protocol version factories.
 * 
 * @author savormix
 */
public class ProtocolVersionManager
{
	private final List<ProtocolVersionFactory<ILoginProtocolVersion>> _factoriesLogin;
	private final List<ProtocolVersionFactory<IGameProtocolVersion>> _factoriesGame;
	private final UnknownLoginProtocolVersion _fallbackLogin;
	private final UnknownGameProtocolVersion _fallbackGame;
	
	private ProtocolVersionManager()
	{
		_factoriesLogin = new CopyOnWriteArrayList<ProtocolVersionFactory<ILoginProtocolVersion>>();
		_factoriesGame = new CopyOnWriteArrayList<ProtocolVersionFactory<IGameProtocolVersion>>();
		
		_fallbackLogin = new UnknownLoginProtocolVersion(-1, getLatestLogin());
		_fallbackGame = new UnknownGameProtocolVersion(-1, getLatestGame());
	}
	
	public void addLoginFactory(ProtocolVersionFactory<ILoginProtocolVersion> factory)
	{
		_factoriesLogin.add(0, factory);
	}
	
	public void addGameFactory(ProtocolVersionFactory<IGameProtocolVersion> factory)
	{
		_factoriesGame.add(0, factory);
	}
	
	public void removeLoginFactory(ProtocolVersionFactory<ILoginProtocolVersion> factory)
	{
		_factoriesLogin.remove(factory);
	}
	
	public void removeGameFactory(ProtocolVersionFactory<IGameProtocolVersion> factory)
	{
		_factoriesGame.remove(factory);
	}
	
	public ILoginProtocolVersion getLoginProtocol(int version)
	{
		for (final ProtocolVersionFactory<ILoginProtocolVersion> factory : _factoriesLogin)
		{
			final ILoginProtocolVersion cpv = factory.getByVersion(version);
			if (cpv != null)
				return cpv;
		}
		
		return getFallbackVersionLogin(version);
	}
	
	public IGameProtocolVersion getGameProtocol(int version)
	{
		for (final ProtocolVersionFactory<IGameProtocolVersion> factory : _factoriesGame)
		{
			final IGameProtocolVersion cpv = factory.getByVersion(version);
			if (cpv != null)
				return cpv;
		}
		
		return getFallbackVersionGame(version);
	}
	
	private static ILoginProtocolVersion getFallbackVersionLogin(int version)
	{
		// TODO: bad idea
		return new UnknownLoginProtocolVersion(version, getLatestLogin());
	}
	
	private static IGameProtocolVersion getFallbackVersionGame(int version)
	{
		// TODO: bad idea
		return new UnknownGameProtocolVersion(version, getLatestGame());
	}
	
	public ILoginProtocolVersion getFallbackProtocolLogin()
	{
		return _fallbackLogin;
	}
	
	public IGameProtocolVersion getFallbackProtocolGame()
	{
		return _fallbackGame;
	}
	
	private static ILoginProtocolVersion getLatestLogin()
	{
		return LoginProtocolVersion.values()[LoginProtocolVersion.values().length - 1];
	}
	
	private static IGameProtocolVersion getLatestGame()
	{
		return ClientProtocolVersion.values()[ClientProtocolVersion.values().length - 1];
	}
	
	public static ProtocolVersionManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder
	{
		private static final ProtocolVersionManager INSTANCE = new ProtocolVersionManager();
	}
}
