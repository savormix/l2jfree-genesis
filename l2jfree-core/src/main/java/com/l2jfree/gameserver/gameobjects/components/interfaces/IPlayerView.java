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
package com.l2jfree.gameserver.gameobjects.components.interfaces;

import com.l2jfree.gameserver.templates.player.Gender;
import com.l2jfree.gameserver.templates.player.Race;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author NB4L1
 */
public interface IPlayerView extends ICharacterView, IElemental
{
	public int canEquipCloak();
	
	public boolean canUseDwarvenRecipes();
	
	public int canUseMinimap();
	
	public int getAbnormalEffect();
	
	public int getAccuracy();
	
	public int getActiveClassId();
	
	public int getAgathionId();
	
	public int getAllianceCrestId();
	
	public int getAllianceId();
	
	public double getAttackSpeedMultiplier();
	
	public int getCarriedWeight();
	
	public double getCollisionHeight();
	
	public double getCollisionRadius();
	
	public int getCON();
	
	public int getCriticalHit();
	
	public int[] getCubicData();
	
	public int getCurrentCP();
	
	public int getCurrentHP();
	
	public int getCurrentMP();
	
	public int getCursedWeaponLevel();
	
	public int getDEX();
	
	public int getDuelTeam();
	
	public int getEvasionRate();
	
	public long getExp();
	
	public double getExpPercent();
	
	public int getFace();
	
	public int getFamePoints();
	
	public int getFishLureX();
	
	public int getFishLureY();
	
	public int getFishLureZ();
	
	public int getFlyRunSpeed();
	
	public int getFlyWalkSpeed();
	
	public Gender getGender();
	
	public int getHairColor();
	
	public int getHairStyle();
	
	public int getHeading();
	
	public int getINT();
	
	public int getKarma();
	
	public int getLevel();
	
	public int getMainClassId();
	
	public int getMAtk();
	
	public int getMAtkSpd();
	
	public int getMaxCarriedWeight();
	
	public int getMaxCP();
	
	public int getMaxHP();
	
	public int getMaxInventorySlots();
	
	public int getMaxMP();
	
	public int getMaxSP();
	
	public int getMaxTalismanSlots();
	
	public int getMDef();
	
	public int getMEN();
	
	public int getMountNpcId();
	
	public int getMountType();
	
	public double getMovementSpeedMultiplier();
	
	public String getName();
	
	public int getNameColor();
	
	public int getPAtk();
	
	public int getPAtkSpd();
	
	public int getPDef();
	
	public int getPkCount();
	
	public int getPledgeCrestId();
	
	public int getPledgeId();
	
	public int getPledgeInsigniaId();
	
	public int getPledgePrivileges();
	
	public int getPledgeRank();
	
	public int getPledgeReputation();
	
	public int getPledgeUnit();
	
	public int getPrivateStoreType();
	
	public int getPvPCount();
	
	public Race getRace();
	
	public int getReceivedRecommendations();
	
	public int getRemainingRecommendations();
	
	public int getRunSpeed();
	
	public int getSiegeRelation();
	
	public int getSpecialEffect();
	
	public int getSTR();
	
	public int getSwimRunSpeed();
	
	public int getSwimWalkSpeed();
	
	public String getTitle();
	
	public int getTitleColor();
	
	public int getTransformationGraphicalId();
	
	public int getVehicleObjectId();
	
	public int getVitalityPoints();
	
	public int getWalkSpeed();
	
	public int getWeaponEnchantGlow();
	
	public int getWeaponStatus();
	
	public int getWIT();
	
	public int getX();
	
	public int getY();
	
	public int getZ();
	
	public int getDestinationX();
	
	public int getDestinationY();
	
	public int getDestinationZ();
	
	public boolean isFishing();
	
	public boolean isFlyingMounted();
	
	public boolean isGM();
	
	public boolean isHero();
	
	public boolean isInCombatAction();
	
	public int isInPvPAction();
	
	public boolean isInvisible();
	
	public boolean isLookingForParty();
	
	public boolean isLyingDead();
	
	public boolean isMoving();
	
	public boolean isNoble();
	
	public void refreshPosition();
	
	public void refreshDestinationPosition();
	
	public void refreshPaperDoll();
	
	public void writePaperDollObjectIds(MMOBuffer buf, boolean withAccessory);
	
	public void writePaperDollItemDisplayIds(MMOBuffer buf, boolean withAccessory);
	
	public void writePaperDollAugmentationIds(MMOBuffer buf, boolean withAccessory);
	
	public void writeCubics(MMOBuffer buf);
}
