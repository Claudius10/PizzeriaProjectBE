package org.pizzeria.api.web.dto.user.dto;

import jakarta.validation.constraints.NotBlank;
import org.pizzeria.api.web.exceptions.constraints.annotation.IntegerLength;
import org.pizzeria.api.web.globals.ValidationResponses;

public record ContactNumberChangeDTO(
		@IntegerLength(min = 9, max = 9, message = ValidationResponses.NUMBER_INVALID)
		Integer contactNumber,

		@NotBlank(message = ValidationResponses.PASSWORD_INVALID)
		String password) {
}