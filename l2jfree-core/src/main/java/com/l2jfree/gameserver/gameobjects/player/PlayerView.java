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
package com.l2jfree.gameserver.gameobjects.player;

import java.util.ArrayList;
import java.util.List;

import com.l2jfree.gameserver.gameobjects.CharacterView;
import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.gameobjects.ObjectPosition;
import com.l2jfree.gameserver.gameobjects.player.PlayerInventory.PaperDollSlot;
import com.l2jfree.gameserver.templates.player.Gender;
import com.l2jfree.gameserver.templates.player.PlayerBaseTemplate;
import com.l2jfree.gameserver.templates.player.Race;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author hex1r0
 * @author NB4L1
 */
public class PlayerView extends CharacterView
{
	private int _abnormalEffect;
	private int _accuracy;
	private int _activeClassId;
	private int _agathionId;
	private int _allianceCrestId;
	private int _allianceId;
	private int _attackElementPower;
	private int _attackElementType;
	private double _attackSpeedMultiplier;
	private int _carriedWeight;
	private double _collisionHeight;
	private double _collisionRadius;
	private boolean _combatAction;
	private int _con;
	private int _criticalHit;
	private int[] _cubics;
	private int _currentCp;
	private int _currentHp;
	private int _currentMp;
	private int _cursedWeaponLevel;
	private int _darkElementDefence;
	private int _dex;
	private int _duelTeam;
	private int _earthElementDefence;
	private int _equipCloak;
	private int _evasionRate;
	private long _exp;
	private int _expPercent;
	private byte _face;
	private int _famePoints;
	private int _fireElementDefence;
	private boolean _fishing;
	private int _fishLureX;
	private int _fishLureY;
	private int _fishLureZ;
	private boolean _flyingMounted;
	private int _flyRunSpeed;
	private int _flyWalkSpeed;
	private Gender _gender;
	private boolean _gm;
	private byte _hairColor;
	private byte _hairStyle;
	private int _heading;
	private boolean _hero;
	private int _holyElementDefence;
	private int _inPvPAction;
	private int _int;
	private boolean _invisible;
	private int _karmaPoints;
	private int _level;
	private boolean _lookingForParty;
	private boolean _lyingDead;
	private int _mainClassId;
	private int _mAtk;
	private int _mAtkSpd;
	private int _maxCarriedWeight;
	private int _maxCp;
	private int _maxHp;
	private int _maxInventorySlots;
	private int _maxMp;
	private int _maxSp;
	private int _maxTalismanSlots;
	private int _mDef;
	private int _men;
	private int _mountNpcId;
	private int _mountType;
	private double _movementSpeedMultiplier;
	private boolean _moving;
	private String _name;
	private int _nameColor;
	private boolean _noble;
	private int _objectId;
	private int _pAtk;
	private int _pAtkSpd;
	private int _pDef;
	private int _pkCount;
	private int _pledgeCrestId;
	private int _pledgeId;
	private int _pledgeInsigniaId;
	private int _pledgePrivileges;
	private int _pledgeRank;
	private int _pledgeReputation;
	private int _pledgeUnit;
	private int _privateStoreType;
	private int _pvpCount;
	private Race _race;
	private int _receivedRecommendations;
	private int _remainingRecommendaions;
	private int _runSpeed;
	private int _siegeRelation;
	private int _specialEffect;
	private int _str;
	private String _title;
	private int _titleColor;
	private int _transformationGraphicalId;
	private boolean _useDwarvenRecipes;
	private int _useMinimap;
	private int _vehicleObjectId;
	private int _vitalityPoints;
	private int _walkSpeed;
	private int _waterElementDefence;
	private int _weaponEnchantGlow;
	private int _weaponStatus;
	private int _windElementDefence;
	private int _wit;
	private int _x;
	private int _y;
	private int _z;
	
	public PlayerView(L2Player activeChar)
	{
		super(activeChar);
	}
	
	@Override
	public final L2Player getActiveChar()
	{
		return (L2Player)super.getActiveChar();
	}
	
	public int canEquipCloak()
	{
		return _equipCloak;
	}
	
	public boolean canUseDwarvenRecipes()
	{
		return _useDwarvenRecipes;
	}
	
