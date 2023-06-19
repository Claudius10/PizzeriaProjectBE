package PizzaApp.api.exceptions.exceptions.order;

public class OrderDataUpdateTimeLimitException extends RuntimeException {

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
