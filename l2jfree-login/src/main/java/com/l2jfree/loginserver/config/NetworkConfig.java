package com.l2jfree.loginserver.config;

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
			"Specify a valid IP address if you require the login server to bind on a single IP.", //
	})
	public static String NET_LISTEN_IP;
	
	@ConfigField(name = "ListenPort", value = "2106", eternal = true, comment = { "Login Server will listen for CLIENT connections on this port." })
	public static int NET_LISTEN_PORT;
	
	@ConfigField(name = "EnableLegacyListener", value = "true", eternal = true, comment = {
			"Whether to listen for old or non-l2jfree game servers.", //
			"Disabling this will have no effect if legacy mode is forced.", //
	})
	public static boolean NET_ENABLE_LEGACY;
	
	@ConfigField(name = "LegacyListenIP", value = "127.0.0.1", eternal = true, comment = { "Login Server will accept LEGACY GAME SERVER connections coming to this IP address only." })
	public static String NET_LEGACY_LISTEN_IP;
	
	@ConfigField(name = "LegacyListenPort", value = "9014", eternal = true, comment = { "Login Server will listen for LEGACY GAME SERVER connections on this port." })
	public static int NET_LEGACY_LISTEN_PORT;
}
