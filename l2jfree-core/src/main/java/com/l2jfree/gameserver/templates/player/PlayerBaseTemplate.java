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
public final class PlayerBaseTemplate
{
	private final Race _race;
	private final ClassType _type;
	private final Gender _gender;
	
	private double _collisionRadius;
	private double _collisionHeight;
	
	private int _runSpeed;
	private int _walkSpeed;
	private int _runSpeedInWater;
	private int _walkSpeedInWater;
	private int _runSpeedFlying;
	private int _walkSpeedFlying;
	private int _runSpeedNoble; // TODO find out what this is
	private int _walkSpeedNoble; // TODO find out what this is
	private int _attackRange;
	private int _physicalAttack;
	private int _breath;
	private int _safeFallHeight;
	private int _jump;
	
	private int _str;
	private int _dex;
	private int _con;
	private int _int;
	private int _wit;
	private int _men;
	
	public PlayerBaseTemplate(Race race, ClassType type, Gender gender)
	{
		_race = race;
		_type = type;
		_gender = gender;
	}
	
	public Race getRace()
	{
		return _race;
	}
	
	public ClassType getType()
	{
		return _type;
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
	
	public int getRunSpeed()
	{
		return _runSpeed;
	}
	
	public void setRunSpeed(int runSpeed)
	{
		_runSpeed = runSpeed;
	}
	
	public int getWalkSpeed()
	{
		return _walkSpeed;
	}
	
	public void setWalkSpeed(int walkSpeed)
	{
		_walkSpeed = walkSpeed;
	}
	
	public int getRunSpeedInWater()
	{
		return _runSpeedInWater;
	}
	
	public void setRunSpeedInWater(int runSpeedInWater)
	{
		_runSpeedInWater = runSpeedInWater;
	}
	
	public int getWalkSpeedInWater()
	{
		return _walkSpeedInWater;
	}
	
	public void setWalkSpeedInWater(int walkSpeedInWater)
	{
		_walkSpeedInWater = walkSpeedInWater;
	}
	
	public int getWalkSpeedFlying()
	{
		return _walkSpeedFlying;
	}
	
	public void setWalkSpeedFlying(int walkSpeedFlying)
	{
		_walkSpeedFlying = walkSpeedFlying;
	}
	
	public int getRunSpeedFlying()
	{
		return _runSpeedFlying;
	}
	
	public void setRunSpeedFlying(int runSpeedFlying)
	{
		_runSpeedFlying = runSpeedFlying;
	}
	
	public int getWalkSpeedNoble()
	{
		return _walkSpeedNoble;
	}
	
	public void setWalkSpeedNoble(int walkSpeedNoble)
	{
		_walkSpeedNoble = walkSpeedNoble;
	}
	
	public int getRunSpeedNoble()
	{
		return _runSpeedNoble;
	}
	
	public void setRunSpeedNoble(int runSpeedNoble)
	{
		_runSpeedNoble = runSpeedNoble;
	}
	
	public int getAttackRange()
	{
		return _attackRange;
	}
	
	public void setAttackRange(int attackRange)
	{
		_attackRange = attackRange;
	}
	
	public int getPhysicalAttack()
	{
		return _physicalAttack;
	}
	
	public void setPhysicalAttack(int physicalAttack)
	{
		_physicalAttack = physicalAttack;
	}
	
	public int getBreath()
	{
		return _breath;
	}
	
	public void setBreath(int breath)
	{
		_breath = breath;
	}
	
	public int getSafeFallHeight()
	{
		return _safeFallHeight;
	}
	
	public void setSafeFallHeight(int safeFallHeight)
	{
		_safeFallHeight = safeFallHeight;
	}
	
	public int getJump()
	{
		return _jump;
	}
	
	public void setJump(int jump)
	{
		_jump = jump;
	}
	
	public int getSTR()
	{
		return _str;
	}
	
	public int getDEX()
	{
		return _dex;
	}
	
	public int getCON()
	{
		return _con;
	}
	
	public int getINT()
	{
		return _int;
	}
	
	public int getWIT()
	{
		return _wit;
	}
	
	public int getMEN()
	{
		return _men;
	}
	
	public void setSTR(int value)
	{
		_str = value;
	}
	
	public void setDEX(int value)
	{
		_dex = value;
	}
	
	public void setCON(int value)
	{
		_con = value;
	}
	
	public void setINT(int value)
	{
		_int = value;
	}
	
	public void setWIT(int value)
	{
		_wit = value;
	}
	
	public void setMEN(int value)
	{
		_men = value;
	}
}
