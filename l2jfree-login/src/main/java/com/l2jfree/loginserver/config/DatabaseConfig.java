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
package com.l2jfree.loginserver.config;

import com.l2jfree.L2Config.ConfigPropertiesLoader;
import com.l2jfree.config.annotation.ConfigClass;
import com.l2jfree.config.annotation.ConfigField;
import com.l2jfree.config.converters.JdbcUrlConverter;
import com.l2jfree.config.postloadhooks.SuperUserValidator;

/**
 * @author NB4L1
 */
@ConfigClass(folderName = "config", fileName = "database")
public final class DatabaseConfig extends ConfigPropertiesLoader
{
	/** Database JDBC URL */
	@ConfigField(name = "JdbcUrl", value = "sqlite:l2jfree_login.db", eternal = true,
			converter = JdbcUrlConverter.class, comment = { "Specifies the JDBC URL of the database.", //
					"Some URLs:", //
					"MySQL: mysql://host.or.ip/database", //
					"PostgreSQL: postgresql://host.or.ip/database", //
					"SQLite: sqlite:file.db" })
	public static String URL;
	
	/** Database login */
	@ConfigField(name = "Login", value = "", eternal = true, postLoadHook = SuperUserValidator.class, comment = {
			"Username for DB access", //
			"The server will not start if a DBMS superuser account is used." })
	public static String USER;
	
	/** Database password */
	@ConfigField(name = "Password", value = "", eternal = true, comment = { "Password for DB access" })
	public static String PASSWORD;
	
	/** Maximum amount of database connections in pool */
	@ConfigField(name = "MaxConnectionsInPool", value = "5", eternal = true, comment = {
			"Specifies the maximum number of database connections active at once.", //
			"At least 2 connections must be assigned." })
	public static int MAX_CONNECTIONS;
	
	/** Whether to optimize database tables on startup */
	@ConfigField(name = "OptimizeTables", value = "true", eternal = true, comment = {
			"Whether to optimize tables on startup.", //
			"Currently only works with MySQL and PostgreSQL." })
	public static boolean OPTIMIZE;
	
	/** Whether to backup database tables on startup */
	@ConfigField(name = "BackupOnStartup", value = "true", eternal = true, comment = {
			"Whether to backup tables during server startup or not.", //
			"Currently only works with MySQL and SQLite." })
	public static boolean BACKUP_ON_STARTUP;
	
	/** Whether to backup database tables on shutdown */
	@ConfigField(name = "BackupOnShutdown", value = "true", eternal = true, comment = {
			"Whether to backup tables during server shutdown or not.", //
			"Currently only works with MySQL and SQLite." })
	public static boolean BACKUP_ON_SHUTDOWN;
}
