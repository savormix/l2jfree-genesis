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
import com.l2jfree.gameserver.modules.sevensigns.Cabal;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.network.mmocore.MMOBuffer;

/**
 * @author savormix (generated)
 */
public abstract class SSQInfoPacket extends StaticPacket
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
		public static final SkyColor NORMAL = new SkyColor(Cabal.NONE, false);
		/** Smooth transition to dusk sky. */
		public static final SkyColor DUSK = new SkyColor(Cabal.DUSK, false);
		/** Smooth transition to dawn sky. */
		public static final SkyColor DAWN = new SkyColor(Cabal.DAWN, false);
		
		/** Normal sky */
		private static final SkyColor NORMAL_INSTANT = new SkyColor(Cabal.NONE, true);
		/** Smooth transition to dusk sky. */
		private static final SkyColor DUSK_INSTANT = new SkyColor(Cabal.DUSK, true);
		/** Smooth transition to dawn sky. */
		private static final SkyColor DAWN_INSTANT = new SkyColor(Cabal.DAWN, true);
		
		public static SkyColor getForCharacterSelection()
		{
			// TODO: implement
			final Cabal ssVictor = Cabal.NONE;
			switch (ssVictor)
			{
				case DUSK:
					return DUSK_INSTANT;
				case DAWN:
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
		 * @see SSQInfoPacket#SSQInfoPacket(Cabal, boolean)
		 */
		private SkyColor(Cabal cabal, boolean instant)
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
	public SSQInfoPacket(Cabal cabal, boolean instant)
	{
		_cabal = cabal.ordinal();
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
