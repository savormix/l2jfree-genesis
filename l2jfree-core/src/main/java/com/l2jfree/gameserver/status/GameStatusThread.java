package com.l2jfree.gameserver.status;

import java.io.IOException;
import java.net.Socket;

import com.l2jfree.status.StatusThread;

/**
 * @author NB4L1
 */
public final class GameStatusThread extends StatusThread
{
	public GameStatusThread(GameStatusServer server, Socket socket) throws IOException
	{
		super(server, socket);
	}
	
	@Override
	protected boolean login() throws IOException
	{
		return false;
	}
}
