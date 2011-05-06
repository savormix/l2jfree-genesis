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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.l2jfree.config.annotation.ConfigClass;
import com.l2jfree.config.annotation.ConfigField;
import com.l2jfree.config.model.ConfigClassInfo;
import com.l2jfree.util.logging.L2Logger;

/**
 * Database backup utility, directly dependent on mysqldump tool.
 * 
 * @author hex1r0
 * @author savormix
 */
public final class DatabaseBackupManager
{
	private static final L2Logger _log = L2Logger.getLogger(DatabaseBackupManager.class);
	
	@ConfigClass(folderName = "config", fileName = "databasebackup")
	private static final class DatabaseBackupConfig
	{
		@ConfigField(name = "DatabaseUser", value = "root")
		public static String DATABASE_USER;
		
		@ConfigField(name = "DatabasePassword", value = "")
		public static String DATABASE_PASSWORD;
		
		@ConfigField(name = "DatabaseName", value = "l2jdb")
		public static String DATABASE_NAME;
		
		@ConfigField(name = "MysqldumpPath", value = ".")
		public static String MYSQLDUMP_PATH;
		
		@ConfigField(name = "SavePath", value = "/backup/database/")
		public static String SAVE_PATH;
		
		@ConfigField(name = "Compression", value = "True")
		public static boolean COMPRESSION;
	}
	
	
	public static void makeBackup() throws Exception
	{
		// loads the config
		ConfigClassInfo.valueOf(DatabaseBackupConfig.class).load();
		
		File f = new File(DatabaseBackupConfig.SAVE_PATH);
		if (!f.mkdirs() && !f.exists())
		{
			_log.warn("Could not create folder " + f.getAbsolutePath());
			return;
		}
		
		_log.info("DatabaseBackupManager: backing up `" + DatabaseBackupConfig.DATABASE_NAME + "`...");
		
		Process run = null;
		try
		{
			run = Runtime.getRuntime().exec("mysqldump" +
						" --user=" + DatabaseBackupConfig.DATABASE_USER +
						" --password=" + DatabaseBackupConfig.DATABASE_PASSWORD +
						" --compact --complete-insert --default-character-set=utf8 --extended-insert --lock-tables --quick --skip-triggers " +
						DatabaseBackupConfig.DATABASE_NAME, null, new File(DatabaseBackupConfig.MYSQLDUMP_PATH));
		}
		catch (Exception e)
		{
		}
		finally
		{
			if (run == null)
			{
				_log.warn("Could not execute mysqldump!");
				return;
			}
		}
		
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			Date time = new Date();
			
			File bf = new File(f, sdf.format(time) + (DatabaseBackupConfig.COMPRESSION ? ".zip" : ".sql"));
			if (!bf.createNewFile())
				throw new IOException("Cannot create backup file: " + bf.getCanonicalPath());
			InputStream input = run.getInputStream();
			OutputStream out = new FileOutputStream(bf);
			if (DatabaseBackupConfig.COMPRESSION)
			{
				ZipOutputStream dflt = new ZipOutputStream(out);
				dflt.setMethod(ZipOutputStream.DEFLATED);
				dflt.setLevel(Deflater.BEST_COMPRESSION);
				dflt.setComment("L2JFree Schema Backup Utility\r\n\r\nBackup date: " +
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS z").format(time));
				dflt.putNextEntry(new ZipEntry(DatabaseBackupConfig.DATABASE_NAME + ".sql"));
				out = dflt;
			}
			
			byte[] buf = new byte[4096];
			int written = 0;
			for (int read; (read = input.read(buf)) != -1;)
			{
				out.write(buf, 0, read);
				
				written += read;
			}
			input.close();
			out.close();
			
			if (written == 0)
			{
				bf.delete();
				BufferedReader br = new BufferedReader(new InputStreamReader(run.getErrorStream()));
				String line;
				while ((line = br.readLine()) != null)
					_log.warn("DatabaseBackupManager: " + line);
				br.close();
			}
			else
				_log.info("DatabaseBackupManager: Schema `" + DatabaseBackupConfig.DATABASE_NAME +
						"` backed up successfully in " + (System.currentTimeMillis() - time.getTime()) / 1000 + " s.");
			
			run.waitFor();
		}
		catch (Exception e)
		{
			_log.warn("DatabaseBackupManager: Could not make backup: ", e);
		}
	}
}
