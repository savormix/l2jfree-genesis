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
package com.l2jfree.security;

import java.nio.ByteBuffer;
import java.util.Arrays;

import com.l2jfree.ClientProtocolVersion;
import com.l2jfree.network.mmocore.MMOBuffer;
import com.l2jfree.util.HexUtil;
import com.l2jfree.util.Rnd;
import com.l2jfree.util.logging.L2Logger;

/**
 * Provides client packet opcode obfuscation and deobfuscation using a pseudo-random seeded opcode
 * shuffling scheme.<BR>
 * <BR>
 * Adapted for L2JFree from <A HREF="http://code.google.com/p/l2packets/">l2packets</A>.<BR>
 * <BR>
 * Reference code (C++):<BR>
 * <A HREF=
 * "http://code.google.com/p/l2packets/source/browse/trunk/src/pcode_obfuscator/L2PCodeObfuscator.h"
 * >Header</A><BR>
 * <A HREF=
 * "http://code.google.com/p/l2packets/source/browse/trunk/src/pcode_obfuscator/L2PCodeObfuscator.cpp"
 * >Source</A>
 * 
 * @author savormix
 */
public final class ObfuscationService
{
	private final L2Logger _log = L2Logger.getLogger(ObfuscationService.class);
	
	private static final int HAS_SECOND = 0xD0;
	// TODO: private static final int HAS_THIRD = 0x51;
	
	private final ClientProtocolVersion _version;
	
	private long _seed;
	private boolean _seeded;
	private boolean _initialized;
	
	private int _s1;
	private int _s2;
	private int _s3;
	private byte[] _decodeTable1;
	private byte[] _decodeTable2;
	private byte[] _decodeTable3;
	private byte[] _encodeTable1;
	private byte[] _encodeTable2;
	private byte[] _encodeTable3;
	
	/**
	 * Constructs an uninitialized service.
	 * 
	 * @param version protocol version
	 */
	public ObfuscationService(ClientProtocolVersion version)
	{
		_version = version;
		_seeded = false;
		_initialized = false;
	}
	
	private void reset()
	{
		_seeded = false;
		_initialized = false;
		_decodeTable1 = _decodeTable2 = _decodeTable3 = null;
		_encodeTable1 = _encodeTable2 = _encodeTable3 = null;
	}
	
	/**
	 * Generates a pseudo-random integer.
	 * 
	 * @return integer from [0;32767]
	 * @throws IllegalStateException if not seeded
	 */
	private int getGenerated() throws IllegalStateException
	{
		if (!_seeded)
			throw new IllegalStateException();
		
		final long step1 = toUnsignedInt(_seed * 0x343fd);
		_seed = toUnsignedInt(step1 + 0x269EC3);
		
		final long result = (_seed >>> 16) & 0x7FFF;
		return (int)result; // safe to cast
	}
	
	/**
	 * Seeds the pseudo-random integer generator.
	 * 
	 * @param seed obfuscation key
	 * @throws IllegalStateException if already seeded
	 */
	private void setGeneratorSeed(long seed) throws IllegalStateException
	{
		if (_seeded)
			throw new IllegalStateException();
		
		if (_log.isDebugEnabled())
			_log.debug("Received key: " + seed);
		_seed = toUnsignedInt(seed);
		_seeded = true;
	}
	
