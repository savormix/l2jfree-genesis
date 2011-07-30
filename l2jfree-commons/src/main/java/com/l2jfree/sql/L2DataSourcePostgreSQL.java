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
