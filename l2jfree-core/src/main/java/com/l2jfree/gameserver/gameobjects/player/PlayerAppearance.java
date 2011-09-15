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
import com.l2jfree.gameserver.gameobjects.components.IComponent;
import com.l2jfree.gameserver.sql.PlayerDB;
import com.l2jfree.gameserver.templates.player.Gender;
import com.l2jfree.util.HexUtil;

/**
 * @author NB4L1
 */
// TODO sql
public class PlayerAppearance implements IComponent
{
	/** The default hexadecimal color of players' name (white is 0xFFFFFF) */
	public static final int DEFAULT_NAME_COLOR = 0xFFFFFF;
	/** The default hexadecimal color of players' title (light blue is 0xFFFF77) */
	public static final int DEFAULT_TITLE_COLOR = 0xFFFF77;
	
	private final L2Player _activeChar;
	
	private Gender _gender;
	private byte _face;
	private byte _hairColor;
	private byte _hairStyle;
	private int _nameColor = DEFAULT_NAME_COLOR;
	private int _titleColor = DEFAULT_TITLE_COLOR;
	
	public PlayerAppearance(L2Player activeChar)
	{
		_activeChar = activeChar;
	}
	
	public void load(PlayerDB playerDB)
	{
		_gender = playerDB.gender;
		_face = playerDB.face;
		_hairColor = playerDB.hairColor;
		_hairStyle = playerDB.hairStyle;
		if (playerDB.nameColor != null)
			_nameColor = reverseRGBChanels(Integer.decode("0x" + playerDB.nameColor));
		if (playerDB.titleColor != null)
			_titleColor = reverseRGBChanels(Integer.decode("0x" + playerDB.titleColor));
	}
	
	public void store(PlayerDB playerDB)
	{
		playerDB.gender = _gender;
		playerDB.face = _face;
		playerDB.hairColor = _hairColor;
		playerDB.hairStyle = _hairStyle;
		if (_nameColor != DEFAULT_NAME_COLOR)
			playerDB.nameColor = HexUtil.fillHex(reverseRGBChanels(_nameColor), 6);
		if (_titleColor != DEFAULT_TITLE_COLOR)
			playerDB.titleColor = HexUtil.fillHex(reverseRGBChanels(_titleColor), 6);
	}
	
	private static int reverseRGBChanels(int rgbColor)
	{
		return (Integer.reverseBytes(rgbColor) >> 8) & 0xFFFFFF;
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
	
	public void setNameColor(int nameColor)
	{
		_nameColor = nameColor;
	}
	
	public void setNameColor(int red, int green, int blue)
	{
		setNameColor((red & 0xFF) + ((green & 0xFF) << 8) + ((blue & 0xFF) << 16));
	}
	
	public int getTitleColor()
	{
		return _titleColor;
	}
	
	public void setTitleColor(int titleColor)
	{
		_titleColor = titleColor;
	}
	
	public void setTitleColor(int red, int green, int blue)
	{
		setTitleColor((red & 0xFF) + ((green & 0xFF) << 8) + ((blue & 0xFF) << 16));
	}
}
