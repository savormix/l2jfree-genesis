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
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.network.ClientProtocolVersion;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix (generated)
 */
public abstract class ShortCutInitPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link ShortCutInitPacket}.
	 * 
	 * @author savormix (generated)
	 * @see ShortCutInitPacket
	 */
	public static final class ShortcutList extends ShortCutInitPacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see ShortCutInitPacket#ShortCutInitPacket()
		 */
		public ShortcutList()
		{
		}
	}
	
	/** Constructs this packet. */
	public ShortCutInitPacket()
	{
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x45;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		final int sizeA = 0; // Shortcut count
		buf.writeD(sizeA);
		for (int i = 0; i < sizeA; i++)
		{
			buf.writeD(0); // Type, branching condition
			buf.writeD(0); // Slot
			// branch with shortcut.ItemShortcut
			{
				buf.writeD(0); // Item OID
			}
			// branch with shortcut.SkillShortcut
			{
				buf.writeD(0); // Skill
				buf.writeD(0); // Level
				if (client.getVersion().isNewerThanOrEqualTo(ClientProtocolVersion.GODDESS_OF_DESTRUCTION))
					buf.writeD(-1); // -1
				buf.writeC(0); // 0
			}
			// branch with shortcut.ActionShortcut
			{
				buf.writeD(0); // Action
			}
			// branch with shortcut.MacroShortcut
			{
				buf.writeD(0); // Macro ID
			}
			// branch with shortcut.RecipeShortcut
			{
				buf.writeD(0); // Recipe
			}
			// branch with shortcut.DefaultShortcut
			{
				buf.writeD(0); // ID
			}
			buf.writeD(0); // Executor
			// branch with shortcut.ItemShortcut
			{
				buf.writeD(0); // Cooldown group
				buf.writeD(0); // Remaining time
				buf.writeD(0); // Cooldown time
				buf.writeD(0); // Augmentation
			}
		}
	}
}
