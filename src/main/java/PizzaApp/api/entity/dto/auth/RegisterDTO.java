package PizzaApp.api.entity.dto.auth;

import PizzaApp.api.exceptions.constraints.FieldMatch;
import PizzaApp.api.utils.globals.ValidationResponses;
import PizzaApp.api.utils.globals.ValidationRules;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@FieldMatch.List({
		@FieldMatch(first = "email", second = "matchingEmail", message = ValidationResponses.EMAIL_NO_MATCH),
		@FieldMatch(first = "password", second = "matchingPassword", message = ValidationResponses.PASSWORD_NO_MATCH)})
public record RegisterDTO(
		@Pattern(regexp = ValidationRules.USER_NAME, message = ValidationResponses.NAME_INVALID)
		String name,

		@Email(message = ValidationResponses.EMAIL_INVALID)
		@NotBlank(message = ValidationResponses.EMAIL_MISSING)
		String email,

		@Email(message = ValidationResponses.EMAIL_INVALID)
		@NotBlank(message = ValidationResponses.EMAIL_MISSING)
		String matchingEmail,

		@Pattern(regexp = ValidationRules.USER_PASSWORD, message = ValidationResponses.PASSWORD_INVALID)
		String password,

		@Pattern(regexp = ValidationRules.USER_PASSWORD, message = ValidationResponses.PASSWORD_INVALID)
		String matchingPassword) {
}