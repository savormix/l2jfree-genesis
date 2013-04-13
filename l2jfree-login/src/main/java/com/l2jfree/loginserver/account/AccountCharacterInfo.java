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

import java.util.Map;

import javolution.util.FastMap;

/**
 * @author savormix
 */
public class AccountCharacterInfo
{
	private final Map<Integer, CharactersOnServer> _characters;
	
	public AccountCharacterInfo()
	{
		// the good thing is, only different IDs can be inserted concurrently
		final FastMap<Integer, CharactersOnServer> characters = FastMap.newInstance();
		characters.setShared(true);
		_characters = characters;
	}
	
	public CharactersOnServer getInfo(int server)
	{
		final CharactersOnServer cos = _characters.get(server);
		if (cos != null)
			return cos;
		
		return CharactersOnServer.DEFAULT;
	}
	
	Map<Integer, CharactersOnServer> getCharacters()
	{
		return _characters;
	}
}
