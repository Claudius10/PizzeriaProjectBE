package org.pizzeria.api.web.dto.order.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.pizzeria.api.web.exceptions.constraints.annotation.IntegerLength;
import org.pizzeria.api.web.globals.ValidationResponses;
import org.pizzeria.api.web.globals.ValidationRules;

public record CustomerDTO(
		@Pattern(regexp = ValidationRules.USER_NAME, message = ValidationResponses.NAME_INVALID)
		String name,

		@IntegerLength(min = 9, max = 9, message = ValidationResponses.NUMBER_INVALID)
		Integer contactNumber,

		@Email(message = ValidationResponses.EMAIL_INVALID)
		String email
) {
}
