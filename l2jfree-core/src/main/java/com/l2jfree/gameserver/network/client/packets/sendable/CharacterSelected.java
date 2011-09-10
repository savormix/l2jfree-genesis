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

import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.gameobjects.ObjectPosition;
import com.l2jfree.gameserver.gameobjects.player.PlayerAppearance;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.gameserver.templates.L2PlayerTemplate;
import com.l2jfree.gameserver.templates.player.PlayerBaseTemplate;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author hex1r0
 */
public class CharacterSelected extends L2ServerPacket
{
	private final int _sessionId;
	
	public CharacterSelected(int sessionId)
	{
		_sessionId = sessionId;
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x0b;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		if (activeChar == null)
			return;
		
		final PlayerAppearance appearance = activeChar.getAppearance();
		final ObjectPosition position = activeChar.getPosition();
		final L2PlayerTemplate template = activeChar.getTemplate();
		final PlayerBaseTemplate baseTemplate = template.getPlayerBaseTemplate(appearance.getGender());
		
		buf.writeS(activeChar.getName());
		buf.writeD(activeChar.getObjectId()); // ??
		buf.writeS("title-test"); // TODO title
		buf.writeD(_sessionId);
		buf.writeD(0/*_activeChar.getClanId()*/); // TODO clan id
		buf.writeD(0x00); // ??
		buf.writeD(appearance.getGender().ordinal());
		buf.writeD(baseTemplate.getRace().ordinal());
		buf.writeD(template.getClassId().getId());
		buf.writeD(0x01); // active ??
		buf.writeD(position.getX());
		buf.writeD(position.getY());
		buf.writeD(position.getZ());
		
		buf.writeF(100/*_activeChar.getCurrentHp()*/); // TODO HP
		buf.writeF(100/*_activeChar.getCurrentMp()*/); // TODO MP
		buf.writeD(10/*activeChar.getSp()*/);
		buf.writeQ(50/*activeChar.getExp()*/);
		buf.writeD(5/*activeChar.getLevel()*/);
		buf.writeD(1/*activeChar.getKarma()*/);
		buf.writeD(3/*activeChar.getPkKills()*/);
		buf.writeD(5/*activeChar.getINT()*/);
		buf.writeD(6/*activeChar.getSTR()*/);
		buf.writeD(3/*activeChar.getCON()*/);
		buf.writeD(7/*activeChar.getMEN()*/);
		buf.writeD(2/*activeChar.getDEX()*/);
		buf.writeD(8/*activeChar.getWIT()*/);
		
		buf.writeD(/*GameTimeController.getInstance().getGameTime() % (24 * 60)*/0); // "reset" on 24th hour
		buf.writeD(0x00);
		
		buf.writeD(template.getClassId().getId());
		
		buf.writeD(0x00);
		buf.writeD(0x00);
		buf.writeD(0x00);
		buf.writeD(0x00);
		
		buf.writeB(new byte[64]);
		buf.writeD(0x00);
	}
	
}
