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

import com.l2jfree.gameserver.gameobjects.Inventory;
import com.l2jfree.gameserver.gameobjects.L2Player;

/**
 * @author NB4L1
 */
public class PlayerInventory extends Inventory
{
	public static enum PaperDollSlot
	{
		UNDER,
		R_EAR,
		L_EAR,
		LREAR,
		NECK,
		L_FINGER,
		R_FINGER,
		LRFINGER,
		HEAD,
		R_HAND,
		L_HAND,
		GLOVES,
		CHEST,
		LEGS,
		FEET,
		CLOAK,
		L_R_HAND,
		FULLARMOR,
		HAIR_1,
		ALLDRESS,
		HAIR_2,
		HAIRALL,
		R_BRACELET,
		L_BRACELET,
		TALISMAN_1,
		TALISMAN_2,
		TALISMAN_3,
		TALISMAN_4,
		TALISMAN_5,
		TALISMAN_6,
		BELT;
		
		public static final int TOTAL_SLOTS = PaperDollSlot.values().length;
	}
	
	public PlayerInventory(L2Player activeChar)
	{
		super(activeChar);
	}
	
	@Override
	public final L2Player getActiveChar()
	{
		return (L2Player)super.getActiveChar();
	}
}
