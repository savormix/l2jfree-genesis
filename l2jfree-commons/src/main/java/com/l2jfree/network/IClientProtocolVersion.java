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

import com.l2jfree.security.ObfuscationService;

/**
 * An interface that allows custom-made protocol version support.
 * 
 * @author savormix
 */
public interface IClientProtocolVersion extends IProtocolVersion
{
	/**
	 * The size of the secondary opcode table. This is equal to [highest opcode in use + 1].
	 * 
	 * @return 2nd opcode table size
	 */
	int getOp2TableSize();
	
	/**
	 * All primary opcodes that should not be [de]obfuscated.
	 * 
	 * @return primary opcodes exempt from {@link ObfuscationService}.
	 */
	int[] getIgnoredOp1s();
	/**
	 * All secondary opcodes that should not be [de]obfuscated.
	 * 
	 * @return secondary opcodes exempt from {@link ObfuscationService}.
	 */
	int[] getIgnoredOp2s();
}
