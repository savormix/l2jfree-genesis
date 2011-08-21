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
 * This class provides a simple yet verbose way to strip certain elements that may be attached to
 * the packet's body before sending (such as checksums).
 * 
 * @author savormix
 */
public class DataSizeHolder
{
	private int _size;
	private int _minPadding;
	private int _maxPadding;
	
	DataSizeHolder(int size)
	{
		_size = size;
		_minPadding = 0;
		_maxPadding = 0;
	}
	
	/**
	 * Returns data size in bytes.
	 * 
	 * @return size of data
	 */
	public int getSize()
	{
		return _size;
	}
	
	/**
	 * Decreases the effective data size. <BR>
	 * <BR>
	 * This method should be used to remove certain elements, such as checksums, that were
	 * deliberately attached AND meaningful AND do not belong to the real packet's body.<BR>
	 * To remove padded bytes (that serve no purpose other that sizing), use
	 * {@link #setPadding(int, int)}.
	 * 
	 * @param diff difference in bytes
	 */
	public void decreaseSize(int diff)
	{
		if (diff > 0)
			_size -= Math.min(getSize(), diff);
	}
	
	/**
	 * Returns the number of padded bytes that are guaranteed to trail a valid packet's body.
	 * 
	 * @return minimum number of padded bytes
	 */
	public int getMinPadding()
	{
		return _minPadding;
	}
	
	/**
	 * Returns the maximum number of padded bytes that may trail a valid packet's body.
	 * 
	 * @return maximum number of padded bytes
	 */
	public int getMaxPadding()
	{
		return _maxPadding;
	}
	
	/**
	 * Specifies the received packet's padding parameters. <BR>
	 * <BR>
	 * This method should be used to notify the underlying networking layer that this packet uses a
	 * padding scheme to achieve specific <U>sizes</U>. The padded bytes serve no other purpose
	 * (are meaningless). <BR>
	 * <BR>
	 * Based on the specified values, the underlying networking layer will decide whether this
	 * packet is a valid packet and whether/how the padded bytes should be removed.<BR>
	 * <B>There is no guarantee that any padded bytes will be removed</B>. <BR>
	 * <BR>
	 * To remove meaningful elements that have been attached to the packet's body on any other
	 * purpose, use {@link #decreaseSize(int)}.<BR>
	 * 
	 * @param minBytes minimum number of padded bytes
	 * @param maxBytes maximum number of padded bytes
	 */
	public void setPadding(int minBytes, int maxBytes)
	{
		maxBytes = Math.max(minBytes, maxBytes);
		setMinPadding(minBytes);
		setMaxPadding(maxBytes);
	}
	
	private void setMinPadding(int minPadding)
	{
		if (minPadding > 0)
			_minPadding = minPadding;
	}
	
	private void setMaxPadding(int maxPadding)
	{
		if (maxPadding > 0)
			_maxPadding = maxPadding;
	}
}
