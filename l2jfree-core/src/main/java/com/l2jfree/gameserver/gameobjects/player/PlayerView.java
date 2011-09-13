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

import com.l2jfree.gameserver.gameobjects.CharacterStat.Element;
import com.l2jfree.gameserver.gameobjects.CharacterView;
import com.l2jfree.gameserver.gameobjects.L2Player;
import com.l2jfree.gameserver.gameobjects.ObjectPosition;
import com.l2jfree.gameserver.gameobjects.components.interfaces.IPlayerInventory;
import com.l2jfree.gameserver.gameobjects.components.interfaces.IPlayerStat;
import com.l2jfree.gameserver.gameobjects.components.interfaces.IPlayerView;
import com.l2jfree.gameserver.gameobjects.item.L2EquipableItem;
import com.l2jfree.gameserver.gameobjects.player.PlayerInventory.PaperDollSlot;
import com.l2jfree.gameserver.templates.player.Gender;
import com.l2jfree.gameserver.templates.player.PlayerBaseTemplate;
import com.l2jfree.gameserver.templates.player.Race;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author hex1r0
 * @author NB4L1
 */
public class PlayerView extends CharacterView implements IPlayerView
{
	private int _abnormalEffect;
	private int _accuracy;
	private int _activeClassId;
	private int _agathionId;
	private int _allianceCrestId;
	private int _allianceId;
	private int _attackElementPower;
	private Element _attackElementType;
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
	private int _dex;
	private int _duelTeam;
	private int _equipCloak;
	private int _evasionRate;
	private long _exp;
	private int _expPercent;
	private byte _face;
	private int _famePoints;
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
	private int _weaponEnchantGlow;
	private int _weaponStatus;
	private int _wit;
	private int _x;
	private int _y;
	private int _z;
	private int _destinationX;
	private int _destinationY;
	private int _destinationZ;
	private final int[] _defenceElementPower = new int[Element.values().length];
	
	public PlayerView(L2Player activeChar)
	{
		super(activeChar);
	}
	
	@Override
	public final L2Player getActiveChar()
	{
		return (L2Player)super.getActiveChar();
	}
	
	@Override
	public int canEquipCloak()
	{
		return _equipCloak;
	}
	
	@Override
	public boolean canUseDwarvenRecipes()
	{
		return _useDwarvenRecipes;
	}
	
	@Override
	public int canUseMinimap()
	{
		return _useMinimap;
	}
	
	@Override
	public int getAbnormalEffect()
	{
		return _abnormalEffect;
	}
	
	@Override
	public int getAccuracy()
	{
		return _accuracy;
	}
	
	@Override
	public int getActiveClassId()
	{
		return _activeClassId;
	}
	
	@Override
	public int getAgathionId()
	{
		return _agathionId;
	}
	
	@Override
	public int getAllianceCrestId()
	{
		return _allianceCrestId;
	}
	
	@Override
	public int getAllianceId()
	{
		return _allianceId;
	}
	
	@Override
	public int getAttackElementPower()
	{
		return _attackElementPower;
	}
	
	@Override
	public int getAttackElementType()
	{
		return _attackElementType.getValue();
	}
	
	@Override
	public double getAttackSpeedMultiplier()
	{
		return _attackSpeedMultiplier;
	}
	
	@Override
	public int getCarriedWeight()
	{
		return _carriedWeight;
	}
	
	@Override
	public double getCollisionHeight()
	{
		return _collisionHeight;
	}
	
	@Override
	public double getCollisionRadius()
	{
		return _collisionRadius;
	}
	
	@Override
	public int getCON()
	{
		return _con;
	}
	
	@Override
	public int getCriticalHit()
	{
		return _criticalHit;
	}
	
	@Override
	public int[] getCubicData()
	{
		return _cubics;
	}
	
	@Override
	public int getCurrentCP()
	{
		return _currentCp;
	}
	
	@Override
	public int getCurrentHP()
	{
		return _currentHp;
	}
	
	@Override
	public int getCurrentMP()
	{
		return _currentMp;
	}
	
	@Override
	public int getCursedWeaponLevel()
	{
		return _cursedWeaponLevel;
	}
	
	@Override
	public int getDefenseElementPower(Element element)
	{
		return _defenceElementPower[element.ordinal()];
	}
	
