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
public abstract class SSQInfoPacket extends L2ServerPacket
{
	/**
	 * A nicer name for {@link SSQInfoPacket}.
	 * 
	 * @author savormix (generated)
	 * @see SSQInfoPacket
	 */
	public static final class SkyColor extends SSQInfoPacket
	{
		/** Smooth transition to normal sky. */
		public static final SkyColor NORMAL = new SkyColor(0, false);
		/** Smooth transition to dusk sky. */
		public static final SkyColor DUSK = new SkyColor(1, false);
		/** Smooth transition to dawn sky. */
		public static final SkyColor DAWN = new SkyColor(2, false);
		
		/** Normal sky */
		private static final SkyColor NORMAL_INSTANT = new SkyColor(0, true);
		/** Smooth transition to dusk sky. */
		private static final SkyColor DUSK_INSTANT = new SkyColor(1, true);
		/** Smooth transition to dawn sky. */
		private static final SkyColor DAWN_INSTANT = new SkyColor(2, true);
		
		public static SkyColor getForCharacterSelection()
		{
			// TODO: implement
			final int ssVictor = 0;
			switch (ssVictor)
			{
				case 1:
					return DUSK_INSTANT;
				case 2:
					return DAWN_INSTANT;
				default:
					return NORMAL_INSTANT;
			}
		}
		
		/**
		 * Constructs this packet.
		 * 
		 * @param cabal seven signs victor
		 * @param instant transition type
		 * @see SSQInfoPacket#SSQInfoPacket(int, boolean)
		 */
		private SkyColor(int cabal, boolean instant)
		{
			super(cabal, instant);
		}
	}
	
	private final int _cabal;
	private final boolean _instant;
	
	/**
	 * Constructs this packet.
	 * 
	 * @param cabal seven signs victor
	 * @param instant transition type
	 */
	public SSQInfoPacket(int cabal, boolean instant)
	{
		_cabal = cabal;
		_instant = instant;
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x73;
	}
	
	@Override
	protected void writeImpl(L2Client client, L2Player activeChar, MMOBuffer buf) throws RuntimeException
	{
		buf.writeC(_cabal); // Color
		buf.writeC(_instant); // Instant change
	}
}
