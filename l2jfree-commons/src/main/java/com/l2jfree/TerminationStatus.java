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
package com.l2jfree;

/**
 * This class contains status codes to be passed to
 * {@link System#exit(int)} and {@link Runtime#halt(int)}
 * methods.
 * @author savormix
 */
public abstract class TerminationStatus {
	/**
	 * <B>Cause</B>: A privileged user issued a shutdown.<BR>
	 * <B>Resolution</B>: Shut down orderly and do not report anything.
	 */
	public static final int MANUAL_SHUTDOWN = 0;
	/**
	 * <B>Cause</B>: A privileged user issued a restart.<BR>
	 * <B>Resolution</B>: Restart orderly and do not report anything.
	 */
	public static final int MANUAL_RESTART = 1;
	
	/**
	 * <B>Cause</B>: Server detected superuser privileges.<BR>
	 * <B>Resolution</B>: Inform the user that the server must be
	 * run on a normal user account and terminate.
	 */
	public static final int ENVIRONMENT_SUPERUSER = 2;
	/**
	 * <B>Cause</B>: Server detected a classpath conflict.<BR>
	 * <B>Resolution</B>: Inform the user that the reported
	 * classpath conflicts must be resolved and terminate.
	 */
	public static final int ENVIRONMENT_CP_CONFLICT = 3;
	/**
	 * <B>Cause</B>: Server needs a specific object or function, but it is not
	 * provided.<BR>
	 * <B>Resolution</B>: Inform the user about the needed feature and terminate.
	 * <BR><BR>
	 * Designed for missing encodings, ciphers, digests, etc.
	 */
	public static final int ENVIRONMENT_MISSING_COMPONENT_OR_SERVICE = 4;
	
	/**
	 * <B>Cause</B>: An [uncaught] error (other than {@link StackOverflowError})
	 * occured during runtime.<BR>
	 * <B>Resolution</B>: Possibly inform the user about the
	 * occured error and restart.
	 */
	public static final int RUNTIME_UNCAUGHT_ERROR = 4;
}
