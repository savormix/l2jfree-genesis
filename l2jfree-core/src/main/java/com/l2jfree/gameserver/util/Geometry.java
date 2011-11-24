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
package com.l2jfree.gameserver.util;

import com.l2jfree.gameserver.gameobjects.L2Object;
import com.l2jfree.gameserver.gameobjects.ObjectPosition;
import com.l2jfree.lang.L2Math.ILocation2D;

/**
 * @author NB4L1
 */
public final class Geometry
{
	public static int calculateHeadingFrom(L2Object obj1, L2Object obj2)
	{
		final ObjectPosition pos1 = obj1.getPosition();
		final ObjectPosition pos2 = obj2.getPosition();
		
		return calculateHeadingFrom(pos1, pos2);
	}
	
	public static int calculateHeadingFrom(ILocation2D loc1, ILocation2D loc2)
	{
		return calculateHeadingFrom(loc1.getX(), loc1.getY(), loc2.getX(), loc2.getY());
	}
	
	public static int calculateHeadingFrom(double obj1X, double obj1Y, double obj2X, double obj2Y)
	{
		double angleTarget = Math.toDegrees(Math.atan2(obj2Y - obj1Y, obj2X - obj1X));
		
		return (int)getValidHeading(angleTarget * 182.044444444);
	}
	
	/**
	 * @param heading (n * 65536 + k)
	 * @return k [0, 65536[
	 */
	public static double getValidHeading(double heading)
	{
		while (heading < 0.)
			heading += 65536.;
		
		while (heading >= 65536.)
			heading -= 65536.;
		
		return heading;
	}
	
	/**
	 * @param degree (n * 360 + k)
	 * @return k [0, 360[
	 */
	public static double getValidDegree(double degree)
	{
		while (degree < 0.)
			degree += 360.;
		
		while (degree >= 360.)
			degree -= 360.;
		
		return degree;
	}
}
