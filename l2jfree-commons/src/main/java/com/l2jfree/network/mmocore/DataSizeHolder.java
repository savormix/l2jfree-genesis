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
package com.l2jfree.network.mmocore;

/**
 * @author savormix
 *
 */
public class DataSizeHolder
{
	private int _size;
	private int _maxPadding;
	
	DataSizeHolder(int size)
	{
		_size = size;
		_maxPadding = 0;
	}
	
	/**
	 * Returns data size in bytes.
	 * @return size of data
	 */
	public int getSize()
	{
		return _size;
	}
	
	/**
	 * Decreases the effective data size.
	 * @param diff difference in bytes
	 */
	public void decreaseSize(int diff)
	{
		if (diff > 0)
			_size -= Math.min(getSize(), diff);
	}
	
	/**
	 * Returns the maximum number of bytes that may appear
	 * after a valid packet's body.
	 * @return maximum number of padded bytes
	 */
	public int getMaxPadding()
	{
		return _maxPadding;
	}
	
	/**
	 * Sets the maximum number of bytes that may appear
	 * after a valid packet's body.
	 * @param maxPadding maximum number of padded bytes
	 */
	public void setMaxPadding(int maxPadding)
	{
		if (maxPadding > 0)
			_maxPadding = maxPadding;
	}
}
