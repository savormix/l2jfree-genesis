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
import com.l2jfree.gameserver.network.client.packets.sendable.CharacterCreateSuccess;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
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
		// TODO Auto-generated method stub
		return 0;
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
		// TODO 
		//CharacterCreateFail
		sendPacket(CharacterCreateSuccess.STATIC_PACKET);
		sendActionFailed();
	}
	
}
