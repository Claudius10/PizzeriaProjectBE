package org.pizzeria.api.web.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.pizzeria.api.web.exceptions.constraints.annotation.FieldMatch;
import org.pizzeria.api.web.globals.ValidationResponses;
import org.pizzeria.api.web.globals.ValidationRules;

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