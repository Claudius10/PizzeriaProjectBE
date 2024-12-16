package org.pizzeria.api.web.dto.user.dto;

import jakarta.validation.constraints.NotBlank;
import org.pizzeria.api.web.globals.ValidationResponses;

public record PasswordDTO(
		@NotBlank(message = ValidationResponses.PASSWORD_INVALID)
		String password) {
}