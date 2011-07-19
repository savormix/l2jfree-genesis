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
package com.l2jfree.loginserver.network.legacy.packets.receivable;

import java.nio.BufferUnderflowException;

import com.l2jfree.loginserver.network.legacy.packets.L2GameServerPacket;
import com.l2jfree.network.mmocore.InvalidPacketException;
import com.l2jfree.network.mmocore.MMOBuffer;
import com.l2jfree.util.HexUtil;

/**
 * @author savormix
 *
 */
public final class GameServerAuth extends L2GameServerPacket
{
	private int _desiredId;
	private boolean _acceptAlternateId;
	private boolean _reservedHost;
	private int _port;
	private int _maxPlayers;
	private byte[] _hexId;
	private String[] _hosts;
	
	/* (non-Javadoc)
	 * @see com.l2jfree.network.mmocore.ReceivablePacket#getMinimumLength()
	 */
	@Override
	protected int getMinimumLength()
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see com.l2jfree.network.mmocore.ReceivablePacket#read(com.l2jfree.network.mmocore.MMOBuffer)
	 */
	@Override
	protected void read(MMOBuffer buf) throws BufferUnderflowException,
			RuntimeException
	{
		_desiredId = buf.readC();
		_acceptAlternateId = (buf.readC() == 0 ? false : true);
		_reservedHost = (buf.readC() == 0 ? false : true);
		_port = buf.readH();
		_maxPlayers = buf.readD();
		int size = buf.readD();
		_hexId = buf.readB(new byte[size]);
		size = 2 * buf.readD();
		_hosts = new String[size];
		for (int i = 0; i < size; i++)
			_hosts[i] = buf.readS();
	}
	
	/* (non-Javadoc)
	 * @see com.l2jfree.network.mmocore.ReceivablePacket#runImpl()
	 */
	@Override
	protected void runImpl() throws InvalidPacketException, RuntimeException
	{
		// TODO Auto-generated method stub
		String hexId = HexUtil.hexToString(_hexId);
	}
}
