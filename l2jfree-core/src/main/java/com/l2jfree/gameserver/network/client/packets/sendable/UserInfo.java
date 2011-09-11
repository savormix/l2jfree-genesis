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
import com.l2jfree.gameserver.gameobjects.player.PlayerView;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author hex1r0
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
		activeChar.getView().refresh();
		
		final PlayerView view = activeChar.getView();
		
		buf.writeD(view.getX()); // Location X
		buf.writeD(view.getY()); // Location Y
		buf.writeD(view.getZ()); // Location Z
		buf.writeD(view.getVehicleObjectId()); // Vehicle OID
		buf.writeD(view.getObjectId()); // My OID
		buf.writeS(view.getName()); // Name
		buf.writeD(view.getRace()); // Race
		buf.writeD(view.getGender()); // Sex
		buf.writeD(view.getMainClassId()); // Main class
		buf.writeD(view.getLevel()); // Level
		buf.writeQ(view.getExp()); // XP
		buf.writeF(view.getExpPercent()); // XP %
		buf.writeD(view.getSTR()); // STR
		buf.writeD(view.getDEX()); // DEX
		buf.writeD(view.getCON()); // CON
		buf.writeD(view.getINT()); // INT
		buf.writeD(view.getWIT()); // WIT
		buf.writeD(view.getMEN()); // MEN
		buf.writeD(view.getMaxHP()); // Maximum HP
		buf.writeD(view.getCurrentHP()); // Current HP
		buf.writeD(view.getMaxMP()); // Maximum MP
		buf.writeD(view.getCurrentMP()); // Current MP
		buf.writeD(view.getMaxSP()); // SP
		buf.writeD(view.getCarriedWeight()); // Current carried weight
		buf.writeD(view.getMaxCarriedWeight()); // Maximum carried weight
		buf.writeD(view.getWeaponStatus()); // Weapon status
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
		buf.writeD(view.getMaxTalismanSlots()); // Talisman slots
		buf.writeD(view.canEquipCloak()); // Can equip cloak
		buf.writeD(view.getPAtk()); // P. Atk.
		buf.writeD(view.getPAtkSpd()); // Attack speed
		buf.writeD(view.getPDef()); // P. Def.
		buf.writeD(view.getEvasionRate()); // Evasion
		buf.writeD(view.getAccuracy()); // Accuracy
		buf.writeD(view.getCriticalHit()); // Critical
		buf.writeD(view.getMAtk()); // M. Atk.
		buf.writeD(view.getMAtkSpd()); // Casting speed
		buf.writeD(100); // Attack speed (dupe) FIXME duplication?
		buf.writeD(view.getMDef()); // M. Def.
		buf.writeD(view.isInPvPAction()); // In PvP
		buf.writeD(view.getKarma()); // Karma
		buf.writeD(view.getRunSpeed()); // Running speed (on ground)
		buf.writeD(view.getWalkSpeed()); // Walking speed (on ground)
		buf.writeD(view.getSwimRunSpeed()); // Running speed (in water)
		buf.writeD(view.getSwimWalkSpeed()); // Walking speed (in water)
		buf.writeD(0); // Running speed (in air) ???
		buf.writeD(0); // Walking speed (in air) ???
		buf.writeD(0); // Running speed (in air) while mounted?
		buf.writeD(0); // Walking speed (in air) while mounted?
		buf.writeF(view.getMovementSpeedMultiplier()); // Movement speed multiplier
		buf.writeF(view.getAttackSpeedMultiplier()); // Attack speed multiplier
		buf.writeF(view.getCollisionRadius()); // Collision radius
		buf.writeF(view.getCollisionHeight()); // Collision height
		buf.writeD(view.getHairStyle()); // Hair style
		buf.writeD(view.getHairColor()); // Hair color
		buf.writeD(view.getFace()); // Face
		buf.writeD(view.isGM()); // Game Master
		buf.writeS(view.getTitle()); // Title
		buf.writeD(view.getPledgeId()); // Pledge ID
		buf.writeD(view.getPledgeCrestId()); // Pledge crest ID
		buf.writeD(view.getAllianceId()); // Alliance ID
		buf.writeD(view.getAllianceCrestId()); // Alliance crest ID
		buf.writeD(view.getSiegeRelation()); // Siege participation
		buf.writeC(view.getMountType()); // Mount type
		buf.writeC(view.getPrivateStoreType()); // Private store
		buf.writeC(view.canUseDwarvenRecipes()); // Can use dwarven recipes
		buf.writeD(view.getPkCount()); // PK Count
		buf.writeD(view.getPvPCount()); // PvP Count
		final int sizeA = 0; // Cubic count
		buf.writeH(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeH(0); // Cubic
		}
		buf.writeC(view.isLookingForParty()); // Looking for party
		buf.writeD(view.getAbnormalEffect()); // Abnormal effect
		buf.writeC(view.isFlyingMounted()); // Flying with mount
		buf.writeD(view.getPledgePrivileges()); // Pledge privileges
		buf.writeH(view.getRemainingRecommendations()); // Recommendations
		buf.writeH(view.getReceivedRecommendations()); // Evaluation score
		buf.writeD(view.getMountNpcId()); // Mount
		buf.writeH(view.getMaxInventorySlots()); // Inventory slots
		buf.writeD(view.getActiveClassId()); // Current class
		buf.writeD(0); // 0
		buf.writeD(view.getMaxCP()); // Maximum CP
		buf.writeD(view.getCurrentCP()); // Current CP
		buf.writeC(view.getWeaponEnchantGlow()); // Weapon enchant glow
		buf.writeC(view.getDuelTeam()); // Duel team
		buf.writeD(view.getPledgeInsigniaId()); // Pledge insignia ID
		buf.writeC(view.isNoble()); // Noble
		buf.writeC(view.isHero()); // Hero
		buf.writeC(view.isFishing()); // Fishing
		buf.writeD(view.getFishLureX()); // Fishing lure X
		buf.writeD(view.getFishLureY()); // Fishing lure Y
		buf.writeD(view.getFishLureZ()); // Fishing lure Z
		buf.writeD(view.getNameColor()); // Name color
		buf.writeC(view.isMoving()); // Moving
		buf.writeD(view.getPledgeRank()); // Pledge rank
		buf.writeD(view.getPledgeUnit()); // Pledge unit
		buf.writeD(view.getTitleColor()); // Title color
		buf.writeD(view.getCursedWeaponLevel()); // Cursed weapon level
		buf.writeD(view.getTransformationGraphicalId()); // Transformation
		buf.writeH(0); // Attack element
		buf.writeH(0); // Attack element power
		buf.writeH(0); // Fire defense
		buf.writeH(0); // Water defense
		buf.writeH(0); // Wind defense
		buf.writeH(0); // Earth defense
		buf.writeH(0); // Holy defense
		buf.writeH(0); // Dark defense
		buf.writeD(view.getAgathionId()); // Agathion
		buf.writeD(view.getFamePoints()); // Fame
		buf.writeD(view.canUseMinimap()); // Can use minimap
		buf.writeD(view.getVitalityPoints()); // Vitality
		buf.writeD(view.getSpecialEffect()); // Special effect
	}
}
