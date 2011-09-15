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
package com.l2jfree.gameserver.templates.player;

/**
 * @author NB4L1
 */
public final class PlayerBaseGenderTemplate
{
	private final Gender _gender;
	
	private double _collisionRadius;
	private double _collisionHeight;
	
	private int _safeFallHeight;
	
	public PlayerBaseGenderTemplate(Gender gender)
	{
		_gender = gender;
	}
	
	public Gender getGender()
	{
		return _gender;
	}
	
	public double getCollisionRadius()
	{
		return _collisionRadius;
	}
	
	public void setCollisionRadius(double collisionRadius)
	{
		_collisionRadius = collisionRadius;
	}
	
	public double getCollisionHeight()
	{
		return _collisionHeight;
	}
	
	public void setCollisionHeight(double collisionHeight)
	{
		_collisionHeight = collisionHeight;
	}
	
	public int getSafeFallHeight()
	{
		return _safeFallHeight;
	}
	
	public void setSafeFallHeight(int safeFallHeight)
	{
		_safeFallHeight = safeFallHeight;
	}
}
