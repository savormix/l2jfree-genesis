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
import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.network.ClientProtocolVersion;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix (generated)
 */
public abstract class RequestMagicSkillList extends L2ClientPacket
{
	/**
	 * A nicer name for {@link RequestMagicSkillList}.
	 * 
	 * @author savormix (generated)
	 * @see RequestMagicSkillList
	 */
	public static final class ReportCharacterInfo extends RequestMagicSkillList
	{
		// only for convenience
	}
	
	/** Packet's identifier */
	public static final int OPCODE = 0x38;
	
	@Override
	protected int getMinimumLength()
	{
		return READ_C + READ_C + READ_C + READ_D + READ_D;
	}
	
	private int _characterId;
	private int _objectId;
	
	//private int _id;
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		final ClientProtocolVersion cpv = getClient().getVersion();
		if (cpv.isNewerThanOrEqualTo(ClientProtocolVersion.GODDESS_OF_DESTRUCTION))
		{
			buf.readC(); // ??? 0
			buf.readC(); // ??? 0
			buf.readC(); // ??? 0
		}
		_characterId = buf.readD(); // Character ID
		_objectId = buf.readD(); // Character OID
		if (cpv.isOlderThanOrEqualTo(ClientProtocolVersion.HIGH_FIVE_UPDATE_3))
			/* _id = */buf.readD(); // Unknown persistent ID
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO: implement
		L2Player activeChar = getClient().getActiveChar();
		if (_characterId != activeChar.getCharacterId().intValue() || _objectId != activeChar.getObjectId().intValue())
			getClient().closeNow();
	}
}
