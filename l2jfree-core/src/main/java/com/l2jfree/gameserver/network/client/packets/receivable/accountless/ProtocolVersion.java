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
package com.l2jfree.gameserver.network.client.packets.receivable.accountless;

import java.nio.BufferUnderflowException;

import com.l2jfree.ClientProtocolVersion;
import com.l2jfree.gameserver.config.VersionConfig;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.L2ClientState;
import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.sendable.characterless.VersionCheck.ProtocolAnswer;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;
import com.l2jfree.util.Rnd;

/**
 * Sent by the client immediately after creating a connection.
 * 
 * @author savormix
 * @see ProtocolAnswer
 */
public class ProtocolVersion extends L2ClientPacket
{
	/** Packet's identifier */
	public static final int OPCODE = 0x0e;
	
	@Override
	protected int getMinimumLength()
	{
		return READ_D;
	}
	
	/* Fields for storing read data */
	private int _version;
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		_version = buf.readD(); // Protocol version
		buf.skipAll();
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO: implement
		if (_log.isDebugEnabled())
			_log.debug("Client protocol version: " + _version);
		
		final L2Client client = getClient();
		final ClientProtocolVersion version = ClientProtocolVersion.getByVersion(_version);
		
		if (!VersionConfig.isSupported(version))
		{
			client.close(ProtocolAnswer.INCOMPATIBLE);
			return;
		}
		
		final int seed = Rnd.nextInt();
		client.getDeobfuscator().init(seed);
		client.setState(L2ClientState.PROTOCOL_OK);
		
		sendPacket(new ProtocolAnswer(client.getCipherKey(), seed));
	}
	
	@Override
	protected boolean blockReadingUntilExecutionIsFinished()
	{
		return true;
	}
}
