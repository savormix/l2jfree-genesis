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
package com.l2jfree.loginserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.commons.io.IOUtils;

import com.l2jfree.util.Rnd;
import com.l2jfree.util.logging.L2Logger;

/**
 * The purpose of this class is classified.
 * <BR><BR>
 * Pun intended.
 * @author savormix
 */
public final class L2LoginIdentifier
{
	private static final L2Logger _log = L2Logger.getLogger(L2LoginIdentifier.class);
	private static final String FILENAME = ".l2jfree-ls";
	
	private long _uid;
	private boolean _loaded;
	
	private L2LoginIdentifier()
	{
		_uid = 0;
		_loaded = false;
	}
	
	/**
	 * Returns the unique identifier for this login server.
	 * @return unique identifier
	 */
	public long getUID()
	{
		if (!isLoaded())
			load();
		return _uid;
	}
	
	private synchronized void load()
	{
		if (isLoaded())
			return;
		
		File f = new File(System.getProperty("user.home", null), FILENAME);
		ByteBuffer bb = ByteBuffer.allocateDirect(8);
		
		if (!f.exists() || f.length() != 8)
		{
			_uid = getRandomUID();
			_loaded = true;
			_log.info("A new UID has been generated for this login server.");
			
			FileOutputStream fos = null;
			try
			{
				f.createNewFile();
				fos = new FileOutputStream(f);
				FileChannel fc = fos.getChannel();
				bb.putLong(getUID());
				bb.flip();
				fc.write(bb);
				fos.flush();
			}
			catch (IOException e)
			{
				_log.warn("Could not store login server's UID!", e);
			}
			finally
			{
				IOUtils.closeQuietly(fos);
				f.setReadOnly();
			}
		}
		else
		{
			FileInputStream fis = null;
			try
			{
				fis = new FileInputStream(f);
				FileChannel fc = fis.getChannel();
				fc.read(bb);
			}
			catch (IOException e)
			{
				_log.warn("Could not read stored login server's UID!", e);
			}
			finally
			{
				IOUtils.closeQuietly(fis);
			}
			
			if (bb.position() > 0)
			{
				bb.flip();
				_uid = bb.getLong();
			}
			else
				_uid = getRandomUID();
			_loaded = true;
		}
	}
	
	private long getRandomUID()
	{
		return Rnd.get(Long.MIN_VALUE, Long.MAX_VALUE);
	}
	
	private boolean isLoaded()
	{
		return _loaded;
	}
	
	/**
	 * Returns a singleton object.
	 * @return an instance of this class
	 */
	public static L2LoginIdentifier getInstance()
	{
		return SingletonHolder._instance;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		static final L2LoginIdentifier _instance = new L2LoginIdentifier();
	}
}
