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
public class ClientProtocolVersionManager implements ProtocolVersionFactory<IClientProtocolVersion>
{
	private final List<ProtocolVersionFactory<IClientProtocolVersion>> _factories;
	
	private ClientProtocolVersionManager()
	{
		_factories = new CopyOnWriteArrayList<ProtocolVersionFactory<IClientProtocolVersion>>();
	}
	
	public void addFactory(ProtocolVersionFactory<IClientProtocolVersion> factory) {
		_factories.add(0, factory);
	}
	
	public void removeFactory(ProtocolVersionFactory<IClientProtocolVersion> factory) {
		_factories.remove(factory);
	}
	
	@Override
	public IClientProtocolVersion getByVersion(int version)
	{
		for (final ProtocolVersionFactory<IClientProtocolVersion> factory : _factories) {
			final IClientProtocolVersion cpv = factory.getByVersion(version);
			if (cpv != null)
				return cpv;
		}
		
		{
			// FALLBACK
			final ClientProtocolVersion cpv = ClientProtocolVersion.values()[ClientProtocolVersion.values().length - 1];
			return new UnknownClientProtocolVersion(version, cpv);
		}
	}
	
	public static ClientProtocolVersionManager getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder {
		private static final ClientProtocolVersionManager INSTANCE = new ClientProtocolVersionManager();
	}
}
