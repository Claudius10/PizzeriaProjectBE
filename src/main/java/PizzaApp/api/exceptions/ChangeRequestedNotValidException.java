package PizzaApp.api.exceptions;

public class ChangeRequestedNotValidException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChangeRequestedNotValidException(String message, Throwable cause) {
		super(message, cause);
	}

	public ChangeRequestedNotValidException(String message) {
		super(message);
	}

	public ChangeRequestedNotValidException(Throwable cause) {
		super(cause);
	}
}