	public int canUseMinimap()
	{
		return _useMinimap;
	}
	
	public int getAbnormalEffect()
	{
		return _abnormalEffect;
	}
	
	public int getAccuracy()
	{
		return _accuracy;
	}
	
	public int getActiveClassId()
	{
		return _activeClassId;
	}
	
	public int getAgathionId()
	{
		return _agathionId;
	}
	
	public int getAllianceCrestId()
	{
		return _allianceCrestId;
	}
	
	public int getAllianceId()
	{
		return _allianceId;
	}
	
	public int getAttackElementPower()
	{
		return _attackElementPower;
	}
	
	public int getAttackElementType()
	{
		return _attackElementType;
	}
	
	public double getAttackSpeedMultiplier()
	{
		return _attackSpeedMultiplier;
	}
	
	public int getCarriedWeight()
	{
		return _carriedWeight;
	}
	
	public double getCollisionHeight()
	{
		return _collisionHeight;
	}
	
	public double getCollisionRadius()
	{
		return _collisionRadius;
	}
	
	public int getCON()
	{
		return _con;
	}
	
	public int getCriticalHit()
	{
		return _criticalHit;
	}
	
	public int[] getCubicData()
	{
		return _cubics;
	}
	
	public int getCurrentCP()
	{
		return _currentCp;
	}
	
	public int getCurrentHP()
	{
		return _currentHp;
	}
	
	public int getCurrentMP()
	{
		return _currentMp;
	}
	
	public int getCursedWeaponLevel()
	{
		return _cursedWeaponLevel;
	}
	
	public int getDarkElementDefence()
	{
		return _darkElementDefence;
	}
	
	public int getDEX()
	{
		return _dex;
	}
	
	public int getDuelTeam()
	{
		return _duelTeam;
	}
	
	public int getEarthElementDefence()
	{
		return _earthElementDefence;
	}
	
	public int getEvasionRate()
	{
		return _evasionRate;
	}
	
	public long getExp()
	{
		return _exp;
	}
	
	public double getExpPercent()
	{
		return _expPercent;
	}
	
	public int getFace()
	{
		return _face;
	}
	
	public int getFamePoints()
	{
		return _famePoints;
	}
	
	public int getFireElementDefence()
	{
		return _fireElementDefence;
	}
	
	public int getFishLureX()
	{
		return _fishLureX;
	}
	
	public int getFishLureY()
	{
		return _fishLureY;
	}
	
	public int getFishLureZ()
	{
		return _fishLureZ;
	}
	
	public int getFlyRunSpeed()
	{
		return _flyRunSpeed;
	}
	
	public int getFlyWalkSpeed()
	{
		return _flyWalkSpeed;
	}
	
	public Gender getGender()
	{
		return _gender;
	}
	
	public int getHairColor()
	{
		return _hairColor;
	}
	
	public int getHairStyle()
	{
		return _hairStyle;
	}
	
	public int getHeading()
	{
		return _heading;
	}
	
	public int getHolyElementDefence()
	{
		return _holyElementDefence;
	}
	
	public int getINT()
	{
		return _int;
	}
	
	public int getKarma()
	{
		return _karmaPoints;
	}
	
	public int getLevel()
	{
		return _level;
	}
	
	public int getMainClassId()
	{
		return _mainClassId;
	}
	
	public int getMAtk()
	{
		return _mAtk;
	}
	
	public int getMAtkSpd()
	{
		return _mAtkSpd;
	}
	
	public int getMaxCarriedWeight()
	{
		return _maxCarriedWeight;
	}
	
	public int getMaxCP()
	{
		return _maxCp;
	}
	
	public int getMaxHP()
	{
		return _maxHp;
	}
	
	public int getMaxInventorySlots()
	{
		return _maxInventorySlots;
	}
	
	public int getMaxMP()
	{
		return _maxMp;
	}
	
	public int getMaxSP()
	{
		return _maxSp;
	}
	
	public int getMaxTalismanSlots()
	{
		return _maxTalismanSlots;
	}
	
	public int getMDef()
	{
		return _mDef;
	}
	
	public int getMEN()
	{
		return _men;
	}
	
	public int getMountNpcId()
	{
		return _mountNpcId;
	}
	
