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

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.l2jfree.gameserver.CoreInfo;
import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.network.ClientProtocolVersion;
import com.l2jfree.network.ProtocolVersionFactory;
import com.l2jfree.network.ProtocolVersionManager;
import com.l2jfree.network.mmocore.MMOConfig;
import com.l2jfree.network.mmocore.MMOController;

/**
 * Manages incoming L2 client connections.
 * 
 * @author savormix
 */
public final class L2ClientController extends MMOController<L2Client, L2ClientPacket, L2ServerPacket> implements
		ProtocolVersionFactory<ClientProtocolVersion>
{
	private static final class SingletonHolder
	{
		static
		{
			final MMOConfig cfg = new MMOConfig(L2ClientController.class.getSimpleName());
			cfg.setSelectorSleepTime(7);
			
			try
			{
				ProtocolVersionManager.getInstance().addGameFactory(INSTANCE = new L2ClientController(cfg));
			}
			catch (IOException e)
			{
				throw new Error(e);
			}
		}
		
		public static final L2ClientController INSTANCE;
	}
	
	/**
	 * Returns a singleton object.
	 * 
	 * @return an instance of this class
	 */
	public static L2ClientController getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private final Map<Integer, ClientProtocolVersion> _versions;
	
	private L2ClientController(MMOConfig config) throws IOException
	{
		//super(config, L2ClientPacketHandler.getInstance());
		super(config, L2ExperimentalPacketHandler.getInstance());
		
		L2ClientSecurity.getInstance();
		
		{
			final Map<Integer, ClientProtocolVersion> versions = new HashMap<Integer, ClientProtocolVersion>();
			for (final ClientProtocolVersion cpv : ClientProtocolVersion.values())
				versions.put(cpv.getVersion(), cpv);
			_versions = Collections.unmodifiableMap(versions);
		}
	}
	
	@Override
	protected L2Client createClient(SocketChannel socketChannel) throws ClosedChannelException
	{
		L2Client client = new L2Client(this, socketChannel);
		L2ClientNetPing.getInstance().startTask(client);
		return client;
	}
	
	@Override
	protected String getVersionInfo()
	{
		return super.getVersionInfo() + " - " + CoreInfo.getVersionInfo();
	}
	
	@Override
	public ClientProtocolVersion getByVersion(int version)
	{
		final ClientProtocolVersion cpv = _versions.get(version);
		if (cpv != null)
			return cpv;
		
		final ClientProtocolVersion[] all = ClientProtocolVersion.values();
		return all[all.length - 1];
	}
}
