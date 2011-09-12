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

import com.l2jfree.gameserver.network.client.packets.receivable.ExGetOnAirShip;
import com.l2jfree.gameserver.network.client.packets.sendable.characterless.CharCreateOk.CharacterCreateSuccess;
import com.l2jfree.gameserver.network.client.packets.sendable.characterless.CharacterSelectionInfo.AvailableCharacters;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * Sent when a player cancels character creation or in response to {@link CharacterCreateSuccess}.
 * Alternate packet definition.
 * 
 * @author savormix
 * @see ExGetOnAirShip
 * @see AvailableCharacters
 */
public final class RequestAvailableCharacters extends ExGetOnAirShip
{
	/** Constructs this packet. */
	public RequestAvailableCharacters()
	{
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		// trigger packet
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO: implement
		AvailableCharacters.sendToClient(getClient());
	}
}
