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
			"Game Server will accept CLIENT connections coming to this IP address only.", //
			"Use 0.0.0.0 to listen on all available adapters.", //
			"Specify a valid IP address if you require the game server to bind on a single IP.", //
	})
	public static String NET_LISTEN_IP;
	
	@ConfigField(name = "ListenPort", value = "7777", eternal = true, comment = { "Game Server will listen for CLIENT connections on this port." })
	public static int NET_LISTEN_PORT;
}
