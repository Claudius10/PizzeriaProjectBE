package PizzaApp.api.validation.exceptions;

public class OrderDataUpdateTimeLimitException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 5L;

	public OrderDataUpdateTimeLimitException(String message, Throwable cause) {
		super(message, cause);
	}

	public OrderDataUpdateTimeLimitException(String message) {
		super(message);
	}

	public OrderDataUpdateTimeLimitException(Throwable cause) {
		super(cause);
	}
}
