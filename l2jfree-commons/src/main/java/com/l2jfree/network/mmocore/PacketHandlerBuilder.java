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
package com.l2jfree.network.mmocore;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.l2jfree.util.HexUtil;

/**
 * @author NB4L1
 * @param <T> connection
 * @param <RP> receivable packet
 * @param <SP> sendable packet
 * @param <S> client state
 */
// FIXME runtime class generation with switch-case (so as the old structure, yet without the extensive maintenance cost)
public final class PacketHandlerBuilder<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>, S extends Enum<S>>
{
	public static final class PacketDefinition<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>, S extends Enum<S>>
	{
		private final Class<? extends RP> _clazz;
		private final Constructor<? extends RP> _constructor;
		private final Integer[] _opcodes;
		private final S[] _states;
		
		public PacketDefinition(Class<? extends RP> clazz, S... states)
		{
			_clazz = clazz;
			_constructor = findConstructor(clazz);
			
			final List<Integer> opcodes = new ArrayList<Integer>();
			
			opcodes.add(findOpcode(clazz, "OPCODE", true));
			
			for (int i = 2;; i++)
			{
				final Integer opcode = findOpcode(clazz, "OPCODE_" + i, false);
				if (opcode != null)
					opcodes.add(opcode);
				else
					break; // break as it was expected
			}
			
			_opcodes = opcodes.toArray(new Integer[opcodes.size()]);
			_states = states;
		}
		
		@SuppressWarnings("static-method")
		private Constructor<? extends RP> findConstructor(Class<? extends RP> clazz)
		{
			// easier than checking all the modifiers, visibility, type, etc :)
			final Constructor<? extends RP> constructor;
			try
			{
				constructor = clazz.getConstructor();
			}
			catch (NoSuchMethodException e)
			{
				System.err.println("Missing 'public " + clazz.getName() + "()'!");
				throw new Error(e);
			}
			
			try
			{
				// try to instance, and return the constructor only on success
				constructor.newInstance();
				return constructor;
			}
			catch (Exception e)
			{
				throw new Error(e);
			}
		}
		
		@SuppressWarnings("static-method")
		private Integer findOpcode(Class<? extends RP> clazz, String fieldName, boolean required)
		{
			// easier than checking all the modifiers, visibility, type, etc :)
			final Field field;
			try
			{
				field = clazz.getField(fieldName);
			}
			catch (NoSuchFieldException e)
			{
				if (required)
				{
					System.err.println("Missing 'public static final int " + fieldName + "'!");
					throw new Error(e);
				}
				else
					return null; // ignore as it was expected
			}
			
			try
			{
				return field.getInt(null);
			}
			catch (IllegalAccessException e)
			{
				System.err.println("Expected 'public static final int " + fieldName + "', but found " + field);
				throw new Error(e);
			}
		}
		
