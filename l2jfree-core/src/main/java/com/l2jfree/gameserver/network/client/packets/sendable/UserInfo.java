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
 * @author hex1r0
 */
public class UserInfo extends L2ServerPacket
{
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		final PlayerAppearance appearance = activeChar.getAppearance();
		final ObjectPosition position = activeChar.getPosition();
		final L2PlayerTemplate template = activeChar.getTemplate();
		final PlayerBaseTemplate baseTemplate = template.getPlayerBaseTemplate(appearance.getGender());
		
		buf.writeD(position.getX());
		buf.writeD(position.getY());
		buf.writeD(position.getZ());
		// TODO
		/*if (_activeChar.getVehicle() != null)
			buf.writeD(activeChar.getVehicle().getObjectId());
		else*/
		buf.writeD(0);
		buf.writeD(activeChar.getObjectId());
		buf.writeS(activeChar.getName());
		buf.writeD(baseTemplate.getRace().ordinal());
		buf.writeD(appearance.getGender().ordinal());
		// TODO
		//if (_activeChar.getClassIndex() == 0) writeD(_activeChar.getClassId().getId());
		buf.writeD(template.getClassId().getId());
		buf.writeD(5); // TODO activeChar.getLevel();
		buf.writeQ(5); // TODO activeChar.getExp();
		buf.writeF(5); // TODO High Five exp %
		
		buf.writeD(5);// TODO activeChar.getSTR());
		buf.writeD(5);// TODO activeChar.getDEX());
		buf.writeD(5);// TODO activeChar.getCON());
		buf.writeD(5);// TODO activeChar.getINT());
		buf.writeD(5);// TODO activeChar.getWIT());
		buf.writeD(5);// TODO activeChar.getMEN());
		
		buf.writeD(5);// TODO activeChar.getMaxVisibleHp());
		buf.writeD(5);// TODO (int) activeChar.getCurrentHp());
		buf.writeD(5);// TODO activeChar.getMaxMp());
		buf.writeD(5);// TODO (int) activeChar.getCurrentMp());
		buf.writeD(5);// TODO activeChar.getSp());
		
		buf.writeD(5);// TODO activeChar.getCurrentLoad());
		buf.writeD(5);// TODO activeChar.getMaxLoad());
		
		buf.writeD(/*_activeChar.getActiveWeaponItem() != null ? 40 :*/20); // 20 no weapon, 40 weapon equipped
		
		// TODO PaperdollObjectId
		for (int i = 0; i < 26; i++)
			buf.writeD(0);
		
		// TODO PaperdollItemId
		for (int i = 0; i < 26; i++)
			buf.writeD(0);
		
		// TODO PaperdollAugmentationId
		for (int i = 0; i < 26; i++)
			buf.writeD(0);
		
		buf.writeD(0);// TODO activeChar.getInventory().getMaxTalismanCount()); // CT2.3
		buf.writeD(0);// TODO activeChar.getInventory().getCloakStatus()); // CT2.3
		buf.writeD(5);// TODO activeChar.getPAtk(null));
		buf.writeD(5);// TODO activeChar.getPAtkSpd());
		buf.writeD(5);// TODO activeChar.getPDef(null));
		buf.writeD(5);// TODO activeChar.getEvasionRate(null));
		buf.writeD(5);// TODO activeChar.getAccuracy());
		buf.writeD(5);// TODO activeChar.getCriticalHit(null, null));
		buf.writeD(5);// TODO activeChar.getMAtk(null, null));
		
		buf.writeD(5);// TODO activeChar.getMAtkSpd());
		buf.writeD(5);// TODO activeChar.getPAtkSpd());
		
		buf.writeD(5);// TODO activeChar.getMDef(null, null));
		
		buf.writeD(0);// TODO activeChar.getPvpFlag()); // 0-non-pvp  1-pvp = violett name
		buf.writeD(5);// TODO activeChar.getKarma());
		
		buf.writeD(baseTemplate.getRunSpeed()); // TODO Math.round((_activeChar.getRunSpeed() / _moveMultiplier));
		buf.writeD(baseTemplate.getWalkSpeed());// TODO
		buf.writeD(baseTemplate.getRunSpeedInWater()); // // TODO 
		buf.writeD(baseTemplate.getWalkSpeedInWater()); // TODO
		buf.writeD(0);
		buf.writeD(0);
		buf.writeD(0);// TODO activeChar.isFlying() ? _runSpd : 0); // fly speed
		buf.writeD(0);// TODO activeChar.isFlying() ? _walkSpd : 0); // fly speed
		buf.writeF(1); /// TODO activeChar.getMovementSpeedMultiplier()
		buf.writeF(1);// TODO activeChar.getAttackSpeedMultiplier());
		
		/*if (_activeChar.getMountType() != 0 && pet != null)
		{
			writeF(pet.getTemplate().fCollisionRadius);
			writeF(pet.getTemplate().fCollisionHeight);
		}
		else if ((trans = _activeChar.getTransformation()) != null)
		{
			writeF(trans.getCollisionRadius());
			writeF(trans.getCollisionHeight());
		}
		else*/
		{
			buf.writeF(baseTemplate.getCollisionRadius());
			buf.writeF(baseTemplate.getCollisionHeight());
		}
		
		buf.writeD(appearance.getHairStyle());
		buf.writeD(appearance.getHairColor());
		buf.writeD(appearance.getFace());
		buf.writeD(0); // TODO _activeChar.isGM()
		
		buf.writeS(activeChar.getTitle());
		
