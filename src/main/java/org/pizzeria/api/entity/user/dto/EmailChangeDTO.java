package org.pizzeria.api.entity.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.pizzeria.api.utils.globals.ValidationResponses;

public record EmailChangeDTO(
		@Email(message = ValidationResponses.EMAIL_INVALID)
		@NotBlank(message = ValidationResponses.EMAIL_MISSING)
		String email,

		@NotBlank(message = ValidationResponses.PASSWORD_MISSING)
		String password) {
}