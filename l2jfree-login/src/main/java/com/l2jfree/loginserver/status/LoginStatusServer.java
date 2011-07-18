package com.l2jfree.loginserver.status;

import java.io.IOException;
import java.net.Socket;

import com.l2jfree.status.StatusServer;
import com.l2jfree.status.StatusThread;

/**
 * @author NB4L1
 */
public final class LoginStatusServer extends StatusServer
{
	private static LoginStatusServer _instance;
	
	public static void initInstance() throws IOException
	{
		if (_instance == null)
			_instance = new LoginStatusServer();
	}
	
	public static void tryBroadcast(String message)
	{
		if (_instance != null)
			_instance.broadcast(message);
	}
	
	private LoginStatusServer() throws IOException
	{
	}
	
	@Override
	protected StatusThread newStatusThread(Socket socket) throws IOException
	{
		return new LoginStatusThread(this, socket);
	}
}