	@Override
	public int getDEX()
	{
		return _dex;
	}
	
	@Override
	public int getDuelTeam()
	{
		return _duelTeam;
	}
	
	@Override
	public int getEvasionRate()
	{
		return _evasionRate;
	}
	
	@Override
	public long getExp()
	{
		return _exp;
	}
	
	@Override
	public double getExpPercent()
	{
		return _expPercent;
	}
	
	@Override
	public int getFace()
	{
		return _face;
	}
	
	@Override
	public int getFamePoints()
	{
		return _famePoints;
	}
	
	@Override
	public int getFishLureX()
	{
		return _fishLureX;
	}
	
	@Override
	public int getFishLureY()
	{
		return _fishLureY;
	}
	
	@Override
	public int getFishLureZ()
	{
		return _fishLureZ;
	}
	
	@Override
	public int getFlyRunSpeed()
	{
		return _flyRunSpeed;
	}
	
	@Override
	public int getFlyWalkSpeed()
	{
		return _flyWalkSpeed;
	}
	
	@Override
	public Gender getGender()
	{
		return _gender;
	}
	
	@Override
	public int getHairColor()
	{
		return _hairColor;
	}
	
	@Override
	public int getHairStyle()
	{
		return _hairStyle;
	}
	
	@Override
	public int getHeading()
	{
		return _heading;
	}
	
	@Override
	public int getINT()
	{
		return _int;
	}
	
	@Override
	public int getKarma()
	{
		return _karmaPoints;
	}
	
	@Override
	public int getLevel()
	{
		return _level;
	}
	
	@Override
	public int getMainClassId()
	{
		return _mainClassId;
	}
	
	@Override
	public int getMAtk()
	{
		return _mAtk;
	}
	
	@Override
	public int getMAtkSpd()
	{
		return _mAtkSpd;
	}
	
	@Override
	public int getMaxCarriedWeight()
	{
		return _maxCarriedWeight;
	}
	
	@Override
	public int getMaxCP()
	{
		return _maxCp;
	}
	
	@Override
	public int getMaxHP()
	{
		return _maxHp;
	}
	
	@Override
	public int getMaxInventorySlots()
	{
		return _maxInventorySlots;
	}
	
	@Override
	public int getMaxMP()
	{
		return _maxMp;
	}
	
	@Override
	public int getMaxSP()
	{
		return _maxSp;
	}
	
	@Override
	public int getMaxTalismanSlots()
	{
		return _maxTalismanSlots;
	}
	
	@Override
	public int getMDef()
	{
		return _mDef;
	}
	
	@Override
	public int getMEN()
	{
		return _men;
	}
	
	@Override
	public int getMountNpcId()
	{
		return _mountNpcId;
	}
	
	@Override
	public int getMountType()
	{
		return _mountType;
	}
	
	@Override
	public double getMovementSpeedMultiplier()
	{
		return _movementSpeedMultiplier;
	}
	
	@Override
	public String getName()
	{
		return _name;
	}
	
	@Override
	public int getNameColor()
	{
		return _nameColor;
	}
	
	@Override
	public int getObjectId()
	{
		return _objectId;
	}
	
	@Override
	public int getPAtk()
	{
		return _pAtk;
	}
	
	@Override
	public int getPAtkSpd()
	{
		return _pAtkSpd;
	}
	
	@Override
	public int getPDef()
	{
		return _pDef;
	}
	
	@Override
	public int getPkCount()
	{
		return _pkCount;
	}
	
	@Override
	public int getPledgeCrestId()
	{
		return _pledgeCrestId;
	}
	
	@Override
	public int getPledgeId()
	{
		return _pledgeId;
	}
	
	@Override
	public int getPledgeInsigniaId()
	{
		return _pledgeInsigniaId;
	}
	
	@Override
	public int getPledgePrivileges()
	{
		return _pledgePrivileges;
	}
	
	@Override
	public int getPledgeRank()
	{
		return _pledgeRank;
	}
	
	@Override
	public int getPledgeReputation()
	{
		return _pledgeReputation;
	}
	
	@Override
	public int getPledgeUnit()
	{
		return _pledgeUnit;
	}
	
	@Override
	public int getPrivateStoreType()
	{
		return _privateStoreType;
	}
	
