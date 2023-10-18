package PizzaApp.api.exceptions.exceptions.order;

public class CartSizeLimitException extends RuntimeException {

	public CartSizeLimitException(String message) {
		super(message);
	}

	public CartSizeLimitException(String message, Throwable cause) {
		super(message, cause);
	}

	public CartSizeLimitException(Throwable cause) {
		super(cause);
	}
}
