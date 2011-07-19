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
package com.l2jfree.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import com.l2jfree.util.logging.L2Logger;

/**
 * 
 */
public final class TableOptimizer
{
	private static final L2Logger _log = L2Logger.getLogger(TableOptimizer.class);
	private static final String MYSQL = "mysql";
	private static final String POSTGRESQL = "postgresql";
	
	/**
	 * Optimizes database tables.
	 * @param jdbcUrl JDBC URL of the database
	 */
	public static void optimize(String jdbcUrl)
	{
		int off = jdbcUrl.indexOf(':') + 1;
		String provider = jdbcUrl.substring(off, jdbcUrl.indexOf(':', off)).toLowerCase();
		if (provider.equals(MYSQL))
			optimizeMySql();
		else if (provider.equals(POSTGRESQL))
			optimizePostgreSql();
		else
			_log.warn("TableOptimizer: Provider (" + provider + ") not supported.");
	}
	
	private static void optimizePostgreSql()
	{
		Connection con = null;
		try
		{
			con = L2Database.getConnection();
			Statement st = con.createStatement();
			_log.info("TableOptimizer: Vacuuming and building usage statistics...");
			st.executeUpdate("VACUUM ANALYZE");
			_log.info("TableOptimizer: Clustering...");
			st.executeUpdate("CLUSTER");
			_log.info("TableOptimizer: Database tables have been optimized.");
			st.close();
		}
		catch (Exception e)
		{
			_log.warn("TableOptimizer: Could not optimize database tables!", e);
		}
		finally
		{
			L2Database.close(con);
		}
	}
	
	private static void optimizeMySql()
	{
		Connection con = null;
		try
		{
			con = L2Database.getConnection();
			Statement st = con.createStatement();
			
			final ArrayList<String> tables = new ArrayList<String>();
			{
				ResultSet rs = st.executeQuery("SHOW FULL TABLES");
				while (rs.next())
				{
					String tableType = rs.getString(2/*"Table_type"*/);
					
					if (tableType.equals("VIEW"))
						continue;
					
					tables.add(rs.getString(1));
				}
				rs.close();
			}
			
			{
				ResultSet rs = st.executeQuery("CHECK TABLE " + StringUtils.join(tables, ","));
				while (rs.next())
				{
					String table = rs.getString("Table");
					String msgType = rs.getString("Msg_type");
					String msgText = rs.getString("Msg_text");
					
					if (msgType.equals("status"))
						if (msgText.equals("OK"))
							continue;
					
					_log.warn("TableOptimizer: CHECK TABLE " + table + ": " + msgType + " -> " + msgText);
				}
				rs.close();
				
				_log.info("TableOptimizer: Database tables have been checked.");
			}
			
			{
				ResultSet rs = st.executeQuery("ANALYZE TABLE " + StringUtils.join(tables, ","));
				while (rs.next())
				{
					String table = rs.getString("Table");
					String msgType = rs.getString("Msg_type");
					String msgText = rs.getString("Msg_text");
					
					if (msgType.equals("status"))
						if (msgText.equals("OK") || msgText.equals("Table is already up to date"))
							continue;
					
					_log.warn("TableOptimizer: ANALYZE TABLE " + table + ": " + msgType + " -> " + msgText);
				}
				rs.close();
				
				_log.info("TableOptimizer: Database tables have been analyzed.");
			}
			
			{
				ResultSet rs = st.executeQuery("OPTIMIZE TABLE " + StringUtils.join(tables, ","));
				while (rs.next())
				{
					String table = rs.getString("Table");
					String msgType = rs.getString("Msg_type");
					String msgText = rs.getString("Msg_text");
					
					if (msgType.equals("status"))
						if (msgText.equals("OK") || msgText.equals("Table is already up to date"))
							continue;
					
					if (msgType.equals("note"))
						if (msgText.equals("Table does not support optimize, doing recreate + analyze instead"))
							continue;
					
					_log.warn("TableOptimizer: OPTIMIZE TABLE " + table + ": " + msgType + " -> " + msgText);
				}
				rs.close();
				
				_log.info("TableOptimizer: Database tables have been optimized.");
			}
			st.close();
		}
		catch (Exception e)
		{
			_log.warn("TableOptimizer: Cannot optimize database tables!", e);
		}
		finally
		{
			L2Database.close(con);
		}
	}
}
