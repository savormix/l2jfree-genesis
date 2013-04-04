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
 * Manages Lineage II client protocol version factories.
 * 
 * @author savormix
 */
public class GameProtocolVersionManager implements ProtocolVersionFactory<IGameProtocolVersion>
{
	private final List<ProtocolVersionFactory<IGameProtocolVersion>> _factories;
	private final UnknownGameProtocolVersion _fallback;
	
	private GameProtocolVersionManager()
	{
		_factories = new CopyOnWriteArrayList<ProtocolVersionFactory<IGameProtocolVersion>>();
		
		_fallback = new UnknownGameProtocolVersion(-1, getLatestFromEnum());
	}
	
	public void addFactory(ProtocolVersionFactory<IGameProtocolVersion> factory)
	{
		_factories.add(0, factory);
	}
	
	public void removeFactory(ProtocolVersionFactory<IGameProtocolVersion> factory)
	{
		_factories.remove(factory);
	}
	
	@Override
	public IGameProtocolVersion getByVersion(int version)
	{
		for (final ProtocolVersionFactory<IGameProtocolVersion> factory : _factories)
		{
			final IGameProtocolVersion cpv = factory.getByVersion(version);
			if (cpv != null)
				return cpv;
		}
		
		return getFallbackVersion(version);
	}
	
	public static IGameProtocolVersion getFallbackVersion(int version)
	{
		return new UnknownGameProtocolVersion(version, getLatestFromEnum());
	}
	
	public IGameProtocolVersion getFallbackVersion()
	{
		return _fallback;
	}
	
	private static IGameProtocolVersion getLatestFromEnum()
	{
		return ClientProtocolVersion.values()[ClientProtocolVersion.values().length - 1];
	}
	
	public static GameProtocolVersionManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder
	{
		private static final GameProtocolVersionManager INSTANCE = new GameProtocolVersionManager();
	}
}