	/**
	 * Initializes service with the given seed (key).
	 * 
	 * @param seed obfuscation key
	 */
	public synchronized void init(final long seed)
	{
		if (_initialized)
			reset();
		
		int i = 0;
		byte tmp = 0;
		int pos;
		int cpos;
		
		_s1 = 0xD0;
		_s2 = _version.getOp2TableSize();
		_s3 = 0x64; // O_o TODO: GF+ triple opcodes
		
		_decodeTable1 = new byte[_s1 + 1];
		_decodeTable2 = new byte[_s2 + 1];
		_decodeTable3 = new byte[_s3 + 1];
		
		for (i = 0; i <= _s1; i++)
			_decodeTable1[i] = (byte)i;
		for (i = 0; i <= _s2; i++)
			_decodeTable2[i] = (byte)i;
		for (i = 0; i <= _s3; i++)
			_decodeTable3[i] = (byte)i;
		
		setGeneratorSeed(seed);
		
		if (seed != 0) // check whether to shuffle
		{
			// mix 1-byte opcode table
			for (i = 1; i <= _s1; i++)
			{
				pos = getGenerated() % (i + 1);
				// swap bytes at indexes [pos] and [i] in DecodeTable1
				tmp = _decodeTable1[pos];
				_decodeTable1[pos] = _decodeTable1[i];
				_decodeTable1[i] = tmp;
			}
			
			// mix 2-byte opcode table
			for (i = 1; i <= _s2; i++)
			{
				pos = getGenerated() % (i + 1);
				// swap bytes at indexes [pos] and [i] in DecodeTable2
				tmp = _decodeTable2[pos];
				_decodeTable2[pos] = _decodeTable2[i];
				_decodeTable2[i] = tmp;
			}
			
			// non-obfuscated main opcodes
			// FIXME: move to ClientProtocolVersion
			cpos = 0;
			while (_decodeTable1[cpos] != 0x12)
				cpos++;
			tmp = _decodeTable1[0x12];
			_decodeTable1[0x12] = 0x12;
			_decodeTable1[cpos] = tmp;
			
			cpos = 0;
			while (_decodeTable1[cpos] != (byte)0xB1)
				cpos++;
			tmp = _decodeTable1[0xB1];
			_decodeTable1[0xB1] = (byte)0xB1;
			_decodeTable1[cpos] = tmp;
			
			cpos = 0;
			while (_decodeTable1[cpos] != 0x11)
				cpos++;
			tmp = _decodeTable1[0x11];
			_decodeTable1[0x11] = 0x11;
			_decodeTable1[cpos] = tmp;
			
			cpos = 0;
			while (_decodeTable1[cpos] != (byte)0xD0)
				cpos++;
			tmp = _decodeTable1[0xD0];
			_decodeTable1[0xD0] = (byte)0xD0;
			_decodeTable1[cpos] = tmp;
			
			for (int op : _version.getIgnoredOp1s())
			{
				cpos = 0;
				while (_decodeTable1[cpos] != (byte)op)
					cpos++;
				tmp = _decodeTable1[op];
				_decodeTable1[op] = (byte)op;
				_decodeTable1[cpos] = tmp;
			}
			
			// non-obfuscated 2nd opcodes
			cpos = 0;
			while (_decodeTable2[cpos] != 0x74)
				cpos++;
			tmp = _decodeTable2[0x74];
			_decodeTable2[0x74] = 0x74;
			_decodeTable2[cpos] = tmp;
			
			for (int op : _version.getIgnoredOp2s())
			{
				cpos = 0;
				while (_decodeTable2[cpos] != (byte)op)
					cpos++;
				tmp = _decodeTable2[op];
				_decodeTable2[op] = (byte)op;
				_decodeTable2[cpos] = tmp;
			}
		}
		
		// mirrored obfuscation tables
		_encodeTable1 = new byte[_s1 + 1];
		_encodeTable2 = new byte[_s2 + 1];
		_encodeTable3 = new byte[_s3 + 1];
		
		for (i = 0; i <= _s1; i++)
		{
			int idx = (_decodeTable1[i] & 0xFF);
			_encodeTable1[idx] = (byte)i;
		}
		for (i = 0; i <= _s2; i++)
		{
			int idx = (_decodeTable2[i] & 0xFF);
			_encodeTable2[idx] = (byte)i;
		}
		for (i = 0; i <= _s3; i++)
		{
			int idx = (_decodeTable3[i] & 0xFF);
			_encodeTable3[idx] = (byte)i;
		}
		
		_initialized = true;
		
		if (_log.isDebugEnabled())
			_log.debug("Final seed is: " + _seed);
	}
	
