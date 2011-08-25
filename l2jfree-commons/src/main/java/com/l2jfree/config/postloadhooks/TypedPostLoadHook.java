package com.l2jfree.config.postloadhooks;

/**
 * @author NB4L1
 * @param <T>
 */
public abstract class TypedPostLoadHook<T> implements PostLoadHook
{
	@Override
	public final void valueLoaded(Object obj)
	{
		if (!getRequiredType().isInstance(obj))
			throw new ClassCastException(getRequiredType() + " value required, but found: '" + obj + "'!");
		
		valueLoadedImpl(getRequiredType().cast(obj));
	}
	
	protected abstract void valueLoadedImpl(T obj);
	
	protected abstract Class<T> getRequiredType();
}
