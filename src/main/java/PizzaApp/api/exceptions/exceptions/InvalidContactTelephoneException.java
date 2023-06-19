package PizzaApp.api.exceptions.exceptions;

public class InvalidContactTelephoneException extends RuntimeException {

	public InvalidContactTelephoneException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidContactTelephoneException(String message) {
		super(message);
	}

	public InvalidContactTelephoneException(Throwable cause) {
		super(cause);
	}

}
