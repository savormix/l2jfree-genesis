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
package com.l2jfree.loginserver.network.client;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

import javolution.util.FastMap;

import com.l2jfree.loginserver.LoginInfo;
import com.l2jfree.loginserver.network.client.L2ClientSecurity.SessionKey;
import com.l2jfree.loginserver.network.client.packets.L2ClientPacket;
import com.l2jfree.loginserver.network.client.packets.L2ServerPacket;
import com.l2jfree.loginserver.network.client.packets.sendable.Init;
import com.l2jfree.network.mmocore.MMOConfig;
import com.l2jfree.network.mmocore.MMOController;

/**
 * @author savormix
 */
public final class L2ClientController extends MMOController<L2Client, L2ClientPacket, L2ServerPacket>
{
	private static final int PROTOCOL_VERSION = 0xc621;
	
	private static final class SingletonHolder
	{
		static
		{
			final MMOConfig cfg = new MMOConfig("Experimental Login");
			cfg.setSelectorSleepTime(40);
			cfg.setThreadCount(Math.min(2, Runtime.getRuntime().availableProcessors()));
			
			try
			{
				INSTANCE = new L2ClientController(cfg);
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
	
	private final FastMap<String, SessionKey> _authorized;
	
	private L2ClientController(MMOConfig config) throws IOException
	{
		super(config, L2ClientPacketHandler.getInstance());
		
		_authorized = FastMap.newInstance();
		_authorized.setShared(true);
	}
	
	/**
	 * Authorizes an account as logged in via this login server.
	 * 
	 * @param client connection
	 */
	public void authorize(L2Client client)
	{
		if (client == null)
			return;
		L2Account acc = client.getAccount();
		SessionKey sk = client.getSessionKey();
		if (acc != null && sk != null)
			getAuthorized().put(acc.getAccount(), sk);
	}
	
	/**
	 * Determines whether the given account has logged in via this login server.
	 * 
	 * @param account account name
	 * @param activeKey session key
	 * @param oldKey previous session key
	 * @return is login valid
	 */
	public boolean isAuthorized(String account, long activeKey, long oldKey)
	{
		if (account == null)
			return false;
		SessionKey sk = getAuthorized().remove(account);
		if (sk == null)
			return false;
		else
			return (sk.getActiveKey() == activeKey && sk.getOldKey() == oldKey);
	}
	
	@Override
	protected L2Client createClient(SocketChannel socketChannel) throws ClosedChannelException
	{
		L2Client client = new L2Client(this, socketChannel, PROTOCOL_VERSION);
		client.sendPacket(new Init(client));
		return client;
	}
	
	private FastMap<String, SessionKey> getAuthorized()
	{
		return _authorized;
	}
	
	@Override
	protected String getVersionInfo()
	{
		return super.getVersionInfo() + " - " + LoginInfo.getVersionInfo();
	}
}
