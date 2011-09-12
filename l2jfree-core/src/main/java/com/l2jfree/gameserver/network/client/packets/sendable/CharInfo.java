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
import com.l2jfree.gameserver.gameobjects.components.interfaces.IPlayerView;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author NB4L1
 * @author savormix (generated)
 */
public abstract class CharInfo extends StaticPacket
{
	/**
	 * A nicer name for {@link CharInfo}.
	 * 
	 * @author savormix (generated)
	 * @see CharInfo
	 */
	public static final class PlayerInfo extends CharInfo
	{
		/** This packet. */
		public static final PlayerInfo PACKET = new PlayerInfo();
		
		/**
		 * Constructs this packet.
		 * 
		 * @see CharInfo#CharInfo()
		 */
		private PlayerInfo()
		{
		}
	}
	
	/** Constructs this packet. */
	public CharInfo()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x31;
	}
	
	@Override
	public void prepareToSend(L2Client client, L2Player activeChar)
	{
		activeChar.getView().refresh();
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		final IPlayerView view = activeChar.getView();
		
		buf.writeD(view.getX()); // Location X
		buf.writeD(view.getY()); // Location Y
		buf.writeD(view.getZ()); // Location Z
		buf.writeD(view.getVehicleObjectId()); // Vehicle OID
		buf.writeD(view.getObjectId()); // Player OID
		buf.writeS(view.getName()); // Name
		buf.writeD(view.getRace()); // Race
		buf.writeD(view.getGender()); // Sex
		buf.writeD(view.getMainClassId()); // Main class
		
		view.writePaperDollItemDisplayIds(buf, false); // Equipment
		view.writePaperDollAugmentationIds(buf, false); // Equipment augmentations
		
		buf.writeD(view.getMaxTalismanSlots()); // Talisman slots
		buf.writeD(view.canEquipCloak()); // Can equip cloak
		buf.writeD(view.isInPvPAction()); // In PvP
		buf.writeD(view.getKarma()); // Karma
		buf.writeD(view.getMAtkSpd()); // Casting speed
		buf.writeD(view.getPAtkSpd()); // Attack speed
		buf.writeD(0); // 0
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
		buf.writeS(view.getTitle()); // Title
		buf.writeD(view.getPledgeId()); // Pledge ID
		buf.writeD(view.getPledgeCrestId()); // Pledge crest ID
		buf.writeD(view.getAllianceId()); // Alliance ID
		buf.writeD(view.getAllianceCrestId()); // Alliance crest ID
		buf.writeC(0); // Waiting FIXME isSitting()
		buf.writeC(view.isMoving()); // Moving FIXME isRunning()
		buf.writeC(view.isInCombatAction()); // In combat
		buf.writeC(view.isLyingDead()); // Lying dead
		buf.writeC(view.isInvisible()); // Invisible
		buf.writeC(view.getMountType()); // Mount type
		buf.writeC(view.getPrivateStoreType()); // Private store
		
		view.writeCubics(buf); // Cubics
		
		buf.writeC(view.isLookingForParty()); // Looking for party
		buf.writeD(view.getAbnormalEffect()); // Abnormal effect
		buf.writeC(view.isFlyingMounted()); // Flying with mount
		buf.writeH(view.getReceivedRecommendations()); // Evaluation score
		buf.writeD(view.getMountNpcId()); // Mount
		buf.writeD(view.getActiveClassId()); // Current class
		buf.writeD(0); // 0
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
		buf.writeD(view.getHeading()); // Heading
		buf.writeD(view.getPledgeRank()); // Pledge rank
		buf.writeD(view.getPledgeUnit()); // Pledge unit
		buf.writeD(view.getTitleColor()); // Title color
		buf.writeD(view.getCursedWeaponLevel()); // Cursed weapon level
		buf.writeD(view.getPledgeReputation()); // Pledge reputation
		buf.writeD(view.getTransformationGraphicalId()); // Transformation
		buf.writeD(view.getAgathionId()); // Agathion
		buf.writeD(0); // ???
		buf.writeD(view.getSpecialEffect()); // Special effect
	}
}
