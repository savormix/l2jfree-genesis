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
import com.l2jfree.config.annotation.ConfigGroupBeginning;
import com.l2jfree.config.annotation.ConfigGroupEnding;

/**
 * @author NB4L1
 */
@ConfigClass(folderName = "config", fileName = "service")
public final class ServiceConfig extends ConfigPropertiesLoader
{
	@ConfigField(name = "ForceLegacyMode", value = "false", eternal = true, comment = {
			"This server will behave as a traditional L2J login server.", //
			"All additional features will be disabled/inaccessible.", //
	})
	public static boolean SVC_FORCE_LEGACY;
	
	@ConfigGroupBeginning(name = "CLIENT")
	@ConfigField(name = "CheckGameGuard", value = "false", eternal = true, comment = {
			"Whether to do a basic check if a client is using GameGuard.", //
			"Clients that do not pass this check are guaranteed to have GameGuard disabled and will", //
			"not be allowed to log in.", //
			
	})
	public static boolean SVC_CHECK_GAMEGUARD;
	
	@ConfigField(name = "ShowEULA", value = "false", eternal = true, comment = {
			"Whether to show NCSoft's EULA before showing the game server list.", //
			"It is a bad idea to show it, as it must be accepted to continue.", //
			"Implementation notice:", //
			"A session key is exchanged in the background.", //
			
	})
	@ConfigGroupEnding(name = "CLIENT")
	public static boolean SVC_SHOW_EULA;
	
	@ConfigGroupBeginning(name = "GAME SERVER")
	@ConfigField(name = "StrictAuthorization", value = "true", eternal = true, comment = {
			"Only authorizes servers that supply a valid ID & HexID combination.", //
			"All game servers must be registered in advance.", //
			"If disabled, all game server requests will be served (as untrusted).", //
			"If a requested ID is already registered or in use, another ID will be assigned.", //
			
	})
	public static boolean SVC_STRICT_AUTHORIZATION;
	
	@ConfigField(name = "PersistentRequests", value = "false", eternal = true, comment = {
			"Only effective when strict authorization is disabled.", //
			"If a game server requests an ID that is not in use, it will be registered on that ID", //
			"with the supplied HexID (as untrusted).", //
	})
	@ConfigGroupEnding(name = "GAME SERVER")
	public static boolean SVC_SAVE_REQUESTS;
}
