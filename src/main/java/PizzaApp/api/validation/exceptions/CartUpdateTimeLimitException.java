package PizzaApp.api.validation.exceptions;

public class CartUpdateTimeLimitException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CartUpdateTimeLimitException(String message, Throwable cause) {
		super(message, cause);
	}

	public CartUpdateTimeLimitException(String message) {
		super(message);
	}

	public CartUpdateTimeLimitException(Throwable cause) {
		super(cause);
	}

}
