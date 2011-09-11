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
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix (generated)
 */
public abstract class SSQStatusPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link SSQStatusPacket}.
	 * 
	 * @author savormix (generated)
	 * @see SSQStatusPacket
	 */
	public static final class SevenSignsInfo extends SSQStatusPacket
	{
		/**
		 * Constructs this packet.
		 * 
		 * @see SSQStatusPacket#SSQStatusPacket()
		 */
		public SevenSignsInfo()
		{
		}
	}

	/** Constructs this packet. */
	public SSQStatusPacket()
	{
	}

	@Override
	protected int getOpcode()
	{
		return 0xfb;
	}

	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		// TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
		buf.writeC(0); // Page, branching condition
		buf.writeC(0); // Current period
		// branch with tab.General
		{
			buf.writeD(0); // Current cycle
			buf.writeD(0); // Period info
			buf.writeD(0); // Period duration info
			buf.writeC(0); // Viewer's cabal
			buf.writeC(0); // Viewer's seal
			buf.writeQ(0L); // Contribution score
			buf.writeQ(0L); // Ancient adena to be collected
			buf.writeQ(0L); // Dusk contribution score
			buf.writeQ(0L); // Dusk festival score
			buf.writeQ(0L); // Dusk total score
			buf.writeC(0); // Dusk total %
			buf.writeQ(0L); // Dawn contribution score
			buf.writeQ(0L); // Dawn festival score
			buf.writeQ(0L); // Dawn total score
			buf.writeC(0); // Dawn total %
		}
		// branch with tab.Festival
		{
			buf.writeH(0); // 1
			final int sizeA = 0; // Festival count
			buf.writeC(sizeA);
			for (int i = 0; i < sizeA; i++)
			{
				buf.writeC(0); // Festival
				buf.writeD(0); // Minimal score
				buf.writeQ(0L); // Dusk score
				final int sizeB = 0; // Achiever party size
				buf.writeC(sizeB);
				for (int j = 0; j < sizeB; j++)
				{
					buf.writeS(""); // Achiever name
				}
				buf.writeQ(0L); // Dawn score
				final int sizeC = 0; // Achiever party size
				buf.writeC(sizeC);
				for (int j = 0; j < sizeC; j++)
				{
					buf.writeS(""); // Achiever name
				}
			}
		}
		// branch with tab.SealStatus
		{
			buf.writeC(0); // % of registrations to retain an owned seal (on victory)
			buf.writeC(0); // % of registrations to claim a seal (on victory)
			final int sizeD = 0; // Seal count
			buf.writeC(sizeD);
			for (int i = 0; i < sizeD; i++)
			{
				buf.writeC(0); // Seal
				buf.writeC(0); // Owner
				buf.writeC(0); // % of Dusk registrations
				buf.writeC(0); // % of Dawn registrations
			}
		}
		// branch with tab.Projection
		{
			buf.writeC(0); // Winning cabal
			final int sizeE = 0; // Seal count
			buf.writeC(sizeE);
			for (int i = 0; i < sizeE; i++)
			{
				buf.writeC(0); // Seal
				buf.writeC(0); // Expected owner
				buf.writeD(0); // Explanation
			}
		}
	}
}
