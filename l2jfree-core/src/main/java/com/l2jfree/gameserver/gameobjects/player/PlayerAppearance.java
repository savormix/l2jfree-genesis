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
package com.l2jfree.gameserver.gameobjects.player;

import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.sql.PlayerDB;
import com.l2jfree.gameserver.templates.player.Gender;

/**
 * @author NB4L1
 */
// TODO sql
public class PlayerAppearance
{
	private final L2Player _activeChar;
	
	private Gender _gender;
	private byte _face;
	private byte _hairColor;
	private byte _hairStyle;
	private int _nameColor;
	private int _titleColor;
	
	public PlayerAppearance(L2Player activeChar, PlayerDB playerDB)
	{
		_activeChar = activeChar;
		
		_gender = playerDB.gender;
		_face = playerDB.face;
		_hairColor = playerDB.hairColor;
		_hairStyle = playerDB.hairStyle;
		//TODO _nameColor = playerDB.nameColor;
		//TODO_titleColor = playerDB.nameColor;
	}
	
	public final L2Player getActiveChar()
	{
		return _activeChar;
	}
	
	public Gender getGender()
	{
		return _gender;
	}
	
	public void setGender(Gender gender)
	{
		_gender = gender;
	}
	
	public byte getFace()
	{
		return _face;
	}
	
	public void setFace(byte face)
	{
		_face = face;
	}
	
	public byte getHairColor()
	{
		return _hairColor;
	}
	
	public void setHairColor(byte hairColor)
	{
		_hairColor = hairColor;
	}
	
	public byte getHairStyle()
	{
		return _hairStyle;
	}
	
	public void setHairStyle(byte hairStyle)
	{
		_hairStyle = hairStyle;
	}
	
	public int getNameColor()
	{
		return _nameColor;
	}
	
	public int getTitleColor()
	{
		return _titleColor;
	}
}
