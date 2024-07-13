package org.pizzeria.api.entity.user.dto;

import jakarta.validation.constraints.NotBlank;
import org.pizzeria.api.utils.globals.ValidationResponses;

public record PasswordDTO(
		@NotBlank(message = ValidationResponses.PASSWORD_MISSING)
		String password) {
}