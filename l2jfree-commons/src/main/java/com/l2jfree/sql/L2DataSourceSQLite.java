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
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;

import com.l2jfree.lang.L2TextBuilder;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author NB4L1
 */
public final class L2DataSourceSQLite extends L2DataSource
{
	protected L2DataSourceSQLite(String name, ComboPooledDataSource dataSource)
	{
		super(name, dataSource);
	}
	
	@Override
	protected String getInformationSchemaTables()
	{
		return "INFORMATION_SCHEMA_TABLES";
	}
	
	@Override
	public void initSQLContext() throws SQLException
	{
		Connection con = null;
		try
		{
			con = getConnection();
			
			final PreparedStatement ps1 = con.prepareStatement("DROP VIEW IF EXISTS INFORMATION_SCHEMA_TABLES");
			ps1.executeUpdate();
			ps1.close();
			
			// source: http://www.sqlite.org/cvstrac/wiki?p=InformationSchema
			final L2TextBuilder tb = L2TextBuilder.newInstance();
			tb.append("CREATE VIEW INFORMATION_SCHEMA_TABLES AS");
			tb.append("    SELECT 'main'     AS TABLE_CATALOG,");
			tb.append("           'sqlite'   AS TABLE_SCHEMA,");
			tb.append("           tbl_name   AS TABLE_NAME,");
			tb.append("           CASE WHEN type = 'table' THEN 'BASE TABLE'");
			tb.append("                WHEN type = 'view'  THEN 'VIEW'");
			tb.append("           END        AS TABLE_TYPE,");
			tb.append("           sql        AS TABLE_SOURCE");
			tb.append("    FROM   sqlite_master");
			tb.append("    WHERE  type IN ('table', 'view')");
			tb.append("           AND tbl_name NOT LIKE 'INFORMATION_SCHEMA_%'");
			tb.append("    ORDER BY TABLE_TYPE, TABLE_NAME;");
			
			final PreparedStatement ps2 = con.prepareStatement(tb.moveToString());
			ps2.executeUpdate();
			ps2.close();
		}
		finally
		{
			L2Database.close(con);
		}
		
		super.initSQLContext();
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
