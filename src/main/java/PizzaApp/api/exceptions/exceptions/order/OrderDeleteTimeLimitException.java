package PizzaApp.api.exceptions.exceptions.order;

public class OrderDeleteTimeLimitException extends RuntimeException {

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
