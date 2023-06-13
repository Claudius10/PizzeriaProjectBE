package PizzaApp.api.validation.exceptions;

public class EmptyCartException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 2L;

	public EmptyCartException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmptyCartException(String message) {
		super(message);
	}

	public EmptyCartException(Throwable cause) {
		super(cause);
	}

}
