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
package com.l2jfree.gameserver.util;

import java.io.File;
import java.io.IOException;

import com.l2jfree.gameserver.config.VersionConfig;

/**
 * @author NB4L1
 */
public final class Datapack
{
	private static final File DATAPACK_ROOT;
	
	// TODO
	static
	{
		try
		{
			if (new File("./data").exists() && !new File("../l2jfree-datapack/data").exists())
				DATAPACK_ROOT = new File(".").getCanonicalFile();
			
			// L2Config.LAUNCHED_FROM_IDE
			else if (!new File("./data").exists() && new File("../l2jfree-datapack/data").exists())
				DATAPACK_ROOT = new File("../l2jfree-datapack").getCanonicalFile();
			
			else
				throw new Error("Unable to discover proper datapack root!");
		}
		catch (IOException e)
		{
			throw new Error(e);
		}
	}
	
	private Datapack()
	{
		// utility class
	}
	
	public static File getDataFolder()
	{
		switch (VersionConfig.DATAPACK_VERSION)
		{
			case FREYA:
				return new File(DATAPACK_ROOT, "data");
			case HIGH_FIVE:
			default:
				throw new IllegalStateException(VersionConfig.DATAPACK_VERSION + " isn't supported yet!");
		}
	}
	
	public static File getDatapackFile(String relativePathToDataFolder)
	{
		return new File(getDataFolder(), relativePathToDataFolder);
	}
}
