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
import java.sql.SQLException;
import java.sql.Statement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author NB4L1
 */
public final class L2DataSourcePostgreSQL extends L2DataSource
{
	protected L2DataSourcePostgreSQL(String name, ComboPooledDataSource dataSource)
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
			
			_log.info("TableOptimizer: Vacuuming and building usage statistics...");
			st.executeUpdate("VACUUM ANALYZE");
			
			_log.info("TableOptimizer: Clustering...");
			st.executeUpdate("CLUSTER");
			
			_log.info("TableOptimizer: Database tables have been optimized.");
			
			st.close();
		}
		finally
		{
			L2Database.close(con);
		}
	}
}
