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

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.l2jfree.gameserver.gameobjects.L2Object;

/**
 * @author NB4L1
 * @param <K>
 * @param <V>
 */
// TODO implement more features, use annotations, optimize performance
@SuppressWarnings("unchecked")
public final class ComponentFactory<K extends L2Object, V extends IComponent>
{
	private final Map<Class<? extends K>, Class<? extends V>> _registryByOwnerClazz =
			new HashMap<Class<? extends K>, Class<? extends V>>();
	
	private final Map<Integer, Class<? extends V>> _registryByTemplateId = new HashMap<Integer, Class<? extends V>>();
	
	private final Map<Class<? extends K>, Map<Integer, Constructor<? extends V>>> _cache =
			new HashMap<Class<? extends K>, Map<Integer, Constructor<? extends V>>>();
	
	private final Class<K> _ownerRootClazz;
	private final Class<? extends Annotation> _annotationClazz;
	private final String _componentName;
	
	protected ComponentFactory(Class<K> ownerRootClazz, Class<? extends Annotation> annotationClazz)
	{
		_ownerRootClazz = ownerRootClazz;
		_annotationClazz = annotationClazz;
		
		try
		{
			_componentName = Class.forName(new Throwable().getStackTrace()[1].getClassName()).getSimpleName();
		}
		catch (ClassNotFoundException e)
		{
			// should never happen
			throw new Error(e);
		}
	}
	
	public final void register(Class<? extends K> ownerClazz, Class<? extends V> componentClazz)
	{
		// FIXME handle replacement
		_registryByOwnerClazz.put(ownerClazz, componentClazz);
		
		// drop cache
		_cache.clear();
	}
	
	public final void register(int templateId, Class<? extends V> componentClazz)
	{
		// FIXME handle replacement
		_registryByTemplateId.put(templateId, componentClazz);
		
		// drop cache
		_cache.clear();
	}
	
	private final Class<? extends V> findComponentClass(final Class<? extends K> ownerClazz,
			final Integer ownerTemplateId)
	{
		// first search for template id based mapping
		Class<? extends V> clazz = _registryByTemplateId.get(ownerTemplateId);
		
		if (clazz != null)
			return clazz;
		
		// then check for class based mappings
		for (Class<?> tmpClazz = ownerClazz; tmpClazz != null; tmpClazz = tmpClazz.getSuperclass())
		{
			clazz = _registryByOwnerClazz.get(tmpClazz);
			
			if (clazz != null)
				return clazz;
		}
		
		// and finally fall-back to default annotations based mappings
		try
		{
			final Method method = _annotationClazz.getMethod("value");
			final Annotation annotation = ownerClazz.getAnnotation(_annotationClazz);
			final Object result = method.invoke(annotation);
			
			clazz = (Class<? extends V>)result;
			
			if (clazz != null)
				return clazz;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		throw new ComponentException(_componentName + " implementation", ownerClazz, ownerTemplateId);
	}
	
	private final Map<Class<? extends K>, Constructor<? extends V>> findComponentContructors(
			final Class<? extends K> ownerClazz, final Integer ownerTemplateId)
	{
		final Class<? extends V> clazz = findComponentClass(ownerClazz, ownerTemplateId);
		
		final Map<Class<? extends K>, Constructor<? extends V>> constructors =
				new HashMap<Class<? extends K>, Constructor<? extends V>>();
		
		for (Constructor<?> constructor : clazz.getConstructors())
		{
			final Class<?>[] parameterTypes = constructor.getParameterTypes();
			
			if (parameterTypes.length != 1 || !_ownerRootClazz.isAssignableFrom(parameterTypes[0]))
				throw new IllegalStateException("Invalid constructor " + constructor + " for class: " + ownerClazz
						+ ", template id: " + ownerTemplateId);
			
			constructors.put((Class<? extends K>)parameterTypes[0], (Constructor<? extends V>)constructor);
		}
		
		return constructors;
	}
	
	private final Constructor<? extends V> findComponentContructor(final Class<? extends K> ownerClazz,
			final Integer ownerTemplateId)
	{
		final Map<Class<? extends K>, Constructor<? extends V>> constructors =
				findComponentContructors(ownerClazz, ownerTemplateId);
		
		for (Class<?> tmpClazz = ownerClazz; tmpClazz != null; tmpClazz = tmpClazz.getSuperclass())
		{
			final Constructor<? extends V> constructor = constructors.get(tmpClazz);
			
			if (constructor != null)
				return constructor;
		}
		
		throw new ComponentException("Proper " + _componentName + " constructor", ownerClazz, ownerTemplateId);
	}
	
	public final V getComponent(K owner)
	{
		final Class<? extends K> ownerClazz = (Class<? extends K>)owner.getClass();
		final Integer ownerTemplateId = owner.getTemplate().getId();
		
		Map<Integer, Constructor<? extends V>> cacheByOwnerClazz = _cache.get(ownerClazz);
		
		if (cacheByOwnerClazz == null)
			_cache.put(ownerClazz, cacheByOwnerClazz = new HashMap<Integer, Constructor<? extends V>>());
		
		Constructor<? extends V> constructor = cacheByOwnerClazz.get(ownerTemplateId);
		
		if (constructor == null)
			cacheByOwnerClazz.put(ownerTemplateId, constructor = findComponentContructor(ownerClazz, ownerTemplateId));
		
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
	
	public static final class ComponentException extends IllegalStateException
	{
		private static final long serialVersionUID = 107476204199857568L;
		
		public ComponentException(String missingEntity, Class<?> ownerClazz, final Integer ownerTemplateId)
		{
			super("No " + missingEntity + " registered for class: " + ownerClazz + ", template id: " + ownerTemplateId);
		}
	}
}
