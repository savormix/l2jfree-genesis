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
public abstract class BypassUserCmd extends L2ClientPacket
{
	/**
	 * A nicer name for {@link BypassUserCmd}.
	 * 
	 * @author savormix (generated)
	 * @see BypassUserCmd
	 */
	public static final class RequestPlayerCommand extends BypassUserCmd
	{
		// only for convenience
	}
	
	/** Packet's identifier */
	public static final int OPCODE = 0xb3;
	
	@Override
	protected int getMinimumLength()
	{
		return READ_D;
	}
	
	/* Fields for storing read data */
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.readD(); // Command
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO: implement
		// 2011-09-14 verified on HF to be the default response to unsupported (on NA/EU) user commands
		// (you can re-verify with '/evangelist on')
		ChatMessage cm = new ChatMessage(Chat.SYSTEM, "SYS", "Lecture Not Support, Check Server");
		sendPacket(cm);
	}
}
