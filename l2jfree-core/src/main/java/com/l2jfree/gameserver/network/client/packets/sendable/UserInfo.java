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
package com.l2jfree.gameserver.network.client.packets.sendable;

import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.gameobjects.ObjectPosition;
import com.l2jfree.gameserver.gameobjects.player.PlayerAppearance;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.gameserver.templates.L2PlayerTemplate;
import com.l2jfree.gameserver.templates.player.PlayerBaseTemplate;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix (generated)
 */
public abstract class UserInfo extends L2ServerPacket
{
	/**
	 * A nicer name for {@link UserInfo}.
	 * 
	 * @author savormix (generated)
	 * @see UserInfo
	 */
	public static final class MyPlayerInfo extends UserInfo
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see UserInfo#UserInfo()
		 */
		public MyPlayerInfo()
		{
		}
	}
	
	/** Constructs this packet. */
	public UserInfo()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x32;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		
		// FIXME: use views to provide data reliably or no longer applicable?
		final PlayerAppearance appearance = activeChar.getAppearance();
		final ObjectPosition position = activeChar.getPosition();
		final L2PlayerTemplate template = activeChar.getTemplate();
		final PlayerBaseTemplate baseTemplate = template.getPlayerBaseTemplate(appearance.getGender());
		
		buf.writeD(position.getX()); // Location X
		buf.writeD(position.getY()); // Location Y
		buf.writeD(position.getZ()); // Location Z
		buf.writeD(0); // Vehicle OID
		buf.writeD(activeChar.getObjectId()); // My OID
		buf.writeS(activeChar.getName()); // Name
		buf.writeD(baseTemplate.getRace()); // Race
		buf.writeD(appearance.getGender()); // Sex
		buf.writeD(template.getClassId().getId()); // Main class
		buf.writeD(1); // Level
		buf.writeQ(0L); // XP
		buf.writeF(0D); // XP %
		buf.writeD(1); // STR
		buf.writeD(1); // DEX
		buf.writeD(1); // CON
		buf.writeD(1); // INT
		buf.writeD(1); // WIT
		buf.writeD(1); // MEN
		buf.writeD(100); // Maximum HP
		buf.writeD(100); // Current HP
		buf.writeD(30); // Maximum MP
		buf.writeD(30); // Current MP
		buf.writeD(0); // SP
		buf.writeD(0); // Current carried weight
		buf.writeD(1); // Maximum carried weight
		buf.writeD(20); // Weapon status
		buf.writeD(0); // Shirt OID
		buf.writeD(0); // Right earring OID
		buf.writeD(0); // Left earring OID
		buf.writeD(0); // Necklace OID
		buf.writeD(0); // Right ring OID
		buf.writeD(0); // Left ring OID
		buf.writeD(0); // Helmet OID
		buf.writeD(0); // Main weapon OID
		buf.writeD(0); // Shield/support weapon OID
		buf.writeD(0); // Gloves OID
		buf.writeD(0); // Chest armor OID
		buf.writeD(0); // Leg armor OID
		buf.writeD(0); // Boots OID
		buf.writeD(0); // Cloak OID
		buf.writeD(0); // Two-handed weapon OID
		buf.writeD(0); // 1st hair item OID
		buf.writeD(0); // 2nd hair item OID
		buf.writeD(0); // Right bracelet OID
		buf.writeD(0); // Left bracelet OID
		buf.writeD(0); // 1st talisman OID
		buf.writeD(0); // 2nd talisman OID
		buf.writeD(0); // 3rd talisman OID
		buf.writeD(0); // 4th talisman OID
		buf.writeD(0); // 5th talisman OID
		buf.writeD(0); // 6th talisman OID
		buf.writeD(0); // Belt OID
		buf.writeD(0); // Shirt
		buf.writeD(0); // Right earring
		buf.writeD(0); // Left earring
		buf.writeD(0); // Necklace
		buf.writeD(0); // Right ring
		buf.writeD(0); // Left ring
		buf.writeD(0); // Helmet
		buf.writeD(0); // Main weapon
		buf.writeD(0); // Shield/support weapon
		buf.writeD(0); // Gloves
		buf.writeD(0); // Chest armor
		buf.writeD(0); // Leg armor
		buf.writeD(0); // Boots
		buf.writeD(0); // Cloak
		buf.writeD(0); // Two-handed weapon
		buf.writeD(0); // 1st hair item
		buf.writeD(0); // 2nd hair item
		buf.writeD(0); // Right bracelet
		buf.writeD(0); // Left bracelet
		buf.writeD(0); // 1st talisman
		buf.writeD(0); // 2nd talisman
		buf.writeD(0); // 3rd talisman
		buf.writeD(0); // 4th talisman
		buf.writeD(0); // 5th talisman
		buf.writeD(0); // 6th talisman
		buf.writeD(0); // Belt
		buf.writeD(0); // Shirt augmentation
		buf.writeD(0); // Right earring augmentation
		buf.writeD(0); // Left earring augmentation
		buf.writeD(0); // Necklace augmentation
		buf.writeD(0); // Right ring augmentation
		buf.writeD(0); // Left ring augmentation
		buf.writeD(0); // Helmet augmentation
		buf.writeD(0); // Main weapon augmentation
		buf.writeD(0); // Shield/support weapon augmentation
		buf.writeD(0); // Gloves augmentation
		buf.writeD(0); // Chest armor augmentation
		buf.writeD(0); // Leg armor augmentation
		buf.writeD(0); // Boots augmentation
		buf.writeD(0); // Cloak augmentation
		buf.writeD(0); // Two-handed weapon augmentation
		buf.writeD(0); // 1st hair item augmentation
		buf.writeD(0); // 2nd hair item augmentation
		buf.writeD(0); // Right bracelet augmentation
		buf.writeD(0); // Left bracelet augmentation
		buf.writeD(0); // 1st talisman augmentation
		buf.writeD(0); // 2nd talisman augmentation
		buf.writeD(0); // 3rd talisman augmentation
		buf.writeD(0); // 4th talisman augmentation
		buf.writeD(0); // 5th talisman augmentation
		buf.writeD(0); // 6th talisman augmentation
		buf.writeD(0); // Belt augmentation
		buf.writeD(0); // Talisman slots
		buf.writeD(0); // Can equip cloak
		buf.writeD(1); // P. Atk.
		buf.writeD(100); // Attack speed
		buf.writeD(1); // P. Def.
		buf.writeD(1); // Evasion
		buf.writeD(1); // Accuracy
		buf.writeD(1); // Critical
		buf.writeD(1); // M. Atk.
		buf.writeD(111); // Casting speed
		buf.writeD(100); // Attack speed (dupe)
		buf.writeD(1); // M. Def.
		buf.writeD(0); // In PvP
		buf.writeD(0); // Karma
		buf.writeD(baseTemplate.getRunSpeed()); // Running speed (on ground)
		buf.writeD(baseTemplate.getWalkSpeed()); // Walking speed (on ground)
		buf.writeD(baseTemplate.getRunSpeedInWater()); // Running speed (in water)
		buf.writeD(baseTemplate.getWalkSpeedInWater()); // Walking speed (in water)
		buf.writeD(0); // Running speed (in air) ???
		buf.writeD(0); // Walking speed (in air) ???
		buf.writeD(0); // Running speed (in air) while mounted?
		buf.writeD(0); // Walking speed (in air) while mounted?
		buf.writeF(1D); // Movement speed multiplier
		buf.writeF(1D); // Attack speed multiplier
		buf.writeF(baseTemplate.getCollisionRadius()); // Collision radius
		buf.writeF(baseTemplate.getCollisionHeight()); // Collision height
		buf.writeD(appearance.getHairStyle()); // Hair style
		buf.writeD(appearance.getHairColor()); // Hair color
		buf.writeD(appearance.getFace()); // Face
		buf.writeD(0); // Game Master
		buf.writeS(activeChar.getTitle()); // Title
		buf.writeD(0); // Pledge ID
		buf.writeD(0); // Pledge crest ID
		buf.writeD(0); // Alliance ID
		buf.writeD(0); // Alliance crest ID
		buf.writeD(0); // Siege participation
		buf.writeC(0); // Mount type
		buf.writeC(0); // Private store
		buf.writeC(0); // Can use dwarven recipes
		buf.writeD(0); // PK Count
		buf.writeD(0); // PvP Count
		final int sizeA = 0; // Cubic count
		buf.writeH(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeH(0); // Cubic
		}
		buf.writeC(0); // Looking for party
		buf.writeD(0); // Abnormal effect
		buf.writeC(0); // Flying with mount
		buf.writeD(0); // Pledge privileges
		buf.writeH(0); // Recomendations
		buf.writeH(0); // Evaluation score
		buf.writeD(0); // Mount
		buf.writeH(10); // Inventory slots
		buf.writeD(template.getClassId().getId()); // Current class
		buf.writeD(0); // 0
		buf.writeD(80); // Maximum CP
		buf.writeD(80); // Current CP
		buf.writeC(0); // Weapon enchant glow
		buf.writeC(0); // Duel team
		buf.writeD(0); // Pledge insignia ID
		buf.writeC(0); // Noble
		buf.writeC(0); // Hero
		buf.writeC(0); // Fishing
		buf.writeD(0); // Fishing lure X
		buf.writeD(0); // Fishing lure Y
		buf.writeD(0); // Fishing lure Z
		buf.writeD(0); // Name color
		buf.writeC(0); // Moving
		buf.writeD(0); // Pledge rank
		buf.writeD(0); // Pledge unit
		buf.writeD(0); // Title color
		buf.writeD(0); // Cursed weapon level
		buf.writeD(0); // Transformation
		buf.writeH(0); // Attack element
		buf.writeH(0); // Attack element power
		buf.writeH(0); // Fire defense
		buf.writeH(0); // Water defense
		buf.writeH(0); // Wind defense
		buf.writeH(0); // Earth defense
		buf.writeH(0); // Holy defense
		buf.writeH(0); // Dark defense
		buf.writeD(0); // Agathion
		buf.writeD(0); // Fame
		buf.writeD(0); // Can use minimap
		buf.writeD(0); // Vitality
		buf.writeD(0); // Special effect
	}
}
