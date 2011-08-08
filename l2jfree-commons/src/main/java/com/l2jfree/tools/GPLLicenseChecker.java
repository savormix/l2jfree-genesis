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
package com.l2jfree.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.l2jfree.L2Config;
import com.l2jfree.lang.L2TextBuilder;

/**
 * @author NB4L1
 */
public final class GPLLicenseChecker extends L2Config
{
	private static final List<String> MODIFIED = new ArrayList<String>();
	
	/**
	 * Determines that it will check the whole project, or only the core itself.<br>
	 * If set to 'true' then it requires other projects to be checked out near the core.
	 */
	private static boolean WHOLE_PROJECT = false;
	private static boolean CLEARED = true;
	
	/**
	 * @param args ignored
	 * @throws IOException if any file to be licensed could not be accessed/modified
	 */
	public static void main(String[] args) throws IOException
	{
		if (WHOLE_PROJECT)
			parse(new File(".").getCanonicalFile().getParentFile());
		else
			parse(new File("."));
		
		System.out.println();
		
		if (MODIFIED.isEmpty())
		{
			System.out.println("There was no modification.");
		}
		else
		{
			System.out.println("Modified files:");
			System.out.println("================");
			
			for (String line : MODIFIED)
				System.out.println(line);
		}
		
		System.out.flush();
	}
	
	private static final FileFilter FILTER = new FileFilter() {
		@Override
		public boolean accept(File f)
		{
			// to skip svn files
			if (f.isHidden())
				return false;
			
			return f.isDirectory() || f.getName().endsWith(".java");
		}
	};
	
	private static void parse(File f) throws IOException
	{
		System.out.println(f.getCanonicalPath());
		
		if (f.isDirectory())
		{
			for (File tmp : f.listFiles(FILTER))
				parse(tmp);
		}
		else
		{
			final List<String> tmpList = read(f);
			
			// to skip the com.sun.script classes
			if (tmpList == null)
				return;
			
			// GPL license check
			final L2TextBuilder tb = L2TextBuilder.newInstance();
			
			if (!CLEARED)
				for (String line : CONFIDENTIAL)
					tb.appendNewline(line);
			
			for (String line : LICENSE)
				tb.appendNewline(line);
			
			boolean foundPackageDeclaration = false;
			for (String line : tmpList)
				if (foundPackageDeclaration |= containsPackageName(line))
					tb.appendNewline(line);
			
			// non-Javadoc check
			final String content = tb.moveToString();
			
			String regex1 = "";
			regex1 += "[ \t]+/\\* \\(non-Javadoc\\)\\r\\n";
			regex1 += "[ \t]+\\* @see [^#]+#[^\\(]+\\([^\\)]*\\)\\r\\n";
			regex1 += "[ \t]+\\*/\\r\\n";
			
			String regex2 = "";
			regex2 += "[ \t]+/\\*\\r\\n";
			regex2 += "[ \t]+\\* \\(non-Javadoc\\)\\r\\n";
			regex2 += "[ \t]+\\* @see [^#]+#[^\\(]+\\([^\\)]*\\)\\r\\n";
			regex2 += "[ \t]+\\*/\\r\\n";
			
			final String content2 = content.replaceAll(regex1, "").replaceAll(regex2, "");
			
			if (!content.equals(content2))
				MODIFIED.add(f.getPath() + ": (non-Javadoc)");
			
			FileUtils.writeStringToFile(f, content2);
		}
	}
	
	private static final String[] LICENSE = { "/*", // ...
			" * This program is free software: you can redistribute it and/or modify it under", // ...
			" * the terms of the GNU General Public License as published by the Free Software", // ...
			" * Foundation, either version 3 of the License, or (at your option) any later", // ...
			" * version.", // ...
			" * ", // ...
			" * This program is distributed in the hope that it will be useful, but WITHOUT", // ...
			" * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS", // ...
			" * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more", // ...
			" * details.", // ...
			" * ", // ...
			" * You should have received a copy of the GNU General Public License along with", // ...
			" * this program. If not, see <http://www.gnu.org/licenses/>.", // ...
			" */", // ...
	};
	
	private static final String[] CONFIDENTIAL = { "/*", // ...
		" * L2JFREE PROPRIETARY/CONFIDENTIAL.", // ...
		" * ", // ...
		" * In order to ensure user satisfaction, this source file is NOT CLEARED for public", // ...
		" * release. Do not redistribute. Do not upload and/or share outside team's private area.", // ...
		" * ", // ...
		" * This notice will be automatically removed on public release.", // ...
		" */", // ...
	};
	
	private static List<String> read(File f) throws IOException
	{
		final List<String> list = new ArrayList<String>();
		
		LineNumberReader lnr = null;
		try
		{
			lnr = new LineNumberReader(new FileReader(f));
			
			for (String line; (line = lnr.readLine()) != null;)
				list.add(line);
		}
		finally
		{
			IOUtils.closeQuietly(lnr);
		}
		
		// to skip the script classes
		for (String line : list)
			if (line.startsWith("package com.sun.script."))
				return null;
		
		int i = 0;
		for (; i < LICENSE.length; i++)
		{
			if (!list.get(i).equals(LICENSE[i]))
			{
				MODIFIED.add(f.getPath() + ":" + i);
				return list;
			}
		}
		
		if (!startsWithPackageName(list.get(i)))
		{
			MODIFIED.add(f.getPath() + ":" + lnr.getLineNumber());
			return list;
		}
		
		return list;
	}
	
	private static final String[] WHOLE_PROJECT_PACKAGE_NAMES = {
		"package com.l2jfree",
	};
	
	private static boolean startsWithPackageName(String line)
	{
		if (!WHOLE_PROJECT)
			return line.startsWith("package com.l2jfree");
		
		for (String packageName : WHOLE_PROJECT_PACKAGE_NAMES)
			if (line.startsWith(packageName))
				return true;
		
		return false;
	}
	
	private static boolean containsPackageName(String line)
	{
		if (!WHOLE_PROJECT)
			return line.contains("package com.l2jfree");
		
		for (String packageName : WHOLE_PROJECT_PACKAGE_NAMES)
			if (line.contains(packageName))
				return true;
		
		return false;
	}
}
