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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.persistence.config.LoggerType;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.logging.SessionLog;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public abstract class DataSourceInitializer
{
	protected abstract String getJdbcUrl();
	
	protected abstract String getUser();
	
	protected abstract String getPassword();
	
	protected abstract int getMinConnections();
	
	protected abstract int getMaxConnections();
	
	@SuppressWarnings("static-method")
	public boolean createEntityManagerFactory()
	{
		return ClassLoader.getSystemResource("META-INF/persistence.xml") != null;
	}
	
	@SuppressWarnings("static-method")
	public Map<Object, Object> initEntityManagerFactoryProperties(L2DataSource source)
	{
		final Map<Object, Object> props = new HashMap<Object, Object>();
		
		props.put("provider", "org.eclipse.persistence.jpa.PersistenceProvider");
		
		props.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, source);
		
		props.put(PersistenceUnitProperties.LOGGING_LOGGER, LoggerType.JavaLogger);
		// let the configured log system handle levels
		props.put(PersistenceUnitProperties.LOGGING_LEVEL, SessionLog.ALL_LABEL);
		
		// TODO to temporarily allow eclipselink to show sqls
		props.put(PersistenceUnitProperties.LOGGING_PARAMETERS, "true");
		props.put(PersistenceUnitProperties.DDL_GENERATION, PersistenceUnitProperties.CREATE_ONLY);
		props.put(PersistenceUnitProperties.DDL_GENERATION_MODE, PersistenceUnitProperties.DDL_SQL_SCRIPT_GENERATION);
		//
		
		return props;
	}
	
	public ComboPooledDataSource initDataSource() throws Exception
	{
		if (getMaxConnections() < getMinConnections())
			throw new IllegalArgumentException("At least " + getMinConnections() + " db connections are required.");
		
		final ComboPooledDataSource source = new ComboPooledDataSource();
		
		source.setJdbcUrl(getJdbcUrl());
		/*
		 * Default: null
		 * For applications using ComboPooledDataSource or any c3p0-implemented unpooled DataSources -
		 * DriverManagerDataSource or the DataSource returned by DataSources.unpooledDataSource() - defines the username
		 * that will be used for the DataSource's default getConnection() method. (See also password.)
		 */
		source.setUser(getUser());
		/*
		 * Default: null
		 * For applications using ComboPooledDataSource or any c3p0-implemented unpooled DataSources -
		 * DriverManagerDataSource or the DataSource returned by DataSources.unpooledDataSource( ... ) - defines the
		 * password that will be used for the DataSource's default getConnection() method. (See also user.)
		 */
		source.setPassword(getPassword());
		
		// =============================================================================================================
		// Basic Pool Configuration
		/*
		 * Default: 3
		 * Minimum number of Connections a pool will maintain at any given time.
		 */
		source.setMinPoolSize(getMinConnections());
		/*
		 * Default: 15
		 * Maximum number of Connections a pool will maintain at any given time.
		 */
		source.setMaxPoolSize(getMaxConnections());
		/*
		 * Default: 3
		 * Number of Connections a pool will try to acquire upon startup. Should be between minPoolSize and maxPoolSize.
		 */
		source.setInitialPoolSize(getMinConnections());
		/*
		 * Default: 3
		 * Determines how many connections at a time c3p0 will try to acquire when the pool is exhausted.
		 */
		source.setAcquireIncrement(getMinConnections());
		
		// =============================================================================================================
		// Managing Pool Size and Connection Age
		/*
		 * Default: 0
		 * Seconds, effectively a time to live. A Connection older than maxConnectionAge will be destroyed and purged
		 * from the pool. This differs from maxIdleTime in that it refers to absolute age. Even a Connection which has
		 * not been much idle will be purged from the pool if it exceeds maxConnectionAge. Zero means no maximum
		 * absolute age is enforced.
		 */
		source.setMaxConnectionAge(0);
		/*
		 * Default: 0
		 * Seconds a Connection can remain pooled but unused before being discarded. Zero means idle connections never
		 * expire.
		 */
		source.setMaxIdleTime(1800);
		/*
		 * Default: 0
		 * Number of seconds that Connections in excess of minPoolSize should be permitted to remain idle in the pool
		 * before being culled. Intended for applications that wish to aggressively minimize the number of open
		 * Connections, shrinking the pool back towards minPoolSize if, following a spike, the load level diminishes and
		 * Connections acquired are no longer needed. If maxIdleTime is set, maxIdleTimeExcessConnections should be
		 * smaller if the parameter is to have any effect. Zero means no enforcement, excess Connections are not idled
		 * out.
		 */
		source.setMaxIdleTimeExcessConnections(900);
		
		// =============================================================================================================
		// Configuring Connection Testing
		/*
		 * Default: null
		 * If provided, c3p0 will create an empty table of the specified name, and use queries against that table to
		 * test the Connection. If automaticTestTable is provided, c3p0 will generate its own test query, therefore any
		 * preferredTestQuery set will be ignored. You should not work with the named table after c3p0 creates it; it
		 * should be strictly for c3p0's use in testing your Connection. (If you define your own ConnectionTester, it
		 * must implement the QueryConnectionTester interface for this parameter to be useful.)
		 */
		// FIXME Affects all DBMSes that allow multiple dbs and/or multiple schemas (so not SQLite)
		// checks whether this table exists IN ANY DB (of >= 1), IN ANY SCHEMA (also of >= 1).
		// if it exists, it MUST be readable (SELECT) to ALL USERS (that access that DBMS with c3p0),
		// otherwise c3p0 will FAIL.
		// dmd.getTables( null, null, automaticTestTable, new String[] {"TABLE"} )
		// see C3P0PooledConnectionPoolManager#initializeAutomaticTestTable(String, DbAuth)
		// - one possibility is to use a custom query for testing, that doesn't require this table
		source.setAutomaticTestTable("_connection_test_table");
		/*
		 * Default: 0
		 * If this is a number greater than 0, c3p0 will test all idle, pooled but unchecked-out connections, every this
		 * number of seconds.
		 */
		source.setIdleConnectionTestPeriod(3600);
		/*
		 * Default: false
		 * If true, an operation will be performed asynchronously at every connection checkin to verify that the
		 * connection is valid. Use in combination with idleConnectionTestPeriod for quite reliable, always asynchronous
		 * Connection testing. Also, setting an automaticTestTable or preferredTestQuery will usually speed up all
		 * connection tests.
		 */
		source.setTestConnectionOnCheckin(false);
		/*
		 * Default: false
		 * Use only if necessary. Expensive. If true, an operation will be performed at every connection checkout to
		 * verify that the connection is valid. Better choice: verify connections periodically using
		 * idleConnectionTestPeriod. Also, setting an automaticTestTable or preferredTestQuery will usually speed up all
		 * connection tests.
		 */
		source.setTestConnectionOnCheckout(false);
		
		// =============================================================================================================
		// Configuring Statement Pooling
		/*
		 * Default: 0
		 * The size of c3p0's global PreparedStatement cache. If both maxStatements and maxStatementsPerConnection are
		 * zero, statement caching will not be enabled. If maxStatements is zero but maxStatementsPerConnection is a
		 * non-zero value, statement caching will be enabled, but no global limit will be enforced, only the
		 * per-connection maximum. maxStatements controls the total number of Statements cached, for all Connections. If
		 * set, it should be a fairly large number, as each pooled Connection requires its own, distinct flock of cached
		 * statements. As a guide, consider how many distinct PreparedStatements are used frequently in your
		 * application, and multiply that number by maxPoolSize to arrive at an appropriate value. Though maxStatements
		 * is the JDBC standard parameter for controlling statement caching, users may find c3p0's alternative
		 * maxStatementsPerConnection more intuitive to use.
		 */
		source.setMaxStatements(0);
		/*
		 * Default: 0
		 * The number of PreparedStatements c3p0 will cache for a single pooled Connection. If both maxStatements and
		 * maxStatementsPerConnection are zero, statement caching will not be enabled. If maxStatementsPerConnection is
		 * zero but maxStatements is a non-zero value, statement caching will be enabled, and a global limit enforced,
		 * but otherwise no limit will be set on the number of cached statements for a single Connection. If set,
		 * maxStatementsPerConnection should be set to about the number distinct PreparedStatements that are used
		 * frequently in your application, plus two or three extra so infrequently statements don't force the more
		 * common cached statements to be culled. Though maxStatements is the JDBC standard parameter for controlling
		 * statement caching, users may find maxStatementsPerConnection more intuitive to use.
		 */
		source.setMaxStatementsPerConnection(100);
		
		// =============================================================================================================
		// Configuring Recovery From Database Outages
		/*
		 * Default: 30
		 * Defines how many times c3p0 will try to acquire a new Connection from the database before giving up. If this
		 * value is less than or equal to zero, c3p0 will keep trying to fetch a Connection indefinitely.
		 */
		source.setAcquireRetryAttempts(0);
		/*
		 * Default: 1000
		 * Milliseconds, time c3p0 will wait between acquire attempts.
		 */
		source.setAcquireRetryDelay(500);
		/*
		 * Default: false
		 * If true, a pooled DataSource will declare itself broken and be permanently closed if a Connection cannot be
		 * obtained from the database after making acquireRetryAttempts to acquire one. If false, failure to obtain a
		 * Connection will cause all Threads waiting for the pool to acquire a Connection to throw an Exception, but the
		 * DataSource will remain valid, and will attempt to acquire again following a call to getConnection().
		 */
		source.setBreakAfterAcquireFailure(false);
		
		// =============================================================================================================
		// Configuring Unresolved Transaction Handling
		/*
		 * Default: false
		 * The JDBC spec is unforgivably silent on what should happen to unresolved, pending transactions on Connection
		 * close. C3P0's default policy is to rollback any uncommitted, pending work. (I think this is absolutely,
		 * undeniably the right policy, but there is no consensus among JDBC driver vendors.) Setting autoCommitOnClose
		 * to true causes uncommitted pending work to be committed, rather than rolled back on Connection close. [Note:
		 * Since the spec is absurdly unclear on this question, application authors who wish to avoid bugs and
		 * inconsistent behavior should ensure that all transactions are explicitly either committed or rolled-back
		 * before close is called.]
		 */
		source.setAutoCommitOnClose(true);
		
		// Configuring to Debug and Workaround Broken Client Applications
		/*
		 * Default: 0
		 * Seconds. If set, if an application checks out but then fails to check-in [i.e. close()] a Connection within
		 * the specified period of time, the pool will unceremoniously destroy() the Connection. This permits
		 * applications with occasional Connection leaks to survive, rather than eventually exhausting the Connection
		 * pool. And that's a shame. Zero means no timeout, applications are expected to close() their own Connections.
		 * Obviously, if a non-zero value is set, it should be to a value longer than any Connection should reasonably
		 * be checked-out. Otherwise, the pool will occasionally kill Connections in active use, which is bad. This is
		 * basically a bad idea, but it's a commonly requested feature. Fix your $%!@% applications so they don't leak
		 * Connections! Use this temporarily in combination with debugUnreturnedConnectionStackTraces to figure out
		 * where Connections are being checked-out that don't make it back into the pool!
		 */
		source.setUnreturnedConnectionTimeout(0);
		/*
		 * Default: false
		 * If true, and if unreturnedConnectionTimeout is set to a positive value, then the pool will capture the stack
		 * trace (via an Exception) of all Connection checkouts, and the stack traces will be printed when unreturned
		 * checked-out Connections timeout. This is intended to debug applications with Connection leaks, that is
		 * applications that occasionally fail to return Connections, leading to pool growth, and eventually exhaustion
		 * (when the pool hits maxPoolSize with all Connections checked-out and lost). This parameter should only be set
		 * while debugging, as capturing the stack trace will slow down every Connection check-out.
		 */
		source.setDebugUnreturnedConnectionStackTraces(false);
		
		// =============================================================================================================
		// Other DataSource Configuration
		/*
		 * Default: 0
		 * The number of milliseconds a client calling getConnection() will wait for a Connection to be checked-in or
		 * acquired when the pool is exhausted. Zero means wait indefinitely. Setting any positive value will cause the
		 * getConnection() call to time-out and break with an SQLException after the specified number of milliseconds.
		 */
		source.setCheckoutTimeout(0);
		/*
		 * Default: 3
		 * c3p0 is very asynchronous. Slow JDBC operations are generally performed by helper threads that don't hold
		 * contended locks. Spreading these operations over multiple threads can significantly improve performance by
		 * allowing multiple operations to be performed simultaneously.
		 */
		source.setNumHelperThreads(Math.max(4, getMaxConnections() / 5));
		/*
		 * Default: 0
		 * Seconds before c3p0's thread pool will try to interrupt an apparently hung task. Rarely useful. Many of
		 * c3p0's functions are not performed by client threads, but asynchronously by an internal thread pool. c3p0's
		 * asynchrony enhances client performance directly, and minimizes the length of time that critical locks are
		 * held by ensuring that slow jdbc operations are performed in non-lock-holding threads. If, however, some of
		 * these tasks "hang", that is they neither succeed nor fail with an Exception for a prolonged period of time,
		 * c3p0's thread pool can become exhausted and administrative tasks backed up. If the tasks are simply slow, the
		 * best way to resolve the problem is to increase the number of threads, via numHelperThreads. But if tasks
		 * sometimes hang indefinitely, you can use this parameter to force a call to the task thread's interrupt()
		 * method if a task exceeds a set time limit. [c3p0 will eventually recover from hung tasks anyway by signalling
		 * an "APPARENT DEADLOCK" (you'll see it as a warning in the logs), replacing the thread pool task threads, and
		 * interrupt()ing the original threads. But letting the pool go into APPARENT DEADLOCK and then recover means
		 * that for some periods, c3p0's performance will be impaired. So if you're seeing these messages, increasing
		 * numHelperThreads and setting maxAdministrativeTaskTime might help. maxAdministrativeTaskTime should be large
		 * enough that any resonable attempt to acquire a Connection from the database, to test a Connection, or two
		 * destroy a Connection, would be expected to succeed or fail within the time set. Zero (the default) means
		 * tasks are never interrupted, which is the best and safest policy under most circumstances. If tasks are just
		 * slow, allocate more threads. If tasks are hanging forever, try to figure out why, and maybe setting
		 * maxAdministrativeTaskTime can help in the meantime.
		 */
		source.setMaxAdministrativeTaskTime(0);
		
		return source;
	}
}
