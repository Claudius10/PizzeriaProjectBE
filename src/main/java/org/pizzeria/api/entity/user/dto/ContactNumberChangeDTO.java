package org.pizzeria.api.entity.user.dto;

import jakarta.validation.constraints.NotBlank;
import org.pizzeria.api.exceptions.constraints.annotation.IntegerLength;
import org.pizzeria.api.utils.globals.ValidationResponses;

public record ContactNumberChangeDTO(
		@IntegerLength(min = 9, max = 9, message = ValidationResponses.NUMBER_INVALID)
		Integer contactNumber,

		@NotBlank(message = ValidationResponses.PASSWORD_MISSING)
		String password) {
}