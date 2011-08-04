package com.l2jfree.util.jar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author NB4L1
 */
public final class ClassFinder
{
	private ClassFinder()
	{
		// utility class
	}
	
	public static List<Class<?>> findClasses(String packageName) throws Exception
	{
		final List<String> classNames = findClassNames(packageName);
		
		Collections.sort(classNames);
		
		final ArrayList<Class<?>> result = new ArrayList<Class<?>>();
		
		for (String className : classNames)
		{
			result.add(Class.forName(className));
		}
		
		return result;
	}
	
	public static List<String> findClassNames(String packageName) throws Exception
	{
		final File rootFile = Locator.getResourceSource(null, packageName.replace('.', '/'));
		final String packageNameAsPath = packageName.replace('.', '/') + '/';
		
		final ArrayList<String> result = new ArrayList<String>();
		
		if (rootFile.isDirectory())
		{
			final File packageFolder = new File(rootFile.getAbsolutePath() + '/' + packageNameAsPath);
			
			result.addAll(findClassNames(rootFile, packageFolder));
		}
		else if (rootFile.isFile() && rootFile.getName().endsWith(".jar"))
		{
			JarFile jarFile = null;
			try
			{
				jarFile = new JarFile(rootFile);
				
				for (JarEntry jarEntry : Collections.list(jarFile.entries()))
				{
					final String jarEntryName = jarEntry.getName();
					
					if (jarEntryName.startsWith(packageNameAsPath))
					{
						if (jarEntryName.endsWith(".class"))
							result.add(jarEntryName);
					}
				}
			}
			finally
			{
				try
				{
					if (jarFile != null)
						jarFile.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		for (int i = 0; i < result.size(); i++)
		{
			result.set(i, result.get(i).replace('/', '.').replace('\\', '.').replace(".class", ""));
		}
		
		return result;
	}
	
	private static ArrayList<String> findClassNames(final File rootFile, final File file)
	{
		final ArrayList<String> result = new ArrayList<String>();
		
		if (file.isDirectory())
		{
			for (File f : file.listFiles())
				result.addAll(findClassNames(rootFile, f));
		}
		else
		{
			if (file.getName().endsWith(".class"))
				result.add(file.getAbsolutePath().substring(rootFile.getAbsolutePath().length() + 1));
		}
		
		return result;
	}
}
