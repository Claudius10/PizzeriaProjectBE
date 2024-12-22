package org.pizzeria.api.web.globals;

public final class SecurityResponses {

	public static final String USER_ID_NO_MATCH = "UserIdNoMatch"; // when userId in token does not match userId sent from FE

	public static final String USER_NOT_FOUND = "UserNotFound"; // when user does not exist in the database

	public static final String BAD_CREDENTIALS = "BadCredentialsException";

	private SecurityResponses() {
	}
}