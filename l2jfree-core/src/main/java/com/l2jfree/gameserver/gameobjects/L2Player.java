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

import com.l2jfree.gameserver.datatables.PlayerNameTable;
import com.l2jfree.gameserver.datatables.PlayerTemplateTable;
import com.l2jfree.gameserver.gameobjects.components.AppearanceComponent;
import com.l2jfree.gameserver.gameobjects.components.InventoryComponent;
import com.l2jfree.gameserver.gameobjects.components.KnownListComponent;
import com.l2jfree.gameserver.gameobjects.components.StatComponent;
import com.l2jfree.gameserver.gameobjects.components.ViewComponent;
import com.l2jfree.gameserver.gameobjects.components.interfaces.IPlayerStat;
import com.l2jfree.gameserver.gameobjects.components.interfaces.IPlayerView;
import com.l2jfree.gameserver.gameobjects.interfaces.IL2Playable;
import com.l2jfree.gameserver.gameobjects.player.PlayerAppearance;
import com.l2jfree.gameserver.gameobjects.player.PlayerInventory;
import com.l2jfree.gameserver.gameobjects.player.PlayerKnownList;
import com.l2jfree.gameserver.gameobjects.player.PlayerStat;
import com.l2jfree.gameserver.gameobjects.player.PlayerView;
import com.l2jfree.gameserver.network.client.Disconnection;
import com.l2jfree.gameserver.network.client.EmptyClient;
import com.l2jfree.gameserver.network.client.IL2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.gameserver.sql.PlayerDB;
import com.l2jfree.gameserver.templates.L2PlayerTemplate;
import com.l2jfree.gameserver.templates.player.ClassId;
import com.l2jfree.gameserver.templates.player.Gender;
import com.l2jfree.gameserver.util.IdFactory;
import com.l2jfree.gameserver.util.IdFactory.IdRange;
import com.l2jfree.gameserver.world.L2World;
import com.l2jfree.sql.L2Database;
import com.l2jfree.util.Rnd;

/**
 * @author NB4L1
 */
@KnownListComponent(PlayerKnownList.class)
@StatComponent(PlayerStat.class)
@ViewComponent(PlayerView.class)
@InventoryComponent(PlayerInventory.class)
@AppearanceComponent(PlayerAppearance.class)
public class L2Player extends L2Character implements IL2Playable, PlayerNameTable.IPlayerInfo
{
	public static L2Player create(String name, String accountName, ClassId classId)
	{
		return create(name, accountName, classId, Gender.Male, (byte)0, (byte)0, (byte)0); // TODO
	}
	
