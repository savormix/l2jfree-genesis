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
package com.l2jfree.sql;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.l2jfree.lang.L2Entity;
import com.l2jfree.lang.L2System;
import com.l2jfree.util.Introspection;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author NB4L1
 */
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class L2DBEntity implements L2Entity<Object>
{
	protected static final L2Logger _log = L2Logger.getLogger(L2DBEntity.class);
	
	// TODO remove once it's not required anymore
	private static final boolean DEBUG = true;
	
	@Override
	@Transient
	public abstract Object getPrimaryKey();
	
	@Transient
	protected abstract Class<?> getClassForEqualsCheck();
	
	@Override
	public int hashCode()
	{
		final Object pk = getPrimaryKey();
		
		if (pk == null)
			return 0;
		
		return L2System.hash(pk.hashCode());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (!(obj instanceof L2DBEntity))
			return false;
		
		final L2DBEntity other = (L2DBEntity)obj;
		
		if (getClassForEqualsCheck() != other.getClassForEqualsCheck())
			return false;
		
		final Object pk1 = getPrimaryKey();
		final Object pk2 = other.getPrimaryKey();
		
		return pk1 == null ? pk2 == null : pk1.equals(pk2);
	}
	
	@Override
	public String toString()
	{
		return Introspection.toString(this);
	}
	
	public String toString(int maxWidth)
	{
		return StringUtils.abbreviate(toString(), maxWidth);
	}
	
	@PostLoad
	public void postLoad()
	{
		if (DEBUG)
			_log.info("L2DBEntity.postLoad(): " + toString(80));
	}
	
	@PostPersist
	public void postPersist()
	{
		if (DEBUG)
			_log.info("L2DBEntity.postPersist(): " + toString(80));
	}
	
	@PostUpdate
	public void postUpdate()
	{
		if (DEBUG)
			_log.info("L2DBEntity.postUpdate(): " + toString(80));
	}
}
