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
package com.l2jfree.gameserver.gameobjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.l2jfree.gameserver.datatables.PlayerTemplateTable;
import com.l2jfree.gameserver.templates.player.ClassId;
import com.l2jfree.sql.L2Database;
import com.l2jfree.util.Rnd;

/**
 * @author NB4L1
 */
public class L2PcInstance extends L2Character implements IL2Playable
{
	static
	{
		ComponentFactory.KNOWNLIST.register(L2PcInstance.class, PcKnownList.class);
	}
	
	public static L2PcInstance create(String name, String accountName, ClassId classId)
	{
		int objectId = Rnd.get(Integer.MAX_VALUE); // TODO
		
		L2PcInstance result = null;
		
		Connection con = null;
		try
		{
			con = L2Database.getConnection();
			
			final PreparedStatement ps =
					con.prepareStatement("INSERT INTO players (objectId, name, accountName) VALUES (?,?,?)");
			ps.setInt(1, objectId);
			ps.setString(2, name);
			ps.setString(3, accountName);
			ps.executeUpdate();
			ps.close();
			
			result = new L2PcInstance(objectId, classId);
			result.setAccountName(accountName);
			result.setName(name);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			L2Database.close(con);
		}
		
		return result;
	}
	
	public static L2PcInstance load(int objectId)
	{
		L2PcInstance result = null;
		
		Connection con = null;
		try
		{
			con = L2Database.getConnection();
			
			final PreparedStatement ps = con.prepareStatement("SELECT * FROM players WHERE objectId = ?");
			ps.setInt(1, objectId);
			
			final ResultSet rs = ps.executeQuery();
			
			if (rs.next())
			{
				final String accountName = rs.getString("accountName");
				final String name = rs.getString("name");
				final ClassId classId = ClassId.HumanFighter; // TODO
				
				result = new L2PcInstance(objectId, classId);
				result.setAccountName(accountName);
				result.setName(name);
			}
			
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			L2Database.close(con);
		}
		
		return result;
	}
	
	private String _accountName;
	private String _name;
	
	private L2PcInstance(int objectId, ClassId classId)
	{
		super(objectId, PlayerTemplateTable.getInstance().getPlayerTemplate(classId));
	}
	
	public String getAccountName()
	{
		return _accountName;
	}
	
	public void setAccountName(String accountName)
	{
		_accountName = accountName;
	}
	
	@Override
	public String getName()
	{
		return _name;
	}
	
	@Override
	public void setName(String name)
	{
		_name = name;
	}
}
