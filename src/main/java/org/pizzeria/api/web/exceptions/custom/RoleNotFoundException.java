package org.pizzeria.api.web.exceptions.custom;

public class RoleNotFoundException extends RuntimeException {
	public RoleNotFoundException(String message) {
		super(message);
	}

	public RoleNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public RoleNotFoundException(Throwable cause) {
		super(cause);
	}

	public RoleNotFoundException() {
		super();
	}
}
