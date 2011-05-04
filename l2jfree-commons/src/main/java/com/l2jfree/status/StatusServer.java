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
package com.l2jfree.status;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javolution.util.FastList;
import javolution.util.FastMap;

import com.l2jfree.L2Config;
import com.l2jfree.config.L2Properties;
import com.l2jfree.util.L2FastSet;
import com.l2jfree.util.Rnd;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author NB4L1
 */
public abstract class StatusServer extends Thread
{
	public interface Filter
	{
		public boolean accept(String host);
	}
	
	private static final class FloodFilter implements Filter
	{
		private final Map<String, Long> _connectionTimes = new FastMap<String, Long>();
		
		@Override
		public boolean accept(String host)
		{
			final Long lastConnectionTime = _connectionTimes.put(host, System.currentTimeMillis());
			
			return lastConnectionTime == null || lastConnectionTime < System.currentTimeMillis() - 1000;
		}
	}
	
	private static final class HostFilter implements Filter
	{
		@Override
		public boolean accept(String host)
		{
			for (String hostAddress : L2Config.getAllowedTelnetHostAddresses())
				if (host.equals(hostAddress))
					return true;
			
			return false;
		}
	}
	
	protected static final L2Logger _log = L2Logger.getLogger(StatusServer.class);
	
	private final ServerSocket _socket;
	private final List<Filter> _filters = new FastList<Filter>();
	private final Set<StatusThread> _threads = new L2FastSet<StatusThread>().setShared(true);
	
	protected StatusServer() throws IOException
	{
		_socket = new ServerSocket(Integer.parseInt(new L2Properties(L2Config.TELNET_FILE).getProperty("StatusPort")));
		
		addFilter(new FloodFilter());
		addFilter(new HostFilter());
		
		setPriority(Thread.MAX_PRIORITY);
		setDaemon(true);
		start();
		
		_log.info("Telnet: Listening on port: " + getServerSocket().getLocalPort());
	}
	
	protected final void addFilter(Filter filter)
	{
		_filters.add(filter);
	}
	
	protected final ServerSocket getServerSocket()
	{
		return _socket;
	}
	
	@Override
	public final void run()
	{
		try
		{
			main_loop: while (!_socket.isClosed())
			{
				try
				{
					final Socket socket = _socket.accept();
					final String host = socket.getInetAddress().getHostAddress();
					
					for (Filter filter : _filters)
					{
						if (!filter.accept(host))
						{
							_log.warn("Telnet: Connection attempt from " + host + " rejected.");
							continue main_loop;
						}
					}
					
					_log.warn("Telnet: Connection attempt from " + host + " accepted.");
					
					newStatusThread(socket).start();
				}
				catch (IOException e)
				{
					_log.warn("", e);
				}
			}
		}
		finally
		{
			close();
		}
	}
	
	protected final void close()
	{
		try
		{
			_socket.close();
		}
		catch (IOException e)
		{
		}
	}
	
	protected abstract StatusThread newStatusThread(Socket socket) throws IOException;
	
	public final void addStatusThread(StatusThread thread)
	{
		_threads.add(thread);
	}
	
	public final void removeStatusThread(StatusThread thread)
	{
		_threads.remove(thread);
	}
	
	public final Set<StatusThread> getStatusThreads()
	{
		return _threads;
	}
	
	public final void broadcast(String message)
	{
		for (StatusThread thread : getStatusThreads())
			thread.println(message);
	}
	
	protected final String generateRandomPassword(int length)
	{
		final String chars = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM0123456789";
		
		final StringBuilder sb = new StringBuilder(length);
		
		for (int i = 0; i < length; i++)
			sb.append(chars.charAt(Rnd.get(chars.length())));
		
		return sb.toString();
	}
}
