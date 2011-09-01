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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.IOUtils;

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
		
		Process run = null;
		try
		{
			ProcessBuilder pb = new ProcessBuilder("pg_dump", "-U", getComboPooledDataSource().getUser(), "-F", "p",
					"-b", "-c", "-E", "utf8", "-n", getComboPooledDataSource().getUser());
			pb.environment().put("PGDATABASE", databaseName);
			pb.environment().put("PGPASSWORD", getComboPooledDataSource().getPassword());
			pb.directory(new File("."));
			run = pb.start();
		}
		catch (Exception e)
		{
			_log.warn("DatabaseBackupManager: Could not make backup:", e);
		}
		
		if (run == null)
		{
			_log.warn("DatabaseBackupManager: Could not execute pg_dump!");
			return;
		}
		
		try
		{
			boolean success = false;
			InputStream in = null;
			try
			{
				in = run.getInputStream();
				
				success = writeBackup(databaseName, in);
			}
			catch (IOException e)
			{
				_log.warn("DatabaseBackupManager: Could not make backup!", e);
			}
			finally
			{
				IOUtils.closeQuietly(in);
			}
			
			if (!success)
			{
				BufferedReader br = null;
				try
				{
					br = new BufferedReader(new InputStreamReader(run.getErrorStream()));
					
					for (String line; (line = br.readLine()) != null;)
						_log.warn("DatabaseBackupManager: " + line);
				}
				catch (Exception e)
				{
					_log.warn("DatabaseBackupManager: Could not make backup!", e);
				}
				finally
				{
					IOUtils.closeQuietly(br);
				}
			}
			
			run.waitFor();
		}
		catch (Exception e)
		{
			_log.warn("DatabaseBackupManager: Could not make backup!", e);
		}
	}
}