	@Override
	public int getPvPCount()
	{
		return _pvpCount;
	}
	
	@Override
	public Race getRace()
	{
		return _race;
	}
	
	@Override
	public int getReceivedRecommendations()
	{
		return _receivedRecommendations;
	}
	
	@Override
	public int getRemainingRecommendations()
	{
		return _remainingRecommendaions;
	}
	
	@Override
	public int getRunSpeed()
	{
		return _runSpeed;
	}
	
	@Override
	public int getSiegeRelation()
	{
		return _siegeRelation;
	}
	
	@Override
	public int getSpecialEffect()
	{
		return _specialEffect;
	}
	
	@Override
	public int getSTR()
	{
		return _str;
	}
	
	@Override
	public int getSwimRunSpeed()
	{
		return getRunSpeed();
	}
	
	@Override
	public int getSwimWalkSpeed()
	{
		return getWalkSpeed();
	}
	
	@Override
	public String getTitle()
	{
		return _title;
	}
	
	@Override
	public int getTitleColor()
	{
		return _titleColor;
	}
	
	@Override
	public int getTransformationGraphicalId()
	{
		return _transformationGraphicalId;
	}
	
	@Override
	public int getVehicleObjectId()
	{
		return _vehicleObjectId;
	}
	
	@Override
	public int getVitalityPoints()
	{
		return _vitalityPoints;
	}
	
	@Override
	public int getWalkSpeed()
	{
		return _walkSpeed;
	}
	
	@Override
	public int getWeaponEnchantGlow()
	{
		return _weaponEnchantGlow;
	}
	
	@Override
	public int getWeaponStatus()
	{
		return _weaponStatus;
	}
	
	@Override
	public int getWIT()
	{
		return _wit;
	}
	
	@Override
	public int getX()
	{
		return _x;
	}
	
	@Override
	public int getY()
	{
		return _y;
	}
	
	@Override
	public int getZ()
	{
		return _z;
	}
	
	@Override
	public int getDestinationX()
	{
		return _destinationX;
	}
	
	@Override
	public int getDestinationY()
	{
		return _destinationY;
	}
	
	@Override
	public int getDestinationZ()
	{
		return _destinationZ;
	}
	
	@Override
	public boolean isFishing()
	{
		return _fishing;
	}
	
	@Override
	public boolean isFlyingMounted()
	{
		return _flyingMounted;
	}
	
	@Override
	public boolean isGM()
	{
		return _gm;
	}
	
	@Override
	public boolean isHero()
	{
		return _hero;
	}
	
	@Override
	public boolean isInCombatAction()
	{
		return _combatAction;
	}
	
	@Override
	public int isInPvPAction()
	{
		return _inPvPAction;
	}
	
	@Override
	public boolean isInvisible()
	{
		return _invisible;
	}
	
	@Override
	public boolean isLookingForParty()
	{
		return _lookingForParty;
	}
	
	@Override
	public boolean isLyingDead()
	{
		return _lyingDead;
	}
	
	@Override
	public boolean isMoving()
	{
		return _moving;
	}
	
	@Override
	public boolean isNoble()
	{
		return _noble;
	}
	
