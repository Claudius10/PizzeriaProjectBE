package PizzaApp.api.validation.exceptions;

public class StoreNotOpenException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public StoreNotOpenException(String message, Throwable cause) {
		super(message, cause);
	}

	public StoreNotOpenException(String message) {
		super(message);
	}

	public StoreNotOpenException(Throwable cause) {
		super(cause);
	}
}
