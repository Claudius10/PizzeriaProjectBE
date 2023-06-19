package PizzaApp.api.exceptions.exceptions.order;

public class InvalidChangeRequestedException extends RuntimeException {

	public InvalidChangeRequestedException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidChangeRequestedException(String message) {
		super(message);
	}

	public InvalidChangeRequestedException(Throwable cause) {
		super(cause);
	}
}
