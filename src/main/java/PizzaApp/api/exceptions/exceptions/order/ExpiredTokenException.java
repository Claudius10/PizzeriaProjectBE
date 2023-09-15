package PizzaApp.api.exceptions.exceptions.order;

public class ExpiredTokenException extends RuntimeException {
	public ExpiredTokenException(String message) {
		super(message);
	}
}
