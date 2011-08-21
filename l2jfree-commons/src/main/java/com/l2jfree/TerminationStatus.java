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
 * This class contains status codes to be passed to {@link Shutdown#exit(TerminationStatus)} and
 * {@link Shutdown#halt(TerminationStatus)} methods.
 * 
 * @author savormix
 */
public enum TerminationStatus
{
	/**
	 * <B>Cause</B>: An unknown reason caused a shutdown.<BR>
	 * <B>Resolution</B>: Shut down for safety reasons and do not report anything.
	 */
	INVALID(-1, "terminating due to an unknown reason"),
	
	/**
	 * <B>Cause</B>: A privileged user issued a shutdown.<BR>
	 * <B>Resolution</B>: Shut down orderly and do not report anything.
	 */
	MANUAL_SHUTDOWN(0, "shutting down, because a privileged user issued a shutdown"),
	
	/**
	 * <B>Cause</B>: A privileged user issued a restart.<BR>
	 * <B>Resolution</B>: Restart orderly and do not report anything.
	 */
	MANUAL_RESTART(1, "restarting, because a privileged user issued a restart"),
	
	/**
	 * <B>Cause</B>: Server detected superuser privileges.<BR>
	 * <B>Resolution</B>: Inform the user that the server must be run on a normal user account and
	 * terminate.
	 */
	ENVIRONMENT_SUPERUSER(2, "shutting down, because the server has detected superuser privileges"),
	
	/**
	 * <B>Cause</B>: Server detected a classpath conflict.<BR>
	 * <B>Resolution</B>: Inform the user that the reported classpath conflicts must be resolved
	 * and terminate.
	 */
	ENVIRONMENT_CP_CONFLICT(3, "shutting down, because the server has detected a classpath conflict"),
	
	/**
	 * <B>Cause</B>: Server needs a specific object or function, but it is not provided.<BR>
	 * <B>Resolution</B>: Inform the user about the needed feature and terminate. <BR>
	 * <BR>
	 * NOTE: Designed for missing encodings, ciphers, digests, etc.
	 */
	ENVIRONMENT_MISSING_COMPONENT_OR_SERVICE(4,
			"shutting down, because the server needs a specific object or function, but it is not provided"),
	
	/**
	 * <B>Cause</B>: An [uncaught] error (other than {@link StackOverflowError}) occurred during
	 * runtime.<BR>
	 * <B>Resolution</B>: Possibly inform the user about the occurred error and restart.
	 */
	RUNTIME_UNCAUGHT_ERROR(5, "shutting down, because an uncaught error occurred during runtime"),
	
	/**
	 * <B>Cause</B>: Configuration files could not be loaded.<BR>
	 * <B>Resolution</B>: Inform the user about the issue and terminate.
	 */
	RUNTIME_INVALID_CONFIGURATION(6, "shutting down, because configuration could not be loaded"),
	
	/**
	 * <B>Cause</B>: Common services, such as binding to socket, thread or database connection
	 * pooling could not be initialized.<BR>
	 * <B>Resolution</B>: Inform the user about the issue and terminate.
	 */
	RUNTIME_INITIALIZATION_FAILURE(7, "shutting down, because required services could not be initialized");
	
	private final int _statusCode;
	private final String _description;
	
	private TerminationStatus(int statusCode, String description)
	{
		_statusCode = statusCode;
		_description = description;
	}
	
	/**
	 * Returns the application's exit code.
	 * 
	 * @return exit code
	 */
	public int getStatusCode()
	{
		return _statusCode;
	}
	
	/**
	 * Returns the description of this status code.
	 * 
	 * @return a message
	 */
	public String getDescription()
	{
		return _description;
	}
}
