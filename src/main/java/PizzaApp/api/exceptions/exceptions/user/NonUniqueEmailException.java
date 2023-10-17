package PizzaApp.api.exceptions.exceptions.user;

public class NonUniqueEmailException extends RuntimeException {

	public NonUniqueEmailException(String message) {
		super(message);
	}

	public NonUniqueEmailException(String message, Throwable cause) {
		super(message, cause);
	}

	public NonUniqueEmailException(Throwable cause) {
		super(cause);
	}
}
