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
package com.l2jfree.gameserver.gameobjects.components;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.l2jfree.gameserver.gameobjects.CharacterStat;
import com.l2jfree.gameserver.gameobjects.CharacterView;
import com.l2jfree.gameserver.gameobjects.IObjectKnownList;
import com.l2jfree.gameserver.gameobjects.L2Object;
import com.l2jfree.gameserver.gameobjects.ObjectPosition;

/**
 * @author NB4L1
 * @param <T>
 */
// TODO implement more features, use annotations, optimize performance
@SuppressWarnings("unchecked")
public abstract class ComponentFactory<T>
{
	public static final ComponentFactory<ObjectPosition> POSITION = new ComponentFactory<ObjectPosition>() {
		@Override
		protected java.lang.Class<? extends ObjectPosition> getRootClass()
		{
			return ObjectPosition.class;
		}
		
		@Override
		protected Class<? extends ObjectPosition> getComponentClassByAnnotation(L2Object owner)
		{
			return owner.getClass().getAnnotation(PositionComponent.class).value();
		}
	};
	
	public static final ComponentFactory<IObjectKnownList> KNOWNLIST = new ComponentFactory<IObjectKnownList>() {
		@Override
		protected java.lang.Class<? extends IObjectKnownList> getRootClass()
		{
			return IObjectKnownList.class;
		}
		
		@Override
		protected Class<? extends IObjectKnownList> getComponentClassByAnnotation(L2Object owner)
		{
			return owner.getClass().getAnnotation(KnownListComponent.class).value();
		}
	};
	
	public static final ComponentFactory<CharacterStat> STAT = new ComponentFactory<CharacterStat>() {
		@Override
		protected java.lang.Class<? extends CharacterStat> getRootClass()
		{
			return CharacterStat.class;
		}
		
		@Override
		protected Class<? extends CharacterStat> getComponentClassByAnnotation(L2Object owner)
		{
			return owner.getClass().getAnnotation(StatComponent.class).value();
		}
	};
	
	public static final ComponentFactory<CharacterView> VIEW = new ComponentFactory<CharacterView>() {
		@Override
		protected java.lang.Class<? extends CharacterView> getRootClass()
		{
			return CharacterView.class;
		}
		
		@Override
		protected Class<? extends CharacterView> getComponentClassByAnnotation(L2Object owner)
		{
			return owner.getClass().getAnnotation(ViewComponent.class).value();
		}
	};
	
	private final Map<Class<? extends L2Object>, Class<? extends T>> _registryByOwnerClass =
			new HashMap<Class<? extends L2Object>, Class<? extends T>>();
	
	private final Map<Integer, Class<? extends T>> _registryByTemplateId = new HashMap<Integer, Class<? extends T>>();
	
	private final Map<Class<? extends L2Object>, Map<Integer, Constructor<? extends T>>> _cache =
			new HashMap<Class<? extends L2Object>, Map<Integer, Constructor<? extends T>>>();
	
	private ComponentFactory()
	{
		//
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
	
	protected abstract Class<? extends T> getRootClass();
	
	protected abstract Class<? extends T> getComponentClassByAnnotation(L2Object owner);
	
	private final Class<? extends T> findComponentClass(L2Object owner)
	{
		// first search for template id based mapping
		Class<? extends T> clazz = _registryByTemplateId.get(owner.getTemplate().getId());
		
		if (clazz != null)
			return clazz;
		
		// then check for class based mappings
		for (Class<?> ownerClazz = owner.getClass(); ownerClazz != null; ownerClazz = ownerClazz.getSuperclass())
		{
			clazz = _registryByOwnerClass.get(ownerClazz);
			
			if (clazz != null)
				return clazz;
		}
		
		// and finally fall-back to default annotations based mappings
		clazz = getComponentClassByAnnotation(owner);
		
		if (clazz != null)
			return clazz;
		
		throw new IllegalStateException("No " + getRootClass() + " implementation registered for " + owner);
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
		
		throw new IllegalStateException("No proper " + getRootClass() + " contructor registered for " + owner);
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
}
