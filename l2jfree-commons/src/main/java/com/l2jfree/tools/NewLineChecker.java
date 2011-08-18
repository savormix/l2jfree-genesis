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
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 */
public class NewLineChecker
{
	/**
	 * @param args ignored
	 * @throws IOException if any file to be checked could not be accessed/modified
	 */
	public static void main(String[] args) throws IOException
	{
		parse(new File("../"));
		
		System.out.println("R: " + R);
		System.out.println("N: " + N);
		System.out.println("RN: " + RN);
		System.out.println("MIXED: " + MIXED);
		System.out.println("MISSING_NEWLINE: " + MISSING_NEWLINE);
	}
	
	private static final FileFilter FILTER = new FileFilter() {
		@Override
		public boolean accept(File f)
		{
			// to skip svn files
			if (f.isHidden())
				return false;
			
			return f.isDirectory() || !f.getName().endsWith(".zip") && !f.getName().endsWith(".class");
		}
	};
	
	private static void parse(File f) throws IOException
	{
		if (f.isDirectory())
		{
			for (File f2 : f.listFiles(FILTER))
				parse(f2);
			return;
		}
		
		final String input = FileUtils.readFileToString(f);
		final List<String> inputLines = FileUtils.readLines(f);
		
		final int r = StringUtils.countMatches(input, "\r");
		final int n = StringUtils.countMatches(input, "\n");
		final int rn = StringUtils.countMatches(input, "\r\n");
		
		final char lastChar = input.charAt(input.length() - 1);
		boolean missingNewline = false;
		
		if (lastChar != '\r' && lastChar != '\n')
		{
			System.out.println("--- " + f.getCanonicalPath());
			System.out.println(lastChar);
			
			MISSING_NEWLINE++;
			
			missingNewline = true;
		}
		
		// fully "\r\n"
		if (r == n && r == rn && n == rn)
		{
			RN++;
			if (missingNewline)
				FileUtils.writeLines(f, inputLines, "\r\n");
			return;
		}
		
		// fully "\n"
		if (r == 0 && rn == 0)
		{
			N++;
			System.out.println("n " + f.getName());
			if (missingNewline)
				FileUtils.writeLines(f, inputLines, "\n");
			return;
		}
		
		System.out.println("--- " + f.getCanonicalPath());
		System.out.println("r: " + r);
		System.out.println("n: " + n);
		System.out.println("rn: " + rn);
		
		FileUtils.writeLines(f, inputLines, f.getName().endsWith(".sh") ? "\n" : "\r\n");
		
		// fully "\r"
		if (n == 0 && rn == 0)
		{
			R++;
			return;
		}
		
		// mixed
		MIXED++;
	}
	
	private static int RN;
	private static int R;
	private static int N;
	private static int MIXED;
	private static int MISSING_NEWLINE;
}