	public int getMountType()
	{
		return _mountType;
	}
	
	public double getMovementSpeedMultiplier()
	{
		return _movementSpeedMultiplier;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public int getNameColor()
	{
		return _nameColor;
	}
	
	public int getObjectId()
	{
		return _objectId;
	}
	
	public int getPAtk()
	{
		return _pAtk;
	}
	
	public int getPAtkSpd()
	{
		return _pAtkSpd;
	}
	
	public int getPDef()
	{
		return _pDef;
	}
	
	public int getPkCount()
	{
		return _pkCount;
	}
	
	public int getPledgeCrestId()
	{
		return _pledgeCrestId;
	}
	
	public int getPledgeId()
	{
		return _pledgeId;
	}
	
	public int getPledgeInsigniaId()
	{
		return _pledgeInsigniaId;
	}
	
	public int getPledgePrivileges()
	{
		return _pledgePrivileges;
	}
	
	public int getPledgeRank()
	{
		return _pledgeRank;
	}
	
	public int getPledgeReputation()
	{
		return _pledgeReputation;
	}
	
	public int getPledgeUnit()
	{
		return _pledgeUnit;
	}
	
	public int getPrivateStoreType()
	{
		return _privateStoreType;
	}
	
	public int getPvPCount()
	{
		return _pvpCount;
	}
	
	public Race getRace()
	{
		return _race;
	}
	
	public int getReceivedRecommendations()
	{
		return _receivedRecommendations;
	}
	
	public int getRemainingRecommendations()
	{
		return _remainingRecommendaions;
	}
	
	public int getRunSpeed()
	{
		return _runSpeed;
	}
	
	public int getSiegeRelation()
	{
		return _siegeRelation;
	}
	
	public int getSpecialEffect()
	{
		return _specialEffect;
	}
	
	public int getSTR()
	{
		return _str;
	}
	
	public int getSwimRunSpeed()
	{
		return getRunSpeed();
	}
	
	public int getSwimWalkSpeed()
	{
		return getWalkSpeed();
	}
	
	public String getTitle()
	{
		return _title;
	}
	
	public int getTitleColor()
	{
		return _titleColor;
	}
	
	public int getTransformationGraphicalId()
	{
		return _transformationGraphicalId;
	}
	
	public int getVehicleObjectId()
	{
		return _vehicleObjectId;
	}
	
	public int getVitalityPoints()
	{
		return _vitalityPoints;
	}
	
	public int getWalkSpeed()
	{
		return _walkSpeed;
	}
	
	public int getWaterElementDefence()
	{
		return _waterElementDefence;
	}
	
	public int getWeaponEnchantGlow()
	{
		return _weaponEnchantGlow;
	}
	
	public int getWeaponStatus()
	{
		return _weaponStatus;
	}
	
	public int getWindElementDefence()
	{
		return _windElementDefence;
	}
	
	public int getWIT()
	{
		return _wit;
	}
	
	public int getX()
	{
		return _x;
	}
	
	public int getY()
	{
		return _y;
	}
	
	public int getZ()
	{
		return _z;
	}
	
	public boolean isFishing()
	{
		return _fishing;
	}
	
	public boolean isFlyingMounted()
	{
		return _flyingMounted;
	}
	
	public boolean isGM()
	{
		return _gm;
	}
	
	public boolean isHero()
	{
		return _hero;
	}
	
	public boolean isInCombatAction()
	{
		return _combatAction;
	}
	
	public int isInPvPAction()
	{
		return _inPvPAction;
	}
	
	public boolean isInvisible()
	{
		return _invisible;
	}
	
	public boolean isLookingForParty()
	{
		return _lookingForParty;
	}
	
	public boolean isLyingDead()
	{
		return _lyingDead;
	}
	
	public boolean isMoving()
	{
		return _moving;
	}
	
	public boolean isNoble()
	{
		return _noble;
	}
	
	@Override
	protected void refreshImpl()
	{
		final L2Player p = getActiveChar();
		final PlayerAppearance appearance = p.getAppearance();
		final PlayerStat stat = p.getStat();
		final PlayerBaseTemplate baseTemplate = p.getTemplate().getPlayerBaseTemplate(appearance.getGender());
		//final PlayerInventory inv = p.getInventory();
		//final L2Transformation transformation = p.getTransformation();
		
		refreshObjectId();
		refreshPosition();
		
		_vehicleObjectId = 0; // TODO
		
		_name = p.getName();
		_title = p.getTitle();
		
		_race = baseTemplate.getRace();
		_gender = appearance.getGender();
		_mainClassId = p.getMainClassId().ordinal();
		_activeClassId = p.getActiveClassId().ordinal();
		
		_level = stat.getLevel();
		
		_exp = stat.getExp();
		_expPercent = 0; // TODO
		_str = stat.getSTR();
		_dex = stat.getDEX();
		_con = stat.getCON();
		_int = stat.getINT();
		_wit = stat.getWIT();
		_men = stat.getMEN();
		
		_maxHp = stat.getMaxHP();
		_currentHp = stat.getCurrentHP();
		
		_maxMp = stat.getMaxMP();
		_currentMp = stat.getCurrentHP();
		
		_maxSp = stat.getMaxSP();
		
		_maxCp = stat.getMaxCP(); // Maximum CP
		_currentCp = stat.getCurrentCP(); // Current CP
		
		_carriedWeight = stat.getCarriedWeight();
		_maxCarriedWeight = stat.getMaxCarriedWeight();
		
		_karmaPoints = stat.getKarmaPoints(); // Karma
		_vitalityPoints = stat.getVitalityPoints(); // Vitality
		_famePoints = stat.getFamePoints(); // Fame
		
		_pkCount = stat.getPkCount(); // PK Count
		_pvpCount = stat.getPvPCount(); // PvP Count
		
		_pAtk = stat.getPAtk(0);
		_pDef = stat.getPDef(0);
		_pAtkSpd = stat.getPAtkSpd();
		
		_mAtk = stat.getMAtk(0, 0);
		_mDef = stat.getMDef(0, 0);
		_mAtkSpd = stat.getMAtkSpd();
		
		_accuracy = stat.getAccuracy();
		_evasionRate = stat.getEvasionRate(0);
		_criticalHit = stat.getCriticalHit(0);
		
		_movementSpeedMultiplier = stat.getMovementSpeedMultiplier();
		_attackSpeedMultiplier = stat.getAttackSpeedMultiplier();
		
		_runSpeed = (int)(stat.getRunSpeed() / _movementSpeedMultiplier);
		_walkSpeed = (int)(stat.getWalkSpeed() / _movementSpeedMultiplier);
		
		_flyRunSpeed = 0; // getActiveChar().isFlying() ? _runSpeed : 0; // TODO
		_flyWalkSpeed = 0; // getActiveChar().isFlying() ? _walkSpeed : 0; // TODO
		
		_remainingRecommendaions = 0;//getRemainingRecommendations(); // Recommendations
		_receivedRecommendations = 0;//getReceivedRecommendations(); // Evaluation score
		
		_weaponStatus = 20; // TODO
		
		_maxInventorySlots = 0;//inv.getMaxInventorySlots(); // Inventory slots
		_maxTalismanSlots = 0;//inv.getMaxTalismanSlots(); // Talisman slots
		_equipCloak = 0;//inv.canEquipCloak(); // Can equip cloak
		_agathionId = 0;//inv.getAgathionId(); // Agathion
		
		_weaponEnchantGlow = 0;//inv.getWeaponEnchantGlow(); // Weapon enchant glow
		
		_inPvPAction = 0;//isInPvPAction(); // In PvP
		
		_hairStyle = appearance.getHairStyle(); // Hair style
		_hairColor = appearance.getHairColor(); // Hair color
		_face = appearance.getFace(); // Face
		_nameColor = appearance.getNameColor();
		_titleColor = appearance.getTitleColor();
		_specialEffect = 0;//getSpecialEffect(); // Special effect
		_cubics = new int[0]; //getCubicData()
		
		_race = baseTemplate.getRace();
		_gender = appearance.getGender();
		
		_gm = p.isGM(); // Game Master
		
		_pledgeId = 0;//getPledgeId(); // Pledge ID
		_pledgeCrestId = 0;//getPledgeCrestId(); // Pledge crest ID
		_allianceId = 0;//getAllianceId(); // Alliance ID
		_allianceCrestId = 0;//getAllianceCrestId(); // Alliance crest ID
		_siegeRelation = 0;//getSiegeRelation(); // Siege participation
		_pledgePrivileges = 0;//getPledgePrivileges(); // Pledge privileges
		_pledgeInsigniaId = 0;//getPledgeInsigniaId(); // Pledge insignia ID
		_pledgeRank = 0;//getPledgeRank(); // Pledge rank
		_pledgeUnit = 0;//getPledgeUnit(); // Pledge unit
		
		_mountType = 0;//getMountType(); // Mount type
		_mountNpcId = 0;//getMountNpcId(); // Mount
		_flyingMounted = false;//isFlyingMounted(); // Flying with mount
		
		_privateStoreType = 0;//getPrivateStore(); // Private store
		_useDwarvenRecipes = false;//canUseDwarvenRecipes(); // Can use dwarven recipes
		
		_lookingForParty = false;//isLookingForParty(); // Looking for party
		_abnormalEffect = 0;//getAbnormalEffect(); // Abnormal effect
		
		_duelTeam = 0;//getDuelTeam(); // Duel team
		
		_noble = false;//isNoble(); // Noble
		_hero = false;//isHero(); // Hero
		
		_fishing = false;//isFishing(); // Fishing
		_fishLureX = 0;//getFishLureX(); // Fishing lure X
		_fishLureY = 0;//getFishLureY(); // Fishing lure Y
		_fishLureZ = 0;//getFishLureZ(); // Fishing lure Z
		
		_moving = isMoving(); // Moving
		
		_attackElementType = 0;//getAttackElementType(); // Attack element
		_attackElementPower = 0;//getAttackElementPower(); // Attack element power
		_fireElementDefence = 0;//getFireElementDefence(); // Fire defense
		_waterElementDefence = 0;//getWaterElementDefence(); // Water defense
		_windElementDefence = 0;//getWindElementDefence(); // Wind defense
		_earthElementDefence = 0;//getEarthElementDefence(); // Earth defense
		_holyElementDefence = 0;//getHolyElementDefence(); // Holy defense
		_darkElementDefence = 0;//getDarkElementDefence(); // Dark defense
		
		_useMinimap = 0;//canUseMinimap(); // Can use minimap
		
		// TODO
		/*if (p.getMountType() != 0)
		{	final L2NpcTemplate template = NpcTable.getInstance().getTemplate(p.getMountNpcId());
			
			_collisionRadius = template.getCollisionRadius();
			_collisionHeight = template.getCollisionHeight();
		}
		else if (transformation != 0 && !transformation.isStance())
		{	_collisionRadius = transformation.getCollisionRadius(cha);
			_collisionHeight = transformation.getCollisionHeight(cha);
		}
		else*/
		{
			_collisionRadius = baseTemplate.getCollisionRadius();
			_collisionHeight = baseTemplate.getCollisionHeight();
		}
		
		// TODO
		/*if (p.isCursedWeaponEquipped())
			_cursedWeaponLevel = CursedWeaponsManager.getInstance().getLevel(p.getCursedWeaponEquippedId());
		else*/
		_cursedWeaponLevel = 0;
		
		// TODO
		/*if (transformation != 0)
			_transformationGraphicalId = transformation.getGraphicalId();
		else*/
		_transformationGraphicalId = 0;
	}
	
	// TODO actually this should never change during object life time
	public void refreshObjectId()
	{
		_objectId = getActiveChar().getObjectId();
	}
	
	public void refreshPosition()
	{
		refreshObjectId();
		
		final ObjectPosition position = getActiveChar().getPosition();
		
		_x = position.getX();
		_y = position.getY();
		_z = position.getZ();
		_heading = position.getHeading();
	}
	
	private final int[] _paperDollObjectIds = new int[PaperDollSlot.TOTAL_SLOTS];
	private final int[] _paperDollItemDisplayIds = new int[PaperDollSlot.TOTAL_SLOTS];
	private final int[] _paperDollAugmentationIds = new int[PaperDollSlot.TOTAL_SLOTS];
	
	private int getPaperDollObjectId(int slot)
	{
		return _paperDollObjectIds[slot];
	}
	
	private int getPaperDollItemDisplayId(int slot)
	{
		return _paperDollItemDisplayIds[slot];
	}
	
	private int getPaperDollAugmentationId(int slot)
	{
		return _paperDollAugmentationIds[slot];
	}
	
	// -- methods for convenience 
	
	public void writeElements(MMOBuffer buf)
	{
		buf.writeH(getAttackElementType()); // Attack element
		buf.writeH(getAttackElementPower()); // Attack element power
		buf.writeH(getFireElementDefence()); // Fire defense
		buf.writeH(getWaterElementDefence()); // Water defense
		buf.writeH(getWindElementDefence()); // Wind defense
		buf.writeH(getEarthElementDefence()); // Earth defense
		buf.writeH(getHolyElementDefence()); // Holy defense
		buf.writeH(getDarkElementDefence()); // Dark defense
	}
	
	public void writePaperDollObjectIds(MMOBuffer buf, boolean withAccessory)
	{
		for (int slot : PlayerView.getSlots(withAccessory))
			buf.writeD(getPaperDollObjectId(slot));
	}
	
	public void writePaperDollItemDisplayIds(MMOBuffer buf, boolean withAccessory)
	{
		for (int slot : PlayerView.getSlots(withAccessory))
			buf.writeD(getPaperDollItemDisplayId(slot));
	}
	
	public void writePaperDollAugmentationIds(MMOBuffer buf, boolean withAccessory)
	{
		for (int slot : PlayerView.getSlots(withAccessory))
			buf.writeD(getPaperDollAugmentationId(slot));
	}
	
	public void writeCubics(MMOBuffer buf)
	{
		buf.writeH(getCubicData().length); // Cubic count
		for (int cubic : getCubicData())
			buf.writeH(cubic); // Cubic
	}
	
	private static final int[] SLOTS_WITH_ACCESSORY = initSlots(true);
	private static final int[] SLOTS_WITHOUT_ACCESSORY = initSlots(false);
	
	private static int[] getSlots(boolean withAccessory)
	{
		return withAccessory ? SLOTS_WITH_ACCESSORY : SLOTS_WITHOUT_ACCESSORY;
	}
	
	private static int[] initSlots(boolean withAccessory)
	{
		final List<PaperDollSlot> slots = new ArrayList<PaperDollSlot>(withAccessory ? 26 : 21);
		
		slots.add(PaperDollSlot.UNDER);
		
		if (withAccessory)
		{
			slots.add(PaperDollSlot.R_EAR);
			slots.add(PaperDollSlot.L_EAR);
			slots.add(PaperDollSlot.NECK);
			slots.add(PaperDollSlot.R_FINGER);
			slots.add(PaperDollSlot.L_FINGER);
		}
		
		slots.add(PaperDollSlot.HEAD);
		slots.add(PaperDollSlot.R_HAND);
		slots.add(PaperDollSlot.L_HAND);
		slots.add(PaperDollSlot.GLOVES);
		slots.add(PaperDollSlot.CHEST);
		slots.add(PaperDollSlot.LEGS);
		slots.add(PaperDollSlot.FEET);
		slots.add(PaperDollSlot.CLOAK);
		slots.add(PaperDollSlot.L_R_HAND);
		slots.add(PaperDollSlot.HAIR_1);
		slots.add(PaperDollSlot.HAIR_2);
		slots.add(PaperDollSlot.R_BRACELET);
		slots.add(PaperDollSlot.L_BRACELET);
		slots.add(PaperDollSlot.TALISMAN_1);
		slots.add(PaperDollSlot.TALISMAN_2);
		slots.add(PaperDollSlot.TALISMAN_3);
		slots.add(PaperDollSlot.TALISMAN_4);
		slots.add(PaperDollSlot.TALISMAN_5);
		slots.add(PaperDollSlot.TALISMAN_6);
		slots.add(PaperDollSlot.BELT);
		
		final int[] indexes = new int[slots.size()];
		
		int i = 0;
		for (PaperDollSlot slot : slots)
			indexes[i++] = slot.ordinal();
		
		return indexes;
	}
}
