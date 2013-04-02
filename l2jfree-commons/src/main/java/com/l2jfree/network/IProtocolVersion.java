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
package com.l2jfree.network;

/**
 * An interface that allows custom-made protocol version support.
 * 
 * @author savormix
 */
public interface IProtocolVersion
{
	/**
	 * Returns the client-to-gameserver protocol version number, as reported in the {@code ProtocolVersion} packet.
	 * 
	 * @return version number
	 */
	int getVersion();
	
	/**
	 * Returns {@code true} if this object represents a protocol version that is older than
	 * the given {@code version}.
	 * 
	 * @param version a protocol version
	 * @return whether this version is older
	 */
	boolean isOlderThan(IProtocolVersion version);
	/**
	 * Returns {@code true} if this object represents a protocol version that is older than or
	 * the same as the given {@code version}.
	 * 
	 * @param version a protocol version
	 * @return whether this version is older or the same
	 */
	boolean isOlderThanOrEqualTo(IProtocolVersion version);
	/**
	 * Returns {@code true} if this object represents a protocol version that is newer than
	 * the given {@code version}.
	 * 
	 * @param version a protocol version
	 * @return whether this version is older
	 */
	boolean isNewerThan(IProtocolVersion version);
	/**
	 * Returns {@code true} if this object represents a protocol version that is newer than or
	 * the same as the given {@code version}.
	 * 
	 * @param version a protocol version
	 * @return whether this version is newer or the same
	 */
	boolean isNewerThanOrEqualTo(IProtocolVersion version);
	
	/**
	 * Returns the actual release date (or last update date) for this protocol version.
	 * 
	 * @return a timestamp
	 */
	long getReleaseDate();
}
