package com.l2jfree.security;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.l2jfree.config.L2Properties;

public final class HexID
{
	/**
	 * Save hexadecimal ID of the server in the properties file.
	 * 
	 * @param string (String) : hexadecimal ID of the server to store
	 * @param fileName (String) : name of the properties file
	 */
	public static void saveHexid(String string, String fileName)
	{
		OutputStream out = null;
		try
		{
			out = new FileOutputStream(fileName);
			
			final L2Properties hexSetting = new L2Properties();
			hexSetting.setProperty("HexID", string);
			hexSetting.store(out, "the hexID to auth into login");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			IOUtils.closeQuietly(out);
		}
	}
	
	/**
	 * Save hexadecimal ID of the server in the properties file.
	 * 
	 * @param hexId (String) : hexadecimal ID of the server to store
	 * @param fileName (String) : name of the properties file
	 */
	public static void saveHexid(int serverId, String hexId, String fileName)
	{
		OutputStream out = null;
		try
		{
			out = new FileOutputStream(fileName);
			
			final L2Properties hexSetting = new L2Properties();
			hexSetting.setProperty("ServerID", String.valueOf(serverId));
			hexSetting.setProperty("HexID", hexId);
			hexSetting.store(out, "the hexID to auth into login");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			IOUtils.closeQuietly(out);
		}
	}
}
