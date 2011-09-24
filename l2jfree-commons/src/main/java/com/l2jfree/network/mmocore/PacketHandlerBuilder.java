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

import java.util.Map;
import java.util.TreeMap;

import com.l2jfree.network.mmocore.packethandlers.PacketDefinition;
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
	private final int _enumValuesLength;
	private final Handler _rootHandler = new Handler(0);
	
	public PacketHandlerBuilder(Class<S> enumClazz)
	{
		_enumValuesLength = enumClazz.getEnumConstants().length;
	}
	
	public void addPacket(Class<? extends RP> clazz, S... states) throws Exception
	{
		final PacketDefinition<T, RP, SP, S> packetDefinition = new PacketDefinition<T, RP, SP, S>(clazz, states);
		
		_rootHandler.addPacketDefinition(packetDefinition);
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
			if (packetDefinition.getOpcodes().length <= _nextOpcodeIndex)
			{
				if (!_handlersByOpcode.isEmpty())
					throw new Error("Conflicting definitions!");
				
				for (S state : packetDefinition.getStates())
					if (_definitionsByState.containsKey(state))
						throw new Error("Conflicting definitions!");
				
				for (S state : packetDefinition.getStates())
					_definitionsByState.put(state, packetDefinition);
			}
			else
			{
				if (!_definitionsByState.isEmpty())
					throw new Error("Conflicting definitions!");
				
				final int nextOpcode = packetDefinition.getOpcodes()[_nextOpcodeIndex];
				
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
		
		public void printStructure(int indent)
		{
			if (_handlersByOpcode.isEmpty())
			{
				boolean constructorWritten = false;
				
				for (Map.Entry<S, PacketDefinition<T, RP, SP, S>> entry : _definitionsByState.entrySet())
				{
					if (!constructorWritten)
					{
						System.out.println(" " + entry.getValue().getClassName());
						
						constructorWritten = true;
					}
					
					for (int i = 0; i < indent + 1; i++)
						System.out.print("\t");
					System.out.println(entry.getKey());
				}
			}
			else
			{
				System.out.println();
				for (Map.Entry<Integer, Handler> entry : _handlersByOpcode.entrySet())
				{
					for (int i = 0; i < indent; i++)
						System.out.print(" |- ");
					System.out.print("0x" + HexUtil.fillHex(entry.getKey(), 2));
					
					entry.getValue().printStructure(indent + 1);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public PacketDefinition<T, RP, SP, S>[][][][] buildTable()
	{
		_rootHandler.printStructure(0);
		
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
}
