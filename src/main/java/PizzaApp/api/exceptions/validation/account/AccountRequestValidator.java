package PizzaApp.api.exceptions.validation.account;

import jakarta.servlet.http.HttpServletRequest;

public interface AccountRequestValidator {

	void validate(String id, HttpServletRequest request);
}
