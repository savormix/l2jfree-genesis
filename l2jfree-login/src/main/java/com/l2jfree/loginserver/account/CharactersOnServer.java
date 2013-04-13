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
package com.l2jfree.loginserver.account;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author savormix
 */
public class CharactersOnServer
{
	static final CharactersOnServer DEFAULT = new CharactersOnServer(0, ArrayUtils.EMPTY_LONG_ARRAY);
	
	private int _characters;
	private long[] _delTime;
	
	public CharactersOnServer(int characters, long[] delTime)
	{
		update(characters, delTime);
	}
	
	public void update(int characters, long[] delTime)
	{
		if (this == DEFAULT)
			return;
		
		_characters = characters;
		_delTime = delTime;
	}
	
	public int getCharacters()
	{
		return _characters;
	}
	
	public long[] getDelTime()
	{
		return _delTime;
	}
}
