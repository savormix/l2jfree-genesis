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

import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * Original name was <TT>CharSelectionInfo</TT>
 * 
 * @author hex1r0
 */
public class AvailableCharacters extends L2ServerPacket
{
	private final String _accountName;
	private final int _sessionId;
	
	public AvailableCharacters(L2Client client)
	{
		_sessionId = 0; // TODO
		_accountName = client.getAccountName();
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x09;
	}
	
	@Override
	protected void writeImpl(L2Client client, MMOBuffer buf) throws RuntimeException
	{
		buf.writeD(0x01); // TODO num of chars
		buf.writeD(0x07);
		buf.writeC(0);
		
		buf.writeS("TestChar"); // TODO
		buf.writeD(Integer.MAX_VALUE); // TODO
		buf.writeS(_accountName);
		buf.writeD(_sessionId);
		buf.writeD(0); // TODO clan id
		buf.writeD(0); // ??
		
		buf.writeD(0); // TODO sex
		buf.writeD(0); // TODO race
		
		buf.writeD(0); // TODO class is
		
		buf.writeD(0x01); // active ?? (no difference between 0 and 1)
		
		buf.writeD(0); // TODO x
		buf.writeD(0); // TODO y
		buf.writeD(0); // TODO z
		
		buf.writeF(100.0); // TODO hp cur
		buf.writeF(100.0); // TODO mp cur
		
		buf.writeD(50); // TODO SP
		buf.writeQ(50); // TODO EXP
		buf.writeF(0.5); // TODO EXP % HF
		buf.writeD(5); // TODO Level 
		
		buf.writeD(5); // TODO karma
		buf.writeD(15); // TODO pk
		buf.writeD(15); // TODO PVP
		
		for (int k = 0; k < 7; k++)
			buf.writeD(0x00);
		
		for (int slot = 0; slot < 26; slot++)
			buf.writeD(0); // TODO PaperdollSlots
		
		buf.writeD(0); // TODO hair style
		buf.writeD(0); // TODO hair color
		buf.writeD(0); // TODO face
		
		buf.writeF(200.0); // TODO hp max
		buf.writeF(200.0); // TODO mp max
		
		buf.writeD(0x00); // TODO delete days left before
		buf.writeD(0); // TODO class id
		buf.writeD(1); // TODO active
		buf.writeC(0); // TODO enchant effect
		buf.writeD(0); // TODO augmentation
		
		buf.writeD(0); // TODO transformation
		
		buf.writeD(0x00);
		buf.writeD(0x00);
		buf.writeD(0x00);
		buf.writeD(0x00);
		buf.writeF(0x00);
		buf.writeF(0x00);
		
		buf.writeD(0); // vitallity points HF
	}
}
