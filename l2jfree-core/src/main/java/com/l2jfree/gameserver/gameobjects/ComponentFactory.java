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

import com.l2jfree.gameserver.templates.L2Template;

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
	
	private final Map<Integer, Class<? extends T>> _registryByTemplateId = new HashMap<Integer, Class<? extends T>>();
	
	private final Map<Class<? extends L2Object>, Map<Integer, Constructor<? extends T>>> _cache =
			new HashMap<Class<? extends L2Object>, Map<Integer, Constructor<? extends T>>>();
	
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
		_cache.clear();
	}
	
	public final void register(int templateId, Class<? extends T> componentClazz)
	{
		// FIXME handle replacement
		_registryByTemplateId.put(templateId, componentClazz);
		
		// drop cache
		_cache.clear();
	}
	
	private final Class<? extends T> findComponentClass(L2Object owner)
	{
		Class<? extends T> clazz = _registryByTemplateId.get(owner.getTemplate().getId());
		
		if (clazz != null)
			return clazz;
		
		for (Class<?> ownerClazz = owner.getClass(); ownerClazz != null; ownerClazz = ownerClazz.getSuperclass())
		{
			clazz = _registryByOwnerClass.get(ownerClazz);
			
			if (clazz != null)
				return clazz;
		}
		
		throw new IllegalStateException("No " + _rootClazz + " implementation registered for " + owner);
	}
	
	private final Map<Class<? extends L2Object>, Constructor<? extends T>> findComponentContructors(L2Object owner)
	{
		final Class<? extends T> clazz = findComponentClass(owner);
		
		final Map<Class<? extends L2Object>, Constructor<? extends T>> constructors =
				new HashMap<Class<? extends L2Object>, Constructor<? extends T>>();
		
		for (Constructor<?> constructor : clazz.getConstructors())
		{
			final Class<?>[] parameterTypes = constructor.getParameterTypes();
			
			if (parameterTypes.length != 1 || !L2Object.class.isAssignableFrom(parameterTypes[0]))
				throw new IllegalStateException("Invalid constructor " + constructor + " for " + owner);
			
			constructors.put((Class<? extends L2Object>)parameterTypes[0], (Constructor<? extends T>)constructor);
		}
		
		return constructors;
	}
	
	private final Constructor<? extends T> findComponentContructor(L2Object owner)
	{
		final Map<Class<? extends L2Object>, Constructor<? extends T>> constructors = findComponentContructors(owner);
		
		for (Class<?> ownerClazz = owner.getClass(); ownerClazz != null; ownerClazz = ownerClazz.getSuperclass())
		{
			final Constructor<? extends T> constructor = constructors.get(ownerClazz);
			
			if (constructor != null)
				return constructor;
		}
		
		throw new IllegalStateException("No proper " + _rootClazz + " contructor registered for " + owner);
	}
	
	public final T getComponent(L2Object owner)
	{
		Map<Integer, Constructor<? extends T>> cacheByOwnerClass = _cache.get(owner.getClass());
		
		if (cacheByOwnerClass == null)
			_cache.put(owner.getClass(), cacheByOwnerClass = new HashMap<Integer, Constructor<? extends T>>());
		
		Constructor<? extends T> constructor = cacheByOwnerClass.get(owner.getTemplate().getId());
		
		if (constructor == null)
			cacheByOwnerClass.put(owner.getTemplate().getId(), constructor = findComponentContructor(owner));
		
		try
		{
			return constructor.newInstance(owner);
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/* only temporarily for testing */
	public static void main(String[] args)
	{
		long begin1 = System.currentTimeMillis();
		for (int i = 0; i < 10000000; i++)
		{
			new L2Object(123123, new L2Template(i % 20000)) {
				@Override
				public void setName(String name)
				{
					//
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
