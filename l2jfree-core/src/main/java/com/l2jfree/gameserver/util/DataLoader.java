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
package com.l2jfree.gameserver.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.io.FileUtils;

import com.l2jfree.util.concurrent.L2ThreadPool;

/**
 * @author NB4L1
 * @param <T>
 */
public abstract class DataLoader<T>
{
	private final int _readerCount;
	private final int _parserCount;
	
	private final BlockingQueue<File> _fileQueue = new LinkedBlockingQueue<File>();
	private final NavigableMap<File, String> _fileContents = new ConcurrentSkipListMap<File, String>();
	private final List<T> _parsedEntities = Collections.synchronizedList(new ArrayList<T>());
	
	private volatile boolean _failure = false;
	
	protected DataLoader()
	{
		this(1, Runtime.getRuntime().availableProcessors());
	}
	
	protected DataLoader(int readerCount, int parserCount)
	{
		_readerCount = readerCount;
		_parserCount = parserCount;
	}
	
	public final List<T> load()
	{
		_fileQueue.addAll(collectFiles());
		
		final ArrayList<ReaderTask> readerTasks = new ArrayList<ReaderTask>();
		
		for (int i = 0; i < _readerCount; i++)
			readerTasks.add(new ReaderTask());
		
		L2ThreadPool.invokeAllLongRunning(readerTasks);
		
		if (_failure)
			return null;
		
		final ArrayList<ParserTask> parserTasks = new ArrayList<ParserTask>();
		
		for (int i = 0; i < _parserCount; i++)
			parserTasks.add(new ParserTask());
		
		L2ThreadPool.invokeAllLongRunning(parserTasks);
		
		if (_failure)
			return null;
		
		return _parsedEntities;
	}
	
	protected abstract List<File> collectFiles();
	
	private final class ReaderTask implements Runnable
	{
		@Override
		public void run()
		{
			for (File file; (file = _fileQueue.poll()) != null;)
			{
				try
				{
					_fileContents.put(file, readFile(file));
				}
				catch (Exception e)
				{
					e.printStackTrace();
					
					_failure = true; // mark as failed
				}
			}
		}
	}
	
	@SuppressWarnings("static-method")
	protected String readFile(File file) throws IOException
	{
		return FileUtils.readFileToString(file);
	}
	
	private final class ParserTask implements Runnable
	{
		@Override
		public void run()
		{
			for (Entry<File, String> entry; (entry = _fileContents.pollFirstEntry()) != null;)
			{
				try
				{
					_parsedEntities.addAll(parseFile(entry.getKey(), entry.getValue()));
				}
				catch (Exception e)
				{
					e.printStackTrace();
					
					_failure = true; // mark as failed
				}
			}
		}
	}
	
	protected abstract List<T> parseFile(File file, String content);
}
