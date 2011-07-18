package com.l2jfree.gameserver.status;

import java.io.IOException;
import java.net.Socket;

import com.l2jfree.status.StatusServer;
import com.l2jfree.status.StatusThread;

/**
 * @author NB4L1
 */
public final class GameStatusServer extends StatusServer
{
	private static GameStatusServer _instance;
	
	public static void initInstance() throws IOException
	{
		if (_instance == null)
			_instance = new GameStatusServer();
	}
	
	public static void tryBroadcast(String message)
	{
		if (_instance != null)
			_instance.broadcast(message);
	}
	
	private GameStatusServer() throws IOException
	{
	}
	
	@Override
	protected StatusThread newStatusThread(Socket socket) throws IOException
	{
		return new GameStatusThread(this, socket);
	}
}
