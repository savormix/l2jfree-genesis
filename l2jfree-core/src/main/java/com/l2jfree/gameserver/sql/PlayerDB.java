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
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.templates.player.ClassId;
import com.l2jfree.gameserver.templates.player.Gender;
import com.l2jfree.gameserver.util.PersistentId;
import com.l2jfree.sql.L2DBEntity;
import com.l2jfree.sql.L2Database;
import com.l2jfree.sql.L2Database.QueryConfigurator;

/**
 * @author NB4L1
 */
@Entity
@Table(name = "players", uniqueConstraints = { @UniqueConstraint(columnNames = "name") })
@SecondaryTables({ @SecondaryTable(name = "player_appearances"), @SecondaryTable(name = "player_positions") })
@NamedQueries({
		@NamedQuery(name = "PlayerDB.findByAccount",
				query = "SELECT p FROM PlayerDB p WHERE p.accountName = :accountName"),
		@NamedQuery(name = "PlayerDB.setOnline",
				query = "UPDATE PlayerDB p SET p.online = :online WHERE p.persistentId = :persistentId"),
		@NamedQuery(name = "PlayerDB.setOfflineAll", query = "UPDATE PlayerDB p SET p.online = 0")

})
public class PlayerDB extends L2DBEntity
{
	@Id
	@Column(name = "persistentId", nullable = false, updatable = false)
	private Integer persistentId;
	
	@Column(name = "creationTime", nullable = false, updatable = false)
	public long creationTime;
	
	@Column(name = "name", unique = true, nullable = false, length = 35)
	public String name;
	
	@Column(name = "accountName", nullable = false, updatable = false, length = 45)
	public String accountName;
	
	@Column(name = "online", nullable = false)
	public boolean online;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "mainClassId", nullable = false, updatable = false)
	public ClassId mainClassId;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "activeClassId", nullable = false)
	public ClassId activeClassId;
	
	// Appearance
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "gender", nullable = false, table = "player_appearances")
	public Gender gender;
	
	@Column(name = "face", nullable = false, table = "player_appearances")
	public byte face;
	
	@Column(name = "hairColor", nullable = false, table = "player_appearances")
	public byte hairColor;
	
	@Column(name = "hairStyle", nullable = false, table = "player_appearances")
	public byte hairStyle;
	// Appearance
	
	// Position
	@Column(name = "x", table = "player_positions")
	public int x;
	
	@Column(name = "y", table = "player_positions")
	public int y;
	
	@Column(name = "z", table = "player_positions")
	public int z;
	
	@Column(name = "heading", table = "player_positions")
	public int heading;
	
	// Position
	
	public PersistentId getPersistentId()
	{
		return new PersistentId(persistentId);
	}
	
	public void setPersistentId(PersistentId persistentId)
	{
		this.persistentId = persistentId.intValue();
	}
	
	@Override
	public Object getPrimaryKey()
	{
		return persistentId;
	}
	
	@Override
	protected Class<?> getClassForEqualsCheck()
	{
		return PlayerDB.class;
	}
	
	public static PlayerDB find(PersistentId persistentId)
	{
		return L2Database.find(PlayerDB.class, persistentId.intValue());
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
	
	public static void setOnlineStatus(final L2Player player, final boolean isOnline)
	{
		L2Database.executeUpdateByNamedQuery("PlayerDB.setOnline", new QueryConfigurator() {
			@Override
			public void configure(Query q)
			{
				q.setParameter("online", isOnline);
				q.setParameter("persistentId", player.getPersistentId());
			}
		});
	}
	
	public static void setOfflineAll()
	{
		L2Database.executeUpdateByNamedQuery("PlayerDB.setOfflineAll");
	}
}
