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
import com.l2jfree.util.EnumValues;

/**
 * @author savormix
 */
public enum L2BanReason implements Reason
{
	/**
	 * Your account information is incorrect. For more details, please contact our customer service
	 * center at http://support.plaync.com.
	 */
	ACCOUNT_INFO_INCORRECT(5),
	/**
	 * Lineage II game services may be used by individuals 15 years of age or older except for PvP
	 * servers, which may only be used by adults 18 years of age and older. (Korea Only)
	 */
	AGE_LIMITATION(12),
	/** Please login after changing your temporary password. */
	CHANGE_TEMP_PASSWORD(17),
	/**
	 * Your game time has expired. To continue playing, please purchase Lineage II either directly
	 * from the PlayNC Store or from any leading games retailer.
	 */
	GAME_TIME_EXPIRED(18),
	/** There is no time left on this account. */
	NO_TIME_LEFT(19),
	/** Access failed. */
	ACCESS_FAILED(21),
	/** <I>Shows an empty string.</I> */
	IGNORE(23),
	/** This week's usage time has finished. */
	WEEK_TIME_FINISHED(30),
	/**
	 * Your account is currently suspended because you have not logged into the game for some time.
	 * You may reactivate your account by visiting the PlayNC website
	 * (http://www.plaync.com/us/support/).
	 */
	SUSPENDED_INACTIVITY(36),
	/**
	 * A guardian's consent is required before this account can be used to play Lineage II.\n Please
	 * try again after this consent is provided.
	 */
	GUARDIANS_CONSENT_NEEDED(38),
	/**
	 * This account has declined the User Agreement or is pending a withdrawl request. \n Please try
	 * again after cancelling this request.
	 */
	PENDING_WITHDRAWL_REQUEST(39),
	/**
	 * This account has been suspended. \nFor more information, please call the Customer's Center
	 * (Tel. 1600-0020).
	 */
	SUSPENDED_PHONE_CC(40),
	/**
	 * Your account can only be used after changing your password and quiz. \n Services will be
	 * available after changing your password and quiz from the PlayNC website
	 * (http://www.plaync.co.kr).
	 */
	CHANGE_PASSWORD_AND_QUIZ(41),
	/** The master account of your account has been restricted. */
	MASTER_ACCOUNT_RESTRICTED(43);
	
	private final int _id;
	
	private L2BanReason(int id)
	{
		_id = id;
	}
	
	@Override
	public int getId()
	{
		return _id;
	}
	
	/**
	 * Returns a reason with the given ID. <BR>
	 * <BR>
	 * If no matches are found, {@link #SUSPENDED_PHONE_CC} is returned instead.
	 * 
	 * @param id Reason's ID
	 * @return a reason
	 */
	public static L2BanReason getById(int id)
	{
		for (L2BanReason lbr : L2BanReason.VALUES)
			if (lbr.getId() == id)
				return lbr;
		return SUSPENDED_PHONE_CC;
	}
	
	public static final EnumValues<L2BanReason> VALUES = new EnumValues<L2BanReason>(L2BanReason.class);
}
