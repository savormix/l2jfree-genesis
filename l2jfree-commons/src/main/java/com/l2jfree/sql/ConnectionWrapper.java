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

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

/**
 * @author NB4L1
 */
public abstract class ConnectionWrapper implements Connection
{
	private final Connection _connection;
	
	public ConnectionWrapper(Connection connection)
	{
		_connection = connection;
	}
	
	@Override
	public void clearWarnings() throws SQLException
	{
		_connection.clearWarnings();
	}
	
	@Override
	public void close() throws SQLException
	{
		_connection.close();
	}
	
	@Override
	public void commit() throws SQLException
	{
		_connection.commit();
	}
	
	@Override
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException
	{
		return _connection.createArrayOf(typeName, elements);
	}
	
	@Override
	public Blob createBlob() throws SQLException
	{
		return _connection.createBlob();
	}
	
	@Override
	public Clob createClob() throws SQLException
	{
		return _connection.createClob();
	}
	
	@Override
	public NClob createNClob() throws SQLException
	{
		return _connection.createNClob();
	}
	
	@Override
	public SQLXML createSQLXML() throws SQLException
	{
		return _connection.createSQLXML();
	}
	
	@Override
	public Statement createStatement() throws SQLException
	{
		return _connection.createStatement();
	}
	
	@Override
	public Statement createStatement(int a, int b) throws SQLException
	{
		return _connection.createStatement(a, b);
	}
	
	@Override
	public Statement createStatement(int a, int b, int c) throws SQLException
	{
		return _connection.createStatement(a, b, c);
	}
	
	@Override
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException
	{
		return _connection.createStruct(typeName, attributes);
	}
	
	@Override
	public boolean getAutoCommit() throws SQLException
	{
		return _connection.getAutoCommit();
	}
	
	@Override
	public String getCatalog() throws SQLException
	{
		return _connection.getCatalog();
	}
	
	@Override
	public Properties getClientInfo() throws SQLException
	{
		return _connection.getClientInfo();
	}
	
	@Override
	public String getClientInfo(String name) throws SQLException
	{
		return _connection.getClientInfo(name);
	}
	
	@Override
	public int getHoldability() throws SQLException
	{
		return _connection.getHoldability();
	}
	
	@Override
	public DatabaseMetaData getMetaData() throws SQLException
	{
		return _connection.getMetaData();
	}
	
	@Override
	public int getTransactionIsolation() throws SQLException
	{
		return _connection.getTransactionIsolation();
	}
	
	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException
	{
		return _connection.getTypeMap();
	}
	
	@Override
	public SQLWarning getWarnings() throws SQLException
	{
		return _connection.getWarnings();
	}
	
	@Override
	public boolean isClosed() throws SQLException
	{
		return _connection.isClosed();
	}
	
	@Override
	public boolean isReadOnly() throws SQLException
	{
		return _connection.isReadOnly();
	}
	
	@Override
	public boolean isValid(int timeout) throws SQLException
	{
		return _connection.isValid(timeout);
	}
	
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException
	{
		return _connection.isWrapperFor(iface);
	}
	
	@Override
	public String nativeSQL(String a) throws SQLException
	{
		return _connection.nativeSQL(a);
	}
	
	@Override
	public CallableStatement prepareCall(String a) throws SQLException
	{
		return _connection.prepareCall(a);
	}
	
	@Override
	public CallableStatement prepareCall(String a, int b, int c) throws SQLException
	{
		return _connection.prepareCall(a, b, c);
	}
	
	@Override
	public CallableStatement prepareCall(String a, int b, int c, int d) throws SQLException
	{
		return _connection.prepareCall(a, b, c, d);
	}
	
	@Override
	public PreparedStatement prepareStatement(String a) throws SQLException
	{
		return _connection.prepareStatement(a);
	}
	
	@Override
	public PreparedStatement prepareStatement(String a, int b) throws SQLException
	{
		return _connection.prepareStatement(a, b);
	}
	
	@Override
	public PreparedStatement prepareStatement(String a, int b, int c) throws SQLException
	{
		return _connection.prepareStatement(a, b, c);
	}
	
	@Override
	public PreparedStatement prepareStatement(String a, int b, int c, int d) throws SQLException
	{
		return _connection.prepareStatement(a, b, c, d);
	}
	
	@Override
	public PreparedStatement prepareStatement(String a, int[] b) throws SQLException
	{
		return _connection.prepareStatement(a, b);
	}
	
	@Override
	public PreparedStatement prepareStatement(String a, String[] b) throws SQLException
	{
		return _connection.prepareStatement(a, b);
	}
	
	@Override
	public void releaseSavepoint(Savepoint a) throws SQLException
	{
		_connection.releaseSavepoint(a);
	}
	
	@Override
	public void rollback() throws SQLException
	{
		_connection.rollback();
	}
	
	@Override
	public void rollback(Savepoint a) throws SQLException
	{
		_connection.rollback(a);
	}
	
	@Override
	public void setAutoCommit(boolean a) throws SQLException
	{
		_connection.setAutoCommit(a);
	}
	
	@Override
	public void setCatalog(String a) throws SQLException
	{
		_connection.setCatalog(a);
	}
	
	@Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException
	{
		_connection.setClientInfo(properties);
	}
	
	@Override
	public void setClientInfo(String name, String value) throws SQLClientInfoException
	{
		_connection.setClientInfo(name, value);
	}
	
	@Override
	public void setHoldability(int a) throws SQLException
	{
		_connection.setHoldability(a);
	}
	
	@Override
	public void setReadOnly(boolean a) throws SQLException
	{
		_connection.setReadOnly(a);
	}
	
	@Override
	public Savepoint setSavepoint() throws SQLException
	{
		return _connection.setSavepoint();
	}
	
	@Override
	public Savepoint setSavepoint(String a) throws SQLException
	{
		return _connection.setSavepoint(a);
	}
	
	@Override
	public void setTransactionIsolation(int a) throws SQLException
	{
		_connection.setTransactionIsolation(a);
	}
	
	@Override
	public void setTypeMap(Map<String, Class<?>> a) throws SQLException
	{
		_connection.setTypeMap(a);
	}
	
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException
	{
		return _connection.unwrap(iface);
	}
}
