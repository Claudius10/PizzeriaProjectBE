package PizzaApp.api.validation.exceptions;

public class OrderDeleteTimeLimitException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6L;

	public OrderDeleteTimeLimitException(String message, Throwable cause) {
		super(message, cause);
	}

	public OrderDeleteTimeLimitException(String message) {
		super(message);
	}

	public OrderDeleteTimeLimitException(Throwable cause) {
		super(cause);
	}

}
