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
package com.l2jfree.gameserver.network.client.packets.receivable.characterless;

import java.nio.BufferUnderflowException;

import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.network.client.L2ClientState;
import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.sendable.SSQInfoPacket.SkyColor;
import com.l2jfree.gameserver.network.client.packets.sendable.characterless.CharacterSelectedPacket.SelectedCharacterInfo;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * Sent when a character is selected and player clicks the 'Start' button.
 * 
 * @author hex1r0
 * @author savormix (generated)
 * @see SkyColor
 * @see SelectedCharacterInfo
 */
public abstract class CharacterSelect extends L2ClientPacket
{
	/**
	 * A nicer name for {@link CharacterSelect}.
	 * 
	 * @author savormix (generated)
	 * @see CharacterSelect
	 */
	public static final class RequestSelectCharacter extends CharacterSelect
	{
		// only for convenience
	}
	
	/** Packet's identifier */
	public static final int OPCODE = 0x12;
	
	@Override
	protected int getMinimumLength()
	{
		return READ_D + READ_H + READ_D + READ_D + READ_D;
	}
	
	/* Fields for storing read data */
	private int _charSlot;
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		_charSlot = buf.readD(); // Slot
		buf.readH();
		buf.readD();
		buf.readD();
		buf.readD();
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO: implement
		final L2Player player = getClient().loadCharacterBySlot(_charSlot);
		
		if (player == null)
		{
			_log.fatal(getClient() + ": player couldn't be loaded (slot:" + _charSlot + ")");
			getClient().closeNow();
			return;
		}
		
		// TODO
		boolean SERVER_GMONLY = false;
		
		if (player.getAccessLevel() < 0 || SERVER_GMONLY && !player.isGM())
		{
			getClient().closeNow();
			return;
		}
		
		// TODO
		
		player.setClient(getClient());
		player.addToWorld();
		
		getClient().setState(L2ClientState.LOGGED_IN);
		
		sendPacket(SkyColor.getForCharacterSelection());
		sendPacket(new SelectedCharacterInfo());
	}
	
	@Override
	protected boolean blockReadingUntilExecutionIsFinished()
	{
		return true;
	}
}
