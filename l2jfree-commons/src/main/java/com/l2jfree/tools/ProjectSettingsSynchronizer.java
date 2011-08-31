package com.l2jfree.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * @author NB4L1
 */
public final class ProjectSettingsSynchronizer
{
	public static void main(String[] args) throws IOException
	{
		final File src = new File(".").getCanonicalFile();
		System.out.println("Copying from: " + src);
		System.out.println();
		
		final List<File> destinations = new ArrayList<File>();
		for (File dest : src.getParentFile().listFiles())
		{
			if (dest.isHidden() || !dest.isDirectory())
				continue;
			
			destinations.add(dest);
			System.out.println("Copying to: " + dest);
		}
		System.out.println();
		
		// .project
		System.out.println(".project");
		System.out.println("================================================================================");
		{
			final List<String> lines = FileUtils.readLines(new File(src, ".project"));
			
			for (File dest : destinations)
			{
				lines.set(2, lines.get(2).replaceAll(src.getName(), dest.getName()));
				writeLines(dest, ".project", lines);
				lines.set(2, lines.get(2).replaceAll(dest.getName(), src.getName()));
			}
		}
		System.out.println();
		
		// .classpath
		System.out.println(".classpath");
		System.out.println("================================================================================");
		{
			final List<String> lines = FileUtils.readLines(new File(src, ".classpath"));
			
			for (File dest : destinations)
			{
				if (dest.getName().endsWith("-main"))
				{
					final ArrayList<String> tmp = new ArrayList<String>();
					
					for (String line : lines)
						if (!line.contains("classpathentry"))
							tmp.add(line);
					
					writeLines(dest, ".classpath", tmp);
					continue;
				}
				
				writeLines(dest, ".classpath", lines);
			}
		}
		System.out.println();
		
		// .settings
		System.out.println(".settings");
		System.out.println("================================================================================");
		for (File settingsFile : new File(src, ".settings").listFiles())
		{
			if (settingsFile.getName().endsWith(".prefs"))
			{
				System.out.println(".settings/" + settingsFile.getName());
				System.out.println("--------------------------------------------------------------------------------");
				
				final List<String> lines = FileUtils.readLines(settingsFile);
				
				if (lines.get(0).startsWith("#"))
					lines.remove(0);
				
				for (File dest : destinations)
				{
					writeLines(new File(dest, ".settings"), settingsFile.getName(), lines);
				}
				System.out.println();
			}
		}
		System.out.println();
	}
	
	private static void writeLines(File parentFile, String fileName, Collection<String> lines) throws IOException
	{
		final File destinationFile = new File(parentFile, fileName);
		System.out.println("Copying: " + destinationFile);
		
		FileUtils.writeLines(destinationFile, lines);
	}
}
