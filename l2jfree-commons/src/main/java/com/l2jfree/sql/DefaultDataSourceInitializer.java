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

import com.l2jfree.config.annotation.ConfigClass;
import com.l2jfree.config.annotation.ConfigField;
import com.l2jfree.config.model.ConfigClassInfo;
import com.l2jfree.util.logging.L2Logger;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public final class DefaultDataSourceInitializer implements DataSourceInitializer
{
	private static final L2Logger _log = L2Logger.getLogger(DefaultDataSourceInitializer.class);
	
	@ConfigClass(folderName = "config", fileName = "datasource")
	private static final class DefaultDataSourceConfig
	{
		@ConfigField(name = "DatabaseMaxConnection", value = "50", eternal = true)
		public static int DATABASE_MAX_CONNECTIONS;
		
		@ConfigField(name = "DatabaseDriverClass", value = "com.mysql.jdbc.Driver", eternal = true)
		public static String DATABASE_DRIVER_CLASS;
		
		@ConfigField(name = "DatabaseJdbcUrl", value = "jdbc:mysql://localhost/l2jdb", eternal = true)
		public static String DATABASE_JDBC_URL;
		
		@ConfigField(name = "DatabaseUser", value = "root", eternal = true)
		public static String DATABASE_USER;
		
		@ConfigField(name = "DatabasePassword", value = "", eternal = true)
		public static String DATABASE_PASSWORD;
	}
	
	@Override
	public ComboPooledDataSource initDataSource() throws Exception
	{
		// loads the config
		ConfigClassInfo.valueOf(DefaultDataSourceConfig.class).load();
		
		if (DefaultDataSourceConfig.DATABASE_MAX_CONNECTIONS < 10)
		{
			DefaultDataSourceConfig.DATABASE_MAX_CONNECTIONS = 10;
			_log.warn("at least " + DefaultDataSourceConfig.DATABASE_MAX_CONNECTIONS + " db connections are required.");
		}
		
		final ComboPooledDataSource source = new ComboPooledDataSource();
		source.setAutoCommitOnClose(true);
		
		source.setInitialPoolSize(10);
		source.setMinPoolSize(10);
		source.setMaxPoolSize(DefaultDataSourceConfig.DATABASE_MAX_CONNECTIONS);
		
		source.setAcquireRetryAttempts(0); // try to obtain connections indefinitely (0 = never quit)
		source.setAcquireRetryDelay(500); // 500 miliseconds wait before try to acquire connection again
		source.setCheckoutTimeout(0); // 0 = wait indefinitely for new connection
		// if pool is exhausted
		source.setAcquireIncrement(5); // if pool is exhausted, get 5 more connections at a time
		// cause there is a "long" delay on acquire connection
		// so taking more than one connection at once will make connection pooling
		// more effective.
		
		// this "_connection_test_table" is automatically created if not already there
		source.setAutomaticTestTable("_connection_test_table");
		source.setTestConnectionOnCheckin(false);
		
		// testing OnCheckin used with IdleConnectionTestPeriod is faster than testing on checkout
		
		source.setIdleConnectionTestPeriod(3600); // test idle connection every 60 sec
		source.setMaxIdleTime(1800); // 0 = idle connections never expire
		// *THANKS* to connection testing configured above
		// but I prefer to disconnect all connections not used
		// for more than 1 hour
		
		// enables statement caching, there is a "semi-bug" in c3p0 0.9.0 but in 0.9.0.2 and later it's fixed
		source.setMaxStatementsPerConnection(100);
		
		source.setBreakAfterAcquireFailure(false); // never fail if any way possible
		// setting this to true will make
		// c3p0 "crash" and refuse to work
		// till restart thus making acquire
		// errors "FATAL" ... we don't want that
		// it should be possible to recover
		source.setDriverClass(DefaultDataSourceConfig.DATABASE_DRIVER_CLASS);
		source.setJdbcUrl(DefaultDataSourceConfig.DATABASE_JDBC_URL);
		source.setUser(DefaultDataSourceConfig.DATABASE_USER);
		source.setPassword(DefaultDataSourceConfig.DATABASE_PASSWORD);
		
		return source;
	}
}
