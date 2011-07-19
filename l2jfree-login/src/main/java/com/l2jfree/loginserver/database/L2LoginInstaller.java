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
package com.l2jfree.loginserver.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.IOUtils;

import com.l2jfree.Shutdown;
import com.l2jfree.TerminationStatus;
import com.l2jfree.sql.L2Database;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author savormix
 *
 */
public class L2LoginInstaller
{
	private static final L2Logger _log = L2Logger.getLogger(L2LoginInstaller.class);
	private static final String SQL_FILE = "login-jfree.sql";
	private static final String MULTILINE_COMMENT = "/*";
	private static final String MULTILINE_TERMINATOR = "*/";
	
	private L2LoginInstaller()
	{
		// singleton
	}
	
	/**
	 * Installs login server to the database.
	 * <BR><BR>
	 * Tables are created in the personal schema. If no such schema
	 * exists, the default schema is used.
	 */
	public void install()
	{
		StringBuilder stmts = new StringBuilder();
		
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream(SQL_FILE), "UTF-8"
			));
			String line;
			boolean comment = false;
			while ((line = br.readLine()) != null)
			{
				line = line.trim();
				if (line.startsWith("-- ") || line.startsWith("#"))
					continue;
				
				if (comment)
				{
					int term = line.indexOf(MULTILINE_TERMINATOR);
					if (term == -1)
						continue;
					
					line = line.substring(term + MULTILINE_TERMINATOR.length());
					comment = false;
				}
				
				int mlc;
				while ((mlc = line.indexOf(MULTILINE_COMMENT)) != -1)
				{
					int term = line.indexOf(MULTILINE_TERMINATOR);
					if (term == -1)
					{
						comment = true;
						line = line.substring(0, mlc);
					}
					else
						line = (line.substring(0, mlc) +
								line.substring(term + MULTILINE_TERMINATOR.length()));
				}
				
				if (line.isEmpty())
					continue;
				
				if (stmts.length() > 0)
					stmts.append(' ');
					//stmts.append("\r\n");
				stmts.append(line);
			}
		}
		catch (UnsupportedEncodingException e)
		{
			_log.fatal("UTF-8 is not available!", e);
			Shutdown.exit(TerminationStatus.ENVIRONMENT_MISSING_COMPONENT_OR_SERVICE);
		}
		catch (IOException e)
		{
			_log.error("Could not process auto-installation SQL file.", e);
			return;
		}
		finally
		{
			IOUtils.closeQuietly(br);
		}
		
		String[] tables = stmts.toString().split(";");
		
		Connection con = null;
		// MySQL forces a commit after DDL
		// Tested and reproduced with MySQL 5.5.8 (CS)
		// TODO: Some MySQL lover make an intelligent rollback on failure
		int i = -1;
		try
		{
			con = L2Database.getConnection();
			con.setAutoCommit(false);
			Statement st = con.createStatement();
			for (i = 0; i < tables.length; i++)
				st.executeUpdate(tables[i]);
			st.close();
			con.commit();
			_log.info("Automatically installed database tables.");
		}
		catch (SQLException e)
		{
			_log.debug("Could not install login server tables!", e);
			rollback(con);
		}
		finally
		{
			L2Database.close(con);
		}
	}
	
	private void rollback(Connection con)
	{
		try
		{
			con.rollback();
		}
		catch (Exception e)
		{
			// not important
		}
	}
	
	/**
	 * Returns a singleton object.
	 * @return an instance of this class
	 */
	public static L2LoginInstaller getInstance()
	{
		return SingletonHolder._instance;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		static final L2LoginInstaller _instance = new L2LoginInstaller();
	}
}