		buf.writeD(0);//TODO _activeChar.getClanId());
		buf.writeD(0);//TODO _activeChar.getClanCrestId());
		buf.writeD(0);//TODO _activeChar.getAllyId());
		buf.writeD(0);//TODO _activeChar.getAllyCrestId()); // ally crest id
		// 0x40 leader rights
		// siege flags: attacker - 0x180 sword over name, defender - 0x80 shield, 0xC0 crown (|leader), 0x1C0 flag (|leader)
		buf.writeD(0);
		buf.writeC(0);//TODO _activeChar.getMountType()); // mount type
		buf.writeC(0);//TODO _activeChar.getPrivateStoreType());
		buf.writeC(0);//TODO _activeChar.hasDwarvenCraft() ? 1 : 0);
		buf.writeD(0);//TODO _activeChar.getPkKills());
		buf.writeD(0);//TODO _activeChar.getPvpKills());
		
		buf.writeH(0);//TODO _activeChar.getCubics().size());
		/*for (int id : _activeChar.getCubics().keySet())
			buf.writeH(id);*/
		
		buf.writeC(0);//TODO _activeChar.isInPartyMatchRoom() ? 1 : 0);
		
		/*if (_activeChar.getAppearance().getInvisible() && _activeChar.isGM())
			buf.writeD(0);//TODO _activeChar.getAbnormalEffect() | AbnormalEffect.STEALTH.getMask());
		else*/
		buf.writeD(0);//TODO _activeChar.getAbnormalEffect());
		buf.writeC(0);//TODO _activeChar.isFlyingMounted() ? 2 : 0);
		
		buf.writeD(0);//TODO _activeChar.getClanPrivileges());
		
		buf.writeH(0);//TODO activeChar.getRecomLeft()); //c2  recommendations remaining
		buf.writeH(0);//TODO activeChar.getRecomHave()); //c2  recommendations received
		buf.writeD(0);//TODO activeChar.getMountNpcId() > 0 ? _activeChar.getMountNpcId() + 1000000 : 0);
		buf.writeH(0);//TODO activeChar.getInventoryLimit());
		
		buf.writeD(template.getClassId().getId());
		buf.writeD(0x00); // special effects? circles around player...
		buf.writeD(100);//TODO activeChar.getMaxCp());
		buf.writeD(50);//TODO activeChar.getCurrentCp());
		buf.writeC(0);//TODO activeChar.isMounted() || _airShipHelm != 0 ? 0 : _activeChar.getEnchantEffect());
		
		/*if (_activeChar.getTeam() == 1)
			buf.writeC(0x01); //team circle around feet 1= Blue, 2 = red
		else if (_activeChar.getTeam() == 2)
			buf.writeC(0x02); //team circle around feet 1= Blue, 2 = red
		else*/
		buf.writeC(0x00); //team circle around feet 1= Blue, 2 = red
		
		buf.writeD(0);//TODO activeChar.getClanCrestLargeId());
		buf.writeC(0);//TODO activeChar.isNoble() ? 1 : 0); //0x01: symbol on char menu ctrl+I
		buf.writeC(0);//TODO activeChar.isHero() || (_activeChar.isGM() && Config.GM_HERO_AURA) ? 1 : 0); //0x01: Hero Aura
		
		buf.writeC(0);//TODO activeChar.isFishing() ? 1 : 0); //Fishing Mode
		buf.writeD(0);//TODO activeChar.getFishx()); //fishing x
		buf.writeD(0);//TODO activeChar.getFishy()); //fishing y
		buf.writeD(0);//TODO activeChar.getFishz()); //fishing z
		buf.writeD(0);//TODO activeChar.getAppearance().getNameColor());
		
		//new c5
		buf.writeC(0);//TODO activeChar.isRunning() ? 0x01 : 0x00); //changes the Speed display on Status Window
		
		buf.writeD(0);//TODO activeChar.getPledgeClass()); //changes the text above CP on Status Window
		buf.writeD(0);//TODO activeChar.getPledgeType());
		
		buf.writeD(0);//TODO activeChar.getAppearance().getTitleColor());
		
		/*if (_activeChar.isCursedWeaponEquipped())
			buf.writeD(CursedWeaponsManager.getInstance().getLevel(_activeChar.getCursedWeaponEquippedId()));
		else*/
		buf.writeD(0x00);
		
		// T1 Starts
		buf.writeD(0);//TODO activeChar.getTransformationId());
		
		buf.writeH(0); //byte attackAttribute = _activeChar.getAttackElement();
		buf.writeH(0);// TODO activeChar.getAttackElementValue(attackAttribute));
		buf.writeH(0);// TODO activeChar.getDefenseElementValue(Elementals.FIRE));
		buf.writeH(0);// TODO activeChar.getDefenseElementValue(Elementals.WATER));
		buf.writeH(0);// TODO activeChar.getDefenseElementValue(Elementals.WIND));
		buf.writeH(0);// TODO activeChar.getDefenseElementValue(Elementals.EARTH));
		buf.writeH(0);// TODO activeChar.getDefenseElementValue(Elementals.HOLY));
		buf.writeH(0);// TODO activeChar.getDefenseElementValue(Elementals.DARK));
		
		buf.writeD(0);// TODO activeChar.getAgathionId());
		
		// T2 Starts
		buf.writeD(0);// TODO activeChar.getFame()); // Fame
		buf.writeD(0);// TODO activeChar.isMinimapAllowed() ? 1 : 0); // Minimap on Hellbound
		buf.writeD(0);// TODO activeChar.getVitalityPoints()); // Vitality Points
		buf.writeD(0);// TODO activeChar.getSpecialEffect());
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x32;
	}
}
