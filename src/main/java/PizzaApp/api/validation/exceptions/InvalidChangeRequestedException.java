package PizzaApp.api.validation.exceptions;

public class InvalidChangeRequestedException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 3L;

	public InvalidChangeRequestedException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidChangeRequestedException(String message) {
		super(message);
	}

	public InvalidChangeRequestedException(Throwable cause) {
		super(cause);
	}
}
