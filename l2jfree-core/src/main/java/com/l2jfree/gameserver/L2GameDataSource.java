package com.l2jfree.gameserver;

import com.l2jfree.sql.DataSourceInitializer;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author NB4L1
 */
public final class L2GameDataSource implements DataSourceInitializer
{
	private static final int DB_MIN_CONNECTIONS = 10;
	
	@Override
	public ComboPooledDataSource initDataSource() throws Exception
	{
		if (Config.DB_MAX_CONNECTIONS < DB_MIN_CONNECTIONS)
			throw new IllegalArgumentException("At least " + DB_MIN_CONNECTIONS + " required in pool.");
		
		ComboPooledDataSource source = new ComboPooledDataSource();
		source.setAutoCommitOnClose(true);
		
		source.setInitialPoolSize(DB_MIN_CONNECTIONS);
		source.setMinPoolSize(DB_MIN_CONNECTIONS);
		source.setMaxPoolSize(Config.DB_MAX_CONNECTIONS);
		
		source.setAcquireRetryAttempts(0);
		source.setAcquireRetryDelay(500);
		source.setCheckoutTimeout(0);
		
		source.setAcquireIncrement(5);
		
		source.setAutomaticTestTable("_connection_test_table");
		source.setTestConnectionOnCheckin(false);
		
		source.setIdleConnectionTestPeriod(3600);
		source.setMaxIdleTime(1800);
		
		source.setMaxStatementsPerConnection(100);
		
		source.setBreakAfterAcquireFailure(false);
		
		source.setJdbcUrl(Config.DB_URL);
		source.setUser(Config.DB_USER);
		source.setPassword(Config.DB_PASSWORD);
		
		return source;
	}
}
