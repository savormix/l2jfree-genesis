package com.l2jfree.gameserver.config;

import com.l2jfree.L2Config.ConfigPropertiesLoader;
import com.l2jfree.config.annotation.ConfigClass;
import com.l2jfree.config.annotation.ConfigField;

/**
 * @author NB4L1
 */
@ConfigClass(folderName = "config", fileName = "network")
public final class NetworkConfig extends ConfigPropertiesLoader
{
	@ConfigField(name = "ListenIP", value = "0.0.0.0", eternal = true, comment = {
			"Login Server will accept CLIENT connections coming to this IP address only.", //
			"Use 0.0.0.0 to listen on all available adapters.", //
			"Specify a valid IP address if you require the game server to bind on a single IP.", //
	})
	public static String NET_LISTEN_IP;
	
	@ConfigField(name = "ListenPort", value = "7777", eternal = true, comment = { "Login Server will listen for CLIENT connections on this port." })
	public static int NET_LISTEN_PORT;
}
