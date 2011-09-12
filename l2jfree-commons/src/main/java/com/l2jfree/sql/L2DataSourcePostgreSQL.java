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

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
	
	@Override
	public void backup()
	{
		final String databaseName = getComboPooledDataSource().getJdbcUrl().replaceAll("^.*/", "");
		
		_log.info("DatabaseBackupManager: backing up \"" + databaseName + "\"...");
		
		final Process run;
		try
		{
			final ArrayList<String> commands = new ArrayList<String>();
			commands.add("pg_dump"); // Extract a PostgreSQL database into a script file or other archive file
			commands.add("-U " + getComboPooledDataSource().getUser()); // User name to connect as.
			commands.add("-F p"); // Selects the format of the output.
			commands.add("-b"); // Include large objects in the dump.
			commands.add("-c"); // Output commands to clean (drop) database objects prior to (the commands for) creating them.
			commands.add("-E utf8"); // Create the dump in the specified character set encoding.
			commands.add("-n " + getComboPooledDataSource().getUser()); // Dump only schemas matching schema; this selects both the schema itself, and all its contained objects.
			
			final ProcessBuilder pb = new ProcessBuilder(commands);
			pb.environment().put("PGDATABASE", databaseName);
			pb.environment().put("PGPASSWORD", getComboPooledDataSource().getPassword());
			pb.directory(new File("."));
			
			run = pb.start();
		}
		catch (Exception e)
		{
			_log.warn("DatabaseBackupManager: Could not execute pg_dump!", e);
			return;
		}
		
		writeBackup(databaseName, run);
	}
}
