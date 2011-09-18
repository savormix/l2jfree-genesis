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
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.l2jfree.util.HexUtil;

/**
 * @author NB4L1
 * @param <T> connection
 * @param <RP> receivable packet
 * @param <SP> sendable packet
 * @param <S> client state
 */
public final class PacketHandlerBuilder<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>, S extends Enum<S>>
{
	private final class PacketDefinition
	{
		private final Constructor<? extends RP> _constructor;
		private final Integer[] _opcodes;
		private final S[] _states;
		
		public PacketDefinition(Class<? extends RP> clazz, S... states)
		{
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
	}
	
	private final Handler _rootHandler = new Handler(0);
	
	public void addPacket(Class<? extends RP> clazz, S... states)
	{
		String prefix = clazz.getPackage().getName() + ".";
		
		final PacketDefinition packetDefinition = new PacketDefinition(clazz, states);
		
		// easier than checking all the modifiers, visibility, etc :)
		System.out.print("[ 0x" + HexUtil.fillHex(packetDefinition._opcodes[0], 2));
		for (int i = 1; i < packetDefinition._opcodes.length; i++)
			System.out.print(" : 0x" + HexUtil.fillHex(packetDefinition._opcodes[i], 2));
		System.out.println(" ] " + packetDefinition._constructor.toString().replace(prefix, ""));
		
		_rootHandler.addPacketDefinition(packetDefinition);
	}
	
	private final class Handler
	{
		private final TreeMap<S, PacketDefinition> _definitionsByState = new TreeMap<S, PacketDefinition>();
		private final TreeMap<Integer, Handler> _handlersByOpcode = new TreeMap<Integer, Handler>();
		
		private final int _nextOpcodeIndex;
		
		private Handler(int nextOpcodeIndex)
		{
			_nextOpcodeIndex = nextOpcodeIndex;
		}
		
		private void addPacketDefinition(PacketDefinition packetDefinition)
		{
			if (packetDefinition._opcodes.length <= _nextOpcodeIndex)
			{
				for (S state : packetDefinition._states)
					if (_definitionsByState.containsKey(state))
						throw new Error("Conflicting definitions!");
				
				for (S state : packetDefinition._states)
					_definitionsByState.put(state, packetDefinition);
			}
			else
			{
				final int nextOpcode = packetDefinition._opcodes[_nextOpcodeIndex];
				
				Handler handler = _handlersByOpcode.get(nextOpcode);
				
				if (handler == null)
					_handlersByOpcode.put(nextOpcode, handler = new Handler(_nextOpcodeIndex + 1));
				
				handler.addPacketDefinition(packetDefinition);
			}
		}
	}
}
