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
package com.l2jfree.gameserver.network.client;

import java.nio.ByteBuffer;

import com.l2jfree.util.Rnd;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author savormix
 *
 */
public final class L2ClientSecurity
{
	private static final L2Logger _log = L2Logger.getLogger(L2ClientSecurity.class);
	
	private static final int KEY_LENGTH = 16;
	private static final int KEY_COUNT = 20;
	
	private final ByteBuffer[] _keys;
	
	private L2ClientSecurity()
	{
		_keys = new ByteBuffer[KEY_COUNT];
		
		for (int i = 0; i < KEY_COUNT; i++)
		{
			ByteBuffer buf = ByteBuffer.allocateDirect(KEY_LENGTH);
			buf.putLong(Rnd.get(Long.MIN_VALUE, Long.MAX_VALUE));
			buf.putLong(0xc8279301a16c3197L);
			buf.clear();
			getKeys()[i] = buf.asReadOnlyBuffer();
		}
		_log.info("Generated " + getKeys().length + " cipher keys (client).");
	}
	
	/**
	 * Returns a read-only cipher key.
	 * @return cipher key
	 */
	public ByteBuffer getKey()
	{
		return Rnd.get(getKeys());
	}
	
	private ByteBuffer[] getKeys()
	{
		return _keys;
	}
	
	/**
	 * Returns a singleton object.
	 * @return an instance of this class
	 */
	public static L2ClientSecurity getInstance()
	{
		return SingletonHolder._instance;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		static final L2ClientSecurity _instance = new L2ClientSecurity();
	}
}
