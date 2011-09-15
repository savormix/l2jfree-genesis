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
package com.l2jfree.gameserver.network.client.packets.sendable;

import com.l2jfree.gameserver.gameobjects.L2Character;
import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.gameserver.util.ObjectId;
import com.l2jfree.network.mmocore.MMOBuffer;
import com.l2jfree.util.EnumValues;

/**
 * @author savormix (generated)
 */
public abstract class CreatureSay extends L2ServerPacket
{
	/**
	 * A nicer name for {@link CreatureSay}.
	 * 
	 * @author savormix (generated)
	 * @see CreatureSay
	 */
	public static final class ChatMessage extends CreatureSay
	{
		/**
		 * Constructs a very simple instance of this packet.
		 * 
		 * @param chat Chat type
		 * @param talker Talker name
		 * @param message Chat message
		 * @see CreatureSay#CreatureSay(Chat, String, String)
		 */
		public ChatMessage(Chat chat, String talker, String message)
		{
			super(chat, talker, message);
		}
		
		/**
		 * Constructs an instance of this packet.
		 * 
		 * @param talker Talker
		 * @param chat Chat type
		 * @param message Chat message
		 */
		public ChatMessage(L2Character talker, Chat chat, String message)
		{
			super(talker.getObjectId(), chat, -1, talker.getName(), -1, message);
		}
		
		/**
		 * Constructs an instance of this packet.
		 * 
		 * @param talker Talker
		 * @param recipient Recipient
		 * @param message Chat message
		 */
		public ChatMessage(L2Character talker, String recipient, String message)
		{
			super(talker.getObjectId(), Chat.PRIVATE, -1, recipient, -1, message);
		}
	}
	
	private final ObjectId _talkerOid;
	private final Chat _chat;
	private final int _sysStringTalker;
	private final String _talker;
	private final int _fStringMessage;
	private final String _message;
	
	/**
	 * Constructs a very simple instance of this packet.
	 * 
	 * @param chat Chat type
	 * @param talker Talker name
	 * @param message Chat message
	 */
	public CreatureSay(Chat chat, String talker, String message)
	{
		this(null, chat, -1, talker, -1, message);
	}
	
	/**
	 * Constructs an instance of this packet.
	 * 
	 * @param talker Talker
	 * @param chat Chat type
	 * @param message Chat message
	 */
	public CreatureSay(L2Character talker, Chat chat, String message)
	{
		this(talker.getObjectId(), chat, -1, talker.getName(), -1, message);
	}
	
	/**
	 * Constructs an instance of this packet.
	 * 
	 * @param talker Talker
	 * @param recipient Recipient
	 * @param message Chat message
	 */
	public CreatureSay(L2Character talker, String recipient, String message)
	{
		this(talker.getObjectId(), Chat.PRIVATE, -1, recipient, -1, message);
	}
	
	private CreatureSay(ObjectId talkerOid, Chat chat, int sysStringTalker, String talker, int fStringMessage,
			String message)
	{
		_talkerOid = talkerOid;
		_chat = chat;
		_sysStringTalker = sysStringTalker;
		_talker = talker;
		_fStringMessage = fStringMessage;
		_message = message;
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x4a;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeD(_talkerOid); // Talker OID
		buf.writeD(_chat); // Chat, branching condition
		// branch with FerryShout
		if (_chat == Chat.FERRY)
		{
			buf.writeD(_sysStringTalker); // Talker
		}
		// branch with DefaultChat
		else
		{
			if (_chat == Chat.PRIVATE && activeChar.getObjectId().equals(_talkerOid))
				buf.append("->");
			buf.writeS(_talker); // Talker
		}
		buf.writeD(_fStringMessage); // Message, branching condition
		// branch with Negative
		if (_fStringMessage < 0)
		{
			buf.writeS(_message); // Message
		}
	}
	
	public enum Chat
	{
		LOCAL,
		SHOUT,
		PRIVATE,
		PARTY,
		CLAN,
		SYSTEM, // like local
		PETITIONER,
		CONSULTANT,
		TRADE,
		ALLIANCE,
		ANNOUNCEMENT,
		FERRY, // like shout, but talker is D instead of S
		FRIEND,
		MSN,
		PARTY_ROOM,
		COMMANDER,
		COMMAND_CHANNEL,
		HERO,
		CRITICAL_ANNOUNCEMENT,
		SCREEN_ANNOUNCEMENT,
		TERRITORY,
		MULTI_PARTY,
		NPC_LOCAL, // like local, using fstring
		NPC_SHOUT; // like shout, using fstring
		
		public static final EnumValues<Chat> VALUES = new EnumValues<Chat>(Chat.class) {
			@Override
			protected Chat defaultValue()
			{
				return LOCAL; // for every non-zero value
			}
		};
	}
}
