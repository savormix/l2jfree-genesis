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

import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.handlers.admincommand.AdminCommandHandler;
import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.sendable.ActionFail.InteractionFinished;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix (generated)
 */
public abstract class RequestBypassToServer extends L2ClientPacket
{
	/**
	 * A nicer name for {@link RequestBypassToServer}.
	 * 
	 * @author savormix (generated)
	 * @see RequestBypassToServer
	 */
	public static final class RequestHtmlCommand extends RequestBypassToServer
	{
		// only for convenience
	}
	
	/** Packet's identifier */
	public static final int OPCODE = 0x23;
	
	@Override
	protected int getMinimumLength()
	{
		return READ_S;
	}
	
	/* Fields for storing read data */
	private String _command;
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		_command = buf.readS(); // Command
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		final L2Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;
		
		if (_command.startsWith("admin_"))
		{
			AdminCommandHandler.useAdminCommand(activeChar, _command);
		}
		else
		{
			// TODO
		}
		
		sendPacket(InteractionFinished.PACKET); // TODO it this right here?
	}
}
