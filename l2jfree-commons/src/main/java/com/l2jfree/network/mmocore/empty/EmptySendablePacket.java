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
package com.l2jfree.network.mmocore.empty;

import com.l2jfree.network.mmocore.MMOBuffer;
import com.l2jfree.network.mmocore.MMOConnection;
import com.l2jfree.network.mmocore.ReceivablePacket;
import com.l2jfree.network.mmocore.SendablePacket;

/**
 * @author NB4L1
 * @param <T>
 * @param <RP>
 * @param <SP>
 */
public class EmptySendablePacket<T extends MMOConnection<T, RP, SP>, RP extends ReceivablePacket<T, RP, SP>, SP extends SendablePacket<T, RP, SP>>
		extends SendablePacket<T, RP, SP>
{
	@Override
	protected void write(T client, MMOBuffer buf) throws RuntimeException
	{
		// do nothing by default
	}
}
