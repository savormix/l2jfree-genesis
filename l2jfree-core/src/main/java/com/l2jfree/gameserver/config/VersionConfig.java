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

import org.apache.commons.lang3.ArrayUtils;

import com.l2jfree.L2Config.ConfigPropertiesLoader;
import com.l2jfree.Shutdown;
import com.l2jfree.TerminationStatus;
import com.l2jfree.config.L2Properties;
import com.l2jfree.config.annotation.ConfigClass;
import com.l2jfree.config.annotation.ConfigField;
import com.l2jfree.gameserver.DatapackVersion;
import com.l2jfree.network.ClientProtocolVersion;
import com.l2jfree.network.IClientProtocolVersion;
import com.l2jfree.util.ArrayBunch;

/**
 * @author NB4L1
 */
@ConfigClass(folderName = "config", fileName = "version", comment = { "WARNING!", //
		"Be aware to don't set client versions not supported by the datapack version!" })
public final class VersionConfig extends ConfigPropertiesLoader
{
	@ConfigField(name = "DatapackVersion", value = "FREYA", eternal = true, comment = { })
	public static DatapackVersion DATAPACK_VERSION;
	
	@ConfigField(name = "SupportedClientProtocolVersions", value = "FREYA", eternal = true, comment = { })
	public static ClientProtocolVersion[] SUPPORTED_CLIENT_PROTOCOL_VERSIONS;
	
	@Override
	protected void loadImpl(L2Properties properties)
	{
		if (!DATAPACK_VERSION.isEnabled())
		{
			System.err.println("Configured datapack version (" + DATAPACK_VERSION + ") is disabled!");
			
			Shutdown.exit(TerminationStatus.RUNTIME_INVALID_CONFIGURATION);
		}
		
		final ArrayBunch<ClientProtocolVersion> tmp = new ArrayBunch<ClientProtocolVersion>();
		
		for (ClientProtocolVersion cpv : SUPPORTED_CLIENT_PROTOCOL_VERSIONS)
		{
			if (!cpv.isEnabled())
				System.err.println("Configured client protocol version (" + cpv + ") is disabled!");
			else if (!ArrayUtils.contains(DATAPACK_VERSION.getSupportedClientProtocolVersions(), cpv))
				System.err.println("Configured client protocol version (" + cpv + ") isn't supported by the datapack!");
			else
				tmp.add(cpv);
		}
		
		if (tmp.size() == 0)
		{
			System.err.println("No supported client protocol version found!");
			
			Shutdown.exit(TerminationStatus.RUNTIME_INVALID_CONFIGURATION);
		}
		
		SUPPORTED_CLIENT_PROTOCOL_VERSIONS = tmp.moveToArray(ClientProtocolVersion.class);
	}
	
	public static boolean isSupported(IClientProtocolVersion version)
	{
		if (version == null)
			return false;
		
		return ArrayUtils.contains(SUPPORTED_CLIENT_PROTOCOL_VERSIONS, version);
	}
}
