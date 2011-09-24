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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.l2jfree.network.mmocore.MMOConnection;
import com.l2jfree.network.mmocore.ReceivablePacket;
import com.l2jfree.network.mmocore.SendablePacket;
import com.l2jfree.util.HexUtil;
import com.l2jfree.util.jar.ClassFinder;

/**
 * @author NB4L1
 * @param <T>
 * @param <RP>
 * @param <SP>
 * @param <S>
 */
public class PacketDefinition<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>, S extends Enum<S>>
{
	public static List<Class<?>> findClasses(String packageName) throws Exception
	{
		final List<Class<?>> classes = new ArrayList<Class<?>>();
		
		for (Class<?> c : ClassFinder.findClasses(packageName))
			if (isInstanceable(c))
				classes.add(c);
		
		return classes;
	}
	
	public static List<Class<?>> findClasses(Class<?> clazz)
	{
		while (clazz.getEnclosingClass() != null)
			clazz = clazz.getEnclosingClass();
		
		final List<Class<?>> classes = new ArrayList<Class<?>>();
		
		classes.add(clazz);
		
		for (int i = 0; i < classes.size(); i++)
			for (Class<?> c : classes.get(i).getDeclaredClasses())
				if (!classes.contains(c))
					if (isInstanceable(c))
						classes.add(c);
		
		return classes;
	}
	
	public static boolean isInstanceable(Class<?> clazz)
	{
		try
		{
			findConstructor(clazz);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public static <T> Constructor<T> findConstructor(Class<T> clazz) throws Exception
	{
		// easier than checking all the modifiers, visibility, type, etc :)
		final Constructor<T> constructor = clazz.getConstructor();
		
		// try to instance, and return the constructor only on success
		constructor.newInstance();
		
		return constructor;
	}
	
	public static Integer findOpcode(Class<?> clazz, String fieldName, boolean required) throws Exception
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
				throw e;
			}
			else
				return null; // ignore as it was expected
		}
		
		return field.getInt(null);
	}
	
	public static String toString(int... opcodes)
	{
		final StringBuilder sb = new StringBuilder();
		
		sb.append("[");
		for (int i = 0; i < opcodes.length; i++)
		{
			if (i != 0)
				sb.append(":");
			sb.append(" 0x").append(HexUtil.fillHex(opcodes[i], 2)).append(" ");
		}
		sb.append("]");
		
		return sb.toString();
	}
	
	private final Class<? extends RP> _clazz;
	private final Constructor<? extends RP> _constructor;
	private final int[] _opcodes;
	private final S[] _states;
	
	public PacketDefinition(Class<? extends RP> clazz, S... states) throws Exception
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
		
		_opcodes = ArrayUtils.toPrimitive(opcodes.toArray(new Integer[opcodes.size()]));
		_states = states;
		
		System.out.println(toString(getOpcodes()) + " " + getClassName());
	}
	
	public String getClassName()
	{
		return _clazz.getCanonicalName().replace(_clazz.getPackage().getName() + ".", "");
	}
	
	public Constructor<? extends RP> getConstructor()
	{
		return _constructor;
	}
	
	public int[] getOpcodes()
	{
		return _opcodes;
	}
	
	public S[] getStates()
	{
		return _states;
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
}
