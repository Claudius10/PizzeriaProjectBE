package PizzaApp.api.exceptions.exceptions.order;

public class EmptyCartException extends RuntimeException {

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