	/**
	 * Deobfuscates client packet opcode(s). Does not modify buffer's position/mark/limit.
	 * 
	 * @param body packet's body
	 * @param size packet's size
	 */
	public void decodeOpcodes(ByteBuffer body, int size)
	{
		if (!_initialized || body == null || body.remaining() == 0)
			return; // no action
			
		final int mainPos = body.position();
		try
		{
			MMOBuffer buf = new MMOBuffer();
			buf.setByteBuffer(body);
			
			final int obOp1 = buf.readUC();
			if (obOp1 > _s1)
			{
				_log.error("Adjust 1st op table size to at least " + obOp1);
				return;
			}
			
			final int op1 = _decodeTable1[obOp1] & 0xFF;
			
			body.position(mainPos);
			buf.writeC(op1);
			
			if (_log.isDebugEnabled())
				_log.debug("Decoded " + HexUtil.fillHex(obOp1, 2) + " to " + HexUtil.fillHex(op1, 2) + " using seed "
						+ _seed);
			
			if (op1 == HAS_SECOND && size > 1)
			{
				final int secondPos = body.position();
				
				final int obOp2 = buf.readUC(); // buf.readUH()
				if (obOp2 > _s2)
				{
					_log.error("Adjust 2nd op table size to at least " + obOp2);
					return;
				}
				
				body.position(secondPos);
				final int op2 = _decodeTable2[obOp2] & 0xFF;
				buf.writeC(op2);
				
				if (_log.isDebugEnabled())
					_log.debug("Decoded(2) " + HexUtil.fillHex(obOp2, 2) + " to " + HexUtil.fillHex(op2, 2)
							+ " using seed " + _seed + " and size " + HexUtil.fillHex(_s2, 2));
			}
		}
		finally
		{
			body.position(mainPos);
		}
	}
	
	/**
	 * Obfuscates client packet opcode(s). Does not modify buffer's position/mark/limit.
	 * 
	 * @param body packet's body
	 * @param size packet's size
	 */
	public void encodeOpcodes(ByteBuffer body, int size)
	{
		if (!_initialized || body == null || size == 0)
			return; // no action
			
		if (_log.isDebugEnabled())
		{
			byte[] b = new byte[size];
			System.arraycopy(body.array(), body.position(), b, 0, b.length);
			_log.debug("Encoding this packet: " + HexUtil.printData(b));
		}
		
		final int mainPos = body.position();
		try
		{
			MMOBuffer buf = new MMOBuffer();
			buf.setByteBuffer(body);
			
			final int op1 = buf.readUC();
			if (op1 > _s1)
			{
				_log.error("Adjust 1st op table size to at least " + op1);
				return;
			}
			
			final int obOp1 = _encodeTable1[op1] & 0xFF;
			
			body.position(mainPos);
			buf.writeC(obOp1);
			
			if (_log.isDebugEnabled())
				_log.debug("Encoded " + HexUtil.fillHex(op1, 2) + " to " + HexUtil.fillHex(obOp1, 2) + " using seed "
						+ _seed);
			
			if (op1 == HAS_SECOND && size > 1)
			{
				final int secondPos = body.position();
				
				final int op2 = buf.readUC(); // buf.readUH()
				if (op2 > _s2)
				{
					_log.error("Adjust 2nd op table size to at least " + op2);
					return;
				}
				
				body.position(secondPos);
				final int obOp2 = _encodeTable2[op2];
				buf.writeC(obOp2);
				
				if (_log.isDebugEnabled())
					_log.debug("Encoded(2) " + HexUtil.fillHex(op2, 2) + " to " + HexUtil.fillHex(obOp2, 2)
							+ " using seed " + _seed);
			}
		}
		finally
		{
			body.position(mainPos);
		}
	}
	
	/**
	 * Returns <TT>lval & 0xFF_FF_FF_FF</TT>.<BR>
	 * <BR>
	 * Used to avoid unwanted side-effects of <TT>lval & -1</TT>, because -1, when expanded to long,
	 * is still -1 (instead of over 4 billion).
	 * 
	 * @param lval some long value
	 * @return value as unsigned int
	 */
	public static long toUnsignedInt(long lval)
	{
		/*
		long loHalf = lval & 0xFFFF;
		long hiHalf = (lval >>> 16) & 0xFFFF;
		long result = (hiHalf << 16) + loHalf;
		return result;
		*/
		return lval & 0xFFFFFFFFL;
	}
	
	/**
	 * Tests this service.
	 * 
	 * @throws InternalError unexpected behavior
	 */
	public static void test() throws InternalError
	{
		ObfuscationService os = new ObfuscationService(ClientProtocolVersion.FREYA);
		os.init(Rnd.nextLong());
		final byte[] opcodes = { (byte)0xD0, (byte)0xFF, 0x00 };
		ByteBuffer bb = ByteBuffer.wrap(opcodes);
		for (int i = 0; i < 0xD1; i++)
		{
			opcodes[0] = (byte)i;
			final byte[] before = opcodes.clone();
			os.decodeOpcodes(bb, opcodes.length);
			os.encodeOpcodes(bb, opcodes.length);
			if (!Arrays.equals(opcodes, before))
				throw new InternalError("Opcode " + i);
		}
	}
}
