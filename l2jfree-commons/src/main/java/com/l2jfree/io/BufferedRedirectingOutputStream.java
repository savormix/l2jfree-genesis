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
package com.l2jfree.io;

/**
 * @author NB4L1
 */
public abstract class BufferedRedirectingOutputStream extends RedirectingOutputStream
{
	private final StringBuilder _buffer = new StringBuilder();
	
	@Override
	protected synchronized final void handleString(String s)
	{
		for (int i = 0, length = s.length(); i < length; i++)
		{
			final char c = s.charAt(i);
			
			switch (c)
			{
				case '\r':
					break;
				case '\n':
				{
					//if (_buffer.length() == 0)
					//	break;
					
					handleLine(_buffer.toString());
					
					_buffer.setLength(0);
					break;
				}
				default:
				{
					_buffer.append(c);
					break;
				}
			}
		}
	}
	
	protected abstract void handleLine(String line);
}
