package PizzaApp.api.exceptions.exceptions.user;

public class MaxTelListSizeException extends RuntimeException {

	public MaxTelListSizeException(String message) {
		super(message);
	}

	public MaxTelListSizeException(String message, Throwable cause) {
		super(message, cause);
	}

	public MaxTelListSizeException(Throwable cause) {
		super(cause);
	}
}
