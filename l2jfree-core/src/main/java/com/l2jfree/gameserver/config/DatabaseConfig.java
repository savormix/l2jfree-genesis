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
package com.l2jfree.gameserver.config;

import com.l2jfree.Shutdown;
import com.l2jfree.TerminationStatus;
import com.l2jfree.L2Config.ConfigPropertiesLoader;
import com.l2jfree.config.L2Properties;
import com.l2jfree.config.annotation.ConfigClass;
import com.l2jfree.config.annotation.ConfigField;
import com.l2jfree.config.converters.DefaultConverter;

/**
 * @author NB4L1
 */
@ConfigClass(folderName = "config", fileName = "database")
public final class DatabaseConfig extends ConfigPropertiesLoader
{
	@ConfigField(name = "JdbcUrl", value = "sqlite:l2jfree_core.db", eternal = true, converter = JdbcUrlConverter.class, comment = {
			"Specifies the JDBC URL of the database.", //
			"Some URLs:", //
			"MySQL: mysql://host.or.ip/database", //
			"PostgreSQL: postgresql://host.or.ip/database", //
			"SQLite: sqlite:file.db", //
	})
	public static String DB_URL;
	
	@ConfigField(name = "Login", value = "", eternal = true, comment = { "Username for DB access",
			"The server will not start if a DBMS superuser account is used.", //
	})
	public static String DB_USER;
	
	@ConfigField(name = "Password", value = "", eternal = true, comment = { "Password for DB access" })
	public static String DB_PASSWORD;
	
	@ConfigField(name = "MaxConnectionsInPool", value = "50", eternal = true, comment = {
			"Specifies the maximum number of database connections active at once.", //
			"At least 10 connections must be assigned.", //
	})
	public static int DB_MAX_CONNECTIONS;
	
	@ConfigField(name = "OptimizeTables", value = "true", eternal = true, comment = {
			"Whether to optimize tables on startup.", //
			"Currently only works with MySQL and PostgreSQL.", //
	})
	public static boolean DB_OPTIMIZE;
	
	@Override
	protected void loadImpl(L2Properties properties)
	{
		if (DB_USER.equalsIgnoreCase("root") || DB_USER.equalsIgnoreCase("postgres"))
		{
			System.err.println("L2jFree servers should not use DBMS superuser accounts ... exited.");
			Shutdown.exit(TerminationStatus.ENVIRONMENT_SUPERUSER);
		}
	}
	
	public static final class JdbcUrlConverter extends DefaultConverter
	{
		@Override
		public Object convertFromString(Class<?> type, String value)
		{
			return "jdbc:" + super.convertFromString(type, value.replace("jdbc:", ""));
		}
		
		@Override
		public String convertToString(Class<?> type, Object obj)
		{
			return super.convertToString(type, obj).replace("jdbc:", "");
		}
	}
}
