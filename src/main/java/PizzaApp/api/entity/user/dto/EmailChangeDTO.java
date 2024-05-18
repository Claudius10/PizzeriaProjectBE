package PizzaApp.api.entity.user.dto;

import PizzaApp.api.utils.globals.ValidationResponses;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailChangeDTO(
		@Email(message = ValidationResponses.EMAIL_INVALID)
		@NotBlank(message = ValidationResponses.EMAIL_MISSING)
		String email,

		@NotBlank(message = ValidationResponses.PASSWORD_MISSING)
		String password) {
}