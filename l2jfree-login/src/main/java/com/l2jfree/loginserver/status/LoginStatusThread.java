package com.l2jfree.loginserver.status;

import java.io.IOException;
import java.net.Socket;

import com.l2jfree.status.StatusThread;

/**
 * @author NB4L1
 */
public final class LoginStatusThread extends StatusThread
{
	public LoginStatusThread(LoginStatusServer server, Socket socket) throws IOException
	{
		super(server, socket);
	}
	
	@Override
	protected boolean login() throws IOException
	{
		return false;
	}
}
