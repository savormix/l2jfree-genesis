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
package com.l2jfree.loginserver;

import com.l2jfree.sql.DataSourceInitializer;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author savormix
 *
 */
public class L2LoginDataSource implements DataSourceInitializer
{
	private static final int DB_MIN_CONNECTIONS = 2;
	
	private ComboPooledDataSource _source;
	
	/** Constructs a login server database initializer. */
	public L2LoginDataSource()
	{
		_source = null;
	}
	
	/* (non-Javadoc)
	 * @see com.l2jfree.sql.DataSourceInitializer#initDataSource()
	 */
	@Override
	public ComboPooledDataSource initDataSource() throws Exception
	{
		// TODO Auto-generated method stub
		if (Config.DB_MAX_CONNECTIONS < DB_MIN_CONNECTIONS)
			throw new IllegalArgumentException("At least " + DB_MIN_CONNECTIONS +
					" required in pool.");
		
		_source = new ComboPooledDataSource();
		_source.setAutoCommitOnClose(true);
		
		_source.setInitialPoolSize(DB_MIN_CONNECTIONS);
		_source.setMinPoolSize(DB_MIN_CONNECTIONS);
		_source.setMaxPoolSize(Config.DB_MAX_CONNECTIONS);
		
		_source.setAcquireRetryAttempts(0);
		_source.setAcquireRetryDelay(500);
		_source.setCheckoutTimeout(0);
		
		_source.setAcquireIncrement(5);
		
		_source.setAutomaticTestTable("_connection_test_table");
		_source.setTestConnectionOnCheckin(false);
		
		_source.setIdleConnectionTestPeriod(3600);
		_source.setMaxIdleTime(1800);
		
		_source.setMaxStatementsPerConnection(100);
		
		_source.setBreakAfterAcquireFailure(false);
		
		_source.setDriverClass(Config.DB_DRIVER);
		_source.setJdbcUrl(Config.DB_URL);
		_source.setUser(Config.DB_USER);
		_source.setPassword(Config.DB_PASSWORD);
		
		_source.getConnection().close();
		
		return _source;
	}
}
