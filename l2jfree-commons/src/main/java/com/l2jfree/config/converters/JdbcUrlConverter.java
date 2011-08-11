package com.l2jfree.config.converters;

/**
 * Defines JDBC URL conversions.
 * 
 * @author NB4L1
 */
public final class JdbcUrlConverter extends TypedConverter<String>
{
	@Override
	protected String convertFromString(String value)
	{
		return "jdbc:" + (value.replace("jdbc:", ""));
	}
	
	@Override
	protected String convertToString(String obj)
	{
		return obj.replace("jdbc:", "");
	}
	
	@Override
	protected Class<String> getRequiredType()
	{
		return String.class;
	}
}