	@Override
	protected void refreshImpl()
	{
		final L2Player p = getActiveChar();
		final PlayerAppearance appearance = p.getAppearance();
		final IPlayerStat stat = p.getStat();
		final PlayerBaseTemplate baseTemplate = p.getTemplate().getPlayerBaseTemplate(appearance.getGender());
		//final IPlayerInventory inv = p.getInventory();
		//final L2Transformation transformation = p.getTransformation();
		
		refreshPosition();
		refreshPaperDoll();
		
		_vehicleObjectId = 0; // TODO
		
		_name = p.getName();
		_title = p.getTitle();
		
		_race = baseTemplate.getRace();
		_gender = appearance.getGender();
		_mainClassId = p.getMainClassId().ordinal();
		_activeClassId = p.getActiveClassId().ordinal();
		
		// --
		
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
		_currentMp = stat.getCurrentMP();
		
		_maxSp = stat.getMaxSP();
		
		_maxCp = stat.getMaxCP();
		_currentCp = stat.getCurrentCP();
		
		_carriedWeight = stat.getCarriedWeight();
		_maxCarriedWeight = stat.getMaxCarriedWeight();
		
		_karmaPoints = stat.getKarmaPoints();
		_vitalityPoints = stat.getVitalityPoints();
		_famePoints = stat.getFamePoints();
		
		_pkCount = stat.getPkCount();
		_pvpCount = stat.getPvPCount();
		
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
		
		_attackElementType = stat.getAttackElement();
		_attackElementPower = stat.getAttackElementPower(_attackElementType);
		_defenceElementPower[Element.FIRE.getValue()] = stat.getDefenseElementPower(Element.FIRE);
		_defenceElementPower[Element.WATER.getValue()] = stat.getDefenseElementPower(Element.WATER);
		_defenceElementPower[Element.WIND.getValue()] = stat.getDefenseElementPower(Element.WIND);
		_defenceElementPower[Element.EARTH.getValue()] = stat.getDefenseElementPower(Element.EARTH);
		_defenceElementPower[Element.HOLY.getValue()] = stat.getDefenseElementPower(Element.HOLY);
		_defenceElementPower[Element.DARK.getValue()] = stat.getDefenseElementPower(Element.DARK);
		
		// --
		
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
		
		_gm = p.isGM();
		
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
	
	@Override
	public void refreshPosition()
	{
		final ObjectPosition position = getActiveChar().getPosition();
		
		_x = position.getX();
		_y = position.getY();
		_z = position.getZ();
		_heading = position.getHeading();
	}
	
	@Override
	public void refreshDestinationPosition()
	{
		final L2Player p = getActiveChar();
		
		_destinationX = p.getDestinationX();
		_destinationY = p.getDestinationY();
		_destinationZ = p.getDestinationZ();
	}
	
	private final int[] _paperDollObjectIds = new int[PaperDollSlot.TOTAL_SLOTS];
	private final int[] _paperDollItemDisplayIds = new int[PaperDollSlot.TOTAL_SLOTS];
	private final int[] _paperDollAugmentationIds = new int[PaperDollSlot.TOTAL_SLOTS];
	
	@Override
	public void refreshPaperDoll()
	{
		final IPlayerInventory inv = getActiveChar().getInventory();
		
		for (int i = 0; i < PaperDollSlot.TOTAL_SLOTS; i++)
		{
			final L2EquipableItem equipableItem = inv.getPaperDollItem(i);
			
			if (equipableItem == null)
			{
				_paperDollObjectIds[i] = 0;
				_paperDollItemDisplayIds[i] = 0;
				_paperDollAugmentationIds[i] = 0;
			}
			else
			{
				_paperDollObjectIds[i] = equipableItem.getObjectId();
				_paperDollItemDisplayIds[i] = equipableItem.getTemplate().getId(); // TODO
				_paperDollAugmentationIds[i] = 0; // TODO
			}
		}
	}
	
	// -- methods for convenience
	
	@Override
	public void writeElements(MMOBuffer buf)
	{
		buf.writeH(getAttackElementType()); // Attack element
		buf.writeH(getAttackElementPower()); // Attack element power
		buf.writeH(getDefenseElementPower(Element.FIRE)); // Fire defense
		buf.writeH(getDefenseElementPower(Element.WATER)); // Water defense
		buf.writeH(getDefenseElementPower(Element.WIND)); // Wind defense
		buf.writeH(getDefenseElementPower(Element.EARTH)); // Earth defense
		buf.writeH(getDefenseElementPower(Element.HOLY)); // Holy defense
		buf.writeH(getDefenseElementPower(Element.DARK)); // Dark defense
	}
	
	@Override
	public void writePaperDollObjectIds(MMOBuffer buf, boolean withAccessory)
	{
		for (int slot : PlayerView.getSlots(withAccessory))
			buf.writeD(_paperDollObjectIds[slot]);
	}
	
	@Override
	public void writePaperDollItemDisplayIds(MMOBuffer buf, boolean withAccessory)
	{
		for (int slot : PlayerView.getSlots(withAccessory))
			buf.writeD(_paperDollItemDisplayIds[slot]);
	}
	
	@Override
	public void writePaperDollAugmentationIds(MMOBuffer buf, boolean withAccessory)
	{
		for (int slot : PlayerView.getSlots(withAccessory))
			buf.writeD(_paperDollAugmentationIds[slot]);
	}
	
	@Override
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
