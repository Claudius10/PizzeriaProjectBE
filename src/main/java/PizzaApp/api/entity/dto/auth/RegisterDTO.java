package PizzaApp.api.entity.dto.auth;

import PizzaApp.api.exceptions.constraints.FieldMatch;
import PizzaApp.api.utils.globals.ValidationResponses;
import PizzaApp.api.utils.globals.ValidationRules;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@FieldMatch.List({
		@FieldMatch(first = "email", second = "matchingEmail", message = ValidationResponses.USER_EMAIL_MATCHING),
		@FieldMatch(first = "password", second = "matchingPassword", message = ValidationResponses.USER_PASSWORD_MATCHING)})
public record RegisterDTO(
		@Pattern(regexp = ValidationRules.USER_NAME, message = ValidationResponses.USER_NAME)
		String name,

		@Email(message = ValidationResponses.USER_EMAIL)
		@NotBlank(message = ValidationResponses.USER_EMAIL)
		String email,

		@Email(message = ValidationResponses.USER_EMAIL)
		@NotBlank(message = ValidationResponses.USER_EMAIL)
		String matchingEmail,

		@Pattern(regexp = ValidationRules.USER_PASSWORD, message = ValidationResponses.USER_PASSWORD)
		String password,

		@Pattern(regexp = ValidationRules.USER_PASSWORD, message = ValidationResponses.USER_PASSWORD)
		String matchingPassword) {
}