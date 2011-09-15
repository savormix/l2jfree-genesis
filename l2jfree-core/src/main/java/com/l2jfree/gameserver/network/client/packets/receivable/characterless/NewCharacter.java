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
import java.util.regex.Pattern;

import com.l2jfree.gameserver.datatables.PlayerNameTable;
import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.sendable.characterless.CharCreateFail.CharacterCreateFailure;
import com.l2jfree.gameserver.network.client.packets.sendable.characterless.CharCreateOk.CharacterCreateSuccess;
import com.l2jfree.gameserver.sql.PlayerDB;
import com.l2jfree.gameserver.templates.player.ClassId;
import com.l2jfree.gameserver.templates.player.ClassLevel;
import com.l2jfree.gameserver.templates.player.Gender;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * Sent when a player fills in the character creation form and clicks the 'Create' button.
 * 
 * @author hex1r0
 * @author NB4L1
 * @author savormix (generated)
 * @see CharacterCreateFailure
 * @see CharacterCreateSuccess
 */
public abstract class NewCharacter extends L2ClientPacket
{
	/**
	 * A nicer name for {@link NewCharacter}.
	 * 
	 * @author savormix (generated)
	 * @see NewCharacter
	 */
	public static final class RequestNewCharacter extends NewCharacter
	{
		// only for convenience
	}
	
	/** Packet's identifier */
	public static final int OPCODE = 0x0c;
	
	@Override
	protected int getMinimumLength()
	{
		return READ_S + READ_D + READ_D + READ_D + READ_D + READ_D + READ_D + READ_D + READ_D + READ_D + READ_D
				+ READ_D + READ_D;
	}
	
	/* Fields for storing read data */
	private String _name;
	private int _sex;
	private int _classId;
	private byte _hairStyle;
	private byte _hairColor;
	private byte _face;
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		_name = buf.readS(); // Name
		buf.readD(); // Race
		_sex = buf.readD(); // Sex
		_classId = buf.readD(); // Class
		buf.readD(); // INT
		buf.readD(); // STR
		buf.readD(); // CON
		buf.readD(); // MEN
		buf.readD(); // DEX
		buf.readD(); // WIT
		_hairStyle = (byte)buf.readD(); // Hair style
		_hairColor = (byte)buf.readD(); // Hair color
		_face = (byte)buf.readD(); // Face
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO: implement
		final ClassId classId = ClassId.VALUES.valueOf(_classId);
		final int MAX_CHARACTERS_NUMBER_PER_ACCOUNT = 7; // TODO
		final String account = getClient().getAccountName();
		final Pattern CNAME_PATTERN = Pattern.compile("[A-Za-z0-9-]{2,16}");
		
		if (classId == null || classId.getLevel() != ClassLevel.First)
		{
			sendPacket(CharacterCreateFailure.UNSPECIFIED);
			return;
		}
		else if (MAX_CHARACTERS_NUMBER_PER_ACCOUNT > 0
				&& MAX_CHARACTERS_NUMBER_PER_ACCOUNT <= PlayerNameTable.getInstance().getPlayerCountForAccount(account))
		{
			if (_log.isDebugEnabled())
				_log.debug("Max number of characters reached. Creation failed.");
			sendPacket(CharacterCreateFailure.LIMIT_REACHED);
			return;
		}
		else if (!CNAME_PATTERN.matcher(_name).matches())
		{
			if (_log.isDebugEnabled())
				_log.debug("charname: " + _name + " is invalid. creation failed.");
			sendPacket(CharacterCreateFailure.TITLE_LENGTH);
			return;
		}
		/*
		else if (NpcTable.getInstance().getTemplateByName(_name) != null || obsceneCheck(_name))
		{
			if (_log.isDebugEnabled())
				_log.debug("charname: " + _name + " overlaps with a NPC. creation failed.");
			sendPacket(CharacterCreateFailure.NAME_RESERVED);
			return;
		}
		*/
		
		// atomic check + creation
		synchronized (getClass())
		{
			if (PlayerNameTable.getInstance().isPlayerNameTaken(_name))
			{
				if (_log.isDebugEnabled())
					_log.debug("charname: " + _name + " already exists. creation failed.");
				sendPacket(CharacterCreateFailure.NAME_EXISTS);
				return;
			}
			
			if (_log.isDebugEnabled())
				_log.debug("charname: " + _name + " classId: " + _classId);
			
			final L2Player player =
					PlayerDB.create(_name, account, classId, Gender.VALUES.valueOf(_sex), _face, _hairColor, _hairStyle);
			
			player.addToWorld();
			
			// TODO
			player.getPosition().setXYZ(-71338, 258271, -3104);
			
			player.removeFromWorld();
		}
		
		sendPacket(CharacterCreateSuccess.PACKET);
	}
}
