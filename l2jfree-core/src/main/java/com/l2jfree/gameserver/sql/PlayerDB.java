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

import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.templates.player.ClassId;
import com.l2jfree.gameserver.templates.player.Gender;
import com.l2jfree.lang.L2System;
import com.l2jfree.sql.L2Database;
import com.l2jfree.sql.L2Database.QueryConfigurator;
import com.l2jfree.util.Introspection;

/**
 * @author NB4L1
 */
@Entity
@Table(name = "players")
@NamedQueries({
		@NamedQuery(name = "PlayerDB.findByAccount",
				query = "SELECT p FROM PlayerDB p WHERE p.accountName = :accountName"),
		@NamedQuery(name = "PlayerDB.findAll", query = "SELECT p FROM PlayerDB p"),
		@NamedQuery(name = "PlayerDB.setOnline",
				query = "UPDATE PlayerDB p SET p.online = :online WHERE p.objectId = :objectId"),
		@NamedQuery(name = "PlayerDB.setOfflineAll", query = "UPDATE PlayerDB p SET p.online = 0"),

})
public class PlayerDB
{
	public static final String TABLE = "players";
	
	public static final String OBJECT_ID = "objectId";
	public static final String NAME = "name";
	public static final String ACCOUNT_NAME = "accountName";
	public static final String ONLINE = "online";
	public static final String BASE_CLASS_ID = "baseClassId";
	public static final String ACTIVE_CLASS_ID = "activeClassId";
	
	// Appearance
	public static final String GENDER = "gender";
	public static final String FACE = "face";
	public static final String HAIR_COLOR = "hairColor";
	public static final String HAIR_STYLE = "hairStyle";
	
	// Position
	public static final String X = "x";
	public static final String Y = "y";
	public static final String Z = "z";
	public static final String HEADING = "heading";
	
	@Id
	@Column(name = OBJECT_ID, nullable = false, updatable = false)
	public int objectId;
	
	@Column(name = NAME, unique = true, nullable = false, length = 35)
	public String name;
	
	@Column(name = ACCOUNT_NAME, nullable = false, updatable = false, length = 45)
	public String accountName;
	
	@Column(name = ONLINE, nullable = false)
	public boolean online;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = BASE_CLASS_ID, nullable = false, updatable = false)
	public ClassId baseClassId;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = ACTIVE_CLASS_ID, nullable = false)
	public ClassId activeClassId;
	
	// Appearance
	@Enumerated(EnumType.ORDINAL)
	@Column(name = GENDER, nullable = false)
	public Gender gender;
	
	@Column(name = FACE, nullable = false)
	public byte face;
	
	@Column(name = HAIR_COLOR, nullable = false)
	public byte hairColor;
	
	@Column(name = HAIR_STYLE, nullable = false)
	public byte hairStyle;
	// Appearance
	
	// Position
	@Column(name = X)
	public int x;
	
	@Column(name = Y)
	public int y;
	
	@Column(name = Z)
	public int z;
	
	@Column(name = HEADING)
	public int heading;
	
	// Position
	
	@Override
	public int hashCode()
	{
		return L2System.hash(objectId);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof PlayerDB))
			return false;
		
		final PlayerDB other = (PlayerDB)obj;
		
		return L2System.equals(objectId, other.objectId);
	}
	
	@Override
	public String toString()
	{
		return Introspection.toString(this);
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
		L2Database.executeUpdateByNamedQuery("PlayerDB.setOfflineAll", new QueryConfigurator() {
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
