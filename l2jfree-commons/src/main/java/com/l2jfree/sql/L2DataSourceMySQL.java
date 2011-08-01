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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author NB4L1
 */
public final class L2DataSourceMySQL extends L2DataSource
{
	protected L2DataSourceMySQL(String name, ComboPooledDataSource dataSource)
	{
		super(name, dataSource);
	}
	
	@Override
	public void optimize() throws SQLException
	{
		Connection con = null;
		try
		{
			con = getConnection();
			
			final Statement st = con.createStatement();
			
			final ArrayList<String> tables = new ArrayList<String>();
			{
				final ResultSet rs = st.executeQuery("SHOW FULL TABLES");
				
				while (rs.next())
				{
					final String tableType = rs.getString(2/*"Table_type"*/);
					
					if (tableType.equals("VIEW"))
						continue;
					
					tables.add(rs.getString(1));
				}
				
				rs.close();
			}
			
			{
				final ResultSet rs = st.executeQuery("CHECK TABLE " + StringUtils.join(tables, ","));
				
				while (rs.next())
				{
					final String table = rs.getString("Table");
					final String msgType = rs.getString("Msg_type");
					final String msgText = rs.getString("Msg_text");
					
					if (msgType.equals("status"))
						if (msgText.equals("OK"))
							continue;
					
					_log.warn("TableOptimizer: CHECK TABLE " + table + ": " + msgType + " -> " + msgText);
				}
				
				rs.close();
				
				_log.info("TableOptimizer: Database tables have been checked.");
			}
			
			{
				final ResultSet rs = st.executeQuery("ANALYZE TABLE " + StringUtils.join(tables, ","));
				
				while (rs.next())
				{
					final String table = rs.getString("Table");
					final String msgType = rs.getString("Msg_type");
					final String msgText = rs.getString("Msg_text");
					
					if (msgType.equals("status"))
						if (msgText.equals("OK") || msgText.equals("Table is already up to date"))
							continue;
					
					_log.warn("TableOptimizer: ANALYZE TABLE " + table + ": " + msgType + " -> " + msgText);
				}
				
				rs.close();
				
				_log.info("TableOptimizer: Database tables have been analyzed.");
			}
			
			{
				final ResultSet rs = st.executeQuery("OPTIMIZE TABLE " + StringUtils.join(tables, ","));
				
				while (rs.next())
				{
					final String table = rs.getString("Table");
					final String msgType = rs.getString("Msg_type");
					final String msgText = rs.getString("Msg_text");
					
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
		finally
		{
			L2Database.close(con);
		}
	}
}