	public static L2Player create(String name, String accountName, ClassId classId, Gender gender, byte face,
			byte hairColor, byte hairStyle)
	{
		try
		{
			final int objectId = IdFactory.getInstance().getNextId(IdRange.PLAYERS);
			
			final PlayerDB playerDB = new PlayerDB();
			playerDB.objectId = objectId;
			playerDB.name = name;
			playerDB.accountName = accountName;
			playerDB.mainClassId = classId;
			playerDB.activeClassId = classId;
			
			// Appearance
			playerDB.gender = gender;
			playerDB.face = face;
			playerDB.hairColor = hairColor;
			playerDB.hairStyle = hairStyle;
			
			// Position
			playerDB.x = Rnd.get(1000);
			playerDB.y = Rnd.get(1000);
			playerDB.z = Rnd.get(1000);
			playerDB.heading = Rnd.get(1000);
			
			L2Database.persist(playerDB);
			
			return new L2Player(playerDB);
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static L2Player load(int objectId)
	{
		L2Player.disconnectIfOnline(objectId);
		
		try
		{
			final PlayerDB playerDB = PlayerDB.find(objectId);
			
			if (playerDB == null)
				return null;
			
			return new L2Player(playerDB);
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static void store(L2Player player)
	{
		try
		{
			final PlayerDB playerDB = new PlayerDB();
			playerDB.objectId = player.getObjectId();
			playerDB.name = player.getName();
			playerDB.accountName = player.getAccountName();
			playerDB.mainClassId = player.getMainClassId();
			playerDB.activeClassId = player.getActiveClassId();
			
			// Appearance
			final PlayerAppearance appearance = player.getAppearance();
			playerDB.gender = appearance.getGender();
			playerDB.face = appearance.getFace();
			playerDB.hairColor = appearance.getHairColor();
			playerDB.hairStyle = appearance.getHairStyle();
			
			// Position
			final ObjectPosition position = player.getPosition();
			playerDB.x = position.getX();
			playerDB.y = position.getY();
			playerDB.z = position.getZ();
			playerDB.heading = position.getHeading();
			
			L2Database.merge(playerDB);
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void disconnectIfOnline(int objectId)
	{
		L2Player onlinePlayer = L2World.findPlayer(objectId);
		
		if (onlinePlayer == null)
			onlinePlayer = L2World.findPlayer(PlayerNameTable.getInstance().getNameByObjectId(objectId));
		
		if (onlinePlayer == null)
			return;
		
		//if (!onlinePlayer.isInOfflineMode())
		_log.warn("Avoiding duplicate character! Disconnecting online character (" + onlinePlayer.getName() + ")");
		
		// FIXME won't be sent because client.close() clears the packet queue
		//onlinePlayer.sendPacket(SystemMessageId.ANOTHER_LOGIN_WITH_ACCOUNT);
		
		new Disconnection(onlinePlayer).defaultSequence(true);
	}
	
	private final String _accountName;
	
	private final ClassId _mainClassId;
	private ClassId _activeClassId;
	
	private String _name;
	private String _title;
	
	private final PlayerAppearance _appearance;
	private IL2Client _client = EmptyClient.getInstance();
	
	private L2Player(PlayerDB playerDB)
	{
		super(playerDB.objectId, PlayerTemplateTable.getInstance().getPlayerTemplate(playerDB.activeClassId));
		
		_accountName = playerDB.accountName;
		
		_mainClassId = playerDB.mainClassId;
		_activeClassId = playerDB.activeClassId;
		
		_appearance = AppearanceComponent.FACTORY.getComponent(this);
		_appearance.init(playerDB);
		
		setName(playerDB.name);
		getPosition().setXYZ(playerDB.x, playerDB.y, playerDB.z);
		getPosition().setHeading(playerDB.heading);
	}
	
	@Override
	public L2PlayerTemplate getTemplate()
	{
		return (L2PlayerTemplate)super.getTemplate();
	}
	
	@Override
	public IPlayerStat getStat()
	{
		return (IPlayerStat)super.getStat();
	}
	
	@Override
	public IPlayerView getView()
	{
		return (IPlayerView)super.getView();
	}
	
	@Override
	public String getAccountName()
	{
		return _accountName;
	}
	
	public ClassId getMainClassId()
	{
		return _mainClassId;
	}
	
	public ClassId getActiveClassId()
	{
		return _activeClassId;
	}
	
	public void setActiveClassId(ClassId activeClassId)
	{
		_activeClassId = activeClassId;
	}
	
	@Override
	public String getName()
	{
		return _name;
	}
	
	@Override
	public void setName(String name)
	{
		final String oldName = _name;
		
		_name = name;
		
		L2World.updateOnlinePlayer(this, oldName, name);
		
		PlayerNameTable.getInstance().update(this);
	}
	
	public String getTitle()
	{
		return _title;
	}
	
	public void setTitle(String title)
	{
		_title = title;
	}
	
	@Override
	public int getAccessLevel()
	{
		return 0; // FIXME
	}
	
	@Override
	public boolean isGM()
	{
		return false; // FIXME
	}
	
	public PlayerAppearance getAppearance()
	{
		return _appearance;
	}
	
	public IL2Client getClient()
	{
		return _client;
	}
	
	public void setClient(IL2Client client)
	{
		_client.setActiveChar(null);
		_client = client != null ? client : EmptyClient.getInstance();
		_client.setActiveChar(this);
	}
	
	@Override
	public boolean sendPacket(L2ServerPacket sp)
	{
		return getClient().sendPacket(sp);
	}
	
	@Override
	public boolean addToWorld()
	{
		if (!super.addToWorld())
			return false;
		
		// TODO
		
		PlayerDB.setOnlineStatus(this, false);
		return true;
	}
	
	@Override
	public boolean removeFromWorld()
	{
		synchronized (this)
		{
			if (getObjectState() == OBJECT_STATE_ALIVE)
			{
				new Disconnection(this).close(false).store();
			}
		}
		
		if (!super.removeFromWorld())
			return false;
		
		// TODO
		
		PlayerDB.setOnlineStatus(this, false);
		return true;
	}
	
	public boolean isOnline()
	{
		return getObjectState() == OBJECT_STATE_ALIVE;
	}
}
