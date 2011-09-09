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
package com.l2jfree.gameserver.network.client.packets.receivable;

import java.nio.BufferUnderflowException;

import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.L2ClientState;
import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.sendable.AvailableCharacters;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * Client sends this packet in response to a compatible <TT>ProtocolAnswer</TT>. <BR>
 * NOTE: original name was <TT>AuthLogin</TT>
 * 
 * @author savormix
 */
@SuppressWarnings("unused")
public final class RequestAuthorization extends L2ClientPacket
{
	/** Packet's identifier */
	public static final int OPCODE = 0x2b;
	
	private String _accountName;
	private int _accountId1;
	private int _currentKey;
	private int _accountId2;
	private int _previousKey;
	private int _unk1;
	private int _bitsInBlock;
	private long _unk2;
	
	@Override
	protected int getMinimumLength()
	{
		return (READ_S * 2) + READ_D + READ_D + READ_D + READ_D // FIXME in which chronicle were these introduced?
				+ READ_D + READ_D + READ_Q;
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		_accountName = buf.readS().trim().toLowerCase();
		_accountId1 = buf.readD();
		_currentKey = buf.readD();
		_accountId2 = buf.readD();
		_previousKey = buf.readD();
		
		// FIXME in which chronicle were these introduced?
		_unk1 = buf.readD();
		_bitsInBlock = buf.readD();
		_unk2 = buf.readQ();
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		//if (_accountId1 != _accountId2 || _unk1 != 1 || _bitsInBlock & 7 != 0 || _unk2 != 0)
		
		final L2Client client = getClient();
		
		// TODO validate session key
		
		client.setAccountName(_accountName);
		//client.setBitsInBlock(_bitsInBlock);
		client.setState(L2ClientState.CHARACTER_MANAGEMENT);
		//client.closeNow();
		
		// FIXME
		sendPacket(new AvailableCharacters(client));
	}
}
