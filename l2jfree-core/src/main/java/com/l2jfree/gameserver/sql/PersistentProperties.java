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
package com.l2jfree.gameserver.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.l2jfree.config.L2Properties;
import com.l2jfree.sql.L2Database;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author NB4L1
 */
public final class PersistentProperties
{
	private static final L2Logger _log = L2Logger.getLogger(PersistentProperties.class);
	
	private static final Map<String, L2Properties> _propertiesByClassNames = new HashMap<String, L2Properties>();
	
	static
	{
		Connection con = null;
		try
		{
			con = L2Database.getConnection();
			
			final Statement st = con.createStatement();
			final ResultSet rs = st.executeQuery("SELECT * FROM persistent_properties");
			
			while (rs.next())
			{
				final String className = rs.getString("className");
				final String propertyName = rs.getString("propertyName");
				final String propertyValue = rs.getString("propertyValue");
				
				getProperties(className).setProperty(propertyName, propertyValue);
			}
			
			rs.close();
			st.close();
		}
		catch (SQLException e)
		{
			_log.warn("", e);
		}
		finally
		{
			L2Database.close(con);
		}
		
		int classCount = 0;
		int propertyCount = 0;
		for (L2Properties properties : _propertiesByClassNames.values())
		{
			classCount++;
			propertyCount += properties.size();
		}
		
		_log.info("PersistentProperties: " + propertyCount + " properties loaded for " + classCount + " classes.");
	}
	
	public synchronized static L2Properties getProperties(Class<?> clazz)
	{
		return getProperties(clazz.getName());
	}
	
	public synchronized static L2Properties getProperties(String className)
	{
		L2Properties map = _propertiesByClassNames.get(className);
		
		if (map == null)
			_propertiesByClassNames.put(className, map = new L2Properties());
		
		return map;
	}
	
	public interface StoreListener
	{
		public void update();
	}
	
	private static final Set<StoreListener> _storeListeners = new HashSet<StoreListener>();
	
	public synchronized static void addStoreListener(StoreListener listener)
	{
		_storeListeners.add(listener);
	}
	
	public synchronized static void store()
	{
		for (StoreListener listener : _storeListeners)
			listener.update();
		
		Connection con = null;
		try
		{
			con = L2Database.getConnection();
			
			{
				final Statement st = con.createStatement();
				st.executeUpdate("TRUNCATE TABLE persistent_properties");
				st.close();
			}
			
			{
				final PreparedStatement ps =
						con.prepareStatement("INSERT INTO persistent_properties "
								+ "(className, propertyName, propertyValue) VALUES (?,?,?)");
				
				for (Map.Entry<String, L2Properties> entry1 : _propertiesByClassNames.entrySet())
				{
					final String className = entry1.getKey();
					final L2Properties properties = entry1.getValue();
					
					for (Map.Entry<String, String> entry2 : properties.entrySet())
					{
						final String propertyName = entry2.getKey();
						final String propertyValue = entry2.getValue();
						
						ps.setString(1, className);
						ps.setString(2, propertyName);
						ps.setString(3, propertyValue);
						ps.executeUpdate();
					}
				}
				
				ps.close();
			}
		}
		catch (SQLException e)
		{
			_log.warn("", e);
		}
		finally
		{
			L2Database.close(con);
		}
	}
}
