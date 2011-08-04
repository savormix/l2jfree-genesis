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
