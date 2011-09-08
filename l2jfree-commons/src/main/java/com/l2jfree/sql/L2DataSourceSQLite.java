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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.IOUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author NB4L1
 */
public final class L2DataSourceSQLite extends L2DataSource
{
	protected L2DataSourceSQLite(String name, ComboPooledDataSource dataSource)
	{
		super(name, dataSource);
		
		System.err.println("NOTE: " + getProviderName() + " isn't recommended for a live server!");
		System.err.println("It is HIGHLY RECOMMENDED to change to another provider!");
	}
	
	@Override
	protected String correctTableDefinition(String defition)
	{
		return defition.replaceAll(",\\s*KEY [^\\)]*\\)", "").replace("UNIQUE KEY", "UNIQUE");
	}
	
	@Override
	public void optimize() throws SQLException
	{
		Connection con = null;
		try
		{
			con = getConnection();
			
			final Statement st = con.createStatement();
			
			_log.info("TableOptimizer: Rebuilding database...");
			st.executeUpdate("VACUUM");
			_log.info("TableOptimizer: Building usage statistics...");
			st.executeUpdate("ANALYZE");
			
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
		final String databaseName = getComboPooledDataSource().getJdbcUrl().replaceAll("^.*:", "");
		
		_log.info("DatabaseBackupManager: backing up `" + databaseName + "`...");
		
		try
		{
			InputStream in = null;
			try
			{
				in = new FileInputStream(databaseName);
				
				writeBackup(databaseName, in);
			}
			catch (IOException e)
			{
				_log.warn("DatabaseBackupManager: Could not make backup:", e);
			}
			finally
			{
				IOUtils.closeQuietly(in);
			}
		}
		catch (Exception e)
		{
			_log.warn("DatabaseBackupManager: Could not make backup: ", e);
		}
	}
}
