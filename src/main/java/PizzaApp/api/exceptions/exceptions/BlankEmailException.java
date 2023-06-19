package PizzaApp.api.exceptions.exceptions;

public class BlankEmailException extends RuntimeException {

	public BlankEmailException(String message) {
		super(message);
	}

	public BlankEmailException(String message, Throwable cause) {
		super(message, cause);
	}

	public BlankEmailException(Throwable cause) {
		super(cause);
	}
}
