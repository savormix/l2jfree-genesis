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
package com.l2jfree.gameserver.sql;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.templates.player.ClassId;
import com.l2jfree.gameserver.templates.player.Gender;
import com.l2jfree.sql.L2DBEntity;
import com.l2jfree.sql.L2Database;
import com.l2jfree.sql.L2Database.QueryConfigurator;

/**
 * @author NB4L1
 */
@Entity
@Table(name = "players", uniqueConstraints = { @UniqueConstraint(columnNames = "name") })
@NamedQueries({
		@NamedQuery(name = "PlayerDB.findByAccount",
				query = "SELECT p FROM PlayerDB p WHERE p.accountName = :accountName"),
		@NamedQuery(name = "PlayerDB.findAll", query = "SELECT p FROM PlayerDB p"),
		@NamedQuery(name = "PlayerDB.setOnline",
				query = "UPDATE PlayerDB p SET p.online = :online WHERE p.objectId = :objectId"),
		@NamedQuery(name = "PlayerDB.setOfflineAll", query = "UPDATE PlayerDB p SET p.online = 0"),

})
public class PlayerDB extends L2DBEntity
{
	@Id
	@Column(name = "objectId", nullable = false, updatable = false)
	public int objectId;
	
	@Column(name = "name", unique = true, nullable = false, length = 35)
	public String name;
	
	@Column(name = "accountName", nullable = false, updatable = false, length = 45)
	public String accountName;
	
	@Column(name = "online", nullable = false)
	public boolean online;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "baseClassId", nullable = false, updatable = false)
	public ClassId baseClassId;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "activeClassId", nullable = false)
	public ClassId activeClassId;
	
	// Appearance
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "gender", nullable = false)
	public Gender gender;
	
	@Column(name = "face", nullable = false)
	public byte face;
	
	@Column(name = "hairColor", nullable = false)
	public byte hairColor;
	
	@Column(name = "hairStyle", nullable = false)
	public byte hairStyle;
	// Appearance
	
	// Position
	@Column(name = "x")
	public int x;
	
	@Column(name = "y")
	public int y;
	
	@Column(name = "z")
	public int z;
	
	@Column(name = "heading")
	public int heading;
	
	// Position
	
	@Override
	public Object getPrimaryKey()
	{
		return objectId;
	}
	
	@Override
	protected Class<?> getClassForEqualsCheck()
	{
		return PlayerDB.class;
	}
	
	public static PlayerDB find(int objectId)
	{
		return L2Database.find(PlayerDB.class, objectId);
	}
	
	public static List<PlayerDB> findByAccount(final String accountName)
	{
		return L2Database.getResultListByNamedQuery("PlayerDB.findByAccount", new QueryConfigurator() {
			@Override
			public void configure(Query q)
			{
				q.setParameter("accountName", accountName);
			}
		});
	}
	
	public static List<PlayerDB> findAll()
	{
		return L2Database.getResultListByNamedQuery("PlayerDB.findAll");
	}
	
	public static void setOnlineStatus(final L2Player player, final boolean isOnline)
	{
		L2Database.executeUpdateByNamedQuery("PlayerDB.setOnline", new QueryConfigurator() {
			@Override
			public void configure(Query q)
			{
				q.setParameter("online", isOnline);
				q.setParameter("objectId", player.getObjectId());
			}
		});
	}
	
	public static void setOfflineAll()
	{
		L2Database.executeUpdateByNamedQuery("PlayerDB.setOfflineAll");
	}
}