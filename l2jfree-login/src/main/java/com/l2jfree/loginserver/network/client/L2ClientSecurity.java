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
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.concurrent.atomic.AtomicInteger;

import javax.crypto.Cipher;

import com.l2jfree.Shutdown;
import com.l2jfree.TerminationStatus;
import com.l2jfree.security.ScrambledKeyPair;
import com.l2jfree.util.RescheduleableTask;
import com.l2jfree.util.Rnd;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author savormix
 */
public class L2ClientSecurity
{
	private static final L2Logger _log = L2Logger.getLogger(L2ClientSecurity.class);
	
	private static final int RSA_KEY_PAIR_COUNT = 10;
	private static final int BLOWFISH_KEY_LENGTH = 16;
	private static final int BLOWFISH_KEY_COUNT = 20;
	
	private final AtomicInteger _sessionId;
	
	private ScrambledKeyPair[] _keyPairs;
	private byte[][] _blowfishKeys;
	
	private L2ClientSecurity()
	{
		_sessionId = new AtomicInteger();
		
		new Updater();
		_log.info("Generated " + getKeyPairs().length + " RSA key pairs (client).");
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
	 * 
	 * @return a session ID
	 */
	public int getNextSessionId()
	{
		return getSessionId().incrementAndGet();
	}
	
	/**
	 * Assigns a session key to an authorized client connection.
	 * 
	 * @param client logged in client
	 * @return the assigned session key
	 */
	public long assignSessionKey(L2LoginClient client)
	{
		if (client == null || client.getAccount() == null)
			throw new IllegalStateException("Client not authorized.");
		SessionKey sk = client.getSessionKey();
		if (sk == null)
			client.setSessionKey(sk = new SessionKey());
		else
			sk.assignNewKey();
		return sk.getActiveKey();
	}
	
	/**
	 * Returns a scrambled RSA key pair.
	 * 
	 * @return a key pair
	 */
	public ScrambledKeyPair getKeyPair()
	{
		return Rnd.get(getKeyPairs());
	}
	
	/**
	 * Returns a Blowfish key.
	 * 
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
	 * 
	 * @return an instance of this class
	 */
	public static L2ClientSecurity getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder
	{
		public static final L2ClientSecurity INSTANCE = new L2ClientSecurity();
	}
	
	/**
	 * A session key to be assigned to an account via a client connection.
	 * 
	 * @author savormix
	 */
	public static class SessionKey
	{
		private long _activeKey;
		private long _oldKey;
		
		private SessionKey()
		{
			assignNewKey();
		}
		
		private long assignNewKey()
		{
			_oldKey = _activeKey;
			_activeKey = Rnd.get(Long.MIN_VALUE, Long.MAX_VALUE);
			return getActiveKey();
		}
		
		/**
		 * Returns the current session key.
		 * 
		 * @return session key
		 */
		public long getActiveKey()
		{
			return _activeKey;
		}
		
		/**
		 * Returns the previous session key.
		 * 
		 * @return session key
		 */
		public long getOldKey()
		{
			return _oldKey;
		}
	}
	
	/**
	 * Periodically generates new keys to replace the old ones.
	 * 
	 * @author NB4L1
	 */
	private final class Updater extends RescheduleableTask
	{
		@Override
		protected void runImpl()
		{
			final KeyPairGenerator rsa;
			try
			{
				rsa = KeyPairGenerator.getInstance("RSA");
				rsa.initialize(new RSAKeyGenParameterSpec(1024, RSAKeyGenParameterSpec.F4));
			}
			catch (GeneralSecurityException e)
			{
				_log.fatal("Could not generate RSA key pairs!", e);
				Shutdown.exit(TerminationStatus.ENVIRONMENT_MISSING_COMPONENT_OR_SERVICE);
				return; // never happens
			}
			
			final int count = Rnd.get(RSA_KEY_PAIR_COUNT / 2, RSA_KEY_PAIR_COUNT * 3 / 2); // so clients will never know if they have all of them or not
			final ScrambledKeyPair[] result = new ScrambledKeyPair[count];
			
			for (int i = 0; i < count; i++)
				result[i] = new ScrambledKeyPair(rsa.generateKeyPair());
			
			_keyPairs = result;
			
			final int count2 = Rnd.get(BLOWFISH_KEY_COUNT / 2, BLOWFISH_KEY_COUNT * 3 / 2); // so clients will never know if they have all of them or not
			byte[][] result2 = new byte[count2][BLOWFISH_KEY_LENGTH];
			for (int i = 0; i < count2; i++)
				Rnd.nextBytes(result2[i]);
			
			_blowfishKeys = result2;
		}
		
		@Override
		protected long getScheduleDelay()
		{
			return _keyPairs.length * 60 * 1000;
		}
	}
}
