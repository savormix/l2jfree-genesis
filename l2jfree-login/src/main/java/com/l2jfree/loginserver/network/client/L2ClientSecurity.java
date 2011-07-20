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
package com.l2jfree.loginserver.network.client;

import java.security.GeneralSecurityException;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.concurrent.atomic.AtomicInteger;

import javax.crypto.Cipher;

import com.l2jfree.Shutdown;
import com.l2jfree.TerminationStatus;
import com.l2jfree.security.ScrambledKeyPair;
import com.l2jfree.util.Rnd;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author savormix
 *
 */
public class L2ClientSecurity
{
	private static final L2Logger _log = L2Logger.getLogger(L2ClientSecurity.class);
	
	private static final int RSA_KEY_PAIR_COUNT = 10;
	private static final int BLOWFISH_KEY_LENGTH = 16;
	private static final int BLOWFISH_KEY_COUNT = 20;
	
	private final AtomicInteger _sessionId;
	
	private final ScrambledKeyPair[] _keyPairs;
	private final byte[][] _blowfishKeys;
	
	private L2ClientSecurity()
	{
		_sessionId = new AtomicInteger();
		
		_keyPairs = new ScrambledKeyPair[RSA_KEY_PAIR_COUNT];
		_blowfishKeys = new byte[BLOWFISH_KEY_COUNT][BLOWFISH_KEY_LENGTH];
		
		KeyPairGenerator rsa = null;
		try
		{
			rsa = KeyPairGenerator.getInstance("RSA");
			AlgorithmParameterSpec spec = new RSAKeyGenParameterSpec(1024,
					RSAKeyGenParameterSpec.F4);
			rsa.initialize(spec);
		}
		catch (GeneralSecurityException e)
		{
			_log.fatal("Could not generate RSA key pairs!", e);
			Shutdown.exit(TerminationStatus.ENVIRONMENT_MISSING_COMPONENT_OR_SERVICE);
			return;
		}
		
		for (int i = 0; i < getKeyPairs().length; i++)
			getKeyPairs()[i] = new ScrambledKeyPair(rsa.generateKeyPair());
		_log.info("Generated " + getKeyPairs().length + " RSA key pairs (client).");
		
		for (int i = 0; i < BLOWFISH_KEY_COUNT; i++)
			Rnd.nextBytes(getBlowfishKeys()[i]);
		_log.info("Generated " + getBlowfishKeys().length + " Blowfish keys (client).");
		
		try
		{
			warmUp(getKeyPair().getPair().getPrivate());
		}
		catch (GeneralSecurityException e)
		{
			_log.fatal("Invalid generated key pair!", e);
			Shutdown.exit(TerminationStatus.ENVIRONMENT_MISSING_COMPONENT_OR_SERVICE);
		}
	}
	
	private void warmUp(PrivateKey key) throws GeneralSecurityException
	{
		// avoid worst-case execution, KenM
		Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
		rsaCipher.init(Cipher.DECRYPT_MODE, key);
	}
	
	/**
	 * Generates a session ID.
	 * @return a session ID
	 */
	public int getNextSessionId()
	{
		return getSessionId().incrementAndGet();
	}
	
	/**
	 * Returns a scrambled RSA key pair.
	 * @return a key pair
	 */
	public ScrambledKeyPair getKeyPair()
	{
		return Rnd.get(getKeyPairs());
	}
	
	/**
	 * Returns a Blowfish key.
	 * @return a Blowfish key
	 */
	public byte[] getBlowfishKey()
	{
		return Rnd.get(getBlowfishKeys());
	}
	
	private AtomicInteger getSessionId()
	{
		return _sessionId;
	}
	
	private ScrambledKeyPair[] getKeyPairs()
	{
		return _keyPairs;
	}
	
	private byte[][] getBlowfishKeys()
	{
		return _blowfishKeys;
	}
	
	/**
	 * Returns a singleton object.
	 * @return an instance of this class
	 */
	public static L2ClientSecurity getInstance()
	{
		return SingletonHolder._instance;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		static final L2ClientSecurity _instance = new L2ClientSecurity();
	}
}
