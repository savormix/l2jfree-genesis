package com.l2jfree.sql;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public interface DataSourceInitializer
{
	public ComboPooledDataSource initDataSource() throws Exception;
}
