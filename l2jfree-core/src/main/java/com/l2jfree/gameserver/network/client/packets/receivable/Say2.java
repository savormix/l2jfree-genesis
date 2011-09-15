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

import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.sendable.CreatureSay.Chat;
import com.l2jfree.gameserver.network.client.packets.sendable.CreatureSay.ChatMessage;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix (generated)
 */
public abstract class Say2 extends L2ClientPacket
{
	/**
	 * A nicer name for {@link Say2}.
	 * 
	 * @author savormix (generated)
	 * @see Say2
	 */
	public static final class RequestSendChatMessage extends Say2
	{
		// only for convenience
	}
	
	/** Packet's identifier */
	public static final int OPCODE = 0x49;
	
	@Override
	protected int getMinimumLength()
	{
		return READ_S + READ_D;
	}
	
	private String _message;
	private Chat _chat;
	private String _recipient;
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		_message = buf.readS(); // Message
		_chat = Chat.VALUES.valueOf(buf.readD()); // Chat, branching condition
		// branch with PrivateMessage
		if (_chat == Chat.PRIVATE)
		{
			_recipient = buf.readS(); // Recipient
		}
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO: implement
		final ChatMessage cm;
		if (_recipient == null)
			cm = new ChatMessage(getClient().getActiveChar(), _chat, _message);
		else
			cm = new ChatMessage(getClient().getActiveChar(), _recipient, _message);
		sendPacket(cm); // only the sender will see this chat :P
	}
}
