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
 * Configuration that is reported to the login server and then reported to players.
 * 
 * @author savormix
 */
@ConfigClass(folderName = "config", fileName = "reported")
public final class ReportedConfig extends ConfigPropertiesLoader
{
	@ConfigField(name = "ServerID", value = "1", eternal = true, comment = { "Server's name shown in the server list depends on this ID." })
	public static int ID;
	
	// TODO: when reloading, notify login servers
	@ConfigField(name = "AgeRestriction", value = "0", comment = { "Minimum allowed player age." })
	public static int AGE_RESTRICTION;
	
	@ConfigField(name = "AllowPvP", value = "true", comment = { "Whether PvP/PK is allowed in this server.",
			"If disabled, players will not be allowed to engage in PvP."
	// TODO: what about sieges, TW, cleft, arenas?
	})
	public static boolean PVP;
	
	@ConfigField(name = "MaxPlayers", value = "100", comment = { "Maximum number of players allowed to connect to this game server simultaneously." })
	public static int MAX_ONLINE;
	
	@ConfigField(name = "Types", value = "0", comment = { "Server type bitmask, some of them show icons next to the server, like relax=clock, free=F, etc." })
	public static int TYPES;
	
	@ConfigField(name = "Brackets", value = "false", comment = { "Whether to show brackets before the server's name." })
	public static boolean BRACKETS;
}
