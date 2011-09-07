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

import com.l2jfree.ClientProtocolVersion;
import com.l2jfree.L2Config.ConfigPropertiesLoader;
import com.l2jfree.Shutdown;
import com.l2jfree.TerminationStatus;
import com.l2jfree.config.L2Properties;
import com.l2jfree.config.annotation.ConfigClass;
import com.l2jfree.config.annotation.ConfigField;
import com.l2jfree.gameserver.DatapackVersion;

/**
 * @author NB4L1
 */
@ConfigClass(folderName = "config", fileName = "version", comment = { "WARNING!", // 
		"Be aware to don't set client versions lower than required version determined by the datapack version!" })
public final class VersionConfig extends ConfigPropertiesLoader
{
	@ConfigField(name = "MinSupportedClientProtocolVersion", value = "FREYA", eternal = true, comment = { })
	public static ClientProtocolVersion MIN_SUPPORTED_CLIENT_PROTOCOL_VERSION;
	
	@ConfigField(name = "MaxSupportedClientProtocolVersion", value = "FREYA", eternal = true, comment = { })
	public static ClientProtocolVersion MAX_SUPPORTED_CLIENT_PROTOCOL_VERSION;
	
	@ConfigField(name = "DatapackVersion", value = "FREYA", eternal = true, comment = { })
	public static DatapackVersion DATAPACK_VERSION;
	
	@Override
	protected void loadImpl(L2Properties properties)
	{
		if (MIN_SUPPORTED_CLIENT_PROTOCOL_VERSION.ordinal() > MAX_SUPPORTED_CLIENT_PROTOCOL_VERSION.ordinal())
		{
			System.err.println("Min supported client protocol version " + MIN_SUPPORTED_CLIENT_PROTOCOL_VERSION
					+ " isn't lower than max " + MAX_SUPPORTED_CLIENT_PROTOCOL_VERSION + "!");
			
			Shutdown.exit(TerminationStatus.RUNTIME_INVALID_CONFIGURATION);
		}
		
		if (MIN_SUPPORTED_CLIENT_PROTOCOL_VERSION.ordinal() < DATAPACK_VERSION
				.getRequiredMinimumClientProtocolVersion().ordinal())
		{
			System.err.println("Too low min supported client protocol version " + MIN_SUPPORTED_CLIENT_PROTOCOL_VERSION
					+ " given for specified datapack version " + DATAPACK_VERSION + "!");
			
			Shutdown.exit(TerminationStatus.RUNTIME_INVALID_CONFIGURATION);
		}
		
		if (MAX_SUPPORTED_CLIENT_PROTOCOL_VERSION.ordinal() < DATAPACK_VERSION
				.getRequiredMinimumClientProtocolVersion().ordinal())
		{
			System.err.println("Too low max supported client protocol version " + MAX_SUPPORTED_CLIENT_PROTOCOL_VERSION
					+ " given for specified datapack version " + DATAPACK_VERSION + "!");
			
			Shutdown.exit(TerminationStatus.RUNTIME_INVALID_CONFIGURATION);
		}
	}
}
