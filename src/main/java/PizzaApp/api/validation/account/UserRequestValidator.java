package PizzaApp.api.validation.account;

import jakarta.servlet.http.HttpServletRequest;

public interface UserRequestValidator {

	void validate(String userId, HttpServletRequest request);
}
