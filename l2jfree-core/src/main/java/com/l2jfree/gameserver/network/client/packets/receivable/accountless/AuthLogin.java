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

import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.L2ClientState;
import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.sendable.characterless.CharacterSelectionInfo.AvailableCharacters;
import com.l2jfree.gameserver.network.client.packets.sendable.characterless.VersionCheck.ProtocolAnswer;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * Sent if server sends a positive {@link ProtocolAnswer}.<BR>
 * <BR>
 * Contains the intended account name long with special authorization data obtained from a login
 * server.
 * 
 * @author savormix
 * @see AvailableCharacters
 */
@SuppressWarnings("unused")
public abstract class AuthLogin extends L2ClientPacket
{
	/**
	 * A nicer name for {@link AuthLogin}.
	 * 
	 * @author savormix (generated)
	 * @see AuthLogin
	 */
	public static final class RequestAuthorization extends AuthLogin
	{
		// only for convenience
	}
	
	/** Packet's identifier */
	public static final int OPCODE = 0x2b;
	
	@Override
	protected int getMinimumLength()
	{
		// account name must not be an empty string
		return (READ_S * 2) + READ_D + READ_D + READ_D + READ_D // FIXME: in which chronicle were these introduced?
				+ READ_D + READ_D + READ_Q;
	}
	
	/* Fields for storing read data */
	private String _account;
	private int _accountId1;
	private int _currentKey;
	private int _accountId2;
	private int _previousKey;
	private int _unk1;
	private int _bitsInBlock;
	private long _unk2;
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		_account = buf.readS().trim().toLowerCase(); // Account name
		_accountId1 = buf.readD(); // Account ID??
		_currentKey = buf.readD(); // Session key (game)
		_accountId2 = buf.readD(); // Account ID??
		_previousKey = buf.readD(); // Session key (login)
		_unk1 = buf.readD(); // 1
		_bitsInBlock = buf.readD(); // 512
		_unk2 = buf.readQ(); // 0
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO: implement
		//if (_accountId1 != _accountId2 || _unk1 != 1 || _bitsInBlock & 7 != 0 || _unk2 != 0)
		
		final L2Client client = getClient();
		
		// TODO: validate session key and authorization
		
		client.setAccountName(_account);
		client.setSessionId(_currentKey);
		//client.setBitsInBlock(_bitsInBlock);
		client.setState(L2ClientState.CHARACTER_MANAGEMENT);
		//client.closeNow();
		
		AvailableCharacters.sendToClient(client);
	}
	
	@Override
	protected boolean blockReadingUntilExecutionIsFinished()
	{
		return true;
	}
}
