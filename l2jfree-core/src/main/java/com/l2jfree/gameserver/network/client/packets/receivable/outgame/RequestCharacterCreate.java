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
package com.l2jfree.gameserver.network.client.packets.receivable.outgame;

import java.nio.BufferUnderflowException;
import java.util.regex.Pattern;

import com.l2jfree.gameserver.datatables.PlayerNameTable;
import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.network.client.packets.L2ClientPacket;
import com.l2jfree.gameserver.network.client.packets.sendable.characterless.CharCreateFail.CharacterCreateFailure;
import com.l2jfree.gameserver.network.client.packets.sendable.characterless.CharCreateOk.CharacterCreateSuccess;
import com.l2jfree.gameserver.templates.player.ClassId;
import com.l2jfree.gameserver.templates.player.ClassLevel;
import com.l2jfree.gameserver.templates.player.Gender;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * This packet is sent after user customized character and pressed [Create]
 * 
 * @author hex1r0
 */
public class RequestCharacterCreate extends L2ClientPacket
{
	public static final int OPCODE = 0x0c;
	
	private String _name;
	//private int				_race;
	private byte _sex;
	private int _classId;
	/*
	private int _int;
	private int _str;
	private int _con;
	private int _men;
	private int _dex;
	private int _wit;
	*/
	private byte _hairStyle;
	private byte _hairColor;
	private byte _face;
	
	@Override
	protected int getMinimumLength()
	{
		return READ_S + READ_D + READ_D + READ_D + READ_D + READ_D + READ_D + READ_D + READ_D + READ_D + READ_D
				+ READ_D + READ_D;
	}
	
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException, RuntimeException
	{
		_name = buf.readS();
		/*_race  = */buf.readD();
		_sex = (byte)buf.readD();
		_classId = buf.readD();
		/*_int   = */buf.readD();
		/*_str   = */buf.readD();
		/*_con   = */buf.readD();
		/*_men   = */buf.readD();
		/*_dex   = */buf.readD();
		/*_wit   = */buf.readD();
		_hairStyle = (byte)buf.readD();
		_hairColor = (byte)buf.readD();
		_face = (byte)buf.readD();
	}
	
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		if (getClient().getActiveChar() != null)
			return;
		
		// TODO 
		//CharacterCreateFail
		
		final ClassId classId = ClassId.VALUES.valueOf(_classId);
		final int MAX_CHARACTERS_NUMBER_PER_ACCOUNT = 3; // TODO
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
			sendPacket(CharacterCreateFail.REASON_INCORRECT_NAME);
			return;
		}
		*/
		
		// atomic check + creation
		synchronized (RequestCharacterCreate.class)
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
					L2Player.create(_name, account, classId, Gender.VALUES.valueOf(_sex), _face, _hairColor, _hairStyle);
			
			player.addToWorld();
			
			// TODO
			player.getPosition().setXYZ(-71338, 258271, -3104);
			
			player.removeFromWorld();
		}
		
		sendPacket(CharacterCreateSuccess.PACKET);
	}
}
