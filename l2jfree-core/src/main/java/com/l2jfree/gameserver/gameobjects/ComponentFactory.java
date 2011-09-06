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
package com.l2jfree.gameserver.gameobjects;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author NB4L1
 * @param <T>
 */
// TODO implement more features, use annotations, optimize performance
@SuppressWarnings("unchecked")
public final class ComponentFactory<T>
{
	public static final ComponentFactory<ObjectPosition> POSITION = new ComponentFactory<ObjectPosition>(
			ObjectPosition.class);
	
	private final Map<Class<? extends L2Object>, Class<? extends T>> _registryByOwnerClass =
			new HashMap<Class<? extends L2Object>, Class<? extends T>>();
	
	private final Map<Class<? extends L2Object>, Class<? extends T>> _cacheByOwnerClass =
			new HashMap<Class<? extends L2Object>, Class<? extends T>>();
	
	private final Map<Integer, Class<? extends T>> _registryByTemplateId = new HashMap<Integer, Class<? extends T>>();
	
	private final Map<Class<? extends T>, Map<Class<? extends L2Object>, Constructor<? extends T>>> _constructorCache =
			new HashMap<Class<? extends T>, Map<Class<? extends L2Object>, Constructor<? extends T>>>();
	
	private final Class<?> _rootClazz;
	
	private ComponentFactory(Class<?> rootClazz)
	{
		_rootClazz = rootClazz;
	}
	
	public final void register(Class<? extends L2Object> ownerClazz, Class<? extends T> componentClazz)
	{
		// FIXME handle replacement
		_registryByOwnerClass.put(ownerClazz, componentClazz);
		
		// drop cache
		_cacheByOwnerClass.clear();
	}
	
	public final void register(int templateId, Class<? extends T> componentClazz)
	{
		// FIXME handle replacement
		_registryByTemplateId.put(templateId, componentClazz);
	}
	
	private final Class<? extends T> findComponentClass(L2Object owner)
	{
		Class<? extends T> clazz = _registryByTemplateId.get(owner.getTemplateId());
		
		if (clazz != null)
			return clazz;
		
		clazz = _cacheByOwnerClass.get(owner.getClass());
		
		if (clazz != null)
			return clazz;
		
		for (Class<?> ownerClazz = owner.getClass(); ownerClazz != null; ownerClazz = ownerClazz.getSuperclass())
		{
			clazz = _registryByOwnerClass.get(ownerClazz);
			
			if (clazz != null)
			{
				_cacheByOwnerClass.put(owner.getClass(), clazz);
				return clazz;
			}
		}
		
		return null;
	}
	
	private final Map<Class<? extends L2Object>, Constructor<? extends T>> findComponentContructors(L2Object owner)
	{
		final Class<? extends T> clazz = findComponentClass(owner);
		
		if (clazz == null)
			throw new IllegalStateException("No " + _rootClazz + " implementation registered for " + owner);
		
		Map<Class<? extends L2Object>, Constructor<? extends T>> constructors = _constructorCache.get(clazz);
		
		if (constructors != null)
			return constructors;
		
		constructors = new HashMap<Class<? extends L2Object>, Constructor<? extends T>>();
		
		for (Constructor<?> constructor : clazz.getConstructors())
		{
			final Class<?>[] parameterTypes = constructor.getParameterTypes();
			
			if (parameterTypes.length != 1 || !L2Object.class.isAssignableFrom(parameterTypes[0]))
				throw new IllegalStateException("Invalid constructor " + constructor + " for " + owner);
			
			constructors.put((Class<? extends L2Object>)parameterTypes[0], (Constructor<? extends T>)constructor);
		}
		
		_constructorCache.put(clazz, constructors);
		
		return constructors;
	}
	
	public final T getComponent(L2Object owner)
	{
		final Map<Class<? extends L2Object>, Constructor<? extends T>> constructors = findComponentContructors(owner);
		
		for (Class<?> ownerClazz = owner.getClass(); ownerClazz != null; ownerClazz = ownerClazz.getSuperclass())
		{
			final Constructor<? extends T> constructor = constructors.get(ownerClazz);
			
			if (constructor == null)
				continue;
			
			try
			{
				return constructor.newInstance(owner);
			}
			catch (Exception e) // IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException
			{
				e.printStackTrace();
			}
		}
		
		throw new IllegalStateException("No proper " + _rootClazz + " contructor registered for " + owner);
	}
	
	public static void main(String[] args)
	{
		long begin1 = System.currentTimeMillis();
		for (int i = 0; i < 10000000; i++)
		{
			new L2Object(123123) {
				@Override
				public void setName(String name)
				{
					//
				}
				
				@Override
				public int getTemplateId()
				{
					return 0;
				}
				
				@Override
				public String getName()
				{
					return null;
				}
			}.hashCode();
			
			// System.out.println(pet.getPosition());
		}
		System.out.println(System.currentTimeMillis() - begin1);
		
		long begin2 = System.currentTimeMillis();
		for (int i = 0; i < 10000000; i++)
		{
			new L2PetInstance(123123) {
				@Override
				public void setName(String name)
				{
					//
				}
				
				@Override
				public int getTemplateId()
				{
					return 0;
				}
				
				@Override
				public String getName()
				{
					return null;
				}
			}.hashCode();
			
			// System.out.println(pet.getPosition());
		}
		System.out.println(System.currentTimeMillis() - begin2);
	}
}