		public RP newInstance()
		{
			try
			{
				return _constructor.newInstance();
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		
		public void print()
		{
			print(_opcodes);
		}
		
		public void print(Integer... opcodes)
		{
			System.out.print("[ 0x" + HexUtil.fillHex(opcodes[0], 2));
			for (int i = 1; i < opcodes.length; i++)
				if (opcodes[i] != -1)
					System.out.print(" : 0x" + HexUtil.fillHex(opcodes[i], 2));
			System.out.print(" ] " + _constructor.toString().replace(_clazz.getPackage().getName() + ".", ""));
		}
	}
	
	private final int _enumValuesLength;
	private final Handler _rootHandler = new Handler(0);
	private final List<PacketDefinition<T, RP, SP, S>> _definitions = new ArrayList<PacketDefinition<T, RP, SP, S>>();
	
	public PacketHandlerBuilder(Class<S> enumClazz)
	{
		_enumValuesLength = enumClazz.getEnumConstants().length;
	}
	
	public void addPacket(Class<? extends RP> clazz, S... states)
	{
		final PacketDefinition<T, RP, SP, S> packetDefinition = new PacketDefinition<T, RP, SP, S>(clazz, states);
		
		packetDefinition.print();
		System.out.println();
		
		_rootHandler.addPacketDefinition(packetDefinition);
		_definitions.add(packetDefinition);
	}
	
	private final class Handler
	{
		private final TreeMap<S, PacketDefinition<T, RP, SP, S>> _definitionsByState =
				new TreeMap<S, PacketDefinition<T, RP, SP, S>>();
		private final TreeMap<Integer, Handler> _handlersByOpcode = new TreeMap<Integer, Handler>();
		
		private final int _nextOpcodeIndex;
		
		private Handler(int nextOpcodeIndex)
		{
			_nextOpcodeIndex = nextOpcodeIndex;
		}
		
		private void addPacketDefinition(PacketDefinition<T, RP, SP, S> packetDefinition)
		{
			if (packetDefinition._opcodes.length <= _nextOpcodeIndex)
			{
				if (!_handlersByOpcode.isEmpty())
					throw new Error("Conflicting definitions!");
				
				for (S state : packetDefinition._states)
					if (_definitionsByState.containsKey(state))
						throw new Error("Conflicting definitions!");
				
				for (S state : packetDefinition._states)
					_definitionsByState.put(state, packetDefinition);
			}
			else
			{
				if (!_definitionsByState.isEmpty())
					throw new Error("Conflicting definitions!");
				
				final int nextOpcode = packetDefinition._opcodes[_nextOpcodeIndex];
				
				Handler handler = _handlersByOpcode.get(nextOpcode);
				
				if (handler == null)
					_handlersByOpcode.put(nextOpcode, handler = new Handler(_nextOpcodeIndex + 1));
				
				handler.addPacketDefinition(packetDefinition);
			}
		}
		
		private PacketDefinition<T, RP, SP, S>[] getDefinitionsByState(PacketDefinition<T, RP, SP, S>[] result)
		{
			for (Map.Entry<S, PacketDefinition<T, RP, SP, S>> entry : _definitionsByState.entrySet())
				result[entry.getKey().ordinal()] = entry.getValue();
			
			return result;
		}
	}
	
	@SuppressWarnings("unchecked")
	public PacketDefinition<T, RP, SP, S>[][][][] buildTable()
	{
		final PacketDefinition<T, RP, SP, S>[][][][] table = new PacketDefinition[256][][][];
		
		for (Map.Entry<Integer, Handler> entry1 : _rootHandler._handlersByOpcode.entrySet())
		{
			final int opcode1 = entry1.getKey();
			final Handler handler1 = entry1.getValue();
			
			if (handler1._handlersByOpcode.isEmpty())
			{
				if (table[opcode1] == null)
					table[opcode1] = new PacketDefinition[1][1][_enumValuesLength];
				
				handler1.getDefinitionsByState(table[opcode1][0][0]);
			}
			else
			{
				if (table[opcode1] == null)
					table[opcode1] = new PacketDefinition[256][][];
				
				for (Map.Entry<Integer, Handler> entry2 : handler1._handlersByOpcode.entrySet())
				{
					final int opcode2 = entry2.getKey();
					final Handler handler2 = entry2.getValue();
					
					if (handler2._handlersByOpcode.isEmpty())
					{
						if (table[opcode1][opcode2] == null)
							table[opcode1][opcode2] = new PacketDefinition[1][_enumValuesLength];
						
						handler2.getDefinitionsByState(table[opcode1][opcode2][0]);
					}
					else
					{
						if (table[opcode1][opcode2] == null)
							table[opcode1][opcode2] = new PacketDefinition[256][];
						
						for (Map.Entry<Integer, Handler> entry3 : handler2._handlersByOpcode.entrySet())
						{
							final int opcode3 = entry3.getKey();
							final Handler handler3 = entry3.getValue();
							
							if (handler3._handlersByOpcode.isEmpty())
							{
								if (table[opcode1][opcode2][opcode3] == null)
									table[opcode1][opcode2][opcode3] = new PacketDefinition[_enumValuesLength];
								
								handler3.getDefinitionsByState(table[opcode1][opcode2][opcode3]);
							}
							else
							{
								throw new Error();
							}
						}
					}
				}
			}
		}
		
		return table;
	}
	
	public static class DynamicPacketHandler<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>, S extends Enum<S>>
			extends PacketHandler<T, RP, SP>
	{
		private PacketDefinition<T, RP, SP, S>[][][][] _table;
		
		protected DynamicPacketHandler(PacketDefinition<T, RP, SP, S>[][][][] table)
		{
			_table = table;
		}
		
		@Override
		public final RP handlePacket(ByteBuffer buf, T client, final int opcode1)
		{
			if (_table[opcode1] == null)
			{
				return unknown(buf, client, opcode1);
			}
			else if (_table[opcode1].length == 1)
			{
				final PacketDefinition<T, RP, SP, S> packetDefinition =
						_table[opcode1][0][0][client.getState().ordinal()];
				
				if (packetDefinition != null)
					return packetDefinition.newInstance();
				
				return invalidState(client, opcode1);
			}
			else
			{
				if (buf.remaining() < 2)
					return underflow(buf, client, opcode1);
				
				final int opcode2 = buf.getShort() & 0xffff;
				
				if (_table[opcode1][opcode2] == null)
				{
					return unknown(buf, client, opcode1, opcode2);
				}
				else if (_table[opcode1][opcode2].length == 1)
				{
					final PacketDefinition<T, RP, SP, S> packetDefinition =
							_table[opcode1][opcode2][0][client.getState().ordinal()];
					
					if (packetDefinition != null)
						return packetDefinition.newInstance();
					
					return invalidState(client, opcode1, opcode2);
				}
				else
				{
					if (buf.remaining() < 4)
						return underflow(buf, client, opcode1, opcode2);
					
					final int opcode3 = buf.getInt() & 0xffffffff;
					
					if (_table[opcode1][opcode2][opcode3] == null)
					{
						return unknown(buf, client, opcode1, opcode2, opcode3);
					}
					else if (_table[opcode1][opcode2][opcode3].length == 1)
					{
						final PacketDefinition<T, RP, SP, S> packetDefinition =
								_table[opcode1][opcode2][opcode3][client.getState().ordinal()];
						
						if (packetDefinition != null)
							return packetDefinition.newInstance();
						
						return invalidState(client, opcode1, opcode2, opcode3);
					}
					else
					{
						throw new Error();
					}
				}
			}
		}
	}
}
