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
package com.l2jfree.config.model;

import java.io.PrintWriter;
import java.lang.reflect.Field;

import org.apache.commons.lang.ArrayUtils;

import com.l2jfree.config.L2Properties;
import com.l2jfree.config.annotation.ConfigField;
import com.l2jfree.config.annotation.ConfigGroupBeginning;
import com.l2jfree.config.annotation.ConfigGroupEnding;
import com.l2jfree.config.converters.Converter;
import com.l2jfree.config.model.ConfigClassInfo.PrintMode;
import com.l2jfree.util.logging.L2Logger;

public final class ConfigFieldInfo
{
	private static final L2Logger _log = L2Logger.getLogger(ConfigFieldInfo.class);
	
	private final Field _field;
	private final ConfigField _configField;
	private final Converter _converter;
	private final ConfigGroupBeginning _configGroupBeginning;
	private final ConfigGroupEnding _configGroupEnding;
	
	private final String _defaultValue;
	
	private ConfigGroup _beginningGroup;
	private ConfigGroup _endingGroup;
	
	private volatile boolean _fieldValueLoaded = false;
	
	public ConfigFieldInfo(Field field) throws InstantiationException, IllegalAccessException
	{
		_field = field;
		_configField = field.getAnnotation(ConfigField.class);
		_converter = getConfigField().converter().newInstance();
		_configGroupBeginning = field.getAnnotation(ConfigGroupBeginning.class);
		_configGroupEnding = field.getAnnotation(ConfigGroupEnding.class);
		
		// to standardize the default values (true, True -> true, etc..)
		final String value = getConfigField().value();
		final Object obj = getConverter().convertFromString(getField().getType(), value);
		
		_defaultValue = getConverter().convertToString(getField().getType(), obj);
	}
	
	public Field getField()
	{
		return _field;
	}
	
	public String getCurrentValue()
	{
		Object obj = null;
		
		try
		{
			obj = getField().get(null);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		
		return getConverter().convertToString(getField().getType(), obj);
	}
	
	public void setCurrentValue(String value)
	{
		Object obj = getConverter().convertFromString(getField().getType(), value);
		
		if (_fieldValueLoaded && getConfigField().eternal())
			_log.warn("Eternal config field (" + getField() + ") (" + getConfigField() + ") assigned multiple times!");
		
		try
		{
			getField().set(null, obj);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		
		_fieldValueLoaded = true;
	}
	
	public void setCurrentValue(L2Properties properties)
	{
		final String newValue = properties.getProperty(getName(), getDefaultValue());
		
		setCurrentValue(newValue);
	}
	
	public ConfigField getConfigField()
	{
		return _configField;
	}
	
	public String getName()
	{
		return getConfigField().name();
	}
	
	public String getDefaultValue()
	{
		return _defaultValue;
	}
	
	public boolean isModified()
	{
		final String currentValue = getCurrentValue();
		
		// config value wasn't initialized
		if (currentValue == null)
			return false;
		
		return !getDefaultValue().equals(currentValue);
	}
	
	public Converter getConverter()
	{
		return _converter;
	}
	
	public ConfigGroupBeginning getConfigGroupBeginning()
	{
		return _configGroupBeginning;
	}
	
	public ConfigGroupEnding getConfigGroupEnding()
	{
		return _configGroupEnding;
	}
	
	public ConfigGroup getBeginningGroup()
	{
		return _beginningGroup;
	}
	
	public void setBeginningGroup(ConfigGroup beginningGroup)
	{
		_beginningGroup = beginningGroup;
	}
	
	public ConfigGroup getEndingGroup()
	{
		return _endingGroup;
	}
	
	public void setEndingGroup(ConfigGroup endingGroup)
	{
		_endingGroup = endingGroup;
	}
	
	public void print(PrintWriter out, PrintMode mode)
	{
		if (getBeginningGroup() != null && (mode != PrintMode.MODIFIED || getBeginningGroup().isModified()))
		{
			out.println("########################################");
			out.println("## " + getConfigGroupBeginning().name());
			
			if (!ArrayUtils.isEmpty(getConfigGroupBeginning().comment()))
				for (String line : getConfigGroupBeginning().comment())
					out.println("# " + line);
			
			out.println();
		}
		
		if (mode != PrintMode.MODIFIED || isModified())
		{
			if (!ArrayUtils.isEmpty(getConfigField().comment()))
				for (String line : getConfigField().comment())
					out.println("# " + line);
			
			out.println("# ");
			out.println("# Default: " + getDefaultValue());
			out.println("# ");
			out.println(getName() + " = " + (mode == PrintMode.DEFAULT ? getDefaultValue() : getCurrentValue()));
			out.println();
		}
		
		if (getEndingGroup() != null && (mode != PrintMode.MODIFIED || getEndingGroup().isModified()))
		{
			if (!ArrayUtils.isEmpty(getConfigGroupEnding().comment()))
				for (String line : getConfigGroupEnding().comment())
					out.println("# " + line);
			
			out.println("## " + getConfigGroupEnding().name());
			out.println("########################################");
			
			out.println();
		}
	}
}
