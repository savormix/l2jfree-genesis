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
package com.l2jfree.loginserver.network.client;

import com.l2jfree.loginserver.network.client.packets.sendable.LoginFailure.Reason;

/**
 * @author savormix
 *
 */
public enum L2NoServiceReason implements Reason
{
	/** There is a system error. Please log in again later. */
	THERE_IS_A_SYSTEM_ERROR(1),
	/** The password you have entered is incorrect. Confirm your account information and log in again later. */
	PASSWORD_INCORRECT(2), // also 3
	/** Access failed. Please try again later. . */
	ACCESS_FAILED_TRY_AGAIN(4), // also 6,8,9,10,11,13,14
	/** Account is already in use. Unable to log in. */
	ALREADY_IN_USE(7),
	/** Due to high server traffic, your login attempt has failed. Please try again soon. */
	TOO_HIGH_TRAFFIC(15),
	/** Currently undergoing game server maintenance. Please log in again later. */
	MAINTENANCE_UNDERGOING(16),
	/** System error. */
	SYSTEM_ERROR(20),
	/** Game connection attempted through a restricted IP. */
	IP_RESTRICTED(22),
	/** <I>Shows an empty string.</I> */
	IGNORE(23),
	/** <I>Shows a dialog to input a number.</I> */
	// when using this, you mustn't terminate the connection!
	INVALID_SECURITY_CARD_NO(31),
	/** Users who have not verified their age may not log in between the hours of 10:00 p.m. and 6:00 a.m. */
	TIME_LIMITATION_AGE_NOT_VERIFIED(32),
	/** This server cannot be accessed by the coupon you are using. */
	INCORRECT_COUPON_FOR_SERVER(33),
	/** You are using a computer that does not allow you to log in with two accounts at the same time. */
	USING_A_COMPUTER_NO_DUAL_BOX(35),
	/**
	 * You must accept the User Agreement before this account can access Lineage II.\n
	 * Please try again after accepting the agreement on the PlayNC website (http://www.plaync.co.kr).
	 */
	MUST_ACCEPT_AGREEMENT(37),
	/** You are currently logged into 10 of your accounts and can no longer access your other accounts. */
	ACCOUNT_LIMITATION(42),
	;
	
	private final int _id;
	
	private L2NoServiceReason(int id)
	{
		_id = id;
	}
	
	@Override
	public int getId()
	{
		return _id;
	}
}
