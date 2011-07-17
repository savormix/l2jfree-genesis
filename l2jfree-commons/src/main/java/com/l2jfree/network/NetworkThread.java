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
package com.l2jfree.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import org.apache.commons.io.IOUtils;

import com.l2jfree.security.NewCipher;
import com.l2jfree.util.HexUtil;
import com.l2jfree.util.logging.L2Logger;

// FIXME new login server/protocol
public abstract class NetworkThread extends Thread
{
	protected static final L2Logger _log = L2Logger.getLogger(NetworkThread.class);
	
	protected NetworkThread()
	{
	}
	
	protected NetworkThread(String name)
	{
		super(name);
	}
	
	private Socket _connection;
	private BufferedInputStream _in;
	private BufferedOutputStream _out;
	private String _connectionIp;
	/**
	 * The BlowFish engine used to encrypt packets.<br>
	 * <br>
	 * It is first initialized with a unified key: <quote>_;v.]05-31!|+-%xT!^[$\00</quote><br>
	 * and then after handshake, with a new key sent by server during the handshake.
	 */
	private NewCipher _blowfish;
	
	protected final void initConnection(Socket con) throws IOException
	{
		_connection = con;
		_connectionIp = con.getInetAddress().getHostAddress();
		
		_in = new BufferedInputStream(con.getInputStream());
		_out = new BufferedOutputStream(con.getOutputStream());
		
		_blowfish = new NewCipher("_;v.]05-31!|+-%xT!^[$\00");
	}
	
	protected final void setBlowfish(NewCipher blowfish)
	{
		_blowfish = blowfish;
	}
	
	protected final String getConnectionIp()
	{
		return _connectionIp;
	}
	
	protected final void sendPacket(SendableBasePacket sbp) throws IOException
	{
		byte[] data = sbp.getContent();
		NewCipher.appendChecksum(data);
		if (_log.isDebugEnabled())
			_log.debug("[S] " + sbp.getClass().getSimpleName() + ":\n" + HexUtil.printData(data));
		data = _blowfish.crypt(data);
		
		int len = data.length + 2;
		synchronized (_out) // avoids tow threads writing in the mean time
		{
			_out.write(len & 0xff);
			_out.write(len >> 8 & 0xff);
			_out.write(data);
			_out.flush();
		}
	}
	
	protected final void sendPacketQuietly(SendableBasePacket sbp)
	{
		try
		{
			sendPacket(sbp);
		}
		catch (IOException e)
		{
			_log.info("", e);
		}
	}
	
	protected final byte[] read()
	{
		int length = -1;
		try
		{
			final int lengthLo = _in.read();
			final int lengthHi = _in.read();
			
			length = lengthHi * 256 + lengthLo;
		}
		catch (SocketException e)
		{
			_log.info(getClass().getSimpleName() + ": the connection was terminated: " + e);
			return null;
		}
		catch (IOException e)
		{
			_log.warn(getClass().getSimpleName() + ": the connection was terminated.", e);
			return null;
		}
		
		if (length < 0)
		{
			_log.warn(getClass().getSimpleName() + ": the connection was terminated.");
			return null;
		}
		
		byte[] data = new byte[length - 2];
		
		for (int receivedBytes = 0; receivedBytes < data.length;)
		{
			int newBytes = -1;
			try
			{
				newBytes = _in.read(data, receivedBytes, data.length - receivedBytes);
			}
			catch (SocketException e)
			{
				_log.info(getClass().getSimpleName() + ": incomplete packet received, closing connection: " + e);
				return null;
			}
			catch (IOException e)
			{
				_log.warn(getClass().getSimpleName() + ": incomplete packet received, closing connection.", e);
				return null;
			}
			
			if (newBytes == -1)
			{
				_log.warn(getClass().getSimpleName() + ": incomplete packet received, closing connection.");
				return null;
			}
			
			receivedBytes += newBytes;
		}
		
		try
		{
			// decrypt if we have a key
			data = _blowfish.decrypt(data);
		}
		catch (IOException e)
		{
			_log.warn("", e);
			return null;
		}
		
		if (!NewCipher.verifyChecksum(data))
		{
			_log.warn(getClass().getSimpleName() + ": Incorrect packet checksum, ignoring packet, closing connection.");
			return null;
		}
		
		if (_log.isDebugEnabled())
			_log.debug("[C]\n" + HexUtil.printData(data));
		
		return data;
	}
	
	protected final void close()
	{
		IOUtils.closeQuietly(_in);
		IOUtils.closeQuietly(_out);
		
		try
		{
			if (_connection != null)
				_connection.close();
		}
		catch (IOException e)
		{
		}
		
		_in = null;
		_out = null;
		_connection = null;
	}
}
