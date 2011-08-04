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
package com.l2jfree.gameserver;

import com.l2jfree.L2Config;
import com.l2jfree.Shutdown;
import com.l2jfree.TerminationStatus;
import com.l2jfree.config.L2Properties;
import com.l2jfree.config.annotation.ConfigClass;
import com.l2jfree.config.annotation.ConfigField;
import com.l2jfree.config.converters.DefaultConverter;
import com.l2jfree.sql.L2Database;
import com.l2jfree.sql.L2DatabaseInstaller;
import com.l2jfree.util.concurrent.L2ThreadPool;

/**
 * @author savormix
 *
 */
public class Config extends L2Config
{
	static
	{
		L2Config.registerConfig(new DatabaseConfig());
		L2Config.registerConfig(new NetworkConfig());
		
		try
		{
			L2Config.loadConfigs();
		}
		catch (Exception e)
		{
			_log.fatal("Could not load configuration files!", e);
			Shutdown.exit(TerminationStatus.RUNTIME_INVALID_CONFIGURATION);
		}
		
		try
		{
			L2ThreadPool.initThreadPools(new L2CoreThreadPools());
		}
		catch (Exception e)
		{
			_log.fatal("Could not initialize thread pools!", e);
			Shutdown.exit(TerminationStatus.RUNTIME_INITIALIZATION_FAILURE);
		}
		
		try
		{
			L2Database.setDataSource("default", new L2CoreDataSource());
		}
		catch (Exception e)
		{
			_log.fatal("Could not initialize DB connections!", e);
			Shutdown.exit(TerminationStatus.RUNTIME_INITIALIZATION_FAILURE);
		}
		
		try
		{
			L2DatabaseInstaller.check();
		}
		catch (Exception e)
		{
			_log.fatal("Could not initialize DB tables!", e);
			Shutdown.exit(TerminationStatus.RUNTIME_INITIALIZATION_FAILURE);
		}
	}
	
	protected Config()
	{
		super();
	}
	
	@ConfigClass(folderName = "config", fileName = "database")
	public static final class DatabaseConfig extends ConfigPropertiesLoader
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
				_log.info("L2jFree servers should not use DBMS superuser accounts ... exited.");
				Shutdown.exit(TerminationStatus.ENVIRONMENT_SUPERUSER);
			}
		}
		
		public static final class JdbcUrlConverter extends DefaultConverter
		{
			@Override
			public Object convertFromString(Class<?> type, String value)
			{
				return super.convertFromString(type, value.replace("jdbc:", ""));
			}
			
			@Override
			public String convertToString(Class<?> type, Object obj)
			{
				return "jdbc:" + super.convertToString(type, obj).replace("jdbc:", "");
			}
		}
	}
	
	@ConfigClass(folderName = "config", fileName = "network")
	public static final class NetworkConfig extends ConfigPropertiesLoader
	{
		@ConfigField(name = "ListenIP", value = "0.0.0.0", eternal = true, comment = {
				"Login Server will accept CLIENT connections coming to this IP address only.", //
				"Use 0.0.0.0 to listen on all available adapters.", //
				"Specify a valid IP address if you require the game server to bind on a single IP.", //
		})
		public static String NET_LISTEN_IP;
		
		@ConfigField(name = "ListenPort", value = "7777", eternal = true, comment = { "Login Server will listen for CLIENT connections on this port." })
		public static int NET_LISTEN_PORT;
	}
}
