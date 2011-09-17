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
import com.l2jfree.gameserver.gameobjects.components.AIComponent;
import com.l2jfree.gameserver.gameobjects.components.AppearanceComponent;
import com.l2jfree.gameserver.gameobjects.components.InventoryComponent;
import com.l2jfree.gameserver.gameobjects.components.KnownListComponent;
import com.l2jfree.gameserver.gameobjects.components.PositionComponent;
import com.l2jfree.gameserver.gameobjects.components.StatComponent;
import com.l2jfree.gameserver.gameobjects.components.ViewComponent;
import com.l2jfree.gameserver.gameobjects.components.interfaces.IPlayerAI;
import com.l2jfree.gameserver.gameobjects.components.interfaces.IPlayerInventory;
import com.l2jfree.gameserver.gameobjects.components.interfaces.IPlayerStat;
import com.l2jfree.gameserver.gameobjects.components.interfaces.IPlayerView;
import com.l2jfree.gameserver.gameobjects.interfaces.IL2Playable;
import com.l2jfree.gameserver.gameobjects.player.PlayerAI;
import com.l2jfree.gameserver.gameobjects.player.PlayerAppearance;
import com.l2jfree.gameserver.gameobjects.player.PlayerInventory;
import com.l2jfree.gameserver.gameobjects.player.PlayerKnownList;
import com.l2jfree.gameserver.gameobjects.player.PlayerPosition;
import com.l2jfree.gameserver.gameobjects.player.PlayerStat;
import com.l2jfree.gameserver.gameobjects.player.PlayerView;
import com.l2jfree.gameserver.network.client.Disconnection;
import com.l2jfree.gameserver.network.client.EmptyClient;
import com.l2jfree.gameserver.network.client.IL2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.gameserver.network.client.packets.sendable.CreatureSay.Chat;
import com.l2jfree.gameserver.network.client.packets.sendable.CreatureSay.ChatMessage;
import com.l2jfree.gameserver.sql.PlayerDB;
import com.l2jfree.gameserver.templates.L2PlayerTemplate;
import com.l2jfree.gameserver.templates.player.ClassId;
import com.l2jfree.gameserver.util.IdFactory.IdRange;
import com.l2jfree.gameserver.util.PersistentId;
import com.l2jfree.gameserver.world.L2World;
import com.l2jfree.lang.L2TextBuilder;

/**
 * @author NB4L1
 */
@PositionComponent(PlayerPosition.class)
@KnownListComponent(PlayerKnownList.class)
@AIComponent(PlayerAI.class)
@StatComponent(PlayerStat.class)
@ViewComponent(PlayerView.class)
@InventoryComponent(PlayerInventory.class)
@AppearanceComponent(PlayerAppearance.class)
public class L2Player extends L2Character implements IL2Playable, PlayerNameTable.IPlayerInfo
{
	public static void disconnectIfOnline(PersistentId persistentId)
	{
		L2Player onlinePlayer = L2World.findPlayerByPersistentId(persistentId);
		
		if (onlinePlayer == null)
			onlinePlayer = L2World.findPlayer(PlayerNameTable.getInstance().getNameByPersistentId(persistentId));
		
		if (onlinePlayer == null)
			return;
		
		//if (!onlinePlayer.isInOfflineMode())
		_log.warn("Avoiding duplicate character! Disconnecting online character (" + onlinePlayer.getName() + ")");
		
		// FIXME won't be sent because client.close() clears the packet queue
		//onlinePlayer.sendPacket(SystemMessageId.ANOTHER_LOGIN_WITH_ACCOUNT);
		
		new Disconnection(onlinePlayer).defaultSequence(true);
	}
	
	private final PersistentId _persistentId;
	private final long _creationTime;
	
	private final String _accountName;
	
	private final ClassId _mainClassId;
	private ClassId _activeClassId;
	
	private String _name;
	private String _title;
	
	private final PlayerAppearance _appearance;
	private IL2Client _client = EmptyClient.getInstance();
	
	public L2Player(PlayerDB playerDB)
	{
		super(playerDB.activeClassId.getTemplate());
		getPosition().load(playerDB);
		
		_persistentId = playerDB.getPersistentId();
		_creationTime = playerDB.creationTime;
		_accountName = playerDB.accountName;
		
		_mainClassId = playerDB.mainClassId;
		_activeClassId = playerDB.activeClassId;
		
		_appearance = AppearanceComponent.Factory.INSTANCE.getComponent(this);
		getAppearance().load(playerDB);
		
		setName(playerDB.name);
		setTitle(playerDB.title);
	}
	
	@Override
	protected IdRange getIdRange()
	{
		return IdRange.PLAYERS;
	}
	
	@Override
	public String toString()
	{
		final L2TextBuilder tb = new L2TextBuilder();
		tb.append("(");
		tb.append(getClass().getSimpleName());
		tb.append(") objectId: ");
		tb.append(getObjectId());
		tb.append(" - persistentId: ");
		tb.append(getPersistentId());
		tb.append(" - name: ");
		tb.append(getName());
		
		return tb.moveToString();
	}
	
	/**
	 * Returns a <U>forever</U>-persistent ID that <U>always</U> maps to the <U>same</U> single
	 * player's character. After character deletion, this ID <B>may not be reclaimed</B>.
	 * 
	 * @return persistent ID
	 */
	@Override
	public PersistentId getPersistentId()
	{
		return _persistentId;
	}
	
	/**
	 * Returns a <U>forever</U>-persistent ID that <U>always</U> maps to the <U>same</U> single
	 * player's character. After character deletion, this ID <B>may not be reclaimed</B>.<br>
	 * Alternative way to access {@link #getPersistentId()}.
	 * 
	 * @return character's ID
	 */
	public PersistentId getCharacterId()
	{
		return getPersistentId();
	}
	
	public long getCreationTime()
	{
		return _creationTime;
	}
	
	@Override
	public L2PlayerTemplate getTemplate()
	{
		return (L2PlayerTemplate)super.getTemplate();
	}
	
	@Override
	public PlayerPosition getPosition()
	{
		return (PlayerPosition)super.getPosition();
	}
	
	@Override
	public IPlayerAI getAI()
	{
		return (IPlayerAI)super.getAI();
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
	public IPlayerInventory getInventory()
	{
		return (IPlayerInventory)super.getInventory();
	}
	
	public PlayerAppearance getAppearance()
	{
		return _appearance;
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
	
	public void store()
	{
		try
		{
			PlayerDB.store(this);
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
		}
	}
	
	// -------------------------
	// FIXME temporal solution , until we implement movement
	int _destinationX;
	int _destinationY;
	int _destinationZ;
	
	public int getDestinationX()
	{
		return _destinationX;
	}
	
	public void setDestination(int x, int y, int z)
	{
		_destinationX = x;
		_destinationY = y;
		_destinationZ = z;
	}
	
	public int getDestinationY()
	{
		return _destinationY;
	}
	
	public int getDestinationZ()
	{
		return _destinationZ;
	}
	
	// -------------------------
	
	public void sendCMessage(Chat chatType, String sender, String message)
	{
		ChatMessage cm = new ChatMessage(chatType, sender, message);
		sendPacket(cm);
	}
}
