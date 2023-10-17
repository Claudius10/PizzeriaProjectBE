package PizzaApp.api.validation.account;

import jakarta.servlet.http.HttpServletRequest;

public interface UserRequestValidator {

	void validate(Long userId, HttpServletRequest request);
}
