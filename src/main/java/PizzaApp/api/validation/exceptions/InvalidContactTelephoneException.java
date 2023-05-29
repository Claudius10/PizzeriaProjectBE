package PizzaApp.api.validation.exceptions;

public class InvalidContactTelephoneException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4L;

	public InvalidContactTelephoneException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidContactTelephoneException(String message) {
		super(message);
	}

	public InvalidContactTelephoneException(Throwable cause) {
		super(cause);
	}

}
