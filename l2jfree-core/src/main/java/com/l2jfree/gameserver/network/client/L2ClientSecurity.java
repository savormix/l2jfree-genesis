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

import com.l2jfree.util.RescheduleableTask;
import com.l2jfree.util.Rnd;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author savormix
 */
public final class L2ClientSecurity
{
	private static final L2Logger _log = L2Logger.getLogger(L2ClientSecurity.class);
	
	private static final int KEY_LENGTH = 16;
	private static final int KEY_COUNT = 20;
	
	private byte[][] _keys;
	
	private L2ClientSecurity()
	{
		new Updater();
		_log.info("Generated " + getKeys().length + " cipher keys (client).");
	}
	
	/**
	 * Returns a read-only cipher key.
	 * 
	 * @return cipher key
	 */
	public byte[] getKey()
	{
		return Rnd.get(getKeys());
	}
	
	private byte[][] getKeys()
	{
		return _keys;
	}
	
	/**
	 * Returns a singleton object.
	 * 
	 * @return an instance of this class
	 */
	public static L2ClientSecurity getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder
	{
		public static final L2ClientSecurity INSTANCE = new L2ClientSecurity();
	}
	
	/**
	 * Periodically generates new keys to replace the old ones.
	 * 
	 * @author NB4L1
	 */
	private final class Updater extends RescheduleableTask
	{
		@Override
		protected void runImpl()
		{
			final int count = Rnd.get(KEY_COUNT / 2, KEY_COUNT * 3 / 2); // so clients will never know if they have all of them or not
			final byte[][] result = new byte[count][KEY_LENGTH];
			
			for (int i = 0; i < count; i++)
			{
				final ByteBuffer buf = ByteBuffer.wrap(result[i]);
				
				buf.putLong(Rnd.get(Long.MIN_VALUE, Long.MAX_VALUE));
				buf.putLong(0xc8279301a16c3197L);
			}
			
			_keys = result;
		}
		
		@Override
		protected long getScheduleDelay()
		{
			return _keys.length * 60 * 1000;
		}
	}
}
