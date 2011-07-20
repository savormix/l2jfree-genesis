package com.l2jfree.network.mmocore;

/**
 * @author savormix
 *
 */
public class DataSizeHolder
{
	private int _size;
	
	DataSizeHolder(int size)
	{
		_size = size;
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
}
