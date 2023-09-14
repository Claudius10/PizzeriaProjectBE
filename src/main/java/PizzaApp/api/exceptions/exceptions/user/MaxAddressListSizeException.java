package PizzaApp.api.exceptions.exceptions.user;

public class MaxAddressListSizeException extends RuntimeException {

	public MaxAddressListSizeException(String message) {
		super(message);
	}

	public MaxAddressListSizeException(String message, Throwable cause) {
		super(message, cause);
	}

	public MaxAddressListSizeException(Throwable cause) {
		super(cause);
	}
}
