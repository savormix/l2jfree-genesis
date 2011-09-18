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
package com.l2jfree.gameserver.network.client.packets.sendable.characterless;

import com.l2jfree.gameserver.config.ReportedConfig;
import com.l2jfree.gameserver.network.client.L2Client;
import com.l2jfree.gameserver.network.client.L2ClientSecurity;
import com.l2jfree.gameserver.network.client.packets.L2ServerPacket;
import com.l2jfree.network.mmocore.MMOBuffer;
import com.l2jfree.security.CoreCipher;
import com.l2jfree.util.Rnd;

/**
 * @author savormix
 */
public abstract class VersionCheck extends L2ServerPacket
{
	/**
	 * A nicer name for {@link VersionCheck}.
	 * 
	 * @author savormix
	 * @see VersionCheck
	 */
	public static final class ProtocolAnswer extends VersionCheck
	{
		/** Default response to unsupported protocol versions. */
		public static final ProtocolAnswer INCOMPATIBLE = new ProtocolAnswer(false);
		
		/**
		 * Constructs a packet to inform the client about protocol compatibility/incompatibility and
		 * keys to be used in further communications.
		 * 
		 * @param compatible whether protocol versions are compatible
		 */
		public ProtocolAnswer(boolean compatible)
		{
			super(compatible);
		}
	}
	
	private static final int CLIENT_KEY_LENGTH = 8;
	
	private final boolean _compatible;
	private final byte[] _key;
	private final CoreCipher _cipher;
	private final int _obfusKey;
	
	/**
	 * Constructs a packet to inform the client about protocol compatibility and keys to be used in
	 * further communications.
	 * 
	 * @param compatible whether protocol versions are compatible
	 */
	private VersionCheck(boolean compatible)
	{
		_compatible = compatible;
		_key = new byte[CLIENT_KEY_LENGTH];
		
		if (_compatible)
		{
			_cipher = new CoreCipher(L2ClientSecurity.getInstance().getKey());
			_cipher.getKey().get(_key);
			_obfusKey = Rnd.nextInt();
		}
		else
		{
			_cipher = null;
			_obfusKey = 0;
		}
	}
	
	@Override
	protected int getOpcode()
	{
		return 0x2e;
	}
	
	@Override
	protected void writeImpl(L2Client client, MMOBuffer buf) throws RuntimeException
	{
		buf.writeC(_compatible); // Compatible
		buf.writeB(_key); // Cipher key half
		
		buf.writeD(0x01); // 1
		buf.writeD(ReportedConfig.ID); // Server
		buf.writeC(0x01); // 1
		
		buf.writeD(_obfusKey); // Character management obfuscation key
	}
	
	@Override
	protected void packetWritten(L2Client client) throws RuntimeException
	{
		if (_compatible)
		{
			client.setCipher(_cipher);
			client.getDeobfuscator().init(_obfusKey);
		}
	}
}
