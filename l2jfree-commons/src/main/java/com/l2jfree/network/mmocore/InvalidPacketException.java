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
 * Exception thrown by {@link ReceivablePacket#runImpl()} to trigger
 * {@link MMOController#report(FloodManager.ErrorMode, MMOConnection, ReceivablePacket, Throwable)}.
 * 
 * @author NB4L1
 */
public class InvalidPacketException extends Exception
{
	private static final long serialVersionUID = -8023992556276431695L;
	
	public InvalidPacketException()
	{
		super();
	}
	
	public InvalidPacketException(String message)
	{
		super(message);
	}
	
	public InvalidPacketException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public InvalidPacketException(Throwable cause)
	{
		super(cause);
	}
}
