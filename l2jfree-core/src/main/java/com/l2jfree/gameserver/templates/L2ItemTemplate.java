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
package com.l2jfree.gameserver.templates;

/**
 * @author NB4L1
 */
public class L2ItemTemplate extends L2Template
{
	private final ItemType _type;
	private final PaperDollSlot _equipSlot;
	
	public L2ItemTemplate(int id, ItemType type, PaperDollSlot equipSlot)
	{
		super(id);
		
		_type = type;
		_equipSlot = equipSlot;
	}
	
	public ItemType getType()
	{
		return _type;
	}
	
	public PaperDollSlot getEquipSlot()
	{
		return _equipSlot;
	}
	
	public enum ItemType
	{
		WEAPON,
		ARMOR_SHIELD,
		JEWELRY_ACCESSORY,
		QUEST,
		ADENA,
		OTHER;
	}
	
	public enum PaperDollSlot
	{
		SHIRT,
		EARRING_RIGHT,
		EARRING_LEFT,
		NECKLACE,
		RING_RIGHT,
		RING_LEFT,
		HELMET,
		WEAPON_MAIN,
		SHIELD,
		GLOVES,
		ARMOR_CHEST,
		ARMOR_LEG,
		BOOTS,
		CLOAK,
		WEAPON_2H,
		ARMOR_FULL,
		ACC_HAIRBAND,
		UNK17,
		ACC_FACEMASK,
		ACC_LARGE,
		BRACELET_RIGHT,
		BRACELET_LEFT,
		TALISMAN, // the only one that is actually used
		TALISMAN_2,
		TALISMAN_3,
		TALISMAN_4,
		TALISMAN_5,
		TALISMAN_6,
		BELT,
		// special slots
		PET_WOLF(-100),
		PET_HATCHLING(-101),
		PET_STRIDER(-102),
		PET_BABY_ANY(-103),
		PET_GREAT_WOLF(-104),
		// convenience slots
		NONE(0),
		EARRING_ANY(EARRING_RIGHT.getMask() | EARRING_LEFT.getMask()),
		RING_ANY(RING_RIGHT.getMask() | RING_LEFT.getMask());
		
		private final int _mask;
		
		private PaperDollSlot()
		{
			_mask = 1 << ordinal();
		}
		
		private PaperDollSlot(int mask)
		{
			_mask = mask;
		}
		
		public int getMask()
		{
			return _mask;
		}
	}
}
