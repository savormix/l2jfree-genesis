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
package com.l2jfree.network.mmocore.packethandlers;

import java.util.Map;
import java.util.TreeMap;

import com.l2jfree.network.mmocore.MMOConnection;
import com.l2jfree.network.mmocore.ReceivablePacket;
import com.l2jfree.network.mmocore.SendablePacket;
import com.l2jfree.util.HexUtil;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author NB4L1
 * @param <T> connection
 * @param <RP> receivable packet
 * @param <SP> sendable packet
 * @param <S> client state
 */
// FIXME runtime class generation with switch-case (so as the old structure, yet without the extensive maintenance cost)
@SuppressWarnings("unchecked")
public final class PacketHandlerBuilder<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>, S extends Enum<S>>
{
	private static final L2Logger _log = L2Logger.getLogger(PacketHandlerBuilder.class);
	
	private final int _enumValuesLength;
	private final S[] _defaultStates;
	private final Handler _rootHandler = new Handler(0);
	
	public PacketHandlerBuilder(Class<S> enumClazz, S... defaultStates) throws Exception
	{
		_enumValuesLength = enumClazz.getEnumConstants().length;
		_defaultStates = defaultStates;
		
		if (_enumValuesLength == 0 || _defaultStates.length == 0)
			throw new Exception();
	}
	
	public void addPacket(Class<? extends RP> clazz) throws Exception
	{
		addPacket(clazz, true);
	}
	
	public void addPacket(Class<? extends RP> clazz, boolean strict) throws Exception
	{
		final PacketDefinition<T, RP, SP, S> packetDefinition =
				new PacketDefinition<T, RP, SP, S>(clazz, _defaultStates);
		
		try
		{
			_rootHandler.addPacketDefinition(packetDefinition);
		}
		catch (Exception e)
		{
			if (strict)
				throw e;
			else
				_log.info("", e);
		}
	}
	
	public void addPackets(String packageName) throws Exception
	{
		for (Class<?> c : PacketDefinition.findClasses(packageName))
			addPacket((Class<? extends RP>)c, false);
	}
	
	public Handler getRootHandler()
	{
		return _rootHandler;
	}
	
	public final class Handler
	{
		private final TreeMap<S, PacketDefinition<T, RP, SP, S>> _definitionsByState =
				new TreeMap<S, PacketDefinition<T, RP, SP, S>>();
		private final TreeMap<Integer, Handler> _handlersByOpcode = new TreeMap<Integer, Handler>();
		
		private final int _nextOpcodeIndex;
		
		private Handler(int nextOpcodeIndex)
		{
			_nextOpcodeIndex = nextOpcodeIndex;
		}
		
		private void addPacketDefinition(PacketDefinition<T, RP, SP, S> packetDefinition) throws Exception
		{
			if (packetDefinition.getOpcodes().length <= _nextOpcodeIndex)
			{
				if (!_handlersByOpcode.isEmpty())
					throw new Exception("Conflicting definitions: " + packetDefinition);
				
				for (S state : packetDefinition.getStates())
					if (_definitionsByState.containsKey(state))
						throw new Exception("Conflicting definitions:\r\n\t\t" + packetDefinition + "\r\n\t\t"
								+ _definitionsByState.get(state));
				
				for (S state : packetDefinition.getStates())
					_definitionsByState.put(state, packetDefinition);
			}
			else
			{
				if (!_definitionsByState.isEmpty())
					throw new Exception("Conflicting definitions: " + packetDefinition);
				
				final int nextOpcode = packetDefinition.getOpcodes()[_nextOpcodeIndex];
				
				Handler handler = _handlersByOpcode.get(nextOpcode);
				
				if (handler == null)
					_handlersByOpcode.put(nextOpcode, handler = new Handler(_nextOpcodeIndex + 1));
				
				handler.addPacketDefinition(packetDefinition);
			}
		}
		
		private PacketDefinition<T, RP, SP, S>[] getDefinitionsByState()
		{
			final PacketDefinition<T, RP, SP, S>[] result = new PacketDefinition[_enumValuesLength];
			
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
		
		public PacketDefinition<T, RP, SP, S>[][] buildOneLevelTable() throws Exception
		{
			final PacketDefinition<T, RP, SP, S>[][] table = new PacketDefinition[256][];
			
			for (Map.Entry<Integer, Handler> entry : _handlersByOpcode.entrySet())
			{
				final int opcode = entry.getKey();
				final Handler handler = entry.getValue();
				
				if (handler._handlersByOpcode.isEmpty())
				{
					table[opcode] = handler.getDefinitionsByState();
				}
				else
				{
					throw new Exception();
				}
			}
			
			return table;
		}
		
		public PacketDefinition<T, RP, SP, S>[][][] buildTwoLevelTable() throws Exception
		{
			final PacketDefinition<T, RP, SP, S>[][][] table = new PacketDefinition[256][][];
			
			for (Map.Entry<Integer, Handler> entry : _handlersByOpcode.entrySet())
			{
				final int opcode = entry.getKey();
				final Handler handler = entry.getValue();
				
				if (handler._handlersByOpcode.isEmpty())
				{
					table[opcode] = new PacketDefinition[][] { handler.getDefinitionsByState() };
				}
				else
				{
					table[opcode] = handler.buildOneLevelTable();
				}
			}
			
			return table;
		}
		
		public PacketDefinition<T, RP, SP, S>[][][][] buildThreeLevelTable() throws Exception
		{
			final PacketDefinition<T, RP, SP, S>[][][][] table = new PacketDefinition[256][][][];
			
			for (Map.Entry<Integer, Handler> entry : _handlersByOpcode.entrySet())
			{
				final int opcode = entry.getKey();
				final Handler handler = entry.getValue();
				
				if (handler._handlersByOpcode.isEmpty())
				{
					table[opcode] = new PacketDefinition[][][] { { handler.getDefinitionsByState() } };
				}
				else
				{
					table[opcode] = handler.buildTwoLevelTable();
				}
			}
			
			return table;
		}
	}
}
