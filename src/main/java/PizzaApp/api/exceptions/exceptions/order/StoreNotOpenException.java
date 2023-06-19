package PizzaApp.api.exceptions.exceptions.order;

public class StoreNotOpenException extends RuntimeException {

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
